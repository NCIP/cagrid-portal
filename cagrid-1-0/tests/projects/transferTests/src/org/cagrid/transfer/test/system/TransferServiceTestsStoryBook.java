package org.cagrid.transfer.test.system;

import java.io.IOException;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;


public class TransferServiceTestsStoryBook extends StoryBook {

    @Override
    protected void stories() {
        try {
            this.addStory(new TransferServiceTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.TOMCAT_CONTAINER)));
            this.addStory(new TransferServiceTest(ServiceContainerFactory
                .createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}