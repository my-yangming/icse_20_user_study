/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.caliper.worker.instrument;

import com.google.caliper.core.Running.Benchmark;
import com.google.caliper.core.Running.BenchmarkMethod;
import com.google.caliper.model.ArbitraryMeasurement;
import com.google.caliper.model.Measurement;
import com.google.caliper.model.Value;
import com.google.caliper.util.Util;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Method;
import java.util.Map;
import javax.inject.Inject;

/** Worker for arbitrary measurements. */
final class ArbitraryMeasurementWorkerInstrument extends WorkerInstrument {
  private final Options options;
  private final String unit;
  private final String description;

  @Inject
  ArbitraryMeasurementWorkerInstrument(
      @Benchmark Object benchmark,
      @BenchmarkMethod Method method,
      @WorkerInstrument.Options Map<String, String> options) {
    super(benchmark, method);
    this.options = new Options(options);
    ArbitraryMeasurement annotation = method.getAnnotation(ArbitraryMeasurement.class);
    this.unit = annotation.units();
    this.description = annotation.description();
  }

  @Override
  public void preMeasure(boolean inWarmup) throws Exception {
    if (options.gcBeforeEach && !inWarmup) {
      Util.forceGc();
    }
  }

  @Override
  public void dryRun() throws Exception {
    benchmarkMethod.invoke(benchmark);
  }

  @Override
  public Iterable<Measurement> measure() throws Exception {
    double measured = (Double) benchmarkMethod.invoke(benchmark);
    return ImmutableSet.of(
        new Measurement.Builder()
            .value(Value.create(measured, unit))
            .weight(1)
            .description(description)
            .build());
  }

  private static class Options {
    final boolean gcBeforeEach;

    Options(Map<String, String> options) {
      this.gcBeforeEach = Boolean.parseBoolean(options.get("gcBeforeEach"));
    }
  }
}
