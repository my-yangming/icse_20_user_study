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
package jetbrains.mps.lang.dataFlow.framework;


import jetbrains.mps.lang.dataFlow.framework.instructions.*;
import jetbrains.mps.lang.dataFlow.framework.analyzers.ReachabilityAnalyzer;
import jetbrains.mps.lang.dataFlow.framework.analyzers.InitializedVariablesAnalyzer;
import jetbrains.mps.lang.dataFlow.framework.analyzers.LivenessAnalyzer;
import jetbrains.mps.lang.dataFlow.framework.analyzers.MayBeInitializedVariablesAnalyzer;

import java.util.*;
import java.util.Map.Entry;

public class Program {
  protected final List<Instruction> myInstructions = new ArrayList<>();
  protected final List<TryFinallyInfo> myTryFinallyInfo = new ArrayList<>();
  protected final Map<Object, Integer> myStarts = new HashMap<>();
  protected final Map<Object, Integer> myEnds = new HashMap<>();
  protected final Stack<Object> myCreationStack = new Stack<>();
  protected List<Object> myVariables = new ArrayList<>();
  protected boolean hasOuterJumps = false;

  public List<Instruction> getInstructions() {
    return Collections.unmodifiableList(myInstructions);
  }

  public List<ProgramState> getStates() {
    List<ProgramState> result = new ArrayList<>();
    for (Instruction i : myInstructions) {
      result.add(new ProgramState(i, true));
      result.add(new ProgramState(i, false));
    }
    return result;
  }

  public Instruction get(int index) {
    return myInstructions.get(index);
  }

  public int size() {
    return myInstructions.size();
  }

  public int indexOf(Instruction i) {
    return myInstructions.indexOf(i);
  }

  public Instruction getStart() {
    return myInstructions.get(0);
  }

  public Instruction getEnd() {
    return myInstructions.get(myInstructions.size() - 1);
  }

  public <E> AnalysisResult<E> analyze(DataFlowAnalyzer<E> analyzer) {
    return new AnalyzerRunner<>(this, analyzer).analyze();
  }

  public List<Object> getVariables() {
    return new ArrayList<>(myVariables);
  }

  public int getVariablesCount() {
    return myVariables.size();
  }

  public int getVariableIndex(Object var) {
    return myVariables.indexOf(var);
  }

  public Object getVariable(int index) {
    return myVariables.get(index);
  }

  protected void add(Instruction instruction) {
    instruction.setProgram(this);
    instruction.setSource(getCurrent());
    instruction.setIndex(myInstructions.size());
    myInstructions.add(instruction);
  }

  @Deprecated
  public void insert(Instruction instruction, int position, boolean update) {
    insert(instruction, position, update, true);
  }

  public void insert(Instruction instruction, int position, boolean update, boolean before) {
    instruction.setProgram(this);
    if (instruction.getSource() == null) {
      instruction.setSource(myInstructions.get(position - 1).getSource());
    }
    instruction.setIndex(position);
    for (Instruction i : myInstructions.subList(position, myInstructions.size())) {
      i.setIndex(i.getIndex() + 1);
    }
    for (Entry<Object, Integer> e : myEnds.entrySet()) {
      if (e.getValue() > position) {
        e.setValue(e.getValue() + 1);
      }
    }
    for (Entry<Object, Integer> e : myStarts.entrySet()) {
      if (e.getValue() >= position) {
        e.setValue(e.getValue() + 1);
      }
    }
    myInstructions.add(position, instruction);
    if (update) {
      updateJumpsOnInsert(position, before);
    }
  }

  protected void start(Object o) {
    if (myCreationStack.contains(o)) {
      throw new RuntimeException("Cycle!");
    }
    myCreationStack.push(o);
    myStarts.put(o, getCurrentPosition());
  }

  protected void end(Object o) {
    if (myCreationStack.isEmpty() || myCreationStack.peek() != o) {
      throw new RuntimeException("Unexpected end");
    }
    myCreationStack.pop();
    myEnds.put(o, getCurrentPosition());
  }

  protected Object getCurrent() {
    if (myCreationStack.isEmpty()) {
      return null;
    }
    return myCreationStack.peek();
  }

  protected int getCurrentPosition() {
    return myInstructions.size();
  }

