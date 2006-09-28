package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionsPreferencesConfigurationPanel;

import java.util.List;

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
