package org.cagrid.gaards.authentication.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.File;
import java.io.IOException;

public class AuthenticationServiceTestsStoryBook extends StoryBook {
	
	

	@Override
	protected void stories() {
		try {
			this
					.addStory(new AuthenticationServiceTest(
							ServiceContainerFactory
									.createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER),
							new File("resources/authentication-config.xml")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