  public int getStart(Object o) {
    if (!myStarts.containsKey(o)) {
      throw new DataflowBuilderException("Can't find a start of node " + o);
    }
    return myStarts.get(o);
  }

  public int getEnd(Object o) {
    if (!myEnds.containsKey(o)) {
      throw new DataflowBuilderException("Can't find an end of node " + o);
    }
    return myEnds.get(o);
  }

  public List<Instruction> getInstructionsFor(Object o) {
    if (myStarts.containsKey(o)) {
      int start = getStart(o);
      int end = getEnd(o);
      if (start <= end) {
        return new ArrayList<>(myInstructions.subList(start, end));
      }
    }
    return new ArrayList<>();
  }

  protected void init() {
    add(new EndInstruction());

    collectVariables();
    buildBlockInfos();
    buildInstructionCaches();

    sanityCheck();
  }

  protected void buildInstructionCaches() {
    for (Instruction i : myInstructions) {
      i.buildCaches();
    }
  }

  protected void collectVariables() {
    Set<Object> result = new LinkedHashSet<>();
    for (Instruction i : myInstructions) {
      if (i instanceof ReadInstruction) {
        result.add(((ReadInstruction) i).getVariable());
      }
      if (i instanceof WriteInstruction) {
        result.add(((WriteInstruction) i).getVariable());
      }
    }

    myVariables = new ArrayList<>(result);
  }

  protected void buildBlockInfos() {
    Stack<TryFinallyInfo> stack = new Stack<>();
    for (Instruction i : myInstructions) {
      if (i instanceof TryInstruction) {
        TryFinallyInfo info = new TryFinallyInfo();
        info.myTry = (TryInstruction) i;
        myTryFinallyInfo.add(info);
        if (!stack.isEmpty()) {
          TryFinallyInfo parent = stack.peek();
          info.myParent = parent;
          parent.myChildren.add(info);
        }
        stack.push(info);
      }

      if (i instanceof FinallyInstruction) {
        if (stack.isEmpty() || stack.peek().myFinally != null) {
          throw new IllegalStateException("unexpected finally");
        }

        stack.peek().myFinally = (FinallyInstruction) i;
      }


      if (i instanceof EndTryInstruction) {
        if (stack.isEmpty() ||
            stack.peek().myEndTry != null) {
          throw new IllegalStateException("unexpected endtry");
        }

        stack.peek().myEndTry = (EndTryInstruction) i;
        stack.pop();
      }
    }

    if (!stack.isEmpty()) {
      throw new IllegalStateException("incomplete try blocks");
    }
  }

  public Set<Instruction> getUnreachableInstructions() {
    AnalysisResult<Boolean> analysisResult = analyze(new ReachabilityAnalyzer());
    Set<Instruction> result = new HashSet<>();
    for (Instruction i : myInstructions) {
      if (!analysisResult.get(i)) {
        result.add(i);
      }
    }
    return result;
  }

  public Set<Instruction> getExpectedReturns() {
    Set<Instruction> result = new HashSet<>();
    AnalysisResult<Boolean> analysisResult = analyze(new ReachabilityAnalyzer());
    ProgramState endWithoutReturn = new ProgramState(getEnd(), false);
    if (analysisResult.get(endWithoutReturn)) {
      for (ProgramState pred : endWithoutReturn.pred()) {
        if (analysisResult.get(pred)) {
          result.add(pred.getInstruction());
        }
      }
    }
    return result;
  }

  public Set<ReadInstruction> getUninitializedReads() {
    AnalysisResult<VarSet> analysisResult = analyze(new InitializedVariablesAnalyzer());
    Set<ReadInstruction> result = new HashSet<>();
    for (Instruction i : myInstructions) {
      if (i instanceof ReadInstruction) {
        ReadInstruction read = (ReadInstruction) i;
        VarSet initializedVars = analysisResult.get(read);
        if (!initializedVars.contains(read.getVariableIndex())) {
          result.add(read);
        }
      }
    }
    return result;
  }

  public boolean isInitializedRewritten(WriteInstruction instruction) {
    AnalysisResult<VarSet> analysisResult = analyze(new MayBeInitializedVariablesAnalyzer(instruction));
    VarSet initializedVars = analysisResult.get(instruction);
    return initializedVars.contains(instruction.getVariableIndex());
  }

