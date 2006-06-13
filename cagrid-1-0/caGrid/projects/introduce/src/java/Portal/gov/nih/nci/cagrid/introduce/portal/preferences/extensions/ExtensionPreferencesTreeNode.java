package gov.nih.nci.cagrid.introduce.portal.preferences.extensions;

import gov.nih.nci.cagrid.introduce.portal.preferences.PreferenceConfigurationContainerPanel;
import gov.nih.nci.cagrid.introduce.portal.preferences.PreferencesTypeTreeNode;

import javax.swing.tree.DefaultTreeModel;

public class ExtensionPreferencesTreeNode extends PreferencesTypeTreeNode {

	public ExtensionPreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
		// TODO Auto-generated constructor stub
	}

	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return null;
	}

	public void initialize() {
		// TODO Auto-generated method stub
	}
	

}
