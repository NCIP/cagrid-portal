package org.cagrid.testing.test;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.IOException;


public class ContainerTestsStoryBook {

    @org.junit.Test
    public void testAllContainers() {
        try {
        	
        	Story s = new ContainerTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.GLOBUS_CONTAINER));
        	
        	s.run();
        	
        	Story s2 = new ContainerTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.TOMCAT_CONTAINER));
        	
        	s2.run();
        	
        	Story s3 = new ContainerTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER));
        	
        	s3.run();
        	
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
