package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.portal.preferences.BasePreferenceConfigurationPanel;

public abstract class ExtensionsPreferencesConfigurationPanel extends
		BasePreferenceConfigurationPanel {
	
	public ExtensionsPreferencesConfigurationPanel(){
		super();
		initialize();
	}
	
	public abstract void initialize();
}  //  @jve:decl-index=0:visual-constraint="10,10"
