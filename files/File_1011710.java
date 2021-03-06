package jetbrains.mps.java.platform.highlighters;

/*Generated by MPS */

import jetbrains.mps.nodeEditor.checking.BaseEditorChecker;
import org.jetbrains.mps.openapi.language.SConcept;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import com.intellij.openapi.project.Project;
import java.util.regex.Pattern;
import com.intellij.spellchecker.SpellCheckerManager;
import java.util.List;
import jetbrains.mps.smodel.event.SModelEvent;
import jetbrains.mps.smodel.event.SModelPropertyEvent;
import jetbrains.mps.nodeEditor.EditorComponent;
import org.jetbrains.annotations.NotNull;
import jetbrains.mps.nodeEditor.checking.UpdateResult;
import jetbrains.mps.util.Cancellable;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.nodeEditor.EditorMessage;
import java.util.ArrayList;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import org.jetbrains.mps.openapi.language.SAbstractConcept;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SPropertyOperations;
import jetbrains.mps.nodeEditor.DefaultEditorMessage;
import jetbrains.mps.openapi.editor.message.EditorMessageOwner;
import com.intellij.ui.JBColor;
import jetbrains.mps.openapi.editor.cells.EditorCell;
import jetbrains.mps.nodeEditor.cells.EditorCell_Label;
import java.awt.Graphics;
import jetbrains.mps.nodeEditor.cells.TextLine;
import jetbrains.mps.ide.util.ColorAndGraphicsUtil;
import java.awt.FontMetrics;
import jetbrains.mps.ide.editor.checkers.ModelProblemMessage;

