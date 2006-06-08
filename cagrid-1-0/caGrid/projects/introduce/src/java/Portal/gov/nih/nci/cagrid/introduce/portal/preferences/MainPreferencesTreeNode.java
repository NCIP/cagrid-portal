package gov.nih.nci.cagrid.introduce.portal.preferences;

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
		IntroducePreferencesTreeNode general = new IntroducePreferencesTreeNode("General", getModel());
		getModel().insertNodeInto(general,this,this.getChildCount());
	}

}
