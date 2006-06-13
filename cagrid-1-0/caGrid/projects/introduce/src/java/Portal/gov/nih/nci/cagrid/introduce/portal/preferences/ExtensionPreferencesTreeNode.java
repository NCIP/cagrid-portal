package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionsPreferencesConfigurationPanel;

import javax.swing.tree.DefaultTreeModel;

public class ExtensionPreferencesTreeNode extends PreferencesTypeTreeNode {

	ExtensionsPreferencesConfigurationPanel panel;
	
	public ExtensionPreferencesTreeNode(ExtensionsPreferencesConfigurationPanel panel, String displayName, DefaultTreeModel model) {
		super(displayName, model);
		this.panel = panel;
	}

	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(this.panel);
	}

	public void initialize() {
		// TODO Auto-generated method stub
	}
	

}
