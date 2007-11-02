package gov.nih.nci.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo1;
import gov.nih.nci.cagrid.introduce.test.steps.CleanupContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.DeployServiceToContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.InvokeSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.StartContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.StopContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.UnzipOldServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.UpgradesStep;
import gov.nih.nci.cagrid.testing.core.TestingConstants;
import gov.nih.nci.cagrid.testing.system.deployment.PortPreference;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Step;
import com.atomicobject.haste.framework.Story;


public class Upgrade_1_0_Test extends Story {
    private TestCaseInfo tci1;


    private static ServiceContainer container = null;
    
    static {
        try {
            PortPreference ports = new PortPreference(
                Integer.valueOf(TestingConstants.TEST_PORT_LOWER_BOUND.intValue() + 1201), 
                Integer.valueOf(TestingConstants.TEST_PORT_UPPER_BOUND.intValue() + 1201), null);
            container = ServiceContainerFactory.createContainer(
                ServiceContainerType.GLOBUS_CONTAINER, null, ports);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }


    public Upgrade_1_0_Test() {
        this.setName("Introduce Upgrades System Test");
    }
    
    
    public String getName() {
        return "Introduce Upgrades System Test";
    }
    
    
    public String getDescription() {
        return "Testing the Introduce code generation tools";
    }


    protected Vector steps() {
        this.tci1 = new TestCaseInfo1();
        Vector<Step> steps = new Vector<Step>();

        try {
            steps.add(new CreateContainerStep(container));
            steps.add(new UnzipOldServiceStep("." + File.separator + "test" + File.separator + "resources"
                + File.separator + "serviceVersions" + File.separator + "IntroduceTestService-1_0.zip", this.tci1));
            steps.add(new UpgradesStep(this.tci1, true));
            steps.add(new DeployServiceToContainerStep(container, this.tci1));
            steps.add(new StartContainerStep(container));
            steps.add(new InvokeSimpleMethodImplStep(container, this.tci1, "newMethod", false));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
        super.storySetUp();

        RemoveSkeletonStep step1 = new RemoveSkeletonStep(this.tci1);
        try {
           step1.runStep();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        return true;
    }


    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(this.tci1);
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
        CleanupContainerStep step3 = new CleanupContainerStep(container);
        try {
            step3.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    // used to make sure that if we are going to use a junit testsuite to test
    // this
    // that the test suite will not error out looking for a single test......
    public void testDummy() throws Throwable {
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
