package org.cagrid.transfer.test.system;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.InvokeClientStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.testing.system.deployment.SecureContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;
import org.cagrid.transfer.test.system.steps.AddCreateTransferMethodImplStep;
import org.cagrid.transfer.test.system.steps.AddCreateTransferMethodStep;
import org.cagrid.transfer.test.system.steps.CopyProxyStep;


public class TransferServiceTest extends Story {

    private TestCaseInfo tci = new TransferTestCaseInfo();
    private ServiceContainer container;


    public TransferServiceTest() {
        PropertyConfigurator.configure("." + File.separator + "conf" + File.separator + "introduce" + File.separator
            + "log4j.properties");
    }


    public String getDescription() {
        return "Transfer Service Test";
    }


    protected Vector steps() {
        Vector steps = new Vector();
        try {
            steps.add(new UnpackContainerStep(container));
            steps.add(new DeployServiceStep(container, "../transfer"));

            steps.add(new CreateSkeletonStep(tci, false));
            steps.add(new AddCreateTransferMethodStep(tci,container, false));
            steps.add(new AddCreateTransferMethodImplStep(tci, false));
            if (container instanceof SecureContainer) {
                steps.add(new CopyProxyStep((SecureContainer) container, tci));
            }
            steps.add(new DeployServiceStep(container, tci.getDir()));
            steps.add(new StartContainerStep(container));

            steps.add(new InvokeClientStep(container, tci));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
        // init the container
        try {
            container = ServiceContainerFactory.createContainer(ServiceContainerType.TOMCAT_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }

        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci);
        try {
            step1.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }


    protected void storyTearDown() throws Throwable {
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci);
        try {
            step1.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        StopContainerStep step2 = new StopContainerStep(container);
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        DestroyContainerStep step3 = new DestroyContainerStep(container);
        try {
            step3.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
