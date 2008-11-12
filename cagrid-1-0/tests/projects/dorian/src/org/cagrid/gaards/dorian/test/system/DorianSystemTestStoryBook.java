package org.cagrid.gaards.dorian.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class DorianSystemTestStoryBook {

    public static File DORIAN_PROPERTIES_FILE = new File("resources/dorian.properties");

    @Test
    public void dorianIdentityProviderAuthenticationTest() throws IOException {
        Story test = new DorianIdentityProviderAuthenticationTest(ServiceContainerFactory
            .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER), DORIAN_PROPERTIES_FILE);
        test.run();
    }

    @Test
    public void dorianLoca;IdentityProviderTest() throws IOException {
    	Story test = new DorianLocaIdentityProviderTest(ServiceContainerFactory
            .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER), DORIAN_PROPERTIES_FILE);
    	test.run();
    }
            
    @Test
    public void dorianRemoteIdentityProviderTest() throws IOException {
    	Story test = new DorianRemoteIdentityProviderTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER), null, DORIAN_PROPERTIES_FILE, new File(
                "resources/authentication-config.xml"));
    	test.run();

    }

}
