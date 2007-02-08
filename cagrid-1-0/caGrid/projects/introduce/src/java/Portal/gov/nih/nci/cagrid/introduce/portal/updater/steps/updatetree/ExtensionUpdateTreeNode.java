package gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree;

import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class ExtensionUpdateTreeNode extends UpdateTypeTreeNode {
	private ExtensionType extension;
	private JCheckBox checkBox;

	public ExtensionUpdateTreeNode(String displayName, DefaultTreeModel model, ExtensionType extension) {
		super(displayName, model);
		this.extension = extension;
		checkBox = new JCheckBox(displayName);
		if (model != null) {
			initialize();
		}
	}
	
	public JCheckBox getCheckBox(){
		return checkBox;
	}


	public void initialize() {
	}
	
	public Object getUserObject(){
		return checkBox;
	}

}
