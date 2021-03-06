package jetbrains.mps.vcs.diff.changes;

/*Generated by MPS */

import org.jetbrains.mps.openapi.model.SNodeId;
import org.jetbrains.mps.openapi.language.SContainmentLink;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import jetbrains.mps.vcs.diff.ChangeSet;
import org.jetbrains.annotations.Nullable;
import jetbrains.mps.vcs.util.MergeStrategy;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.vcs.mergehints.runtime.VCSAspectUtil;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import jetbrains.mps.internal.collections.runtime.ListSequence;
import jetbrains.mps.internal.collections.runtime.ISelector;
import org.jetbrains.mps.openapi.model.SModel;
import jetbrains.mps.internal.collections.runtime.IVisitor;
import jetbrains.mps.internal.collections.runtime.Sequence;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.internal.collections.runtime.IterableUtils;
import jetbrains.mps.util.NameUtil;
import java.util.Objects;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.AttributeOperations;

public class NodeGroupChange extends ModelChange {
  private final SNodeId myParentNodeId;
  private final SContainmentLink myRole;
  private final int myBegin;
  private final int myEnd;
  private final int myResultBegin;
  private final int myResultEnd;
  private List<SNodeId> myPreparedIdsToDelete = null;
  private SNodeId myBeforeAnchorId = null;
  public NodeGroupChange(@NotNull ChangeSet changeSet, @NotNull SNodeId parentNodeId, @NotNull SContainmentLink role, int begin, int end, int resultBegin, int resultEnd) {
    super(changeSet);
    myParentNodeId = parentNodeId;
    myRole = role;
    myBegin = begin;
    myEnd = end;
    myResultBegin = resultBegin;
    myResultEnd = resultEnd;
  }
  @NotNull
  public SNodeId getParentNodeId() {
    return myParentNodeId;
  }
  @NotNull
  public SNodeId getParentNodeId(boolean isNewModel) {
    return myParentNodeId;
  }
  /**
   * 
   * @deprecated use getRoleLink()
   */
  @NotNull
  @Deprecated
  public String getRole() {
    return myRole.getRoleName();
  }
  @NotNull
  public SContainmentLink getRoleLink() {
    return myRole;
  }
  public boolean isAbout(SContainmentLink link) {
    return myRole.equals(link);
  }
  public int getBegin() {
    return myBegin;
  }
  public int getEnd() {
    return myEnd;
  }
  public int getResultEnd() {
    return myResultEnd;
  }
  public int getResultBegin() {
    return myResultBegin;
  }
  @Nullable
  @Override
  public MergeStrategy getMergeHint() {
    // get "nonconflicting" attribute in metamodel 
    SNode n = getParent(false);
    MergeStrategy hint = VCSAspectUtil.getDefaultMergeStrategy(myRole);
    if (hint != null) {
      return hint;
    }
    return VCSAspectUtil.getDefaultMergeStrategy(SNodeOperations.getConcept(n));
  }
  private SNode getParent(boolean isNewModel) {
    return ((isNewModel ? getChangeSet().getNewModel() : getChangeSet().getOldModel())).getNode(getParentNodeId(isNewModel));
  }

  public final List<SNode> getChangedCollection(boolean isNewModel) {
    return check_yjf6x2_a0a12(check_yjf6x2_a0a0v(getParent(isNewModel), myRole, this), this);
  }


