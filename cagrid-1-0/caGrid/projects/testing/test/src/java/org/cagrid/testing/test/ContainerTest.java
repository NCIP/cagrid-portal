package org.cagrid.testing.test;


import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.story.ServiceStoryBase;

import java.util.Vector;



public class ContainerTest extends ServiceStoryBase {

   

    public ContainerTest(ServiceContainer container) {
       super(container);
    }
    
    public ContainerTest() {
       
        try {
            this.setContainer(ServiceContainerFactory.createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }


    public String getDescription() {
        if(getContainer().getProperties().isSecure()){
            return "Secure Transfer Service Test";
        }
        return "Transfer Service Test";
    }


    protected Vector steps() {
        Vector steps = new Vector();
        try {
            steps.add(new UnpackContainerStep(getContainer()));
            
            steps.add(new StartContainerStep(getContainer()));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {

        return true;
    }


    protected void storyTearDown() throws Throwable {
     
        StopContainerStep step2 = new StopContainerStep(getContainer());
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        DestroyContainerStep step3 = new DestroyContainerStep(getContainer());
        try {
            step3.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
