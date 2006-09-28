package gov.nih.nci.cagrid.introduce.portal.preferences;

import javax.swing.tree.DefaultTreeModel;


public class IntroducePreferencesTreeNode extends PreferencesTypeTreeNode {

	public IntroducePreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
		// TODO Auto-generated constructor stub
	}


	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(new PreferencesConfigurationPanelAdapter());
	}


	public void initialize() {
		ServiceURLsPreferencesTreeNode serviceURLs = new ServiceURLsPreferencesTreeNode("Service URLs", getModel());
		getModel().insertNodeInto(serviceURLs, this, this.getChildCount());
		PreferencesPreferencesTreeNode general = new PreferencesPreferencesTreeNode("Preferences", getModel());
		getModel().insertNodeInto(general, this, this.getChildCount());
	}

}
