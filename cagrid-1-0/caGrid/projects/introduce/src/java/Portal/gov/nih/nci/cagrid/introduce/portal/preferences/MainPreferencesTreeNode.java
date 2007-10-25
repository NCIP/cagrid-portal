package gov.nih.nci.cagrid.introduce.portal.preferences;





import gov.nih.nci.cagrid.introduce.portal.extension.preferences.tree.ExtensionsPreferencesTreeNode;

import javax.swing.tree.DefaultTreeModel;


public class MainPreferencesTreeNode extends PreferencesTypeTreeNode {

	public MainPreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
	}


	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(new PreferencesConfigurationPanelAdapter());
	}


	public void initialize() {
		IntroducePreferencesTreeNode general = new IntroducePreferencesTreeNode("General", getModel());
		getModel().insertNodeInto(general, this, this.getChildCount());
		ExtensionsPreferencesTreeNode extensions = new ExtensionsPreferencesTreeNode("Extensions", getModel());
		getModel().insertNodeInto(extensions, this, this.getChildCount());
	}

}
