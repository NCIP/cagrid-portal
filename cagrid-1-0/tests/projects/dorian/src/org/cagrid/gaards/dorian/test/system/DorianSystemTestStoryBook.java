package org.cagrid.gaards.dorian.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.IOException;


public class DorianSystemTestStoryBook extends StoryBook {
	
	
	protected void stories() {
		try {
			this.addStory(new DorianSystemTest(
							ServiceContainerFactory
									.createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
