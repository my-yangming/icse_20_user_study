package me.coley.recaf.ui.component.editor;

import me.coley.recaf.event.*;
import org.controlsfx.control.PropertySheet.Item;
import org.objectweb.asm.tree.ClassNode;
import javafx.scene.Node;
import javafx.scene.control.*;
import me.coley.event.Bus;
import me.coley.recaf.ui.component.*;

/**
 * String editor that also emits a ClassRenameEvent when the enter key is
 * pressed.
 * 
 * @author Matt
 */
public class ClassNameEditor extends StagedCustomEditor<String> {
	public ClassNameEditor(Item item) {
		super(item);
	}

	@Override
	public Node getEditor() {
		ReflectiveClassNodeItem refItem = (ReflectiveClassNodeItem) item;
		ClassNode cn = refItem.getNode();
		TextField txtName = new TextField();
		txtName.setText(cn.name);
		txtName.setOnAction(e -> rename(cn, txtName));
		return txtName;
	}

	private void rename(ClassNode node, TextField txtName) {
		String text = txtName.getText();
		if (!txtName.isDisabled() && !text.equals(node.name)) {
			Bus.post(new ClassRenameEvent(node, node.name, text));
			Bus.post(new ClassReloadEvent(node.name, text));
			Bus.post(new ClassHierarchyUpdateEvent());
			// use disable property to prevent-double send
			txtName.setDisable(true);
		}
	}
}
