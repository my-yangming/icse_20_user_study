/*
 * Copyright 2003-2011 JetBrains s.r.o.
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
package jetbrains.mps.typesystem.inference.util;

import org.jetbrains.mps.openapi.model.SNode;

import java.util.Map;

public class StructuralNodeSetView {

  Map<SNode, StructuralNodeSet<Integer>> myStructuralNodeSets;

  public StructuralNodeSetView(Map<SNode, StructuralNodeSet<Integer>> structuralNodeSets) {
    myStructuralNodeSets = structuralNodeSets;
  }

  public void show() {
    for (SNode node : myStructuralNodeSets.keySet()) {
      System.err.print("node " + node + " -> ");
      StructuralNodeSet<Integer> superNodes = myStructuralNodeSets.get(node);
      for (SNode superNode : superNodes) {
//        System.err.print(" | " + superNode + "[[" + superNodes.getTag(superNode) + "]]");
      }
      System.err.println();
    }
  }

}
