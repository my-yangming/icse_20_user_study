package jetbrains.mps.execution.lib;

/*Generated by MPS */

import jetbrains.mps.baseLanguage.tuples.runtime.MultiTuple;
import org.jetbrains.mps.openapi.language.SAbstractConcept;
import jetbrains.mps.baseLanguage.closures.runtime._FunctionTypes;
import org.jetbrains.mps.openapi.model.SNode;

public class NodesDescriptor extends MultiTuple._2<SAbstractConcept, _FunctionTypes._return_P1_E0<? extends Boolean, ? super SNode>> {
  public NodesDescriptor() {
    super();
  }
  public NodesDescriptor(SAbstractConcept concept, _FunctionTypes._return_P1_E0<? extends Boolean, ? super SNode> filter) {
    super(concept, filter);
  }
  public SAbstractConcept concept(SAbstractConcept value) {
    return super._0(value);
  }
  public _FunctionTypes._return_P1_E0<? extends Boolean, ? super SNode> filter(_FunctionTypes._return_P1_E0<? extends Boolean, ? super SNode> value) {
    return super._1(value);
  }
  public SAbstractConcept concept() {
    return super._0();
  }
  public _FunctionTypes._return_P1_E0<? extends Boolean, ? super SNode> filter() {
    return super._1();
  }
}
