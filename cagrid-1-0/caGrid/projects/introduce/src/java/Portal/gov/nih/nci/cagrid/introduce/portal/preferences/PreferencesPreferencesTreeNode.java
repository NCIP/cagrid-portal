package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferenceConfigurationContainerPanel;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferencesTypeTreeNode;

import javax.swing.tree.DefaultTreeModel;


public class PreferencesPreferencesTreeNode extends PreferencesTypeTreeNode {

	public PreferencesPreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
	}


	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(new PreferencesPreferencesConfigurationPanel());
	}


	public void initialize() {

	}

}
