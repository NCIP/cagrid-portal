package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


public class PreferencesPreferencesConfigurationPanel extends DynamicPreferencesConfigurationPanel {

    private static final Logger logger = Logger.getLogger(PreferencesPreferencesConfigurationPanel.class);
    
    public PreferencesPreferencesConfigurationPanel() {
        super();
        initialize();
    }


    private boolean hasKey(Enumeration keys, String key) {
        while (keys.hasMoreElements()) {
            String testKey = (String) keys.nextElement();
            if (testKey.equals(key)) {
                return true;
            }
        }
        return false;
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
                }
                if (hasKey(ResourceManager.getConfigurationPropertyKeys(), key + ".options")) {
                    String options = ResourceManager.getConfigurationProperty(key + ".options");
                    StringTokenizer strtok = new StringTokenizer(options, ",", false);
                    List optionsList = new ArrayList();
                    while (strtok.hasMoreElements()) {
                        optionsList.add(strtok.nextToken());
                    }
                    String[] optionsArray = new String[optionsList.size()];
                    optionsList.toArray(optionsArray);
                    addDropDown(this, key, value, optionsArray, count++, true);
                } else if(hasKey(ResourceManager.getConfigurationPropertyKeys(), key + ".boolean")){
                    addCheckBox(this, key, new Boolean(value).booleanValue(), count++, true);
                } else if (!key.endsWith(".options") && !key.endsWith(".boolean")) {
                    addTextField(this, key, value, count++, true);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }


    public void apply() {
        Enumeration enumeration;
        try {
            enumeration = ResourceManager.getConfigurationPropertyKeys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = this.getTextFieldValue(key);
                if (value == null) {
                    value = this.getDropDownValue(key);
                }
                if(value == null){
                    value = this.getCheckBoxValue(key);
                }
                if (value != null) {
                    ResourceManager.setConfigurationProperty(key, value);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
