package jetbrains.mps.vcs.diff.changes;

/*Generated by MPS */

import org.jetbrains.mps.openapi.model.SNodeId;
import org.jetbrains.annotations.NotNull;
import jetbrains.mps.vcs.diff.ChangeSet;
import org.jetbrains.mps.openapi.language.SContainmentLink;
import org.jetbrains.mps.openapi.model.SModel;

public class NodeGroupStructChange extends NodeGroupChange {
  private final SNodeId myOppositeNodeId;
  public NodeGroupStructChange(@NotNull ChangeSet changeSet, @NotNull SNodeId parentNodeId, @NotNull SNodeId newParentNodeId, @NotNull SContainmentLink role, int begin, int end, int resultBegin, int resultEnd) {
    super(changeSet, parentNodeId, role, begin, end, resultBegin, resultEnd);
    myOppositeNodeId = newParentNodeId;
  }

  @NotNull
  @Override
  public SNodeId getParentNodeId(boolean isNewModel) {
    return (isNewModel ? myOppositeNodeId : super.getParentNodeId(false));
  }
  @Override
  public void apply(@NotNull SModel model, @NotNull NodeCopier nodeCopier) {
    assert model == getChangeSet().getOldModel();
    super.apply(model, nodeCopier);
  }
  @NotNull
  @Override
  protected ModelChange createOppositeChange() {
    return new NodeGroupStructChange(getChangeSet().getOppositeChangeSet(), getParentNodeId(true), getParentNodeId(false), getRoleLink(), getResultBegin(), getResultEnd(), getBegin(), getEnd());
  }
}
