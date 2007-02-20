package gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree;

import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultTreeModel;

public class ExtensionUpdateTreeNode extends UpdateTypeTreeNode {
	private JCheckBox checkBox;
	private boolean installed = false;
	private ExtensionType extension;

	public ExtensionUpdateTreeNode(String displayName, DefaultTreeModel model,
			ExtensionType extension) {
		super(displayName, model);
		this.extension = extension;
		checkBox = new JCheckBox(displayName);
		if (model != null) {
			initialize();
		}
		if ((ExtensionsLoader.getInstance().getExtension(extension.getName()) != null)
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

	public boolean isSelected() {
		return getCheckBox().isSelected();
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public boolean isInstalled() {
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

	public ExtensionType getExtension() {
		return extension;
	}

	public void setExtension(ExtensionType extension) {
		this.extension = extension;
	}

}
