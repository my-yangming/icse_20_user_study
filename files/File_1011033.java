package jetbrains.mps.baseLanguage.typesystem;

/*Generated by MPS */

import jetbrains.mps.errors.QuickFix_Runtime;
import jetbrains.mps.smodel.SNodePointer;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SLinkOperations;

public class FixInstanceOfExpressionPrecedences_QuickFix extends QuickFix_Runtime {
  public FixInstanceOfExpressionPrecedences_QuickFix() {
    super(new SNodePointer("r:00000000-0000-4000-0000-011c895902c5(jetbrains.mps.baseLanguage.typesystem)", "2643065713351098644"));
  }
  public void execute(SNode node) {
    SNode instanceOfExpression = SNodeOperations.cast(node, MetaAdapterFactory.getConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xfbbff03700L, "jetbrains.mps.baseLanguage.structure.InstanceOfExpression"));
    SNodeOperations.replaceWithAnother(instanceOfExpression, SLinkOperations.getTarget(instanceOfExpression, MetaAdapterFactory.getContainmentLink(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xfbbff03700L, 0xfbbff06218L, "leftExpression")));
    SNodeOperations.replaceWithAnother(((SNode) FixInstanceOfExpressionPrecedences_QuickFix.this.getField("expressionRoot")[0]), instanceOfExpression);
    SLinkOperations.setTarget(instanceOfExpression, MetaAdapterFactory.getContainmentLink(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xfbbff03700L, 0xfbbff06218L, "leftExpression"), ((SNode) FixInstanceOfExpressionPrecedences_QuickFix.this.getField("expressionRoot")[0]));
  }
}
