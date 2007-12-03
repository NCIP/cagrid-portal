package gov.nih.nci.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfoMain;
import gov.nih.nci.cagrid.introduce.test.steps.InvokeClientStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.UnzipOldServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.UpgradesStep;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.log4j.PropertyConfigurator;


public class Upgrade_1_0_Test extends Story {
    private TestCaseInfo tci1;

    private ServiceContainer container;


    public Upgrade_1_0_Test() {
        this.setName("Introduce Upgrades System Test");
        PropertyConfigurator.configure("." + File.separator + "conf" + File.separator + "introduce" + File.separator
            + "log4j.properties");
    }
    
    
    public String getName() {
        return "Introduce Upgrades System Test";
    }
    
    
    public String getDescription() {
        return "Testing the Introduce code generation tools";
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();

        try {
            steps.add(new UnpackContainerStep(container));
            steps.add(new UnzipOldServiceStep("." + File.separator + "test" + File.separator + "resources"
                + File.separator + "serviceVersions" + File.separator + "IntroduceTestService-1_0.zip", this.tci1));
            steps.add(new UpgradesStep(this.tci1, true));
            steps.add(new DeployServiceStep(container, tci1.getDir()));
            steps.add(new StartContainerStep(container));
            steps.add(new InvokeClientStep(container, this.tci1));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
        // init the container
        try {
            container = ServiceContainerFactory.createContainer(
                ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
        this.tci1 = new TestCaseInfoMain();
        
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(this.tci1);
        try {
           step1.runStep();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        return true;
    }


    protected void storyTearDown() throws Throwable {
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(this.tci1);
        try {
//            step1.runStep();
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


    /**
     * Convenience method for running all the Steps in this Story.
     */
    public static void main(String args[]) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(Upgrade_1_0_Test.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
