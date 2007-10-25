package gov.nih.nci.cagrid.introduce.portal.extension.preferences.tree;





import gov.nih.nci.cagrid.introduce.portal.extension.preferences.ExtensionsPreferencesConfigurationPanel;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferenceConfigurationContainerPanel;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferencesTypeTreeNode;

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