  public void prepare() {
    if (myPreparedIdsToDelete == null) {
      List<SNode> children = getChangedCollection(false);
      assert children != null;
      myPreparedIdsToDelete = ListSequence.fromList(children).page(myBegin, myEnd).select(new ISelector<SNode, SNodeId>() {
        public SNodeId select(SNode it) {
          return it.getNodeId();
        }
      }).toListSequence();
      myBeforeAnchorId = (myEnd >= ListSequence.fromList(children).count() ? null : children.get(myEnd).getNodeId());
    }
  }
  @Override
  public void apply(@NotNull final SModel model, @NotNull final NodeCopier nodeCopier) {
    // delete old nodes 
    prepare();
    // some nodes can be already deleted in editor (if editing is allowed) 
    ListSequence.fromList(myPreparedIdsToDelete).visitAll(new IVisitor<SNodeId>() {
      public void visit(SNodeId id) {
        check_yjf6x2_a0a0a0d0z(model.getNode(id));
      }
    });
    myPreparedIdsToDelete = null;

    // copy nodes to insert 
    Iterable<SNode> nodesToAdd = ListSequence.fromList(getChangedCollection(true)).page(myResultBegin, myResultEnd).select(new ISelector<SNode, SNode>() {
      public SNode select(SNode child) {
        return nodeCopier.copyNode(child);
      }
    });

    // insert new nodes 
    SNode beforeAnchor = (myBeforeAnchorId == null ? null : model.getNode(myBeforeAnchorId));
    SNode parent = model.getNode(myParentNodeId);
    for (SNode newNode : Sequence.fromIterable(nodesToAdd)) {
      insertNodeBeforeAnchor(parent, newNode, beforeAnchor);
    }
  }
  private SNode insertNodeBeforeAnchor(SNode parent, SNode newNode, SNode anchor) {
    SContainmentLink link = (SNodeOperations.isInstanceOf(newNode, MetaAdapterFactory.getConcept(0xceab519525ea4f22L, 0x9b92103b95ca8c0cL, 0x9d98713f247885aL, "jetbrains.mps.lang.core.structure.ChildAttribute")) ? MetaAdapterFactory.getContainmentLink(0xceab519525ea4f22L, 0x9b92103b95ca8c0cL, 0x10802efe25aL, 0x47bf8397520e5942L, "smodelAttribute") : myRole);
    parent.insertChildBefore(link, newNode, anchor);
    return newNode;
  }
  @Nullable
  @Override
  public SNodeId getRootId() {
    return SNodeOperations.getContainingRoot(getParent(false)).getNodeId();
  }
  @NotNull
  @Override
  public ChangeType getType() {
    if (myBegin == myEnd) {
      return ChangeType.ADD;
    }
    if (myResultBegin == myResultEnd) {
      return ChangeType.DELETE;
    }
    return ChangeType.CHANGE;
  }
  @Override
  public String toString() {
    if (myEnd == myBegin) {
      return String.format("Insert %s into position #%d in role %s of node %s", nodeRange(myResultBegin, myResultEnd), myBegin, myRole, myParentNodeId);
    }
    if (myResultEnd == myResultBegin) {
      return String.format("Delete %s in role %s of node %s", nodeRange(myBegin, myEnd), myRole, myParentNodeId);
    }
    return String.format("Replace %s with nodes %s in role %s of node %s", nodeRange(myBegin, myEnd), nodeRange(myResultBegin, myResultEnd), myRole, myParentNodeId);
  }
  @Override
  public String getDescription() {
    return getDescription(true);
  }
  public String getDescription(boolean verbose) {
    List<SNode> newChildren = null;
    String newIds = null;
    if (verbose) {
      newChildren = getChangedCollection(true);
      newIds = IterableUtils.join(ListSequence.fromList(newChildren).page(myResultBegin, myResultEnd).select(new ISelector<SNode, String>() {
        public String select(SNode n) {
          return "#" + n.getNodeId();
        }
      }), ", ");
    }

    String role = myRole.getName();
    String oldStuff = (myEnd - myBegin == 1 ? role : NameUtil.formatNumericalString(myEnd - myBegin, role));
    String newStuff = (myResultEnd - myResultBegin == 1 ? role : NameUtil.formatNumericalString(myResultEnd - myResultBegin, role));
    // FIXME get rid of this dirty magic with role names "pluralization". PLEASE!!! 
    if (Objects.equals(newStuff, role) && Objects.equals(oldStuff, role)) {
      newStuff = "another";
    } else if (myEnd != myBegin) {
      newStuff = "another " + newStuff;
    }
    if (myEnd == myBegin) {
      if (verbose) {
        String addedOrInserted = (myResultEnd == ListSequence.fromList(newChildren).count() ? "Added" : "Inserted");
        return String.format("%s %s: %s", addedOrInserted, newStuff, newIds);
      } else {
        return String.format("Added %s", newStuff);
      }
    }
    if (myResultEnd == myResultBegin) {
      return String.format("Removed %s", oldStuff);
    }
    if (verbose) {
      return String.format("Replaced %s with %s: %s", oldStuff, newStuff, newIds);
    } else {
      return String.format("Replaced %s with %s", oldStuff, newStuff);
    }
  }
  @NotNull
  @Override
  protected ModelChange createOppositeChange() {
    return new NodeGroupChange(getChangeSet().getOppositeChangeSet(), myParentNodeId, myRole, myResultBegin, myResultEnd, myBegin, myEnd);
  }
  private static String nodeRange(int begin, int end) {
    return (begin + 1 == end ? String.format("node #%d", begin) : String.format("nodes #%d-%d", begin, end - 1));
  }
  private static List<SNode> check_yjf6x2_a0a12(Iterable<SNode> checkedDotOperand, NodeGroupChange checkedDotThisExpression) {
    if (null != checkedDotOperand) {
      return Sequence.fromIterable(checkedDotOperand).toListSequence();
    }
    return null;
  }
  private static Iterable<SNode> check_yjf6x2_a0a0v(SNode checkedDotOperand, SContainmentLink myRole, NodeGroupChange checkedDotThisExpression) {
    if (null != checkedDotOperand) {
      return AttributeOperations.getChildNodesAndAttributes(checkedDotOperand, myRole);
    }
    return null;
  }
  private static void check_yjf6x2_a0a0a0d0z(SNode checkedDotOperand) {
    if (null != checkedDotOperand) {
      checkedDotOperand.delete();
    }

  }
}
