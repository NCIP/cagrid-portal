package gov.nih.nci.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo1;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo2;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo3;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo4;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo5;
import gov.nih.nci.cagrid.introduce.test.steps.AddResourcePropertyStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.CleanupContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.DeployServiceToContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.StartContainerStep;
import gov.nih.nci.cagrid.introduce.test.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.core.TestingConstants;
import gov.nih.nci.cagrid.testing.system.deployment.PortPreference;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Step;
import com.atomicobject.haste.framework.Story;


public class ResourceCreationTest extends Story {
    private TestCaseInfo tci1;

    private TestCaseInfo tci2;

    private TestCaseInfo tci3;

    private TestCaseInfo tci4;
    
    private TestCaseInfo tci5;

    private static ServiceContainer container = null;
    
    static {
        try {
            PortPreference ports = new PortPreference(
                Integer.valueOf(TestingConstants.TEST_PORT_LOWER_BOUND.intValue() + 1001), 
                Integer.valueOf(TestingConstants.TEST_PORT_UPPER_BOUND.intValue() + 1001), null);
            container = ServiceContainerFactory.createContainer(
                ServiceContainerType.GLOBUS_CONTAINER, null, ports);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }

    public ResourceCreationTest() {
        this.setName("Introduce Resource Creation System Test");
    }


    public String getName() {
        return "Introduce Resource Creation System Test";
    }


    public String getDescription() {
        return "Testing the Introduce code generation tools";
    }


    protected Vector steps() {
        tci1 = new TestCaseInfo1();
        tci2 = new TestCaseInfo2();
        tci3 = new TestCaseInfo3();
        tci4 = new TestCaseInfo4();
        tci5 = new TestCaseInfo5();
        Vector<Step> steps = new Vector<Step>();

        try {
            steps.add(new CreateSkeletonStep(tci1, true));
            steps.add(new AddServiceStep(tci2, false));
            steps.add(new AddServiceStep(tci3, false));
            steps.add(new AddServiceStep(tci4, false));
            steps.add(new AddServiceStep(tci5, false));
            steps.add(new AddResourcePropertyStep(tci1, false));
            steps.add(new AddResourcePropertyStep(tci2, false));
            steps.add(new AddResourcePropertyStep(tci3, false));
            steps.add(new AddResourcePropertyStep(tci4, false));
            steps.add(new AddResourcePropertyStep(tci5, true));
            steps.add(new DeployServiceToContainerStep(container, tci1));
            // TODO: do we need to deploy the other services (tci2-5)?
            steps.add(new StartContainerStep(container));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
        super.storySetUp();

        Step step2 = new CreateContainerStep(container);
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci1);
        try {
            step1.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }


    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci1);
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
        TestResult result = runner.doRun(new TestSuite(ResourceCreationTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
