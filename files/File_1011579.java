package jetbrains.mps.execution.configurations.implementation.plugin.plugin;

/*Generated by MPS */

import com.intellij.ui.components.JBPanel;
import com.intellij.openapi.project.Project;
import java.awt.GridBagLayout;
import jetbrains.mps.internal.collections.runtime.ListSequence;
import java.util.ArrayList;
import org.jetbrains.mps.openapi.model.SNodeReference;
import jetbrains.mps.ide.common.LayoutUtil;
import java.util.List;
import jetbrains.mps.execution.lib.PointerUtils;
import jetbrains.mps.execution.lib.ClonableList;
import jetbrains.mps.internal.collections.runtime.ISelector;

public class DeployEditorPanel extends JBPanel {
  private final PluginsListPanel myPluginsPanel;

  public DeployEditorPanel(Project project) {
    super(new GridBagLayout());
    myPluginsPanel = new PluginsListPanel(project);
    myPluginsPanel.setData(ListSequence.fromList(new ArrayList<SNodeReference>()));
    add(myPluginsPanel, LayoutUtil.createPanelConstraints(0));
  }

  public void reset(DeployPluginsSettings_Configuration settings) {
    List<SNodeReference> clonableListToNodes = PointerUtils.clonableListToNodes(settings.getPluginsToDeploy());
    myPluginsPanel.setData(clonableListToNodes);
  }

  public void apply(DeployPluginsSettings_Configuration settings) {
    ClonableList<String> list = settings.getPluginsToDeploy();
    list.clear();
    ListSequence.fromList(list.getData()).addSequence(ListSequence.fromList(myPluginsPanel.getItems()).select(new ISelector<SNodeReference, String>() {
      public String select(SNodeReference it) {
        return PointerUtils.pointerToString(it);
      }
    }));
  }

  public void dispose() {
  }
}
