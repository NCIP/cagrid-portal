package org.cagrid.gaards.dorian.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.File;
import java.io.IOException;


public class DorianSystemTestStoryBook extends StoryBook {

    public static File DORIAN_PROPERTIES_FILE = new File("resources/dorian.properties");


    protected void stories() {
        try {
            
            /*
            this.addStory(new DorianIdentityProviderAuthenticationTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER), DORIAN_PROPERTIES_FILE));
            this.addStory(new DorianLocaIdentityProviderTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER), DORIAN_PROPERTIES_FILE));
*/
            this.addStory(new DorianRemoteIdentityProviderTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER), null, DORIAN_PROPERTIES_FILE, new File(
                "resources/authentication-config.xml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
