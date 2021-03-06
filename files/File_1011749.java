package jetbrains.mps.vcs.diff.changes;

/*Generated by MPS */

import org.jetbrains.mps.openapi.model.SNodeId;
import org.jetbrains.annotations.NotNull;
import jetbrains.mps.vcs.diff.ChangeSet;
import org.jetbrains.mps.openapi.language.SProperty;
import org.jetbrains.mps.openapi.model.SModel;
import org.jetbrains.mps.openapi.model.SNode;

public class SetPropertyStructChange extends SetPropertyChange {
  private SNodeId myOppositeNodeId;

  public SetPropertyStructChange(@NotNull ChangeSet changeSet, @NotNull SNodeId oldNodeId, @NotNull SNodeId newNodeId, SProperty property, String newValue) {
    super(changeSet, oldNodeId, property, newValue);
    myOppositeNodeId = newNodeId;
  }
  @NotNull
  @Override
  public SNodeId getAffectedNodeId(boolean isNewModel) {
    return (isNewModel ? myOppositeNodeId : super.getAffectedNodeId(false));
  }
  @Override
  public void apply(@NotNull SModel model, @NotNull NodeCopier nodeCopier) {
    assert model == getChangeSet().getOldModel();
    super.apply(model, nodeCopier);
  }
  @NotNull
  @Override
  protected ModelChange createOppositeChange() {
    SNode node = getChangeSet().getOldModel().getNode(getAffectedNodeId(false));
    assert node != null;
    return new SetPropertyStructChange(getChangeSet().getOppositeChangeSet(), getAffectedNodeId(true), getAffectedNodeId(false), getProperty(), node.getProperty(getProperty()));
  }
}
