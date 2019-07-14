/*
 * Copyright 2019 The Error Prone Authors.
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

package com.google.errorprone.bugpatterns;

import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import static com.google.errorprone.matchers.Matchers.staticMethod;
import static com.google.errorprone.util.ASTHelpers.isSubtype;

import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

/**
 * Points out if #valueOf() is called on a Protocol Buffer Enum.
 *
 * @author bhagwani@google.com (Sumit Bhagwani)
 */
@BugPattern(
    name = "LiteEnumValueOf",
    summary =
        "Instead of converting enums to string and back, its numeric value should be used instead"
            + " as it is the stable part of the protocol defined by the enum.",
    severity = WARNING)
public class LiteEnumValueOf extends BugChecker implements MethodInvocationTreeMatcher {

  private static final Matcher<ExpressionTree> PROTO_MSG_VALUE_OF_MATCHER =
      staticMethod()
          .onClass(LiteEnumValueOf::isEnumLiteProtoOnly)
          .named("valueOf")
          .withParameters("java.lang.String");

  @Override
  public Description matchMethodInvocation(
      MethodInvocationTree methodInvocationTree, VisitorState state) {
    if (!PROTO_MSG_VALUE_OF_MATCHER.matches(methodInvocationTree, state)) {
      return Description.NO_MATCH;
    }
    // AutoValue Parcelable generated code uses the API in #createForParcel implementation.
    // We explicitly suppress the matches.
    if (ASTHelpers.getGeneratedBy(state)
        .contains("com.ryanharter.auto.value.parcel.AutoValueParcelExtension")) {
      return Description.NO_MATCH;
    }
    return describeMatch(methodInvocationTree);
  }

  private static boolean isEnumLiteProtoOnly(Type type, VisitorState state) {
    Symbol symbol = type.asElement();
    if (symbol == null) {
      return false;
    }
    boolean isLiteEnum =
        isSubtype(type, state.getTypeFromString("com.google.protobuf.Internal.EnumLite"), state)
            && !isSubtype(
                type, state.getTypeFromString("com.google.protobuf.ProtocolMessageEnum"), state);
    if (!isLiteEnum) {
      return false;
    }
    // we check the outer class of the type as well since oneof keyword generates EnumLite enums
    // in full proto library.
    Symbol outerClass = ASTHelpers.enclosingClass(symbol);
    if (outerClass == null) {
      // lite enums can be top level classes with java_multiple_files.
      return isLiteEnum;
    }
    Type outerClassType = outerClass.asType();
    return !isSubtype(
        outerClassType, state.getTypeFromString("com.google.protobuf.GeneratedMessage"), state);
  }
}
