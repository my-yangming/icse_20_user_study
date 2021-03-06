package jetbrains.mps.lang.editor.enumMigration;

/*Generated by MPS */

import org.jetbrains.mps.openapi.language.SEnumerationLiteral;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SEnumOperations;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.lang.smodel.EnumerationLiteralsIndex;

public class _Layout_Constraints_Enum_MigrationUtils {
  public static String value(SEnumerationLiteral enummember) {
    switch (enumSwitchIndex_pd2um9_a0a0a.indexNullable(enummember)) {
      case 0:
        return "punctuation";
      case 1:
        return "noflow";
      case 2:
        return null;
      default:
        return null;
    }
  }
  public static SEnumerationLiteral fromValue(String value) {
    if (value == null) {
      return SEnumOperations.getMember(MetaAdapterFactory.getEnumeration(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10901d9d75fL, "jetbrains.mps.lang.editor.structure._Layout_Constraints_Enum"), 0x10901dd9077L, "none");
    }
    switch (value) {
      case "punctuation":
        return SEnumOperations.getMember(MetaAdapterFactory.getEnumeration(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10901d9d75fL, "jetbrains.mps.lang.editor.structure._Layout_Constraints_Enum"), 0x10901d9d84bL, "punctuation");
      case "noflow":
        return SEnumOperations.getMember(MetaAdapterFactory.getEnumeration(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10901d9d75fL, "jetbrains.mps.lang.editor.structure._Layout_Constraints_Enum"), 0x10901dd62feL, "noflow");
      default:
        return null;
    }
  }
  private static EnumerationLiteralsIndex enumSwitchIndex_pd2um9_a0a0a = EnumerationLiteralsIndex.build(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10901d9d75fL, 0x10901d9d84bL, 0x10901dd62feL, 0x10901dd9077L);
}
