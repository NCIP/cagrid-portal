package gov.nih.nci.cagrid.introduce.portal.preferences;

import javax.swing.tree.DefaultTreeModel;


public class ServiceURLsPreferencesTreeNode extends PreferencesTypeTreeNode {

	public ServiceURLsPreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
	}


	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(new ServiceURLsPreferencesConfigurationPanel());
	}


	public void initialize() {
		
	}

}
