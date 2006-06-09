package gov.nih.nci.cagrid.introduce.portal.preferences;

import javax.swing.tree.DefaultTreeModel;

public class IntroducePreferencesTreeNode extends PreferencesTypeTreeNode {

	public IntroducePreferencesTreeNode(String displayName, DefaultTreeModel model) {
		super(displayName, model);
		// TODO Auto-generated constructor stub
	}

	public PreferenceConfigurationContainerPanel getConfigurationPanel() {
		return new PreferenceConfigurationContainerPanel(new IntroducePreferencesConfigurationPanel());
	}

	public void initialize() {
		// TODO Auto-generated method stub
	}
	

}
