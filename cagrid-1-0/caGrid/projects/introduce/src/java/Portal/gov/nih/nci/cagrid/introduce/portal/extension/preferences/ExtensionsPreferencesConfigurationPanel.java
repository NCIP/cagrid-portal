package gov.nih.nci.cagrid.introduce.portal.extension.preferences;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.portal.preferences.base.BasePreferenceConfigurationPanel;

public abstract class ExtensionsPreferencesConfigurationPanel extends
		BasePreferenceConfigurationPanel {
	ExtensionDescription ext;
	
	public ExtensionsPreferencesConfigurationPanel(ExtensionDescription ext){
		super();
		this.ext = ext;
	}
	
	public ExtensionDescription getExtensionDescription(){
		return this.ext;
	}
}
