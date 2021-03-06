package jetbrains.mps.baseLanguage.jdk8.typesystem;

/*Generated by MPS */

import jetbrains.mps.errors.QuickFix_Runtime;
import jetbrains.mps.smodel.SNodePointer;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SPropertyOperations;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;

public class RemoveAbstractModifier_QuickFix extends QuickFix_Runtime {
  public RemoveAbstractModifier_QuickFix() {
    super(new SNodePointer("r:0396c9ff-0a4d-4e54-9678-835fc58468cd(jetbrains.mps.baseLanguage.jdk8.typesystem)", "2577576048763133371"));
  }
  public String getDescription(SNode node) {
    return "Remove the 'abstract' modifier";
  }
  public void execute(SNode node) {
    assert SPropertyOperations.getBoolean(((SNode) RemoveAbstractModifier_QuickFix.this.getField("method")[0]), MetaAdapterFactory.getProperty(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xf8cc56b21dL, 0x1126a8d157dL, "isAbstract"));
    SPropertyOperations.assign(((SNode) RemoveAbstractModifier_QuickFix.this.getField("method")[0]), MetaAdapterFactory.getProperty(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xf8cc56b21dL, 0x1126a8d157dL, "isAbstract"), false);
  }
}
