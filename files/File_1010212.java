package jetbrains.mps.generator.impl;

/*Generated by MPS */

import org.jetbrains.mps.openapi.model.SModel;
import org.jetbrains.mps.openapi.model.SNode;
import org.jetbrains.mps.openapi.model.SNodeUtil;
import org.jetbrains.mps.openapi.model.SNodeId;
import jetbrains.mps.smodel.behaviour.BHReflection;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.core.aspects.behaviour.SMethodTrimmedId;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.AttributeOperations;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.IAttributeDescriptor;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SLinkOperations;

/**
 * Modifies checkpoint model, on save() creates node attribute for nodes with 'origin trace' user object, on load(), injects a user object for nodes with attribute.
 * Doesn't clear UO on save() nor node attribute on load(). Perhaps, should, no clear idea yet.
 * The whole idea of this class is to fix https://youtrack.jetbrains.com/issue/MPS-28373 in a 2018.2 bugfix with least possible change. 
 * Generally, shall re-consider use of UO for origin trace and the way I save extra information along with CP model.
 */
public final class TransitionTracePersistence {
  private final SModel myCheckpointModel;

  public TransitionTracePersistence(SModel checkpointModel) {
    myCheckpointModel = checkpointModel;
  }

  public void save(TransitionTrace originTrace) {
    // myCheckpointModel.nodes() gives a list! 
    for (SNode n : SNodeUtil.getDescendants(myCheckpointModel)) {
      if (!(originTrace.hasOrigin(n))) {
        continue;
      }
      SNodeId origin = originTrace.getOrigin(n);
      SNode nid = ((SNode) BHReflection.invoke0(SNodeOperations.asSConcept(MetaAdapterFactory.getConcept(0xb401a68083254110L, 0x8fd384331ff25befL, 0x3279d292ec74a708L, "jetbrains.mps.lang.generator.structure.ElementaryNodeId")), MetaAdapterFactory.getConcept(0xb401a68083254110L, 0x8fd384331ff25befL, 0x3279d292ec74a708L, "jetbrains.mps.lang.generator.structure.ElementaryNodeId"), SMethodTrimmedId.create("create", MetaAdapterFactory.getConcept(0xb401a68083254110L, 0x8fd384331ff25befL, 0x3279d292ec74a708L, "jetbrains.mps.lang.generator.structure.ElementaryNodeId"), "6UZRahyzeh3"), myCheckpointModel, origin));
      SNode ot = AttributeOperations.createAndSetAttrbiute(n, new IAttributeDescriptor.NodeAttribute(MetaAdapterFactory.getConcept(0xb401a68083254110L, 0x8fd384331ff25befL, 0x6ebfdca4628bfd48L, "jetbrains.mps.lang.generator.structure.OriginTrace")), MetaAdapterFactory.getConcept(0xb401a68083254110L, 0x8fd384331ff25befL, 0x6ebfdca4628bfd48L, "jetbrains.mps.lang.generator.structure.OriginTrace"));
      SLinkOperations.setTarget(ot, MetaAdapterFactory.getContainmentLink(0xb401a68083254110L, 0x8fd384331ff25befL, 0x6ebfdca4628bfd48L, 0x6ebfdca4628bfd4dL, "origin"), nid);
    }
  }

  public void load(TransitionTrace into) {
    for (SNode n : SNodeUtil.getDescendants(myCheckpointModel)) {
      SNode originTrace = AttributeOperations.getAttribute(n, new IAttributeDescriptor.NodeAttribute(MetaAdapterFactory.getConcept(0xb401a68083254110L, 0x8fd384331ff25befL, 0x6ebfdca4628bfd48L, "jetbrains.mps.lang.generator.structure.OriginTrace")));
      if ((originTrace == null)) {
        continue;
      }
      SNodeId value = ((SNodeId) BHReflection.invoke0(SLinkOperations.getTarget(originTrace, MetaAdapterFactory.getContainmentLink(0xb401a68083254110L, 0x8fd384331ff25befL, 0x6ebfdca4628bfd48L, 0x6ebfdca4628bfd4dL, "origin")), MetaAdapterFactory.getInterfaceConcept(0xb401a68083254110L, 0x8fd384331ff25befL, 0x7d58bd9fd9b5e358L, "jetbrains.mps.lang.generator.structure.NodeIdentity"), SMethodTrimmedId.create("getNodeId", null, "39TODbGsIdf")));
      into.setOrigin(n, value);
    }
  }
}
