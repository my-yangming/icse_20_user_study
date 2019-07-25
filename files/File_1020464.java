/*
 * Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.j2cl.tools.rta;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toList;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Wrapper Object containing the set of live types and live members discovered by the RTA algorithm.
 */
@AutoValue
abstract class RtaResult {
  abstract ImmutableList<String> getUnusedTypes();

  abstract ImmutableList<String> getUnusedMembers();

  abstract CodeRemovalInfo getCodeRemovalInfo();

  static RtaResult build(Set<Type> unusedTypeSet, Set<Member> unusedMemberSet) {
    return new AutoValue_RtaResult.Builder()
        .setUnusedTypes(toSortedList(unusedTypeSet.stream().map(Type::getName)))
        .setUnusedMembers(toSortedList(unusedMemberSet.stream().map(RtaResult::createMemberId)))
        .setCodeRemovalInfo(buildCodeRemovalInfo(unusedTypeSet, unusedMemberSet))
        .build();
  }

  private static CodeRemovalInfo buildCodeRemovalInfo(
      Set<Type> unusedTypeSet, Set<Member> unusedMemberSet) {

    CodeRemovalInfo.Builder codeRemovalInfo = CodeRemovalInfo.newBuilder();

    codeRemovalInfo.addAllUnusedFiles(
        unusedTypeSet.stream()
            .flatMap(t -> Stream.of(t.getHeaderSourceFile(), t.getImplSourceFile()))
            .sorted()
            .collect(Collectors.toList()));

    // Because the order used to add an entry in the map depends on the unstable order of the
    // unusedMemberSet iterator, we use a tree map to ensure a deterministic output of our
    // skylark rule.
    Map<String, UnusedLines.Builder> unusedLinesBuilderByFileName = new TreeMap<>();

    unusedMemberSet.stream()
        // Don't process members of unused types because the files will be totally removed.
        .filter(m -> !unusedTypeSet.contains(m.getDeclaringType()))
        // Consider member with file position info.
        .filter(m -> m.getStartLine() != -1)
        .sorted(Comparator.comparing(Member::getStartLine))
        .forEach(
            member ->
                unusedLinesBuilderByFileName
                    .computeIfAbsent(
                        member.getDeclaringType().getImplSourceFile(),
                        (file) -> UnusedLines.newBuilder().setFileKey(file))
                    .addUnusedRanges(
                        LineRange.newBuilder()
                            .setLineStart(member.getStartLine())
                            .setLineEnd(member.getEndLine())
                            .build()));

    return codeRemovalInfo
        .addAllUnusedLines(
            unusedLinesBuilderByFileName.values().stream()
                .map(UnusedLines.Builder::build)
                .collect(toList()))
        .build();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setCodeRemovalInfo(CodeRemovalInfo codeRemovalInfo);

    abstract Builder setUnusedMembers(List<String> unusedMembers);

    abstract Builder setUnusedTypes(List<String> unusedFiles);

    abstract RtaResult build();
  }

  private static String createMemberId(Member member) {
    return member.getDeclaringType().getName() + ":" + member.getName();
  }

  private static <E> List<E> toSortedList(Stream<E> stream) {
    return stream.sorted().collect(toImmutableList());
  }
}
