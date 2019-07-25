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

import jetbrains.mps.lang.dataFlow.framework.instructions.IfJumpInstruction;
import jetbrains.mps.lang.dataFlow.framework.instructions.JumpInstruction;
import jetbrains.mps.lang.dataFlow.framework.instructions.WriteInstruction;
import jetbrains.mps.lang.dataFlow.framework.instructions.Instruction;
import jetbrains.mps.lang.dataFlow.framework.*;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

public class MayBeInitializedVariablesAnalyzer implements DataFlowAnalyzer<VarSet> {

  private Set<Instruction> myExclusions;

  public MayBeInitializedVariablesAnalyzer(Instruction... exclusions) {
    myExclusions = new HashSet<>(exclusions.length);
    myExclusions.addAll(Arrays.asList(exclusions));
  }

  @Override
  public VarSet initial(Program p) {
    return new VarSet(p, false);
  }

  @Override
  public VarSet merge(Program p, List<VarSet> input) {
    if (input.isEmpty()) {
      return initial(p);
    }

    VarSet result = new VarSet(p, false);
    for (VarSet anInput : input) {
      result.addAll(anInput);
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

    if (instruction instanceof WriteInstruction && !myExclusions.contains(instruction)) {
      WriteInstruction write = (WriteInstruction) instruction;
      result.add(write.getVariableIndex());
    }

    if (instruction instanceof JumpInstruction || instruction instanceof IfJumpInstruction) {
      final int to;
      if (instruction instanceof JumpInstruction) {
        to = ((JumpInstruction) instruction).getJumpTo();
      } else {
        to = ((IfJumpInstruction) instruction).getJumpTo();
      }
      final int current = s.getInstruction().getIndex();
      if (to < current) {  //jumping backwards
        final Program program = instruction.getProgram();
        for (Object var : program.getVariables()) {
          final List<Instruction> instructions = program.getInstructionsFor(var);
          if (!instructions.isEmpty()) {
            if (to < instructions.get(0).getIndex()) {  //declaration is inside the loop
              result.remove(var);
            } else {  //declaration is before the loop
              result.add(var);
            }
          }
        }
      }
    }

    return result;
  }

  @Override
  public AnalysisDirection getDirection() {
    return AnalysisDirection.FORWARD;
  }
}