public class CommentSpellChecker extends BaseEditorChecker {
  private final SConcept mySingleLineCmment = MetaAdapterFactory.getConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0x57d533a7af15ed3dL, "jetbrains.mps.baseLanguage.structure.TextCommentPart");
  private final SConcept myJavadocComment = MetaAdapterFactory.getConcept(0xf280165065d5424eL, 0xbb1b463a8781b786L, 0x7c7f5b2f31990287L, "jetbrains.mps.baseLanguage.javadoc.structure.TextCommentLinePart");
  private final SConcept word = MetaAdapterFactory.getConcept(0xc7fb639fbe784307L, 0x89b0b5959c3fa8c8L, 0x229012ddae35f04L, "jetbrains.mps.lang.text.structure.Word");
  private final SConcept myStringLiteral = MetaAdapterFactory.getConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xf93d565d10L, "jetbrains.mps.baseLanguage.structure.StringLiteral");

  private boolean myUpdateNeeded;
  private Boolean myEnabled;
  private final Project myProject;

  /**
   * \s for whitespace, rest is almost identical to \{Punct} class, except for single/double quotation marks
   */
  private final Pattern myWordSplit = Pattern.compile("[\\s!#$%&()*+,-\\./:;<=>?@\\^\\[\\]\\\\`_{|}~]");


  public CommentSpellChecker(Project project) {
    myProject = project;
  }

  private boolean isDisabled() {
    return !(isEnabled());
  }

  private boolean isEnabled() {
    if (myEnabled == null) {
      myEnabled = SpellCheckerManager.getInstance(myProject) != null;
    }
    return myEnabled.booleanValue();
  }

  @Override
  public void processEvents(List<SModelEvent> events) {
    if (isDisabled()) {
      return;
    }
    if (myUpdateNeeded) {
      return;
    }
    for (SModelEvent e : events) {
      if (e instanceof SModelPropertyEvent) {
        final SConcept c = ((SModelPropertyEvent) e).getNode().getConcept();
        if (c.isSubConceptOf(mySingleLineCmment) || c.isSubConceptOf(myJavadocComment) || c.equals(myStringLiteral) || c.isSubConceptOf(word)) {
          myUpdateNeeded = true;
          break;
        }
      }
    }
  }

  @Override
  public boolean needsUpdate(EditorComponent component) {
    return isEnabled() && myUpdateNeeded;
  }

  @Override
  public void doneUpdating() {
    myUpdateNeeded = false;
  }

  @NotNull
  @Override
  public UpdateResult update(EditorComponent component, boolean incremental, boolean applyQuickFix, Cancellable cancellable) {
    SNode editedNode = component.getEditedNode();
    List<EditorMessage> messages = new ArrayList<EditorMessage>(4);
    for (SNode n : SNodeOperations.getNodeDescendants(editedNode, null, true, new SAbstractConcept[]{})) {
      if (cancellable.isCancelled()) {
        return UpdateResult.CANCELLED;
      }
      if (SNodeOperations.isInstanceOf(n, SNodeOperations.asSConcept(myJavadocComment))) {
        SNode p = SNodeOperations.as(n, SNodeOperations.asSConcept(myJavadocComment));
        spellCheck(SPropertyOperations.getString(p, MetaAdapterFactory.getProperty(0xf280165065d5424eL, 0xbb1b463a8781b786L, 0x7c7f5b2f31990287L, 0x7c7f5b2f31990288L, "text")), n, messages);
      } else if (SNodeOperations.isInstanceOf(n, SNodeOperations.asSConcept(mySingleLineCmment))) {
        SNode p = SNodeOperations.as(n, SNodeOperations.asSConcept(mySingleLineCmment));
        spellCheck(SPropertyOperations.getString(p, MetaAdapterFactory.getProperty(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0x57d533a7af15ed3dL, 0x57d533a7af15ed3eL, "text")), n, messages);
      } else if (SNodeOperations.isInstanceOf(n, SNodeOperations.asSConcept(word))) {
        SNode w = SNodeOperations.as(n, SNodeOperations.asSConcept(word));
        spellCheck(SPropertyOperations.getString(w, MetaAdapterFactory.getProperty(0xc7fb639fbe784307L, 0x89b0b5959c3fa8c8L, 0x229012ddae35f04L, 0x229012ddae35f05L, "value")), n, messages);
      } else if (SNodeOperations.isInstanceOf(n, SNodeOperations.asSConcept(myStringLiteral))) {
        SNode l = SNodeOperations.as(n, SNodeOperations.asSConcept(myStringLiteral));
        spellCheck(SPropertyOperations.getString(l, MetaAdapterFactory.getProperty(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xf93d565d10L, 0xf93d565d11L, "value")), n, messages);
      }
    }
    return new UpdateResult.Completed(true, messages);
  }

  private void spellCheck(String text, SNode n, List<EditorMessage> messages) {
    if ((text == null || text.length() == 0)) {
      return;
    }
    final SpellCheckerManager spcm = SpellCheckerManager.getInstance(myProject);

    ArrayList<String> mistakes = null;
    for (String w : myWordSplit.split(text, 0)) {
      if (w.length() < 2 || !(spcm.hasProblem(w))) {
        continue;
      }
      int s = 0;
      int e = w.length() - 1;
      while (s < e) {
        // main purpose is to strip off quotation marks around words, but do not touch apostrophes (e.g. "isn't") 
        final char c1 = w.charAt(s);
        final char c2 = w.charAt(e);
        final int t1 = Character.getType(c1);
        final int t2 = Character.getType(c2);
        if (t1 == Character.OTHER_PUNCTUATION && c1 == c2) {
          s++;
          e--;
          continue;
        }
        if (t1 == Character.INITIAL_QUOTE_PUNCTUATION && t2 == Character.FINAL_QUOTE_PUNCTUATION) {
          // e.g. « and » 
          s++;
          e--;
          continue;
        }
        boolean found = false;
        if (t1 == Character.START_PUNCTUATION) {
          s++;
          found = true;
          // fallthrough 
        }
        if (t2 == Character.END_PUNCTUATION) {
          e--;
          found = true;
          // fall through 
        }
        if (!(found)) {
          break;
        }
      }
      if (s >= e) {
        // s==e is legitimate 1-letter case, but I don't care to check it. 
        continue;
      }
      if (s > 0 || e < w.length() - 1) {
        // we stripped off some heading/trailing chars 
        w = w.substring(s, e + 1);
        if (!(spcm.hasProblem(w))) {
          continue;
        }
        // otherwise, fall through 
      }
      if (!(isRegularWord(w))) {
        // not sure whether 'word' check is faster than dictionary presence check, assume latter is faster, hence comes first. 
        continue;
      }
      if (mistakes == null) {
        mistakes = new ArrayList<String>(4);
      }
      mistakes.add(w);
    }
    if (mistakes == null) {
      return;
    }
    messages.add(new CommentSpellChecker.M(mistakes, n, this));
  }

  private boolean isRegularWord(String w) {
    for (int i = 0; i < w.length(); i++) {
      char ch = w.charAt(i);
      // any non-letter character immediately renders word irregular. perhaps, shall treat quoted words separately (words that start and end with the same quotation mark) 
      if (!(Character.isLetter(ch))) {
        return false;
      }
      if (Character.isUpperCase(ch) && i > 0) {
        return false;
      }
    }
    return true;
  }

  /*package*/ static class M extends DefaultEditorMessage {
    private final String[] myWords;
    /*package*/ M(List<String> mistakes, SNode n, EditorMessageOwner owner) {
      super(n, JBColor.GRAY, msg(mistakes), owner);
      myWords = new String[mistakes.size()];
      mistakes.toArray(myWords);
    }

    private static String msg(List<String> mistakes) {
      if (mistakes.size() == 1) {
        return String.format("Typo in word '%s'", mistakes.get(0));
      } else if (mistakes.size() == 2) {
        return String.format("Typo in words '%s' and '%s'", mistakes.get(0), mistakes.get(1));
      } else {
        return String.format("Typo in words '%s', '%s',...", mistakes.get(0), mistakes.get(1));
      }
    }


    @Override
    public boolean acceptCell(EditorCell cell, EditorComponent editor) {
      return cell instanceof EditorCell_Label && editor.isValid(cell);
    }

    @Override
    public void paint(Graphics g, EditorComponent editorComponent, EditorCell cell) {
      if (cell instanceof EditorCell_Label) {
        TextLine tl = ((EditorCell_Label) cell).getRenderedTextLine();
        final int y = cell.getY() + cell.getHeight() - ColorAndGraphicsUtil.WAVE_HEIGHT;
        FontMetrics fm = tl.getFontMetrics();
        final String renderedText = tl.getText();
        final char[] renderedChars = renderedText.toCharArray();
        g.setColor(getColor());
        int s = 0;
        // highlight each word only once (just for simplicity now, perhaps, shall highlight all entries. just need to be careful about 
        // word boundaries to avoid sub-matches) 
        // assume mistakes are reported in the order they are encountered in the text (though not a big deal not to care about order) 
        for (String w : myWords) {
          int i = renderedText.indexOf(w, s);
          if (i >= s) {
            int x1 = fm.charsWidth(renderedChars, 0, i);
            int x2 = fm.charsWidth(renderedChars, 0, i + w.length());
            ColorAndGraphicsUtil.drawWave(g, cell.getX() + x1, cell.getX() + x2, y);
            s = i + w.length();
          }
        }
      } else {
        ModelProblemMessage.drawWaveUnderCell(g, getColor(), cell);
      }
    }
  }
}
