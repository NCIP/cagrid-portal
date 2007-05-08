package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.common.ResourceManager;

import java.util.Enumeration;


public class ServiceURLsPreferencesConfigurationPanel extends DynamicPreferencesConfigurationPanel {

	public ServiceURLsPreferencesConfigurationPanel() {
		super();
		initialize();
	}


	private void initialize() {
		try {
			Enumeration enumeration = ResourceManager.getServiceURLPropertyKeys();
			int count = 0;
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				String value = ResourceManager.getServiceURLProperty(key);
				addTextField(this, key, value, count++, true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void apply() {
		Enumeration enumeration;
		try {
			enumeration = ResourceManager.getServiceURLPropertyKeys();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				String value = this.getTextFieldValue(key);
				ResourceManager.setServiceURLProperty(key, value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
