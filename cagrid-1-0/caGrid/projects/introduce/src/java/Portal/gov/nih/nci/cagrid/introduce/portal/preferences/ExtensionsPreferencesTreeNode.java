package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionsPreferencesConfigurationPanel;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;


public class ExtensionsPreferencesTreeNode extends PreferencesTypeTreeNode {

	public ExtensionsPreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
	}


	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(new PreferencesConfigurationPanelAdapter());
	}


	public void initialize() {
		ExtensionsLoader loader = ExtensionsLoader.getInstance();
		List serviceExtensions = loader.getServiceExtensions();
		for (int i = 0; i < serviceExtensions.size(); i++) {
			ServiceExtensionDescriptionType service = (ServiceExtensionDescriptionType) serviceExtensions.get(i);
			try {
				ExtensionsPreferencesConfigurationPanel panel = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
					.getExtensionsPreferencesConfigurationPanel(service.getName());
				if (panel != null) {
					ExtensionPreferencesTreeNode node = new ExtensionPreferencesTreeNode(panel, service
						.getDisplayName(), this.getModel());

					getModel().insertNodeInto(node, this, this.getChildCount());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List discoveryExtensions = loader.getDiscoveryExtensions();
		for (int i = 0; i < discoveryExtensions.size(); i++) {
			DiscoveryExtensionDescriptionType discovery = (DiscoveryExtensionDescriptionType) discoveryExtensions
				.get(i);
			try {
				ExtensionsPreferencesConfigurationPanel panel = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
					.getExtensionsPreferencesConfigurationPanel(discovery.getName());
				if (panel != null) {
					ExtensionPreferencesTreeNode node = new ExtensionPreferencesTreeNode(panel, discovery
						.getDisplayName(), this.getModel());
					getModel().insertNodeInto(node, this, this.getChildCount());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
