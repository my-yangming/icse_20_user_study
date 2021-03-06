package jetbrains.mps.smodel;

/*Generated by MPS */

import java.util.Set;
import org.jetbrains.mps.openapi.model.SNode;
import java.util.HashSet;
import org.jetbrains.mps.openapi.model.SModel;
import org.jetbrains.mps.openapi.module.SModule;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SModelOperations;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SLinkOperations;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import jetbrains.mps.smodel.behaviour.BHReflection;
import jetbrains.mps.core.aspects.behaviour.SMethodTrimmedId;
import java.util.Collections;

/**
 * Scan an editor aspect model to find cross-model dependencies.
 * Similar to {@link jetbrains.mps.smodel.ConceptDeclarationScanner }, tailored to figure out necessary 
 * extends dependency between languages due to editor aspect dependencies
 */
public class EditorDeclarationScanner {
  private final Set<SNode> myExternalConcepts = new HashSet<SNode>();
  private final Set<SModel> myExtendedModels = new HashSet<SModel>();
  private final Set<SModule> myExtendedModules = new HashSet<SModule>();


  public EditorDeclarationScanner() {
  }

  public EditorDeclarationScanner scan(SModel m) {
    SModule owner = m.getModule();
    for (SNode ac : SModelOperations.roots(m, MetaAdapterFactory.getConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10f7df344a9L, "jetbrains.mps.lang.editor.structure.AbstractComponent"))) {
      SNode cd = SLinkOperations.getTarget(ac, MetaAdapterFactory.getReferenceLink(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10f7df344a9L, 0x10f7df451aeL, "conceptDeclaration"));
      if (cd != null && SNodeOperations.getModel(cd).getModule() != owner) {
        myExternalConcepts.add(cd);
        myExtendedModels.add(SNodeOperations.getModel(cd));
      }
    }
    for (SNode menuRef : SModelOperations.nodes(m, MetaAdapterFactory.getInterfaceConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x169efbc9a90a41b3L, "jetbrains.mps.lang.editor.structure.IMenuReference"))) {
      SNode cd = ((SNode) BHReflection.invoke0(menuRef, MetaAdapterFactory.getInterfaceConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x169efbc9a90a41b3L, "jetbrains.mps.lang.editor.structure.IMenuReference"), SMethodTrimmedId.create("getApplicableConcept", null, "1quYWAD4TFX")));
      if (cd != null && SNodeOperations.getModel(cd).getModule() != owner) {
        myExternalConcepts.add(cd);
        myExtendedModels.add(SNodeOperations.getModel(cd));
      }
    }
    for (SModel em : myExtendedModels) {
      myExtendedModules.add(em.getModule());
    }
    return this;
  }

  public Set<SModel> getExternalStructureModels() {
    return Collections.unmodifiableSet(myExtendedModels);
  }

  public Set<SModule> getDependencyModules() {
    return Collections.unmodifiableSet(myExtendedModules);
  }

  public Set<SNode> getExternalConcepts() {
    return Collections.unmodifiableSet(myExternalConcepts);
  }
}