  public Set<WriteInstruction> getUnusedAssignments() {
    AnalysisResult<VarSet> analysisResult = analyze(new LivenessAnalyzer());
    Set<WriteInstruction> retModeTrue = new HashSet<>();
    Set<WriteInstruction> retModeFalse = new HashSet<>();
    for (ProgramState s : analysisResult.getStates()) {
      if (s.getInstruction() instanceof WriteInstruction) {
        WriteInstruction write = (WriteInstruction) s.getInstruction();
        VarSet liveAfter = new VarSet(this);
        for (ProgramState succ : s.succ()) {
          liveAfter.addAll(analysisResult.get(succ));
        }
        if (!liveAfter.contains(write.getVariableIndex())) {
          if (s.isReturnMode()) {
            retModeTrue.add(write);
          } else {
            retModeFalse.add(write);
          }
        }
      }
    }
    retModeFalse.retainAll(retModeTrue);
    return retModeFalse;
  }

  public String toString() {
    return toString(false);
  }

  public String toString(boolean showSource) {
    StringBuilder result = new StringBuilder();
    for (Instruction instruction : myInstructions) {
      result.append(instruction);
      if (instruction.getSource() != null && showSource) {
        result.append(' ').append(instruction.getSource());
      }
      result.append('\n');
    }
    return result.toString();
  }

  public List<TryFinallyInfo> getBlockInfos() {
    return Collections.unmodifiableList(myTryFinallyInfo);
  }

  public ProgramState getState(int n) {
    return new ProgramState(myInstructions.get(n >> 1), (n & 1) == 0);
  }

  protected void sanityCheck() {
    for (Instruction i : myInstructions) {
      sanityCheck(new ProgramState(i, true));
      sanityCheck(new ProgramState(i, false));
    }
  }

  protected void sanityCheck(ProgramState ps) {
    for (ProgramState pred : ps.pred()) {
      if (!pred.succ().contains(ps)) {
        ps.pred();
        pred.succ();
        throw new RuntimeException("\n" + this.toString());
      }
    }
    for (ProgramState succ : ps.succ()) {
      if (!succ.pred().contains(ps)) {
        ps.succ();
        succ.pred();
        throw new RuntimeException("\n" + this.toString());
      }
    }
  }

  public class TryFinallyInfo {
    protected TryInstruction myTry;
    protected FinallyInstruction myFinally;
    protected EndTryInstruction myEndTry;
    protected TryFinallyInfo myParent;
    protected List<TryFinallyInfo> myChildren = new ArrayList<>();

    public TryInstruction getTry() {
      return myTry;
    }

    public FinallyInstruction getFinally() {
      return myFinally;
    }

    public EndTryInstruction getEndTry() {
      return myEndTry;
    }

    public TryFinallyInfo getParent() {
      return myParent;
    }

    public List<TryFinallyInfo> getChildren() {
      return Collections.unmodifiableList(myChildren);
    }
  }

  public boolean contains(Object o) {
    return myStarts.containsKey(o);
  }

  public boolean hasOuterJumps() {
    return hasOuterJumps;
  }

  public void setHasOuterJumps(boolean hasOuterJumps) {
    this.hasOuterJumps = hasOuterJumps;
  }

  public void updateJumpsOnInsert(int position, boolean before) {
    for (Instruction i : myInstructions) {
      if (i instanceof IfJumpInstruction) {
        IfJumpInstruction ifJump = ((IfJumpInstruction) i);
        int jumpTo = ifJump.getJumpTo();
        if (jumpTo > position) {
          ifJump.setJumpTo(jumpTo + 1);
        } else if (jumpTo == position) {
          if (before) {
            ifJump.updateJumps(jumpTo + 1);
          } else {
            ifJump.setJumpTo(jumpTo + 1);
          }
        }
      } else if (i instanceof JumpInstruction) {
        JumpInstruction jump = ((JumpInstruction) i);
        int jumpTo = jump.getJumpTo();
        if (jumpTo > position) {
          jump.setJumpTo(jumpTo + 1);
        } else if (jumpTo == position) {
          jump.updateJumps(jumpTo + 1);
          if (before) {
            jump.updateJumps(jumpTo + 1);
          } else {
            jump.setJumpTo(jumpTo + 1);
          }
        }
      }
    }
  }

  public Program copy() {
    Program program = new Program();
    for (Instruction i : myInstructions) {
      program.add(i);
    }
    return program;
  }
}
