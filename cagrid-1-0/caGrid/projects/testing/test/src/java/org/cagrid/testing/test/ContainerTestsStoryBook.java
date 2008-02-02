package org.cagrid.testing.test;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.IOException;


public class ContainerTestsStoryBook extends StoryBook {

    protected void stories() {
        try {
        	this.addStory(new ContainerTest(ServiceContainerFactory
                    .createContainer(ServiceContainerType.GLOBUS_CONTAINER)));
        	this.addStory(new ContainerTest(ServiceContainerFactory
                    .createContainer(ServiceContainerType.TOMCAT_CONTAINER)));
            this.addStory(new ContainerTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
