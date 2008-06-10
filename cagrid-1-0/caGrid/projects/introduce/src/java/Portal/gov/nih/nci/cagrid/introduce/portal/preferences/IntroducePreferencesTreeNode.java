package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferenceConfigurationContainerPanel;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferencesConfigurationPanelAdapter;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.PreferencesTypeTreeNode;

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
		
	}

}
