package jetbrains.mps.lang.migration.intentions;

/*Generated by MPS */

import jetbrains.mps.intentions.AbstractIntentionDescriptor;
import jetbrains.mps.openapi.intentions.IntentionFactory;
import java.util.Collection;
import jetbrains.mps.openapi.intentions.IntentionExecutable;
import jetbrains.mps.openapi.intentions.Kind;
import jetbrains.mps.smodel.SNodePointer;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.openapi.editor.EditorContext;
import java.util.Collections;
import jetbrains.mps.intentions.AbstractIntentionExecutable;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import jetbrains.mps.lang.migration.util.NodeReferenceUtil;
import jetbrains.mps.lang.migration.behavior.AbstractNodeReference__BehaviorDescriptor;
import jetbrains.mps.openapi.intentions.IntentionDescriptor;

public final class MakeUndirect_Intention extends AbstractIntentionDescriptor implements IntentionFactory {
  private Collection<IntentionExecutable> myCachedExecutable;
  public MakeUndirect_Intention() {
    super(Kind.NORMAL, false, new SNodePointer("r:8524dd83-cdb1-4841-877b-826d11a828b5(jetbrains.mps.lang.migration.intentions)", "3116305438947603583"));
  }
  @Override
  public String getPresentation() {
    return "MakeUndirect";
  }
  @Override
  public boolean isApplicable(final SNode node, final EditorContext editorContext) {
    return true;
  }
  @Override
  public boolean isSurroundWith() {
    return false;
  }
  public Collection<IntentionExecutable> instances(final SNode node, final EditorContext context) {
    if (myCachedExecutable == null) {
      myCachedExecutable = Collections.<IntentionExecutable>singletonList(new MakeUndirect_Intention.IntentionImplementation());
    }
    return myCachedExecutable;
  }
  /*package*/ final class IntentionImplementation extends AbstractIntentionExecutable {
    public IntentionImplementation() {
    }
    @Override
    public String getDescription(final SNode node, final EditorContext editorContext) {
      return "Make Undirect";
    }
    @Override
    public void execute(final SNode node, final EditorContext editorContext) {
      SNodeOperations.replaceWithAnother(node, NodeReferenceUtil.makeReflection(AbstractNodeReference__BehaviorDescriptor.tryToFindNode_id6szrkDoc2K7.invoke(node, editorContext.getRepository())));
    }
    @Override
    public IntentionDescriptor getDescriptor() {
      return MakeUndirect_Intention.this;
    }
  }
}
