package org.cagrid.gaards.authentication.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class AuthenticationServiceTestsStoryBook {
	
	

    @Test
    public void authenticationServiceTests() throws IOException {
    	Story s = new AuthenticationServiceTest(
							ServiceContainerFactory
									.createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER),
			new File("resources/authentication-config.xml"));

    	s.run();
	}

}
