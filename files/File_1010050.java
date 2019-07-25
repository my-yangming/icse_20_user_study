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
package jetbrains.mps.lang.dataFlow.framework.analyzers;

import jetbrains.mps.lang.dataFlow.framework.instructions.WriteInstruction;
import jetbrains.mps.lang.dataFlow.framework.instructions.Instruction;
import jetbrains.mps.lang.dataFlow.framework.*;

import java.util.List;

public class InitializedVariablesAnalyzer implements DataFlowAnalyzer<VarSet> {

  public InitializedVariablesAnalyzer() {

  }

  @Override
  public VarSet initial(Program p) {
    return new VarSet(p, true);
  }

  @Override
  public VarSet merge(Program p, List<VarSet> input) {
    if (input.isEmpty()) {
      return initial(p);
    }

    VarSet result = new VarSet(p, true);
    for (VarSet anInput : input) {
      result.retainAll(anInput);
    }
    return result;
  }

  @Override
  public VarSet fun(VarSet input, ProgramState s) {
    Instruction instruction = s.getInstruction();
    VarSet result = input;

    if (s.isStart()) {
      result.clear();
    }

    if (instruction instanceof WriteInstruction) {
      WriteInstruction write = (WriteInstruction) instruction;
      result.add(write.getVariableIndex());
    }

    return result;
  }

  @Override
  public AnalysisDirection getDirection() {
    return AnalysisDirection.FORWARD;
  }
}
