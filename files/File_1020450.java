/*
 * Copyright 2018, OpenCensus Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opencensus.implcore.trace.propagation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.SpanId;
import io.opencensus.trace.TraceId;
import io.opencensus.trace.TraceOptions;
import io.opencensus.trace.Tracestate;
import io.opencensus.trace.propagation.SpanContextParseException;
import io.opencensus.trace.propagation.TextFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/*>>>
import org.checkerframework.checker.nullness.qual.NonNull;
*/

/**
 * Implementation of the TraceContext propagation protocol. See <a
 * href=https://github.com/w3c/distributed-tracing>w3c/distributed-tracing</a>.
 */
public class TraceContextFormat extends TextFormat {
  private static final Tracestate TRACESTATE_DEFAULT = Tracestate.builder().build();
  @VisibleForTesting static final String TRACEPARENT = "traceparent";
  @VisibleForTesting static final String TRACESTATE = "tracestate";
  private static final List<String> FIELDS =
      Collections.unmodifiableList(Arrays.asList(TRACEPARENT, TRACESTATE));

  private static final String VERSION = "00";
  private static final int VERSION_SIZE = 2;
  private static final char TRACEPARENT_DELIMITER = '-';
  private static final int TRACEPARENT_DELIMITER_SIZE = 1;
  private static final int TRACE_ID_HEX_SIZE = 2 * TraceId.SIZE;
  private static final int SPAN_ID_HEX_SIZE = 2 * SpanId.SIZE;
  private static final int TRACE_OPTION_HEX_SIZE = 2 * TraceOptions.SIZE;
  private static final int TRACE_ID_OFFSET = VERSION_SIZE + TRACEPARENT_DELIMITER_SIZE;
  private static final int SPAN_ID_OFFSET =
      TRACE_ID_OFFSET + TRACE_ID_HEX_SIZE + TRACEPARENT_DELIMITER_SIZE;
  private static final int TRACE_OPTION_OFFSET =
      SPAN_ID_OFFSET + SPAN_ID_HEX_SIZE + TRACEPARENT_DELIMITER_SIZE;
  private static final int TRACEPARENT_HEADER_SIZE = TRACE_OPTION_OFFSET + TRACE_OPTION_HEX_SIZE;
  private static final int TRACESTATE_MAX_SIZE = 512;
  private static final int TRACESTATE_MAX_MEMBERS = 32;
  private static final char TRACESTATE_KEY_VALUE_DELIMITER = '=';
  private static final char TRACESTATE_ENTRY_DELIMITER = ',';
  private static final Splitter TRACESTATE_ENTRY_DELIMITER_SPLITTER =
      Splitter.on(Pattern.compile("[ \t]*" + TRACESTATE_ENTRY_DELIMITER + "[ \t]*"));

  @Override
  public List<String> fields() {
    return FIELDS;
  }

  @Override
  public <C /*>>> extends @NonNull Object*/> void inject(
      SpanContext spanContext, C carrier, Setter<C> setter) {
    checkNotNull(spanContext, "spanContext");
    checkNotNull(setter, "setter");
    checkNotNull(carrier, "carrier");
    char[] chars = new char[TRACEPARENT_HEADER_SIZE];
    chars[0] = VERSION.charAt(0);
    chars[1] = VERSION.charAt(1);
    chars[2] = TRACEPARENT_DELIMITER;
    spanContext.getTraceId().copyLowerBase16To(chars, TRACE_ID_OFFSET);
    chars[SPAN_ID_OFFSET - 1] = TRACEPARENT_DELIMITER;
    spanContext.getSpanId().copyLowerBase16To(chars, SPAN_ID_OFFSET);
    chars[TRACE_OPTION_OFFSET - 1] = TRACEPARENT_DELIMITER;
    spanContext.getTraceOptions().copyLowerBase16To(chars, TRACE_OPTION_OFFSET);
    setter.put(carrier, TRACEPARENT, new String(chars));
    List<Tracestate.Entry> entries = spanContext.getTracestate().getEntries();
    if (entries.isEmpty()) {
      // No need to add an empty "tracestate" header.
      return;
    }
    StringBuilder stringBuilder = new StringBuilder(TRACESTATE_MAX_SIZE);
    for (Tracestate.Entry entry : entries) {
      if (stringBuilder.length() != 0) {
        stringBuilder.append(TRACESTATE_ENTRY_DELIMITER);
      }
      stringBuilder
          .append(entry.getKey())
          .append(TRACESTATE_KEY_VALUE_DELIMITER)
          .append(entry.getValue());
    }
    setter.put(carrier, TRACESTATE, stringBuilder.toString());
  }

  @Override
  public <C /*>>> extends @NonNull Object*/> SpanContext extract(C carrier, Getter<C> getter)
      throws SpanContextParseException {
    checkNotNull(carrier, "carrier");
    checkNotNull(getter, "getter");
    TraceId traceId;
    SpanId spanId;
    TraceOptions traceOptions;
    String traceparent = getter.get(carrier, TRACEPARENT);
    if (traceparent == null) {
      throw new SpanContextParseException("Traceparent not present");
    }
    try {
      // TODO(bdrutu): Do we need to verify that version is hex and that for the version
      // the length is the expected one?
      checkArgument(
          traceparent.charAt(TRACE_OPTION_OFFSET - 1) == TRACEPARENT_DELIMITER
              && (traceparent.length() == TRACEPARENT_HEADER_SIZE
                  || (traceparent.length() > TRACEPARENT_HEADER_SIZE
                      && traceparent.charAt(TRACEPARENT_HEADER_SIZE) == TRACEPARENT_DELIMITER))
              && traceparent.charAt(SPAN_ID_OFFSET - 1) == TRACEPARENT_DELIMITER
              && traceparent.charAt(TRACE_OPTION_OFFSET - 1) == TRACEPARENT_DELIMITER,
          "Missing or malformed TRACEPARENT.");

      traceId = TraceId.fromLowerBase16(traceparent, TRACE_ID_OFFSET);
      spanId = SpanId.fromLowerBase16(traceparent, SPAN_ID_OFFSET);
      traceOptions = TraceOptions.fromLowerBase16(traceparent, TRACE_OPTION_OFFSET);
    } catch (IllegalArgumentException e) {
      throw new SpanContextParseException("Invalid traceparent: " + traceparent, e);
    }

    String tracestate = getter.get(carrier, TRACESTATE);
    try {
      if (tracestate == null || tracestate.isEmpty()) {
        return SpanContext.create(traceId, spanId, traceOptions, TRACESTATE_DEFAULT);
      }
      Tracestate.Builder tracestateBuilder = Tracestate.builder();
      List<String> listMembers = TRACESTATE_ENTRY_DELIMITER_SPLITTER.splitToList(tracestate);
      checkArgument(
          listMembers.size() <= TRACESTATE_MAX_MEMBERS, "Tracestate has too many elements.");
      // Iterate in reverse order because when call builder set the elements is added in the
      // front of the list.
      for (int i = listMembers.size() - 1; i >= 0; i--) {
        String listMember = listMembers.get(i);
        int index = listMember.indexOf(TRACESTATE_KEY_VALUE_DELIMITER);
        checkArgument(index != -1, "Invalid tracestate list-member format.");
        tracestateBuilder.set(
            listMember.substring(0, index), listMember.substring(index + 1, listMember.length()));
      }
      return SpanContext.create(traceId, spanId, traceOptions, tracestateBuilder.build());
    } catch (IllegalArgumentException e) {
      throw new SpanContextParseException("Invalid tracestate: " + tracestate, e);
    }
  }
}
