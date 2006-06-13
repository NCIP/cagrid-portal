package gov.nih.nci.cagrid.introduce.portal.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.common.BasePreferenceConfigurationPanel;

public abstract class ExtensionsPreferencesConfigurationPanel extends
		BasePreferenceConfigurationPanel {
	ExtensionDescription ext;
	
	public ExtensionsPreferencesConfigurationPanel(ExtensionDescription ext){
		super();
		this.ext = ext;
		initialize();
	}
	
	public ExtensionDescription getExtensionDescription(){
		return this.ext;
	}
	
	public abstract void initialize();
}  //  @jve:decl-index=0:visual-constraint="10,10"
