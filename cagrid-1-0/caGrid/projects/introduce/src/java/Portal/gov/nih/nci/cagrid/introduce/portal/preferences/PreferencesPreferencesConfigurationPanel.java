package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;

import java.util.Enumeration;


public class PreferencesPreferencesConfigurationPanel extends DynamicPreferencesConfigurationPanel {

    public PreferencesPreferencesConfigurationPanel() {
        super();
        initialize();
    }


    private void initialize() {
        try {
            Enumeration enumeration = ResourceManager.getConfigurationPropertyKeys();
            int count = 0;
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = ResourceManager.getConfigurationProperty(key);
                if (key.equals(IntroduceConstants.GLOBUS_LOCATION)) {
                    addTextField(this, key, value, count++, false);
                } else {
                    addTextField(this, key, value, count++, true);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void apply() {
        Enumeration enumeration;
        try {
            enumeration = ResourceManager.getConfigurationPropertyKeys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = this.getTextFieldValue(key);
                ResourceManager.setConfigurationProperty(key, value);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
