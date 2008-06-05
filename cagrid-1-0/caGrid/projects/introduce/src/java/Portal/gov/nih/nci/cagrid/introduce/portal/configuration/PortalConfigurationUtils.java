package gov.nih.nci.cagrid.introduce.portal.configuration;

import org.cagrid.grape.GridApplication;

import gov.nih.nci.cagrid.introduce.beans.configuration.IntroducePortalConfiguration;
import gov.nih.nci.cagrid.introduce.beans.configuration.IntroduceServiceDefaults;

public class PortalConfigurationUtils {
    
    public static IntroducePortalConfiguration getIntroducePortalConfiguration() {
        try {
            return (IntroducePortalConfiguration)GridApplication.getContext().getConfigurationManager().getConfigurationObject("introducePortal");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    public static IntroduceServiceDefaults getIntroduceServiceDefaults() {
        try {
            return (IntroduceServiceDefaults)GridApplication.getContext().getConfigurationManager().getConfigurationObject("introduceServiceDefaults");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
