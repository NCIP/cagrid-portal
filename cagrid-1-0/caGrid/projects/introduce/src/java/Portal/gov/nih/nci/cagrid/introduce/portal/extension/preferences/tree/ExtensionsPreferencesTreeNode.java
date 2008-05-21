package gov.nih.nci.cagrid.introduce.portal.extension.preferences.tree;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.extension.preferences.ExtensionPreferenceTool;
import gov.nih.nci.cagrid.introduce.portal.extension.preferences.ExtensionsPreferencesConfigurationPanel;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferenceConfigurationContainerPanel;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferencesConfigurationPanelAdapter;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferencesTypeTreeNode;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;


public class ExtensionsPreferencesTreeNode extends PreferencesTypeTreeNode {
    
    private static final Logger logger = Logger.getLogger(ExtensionsPreferencesTreeNode.class);

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
				ExtensionsPreferencesConfigurationPanel panel = ExtensionPreferenceTool
					.getExtensionsPreferencesConfigurationPanel(service.getName());
				if (panel != null) {
					ExtensionPreferencesTreeNode node = new ExtensionPreferencesTreeNode(panel, service
						.getDisplayName(), this.getModel());

					getModel().insertNodeInto(node, this, this.getChildCount());
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		List discoveryExtensions = loader.getDiscoveryExtensions();
		for (int i = 0; i < discoveryExtensions.size(); i++) {
			DiscoveryExtensionDescriptionType discovery = (DiscoveryExtensionDescriptionType) discoveryExtensions
				.get(i);
			try {
				ExtensionsPreferencesConfigurationPanel panel = ExtensionPreferenceTool
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
