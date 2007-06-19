package gov.nih.nci.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo1;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo2;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo3;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo4;
import gov.nih.nci.cagrid.introduce.test.steps.AddResourcePropertyStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.CleanupGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.DeployGlobusServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.StartGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.StopGlobusStep;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;


public class ResourceCreationTest extends Story {
    private TestCaseInfo tci1;

    private TestCaseInfo tci2;
    
    private TestCaseInfo tci3;
    
    private TestCaseInfo tci4;

    private GlobusHelper helper;


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
        helper = new GlobusHelper(false, new File(IntroduceTestConstants.TEST_TEMP), IntroduceTestConstants.TEST_PORT);
        Vector steps = new Vector();

        try {
            steps.add(new CreateGlobusStep(helper));
            steps.add(new CreateSkeletonStep(tci1, true));
            steps.add(new AddServiceStep(tci2, false));
            steps.add(new AddServiceStep(tci3, false));
            steps.add(new AddServiceStep(tci4, false));
            steps.add(new AddResourcePropertyStep(tci1, false));
            steps.add(new AddResourcePropertyStep(tci2, false));
            steps.add(new AddResourcePropertyStep(tci3, false));
            steps.add(new AddResourcePropertyStep(tci4, true));
            steps.add(new DeployGlobusServiceStep(helper, tci1));
            steps.add(new StartGlobusStep(helper));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
        // TODO Auto-generated method stub
        super.storySetUp();

        StopGlobusStep step2 = new StopGlobusStep(helper);
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci1);
        try {
            step1.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StopGlobusStep step2 = new StopGlobusStep(helper);
        try {
            step2.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CleanupGlobusStep step3 = new CleanupGlobusStep(helper);
        try {
            step3.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
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
