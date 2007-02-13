package gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree;

import java.awt.Font;

import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ExtensionUpdateTreeNode extends UpdateTypeTreeNode {
	private ExtensionType extension;
	private JCheckBox checkBox;
	private boolean installed = false;;

	public ExtensionUpdateTreeNode(String displayName, DefaultTreeModel model,
			ExtensionType extension) {
		super(displayName, model);
		this.extension = extension;
		checkBox = new JCheckBox(displayName);
		if (model != null) {
			initialize();
		}
		if (ExtensionsLoader.getInstance().getExtension(extension.getName()) != null
				&& ExtensionsLoader.getInstance().getExtension(
						extension.getName()).getVersion().equals(
						extension.getVersion())) {
			checkBox.setEnabled(false);
			checkBox.setSelected(true);
			installed = true;
			checkBox.setText(checkBox.getText() + " installed");
			checkBox.setFont(checkBox.getFont().deriveFont(Font.ITALIC));
		}
	}
	
	public void setInstalled(boolean installed){
		this.installed = installed;
	}
	
	public boolean isInstalled(){
		return installed;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public void initialize() {
	}

	public Object getUserObject() {
		return checkBox;
	}

}
