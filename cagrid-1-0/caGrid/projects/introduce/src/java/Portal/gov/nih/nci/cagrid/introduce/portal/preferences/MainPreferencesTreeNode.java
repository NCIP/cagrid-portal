package gov.nih.nci.cagrid.introduce.portal.preferences;

import java.util.List;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionsPreferencesConfigurationPanel;

import javax.swing.tree.DefaultTreeModel;

public class MainPreferencesTreeNode extends PreferencesTypeTreeNode {

	public MainPreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
		// TODO Auto-generated constructor stub
	}

	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(
				new PreferencesConfigurationPanelAdapter());
	}

	public void initialize() {
		IntroducePreferencesTreeNode general = new IntroducePreferencesTreeNode(
				"General", getModel());
		getModel().insertNodeInto(general, this, this.getChildCount());
		ExtensionsLoader loader = ExtensionsLoader.getInstance();
		List serviceExtensions = loader.getServiceExtensions();
		for (int i = 0; i < serviceExtensions.size(); i++) {
			ServiceExtensionDescriptionType service = (ServiceExtensionDescriptionType) serviceExtensions
					.get(i);
			try {
				ExtensionsPreferencesConfigurationPanel panel = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
						.getExtensionsPreferencesConfigurationPanel(service
								.getName());
				if (panel != null) {
					ExtensionPreferencesTreeNode node = new ExtensionPreferencesTreeNode(
							panel, service.getDisplayName(), this.getModel());

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
						.getExtensionsPreferencesConfigurationPanel(discovery
								.getName());
				if (panel != null) {
					ExtensionPreferencesTreeNode node = new ExtensionPreferencesTreeNode(
							panel, discovery.getDisplayName(), this.getModel());
					getModel().insertNodeInto(node, this, this.getChildCount());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
