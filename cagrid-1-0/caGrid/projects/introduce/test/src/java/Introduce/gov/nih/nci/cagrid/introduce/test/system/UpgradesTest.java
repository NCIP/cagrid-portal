package gov.nih.nci.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo1;
import gov.nih.nci.cagrid.introduce.test.steps.CleanupGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.DeployGlobusServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.InvokeSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.StartGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.StopGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.UnzipOldServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.UpgradesStep;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;


public class UpgradesTest extends Story {
    private TestCaseInfo tci1;

    private GlobusHelper helper;


    public UpgradesTest() {
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
        this.helper = new GlobusHelper(false, new File(IntroduceTestConstants.TEST_TEMP),
            IntroduceTestConstants.TEST_PORT);
        Vector steps = new Vector();

        try {
            steps.add(new CreateGlobusStep(this.helper));
            steps.add(new UnzipOldServiceStep("." + File.separator + "test" + File.separator + "resources"
                + File.separator + "serviceVersions" + File.separator + "IntroduceTest.zip", this.tci1));
            steps.add(new UpgradesStep(this.tci1, true));
            steps.add(new DeployGlobusServiceStep(this.helper, this.tci1));
            steps.add(new StartGlobusStep(this.helper));
            steps.add(new InvokeSimpleMethodImplStep(this.tci1, "newMethod", false));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {

        super.storySetUp();

        StopGlobusStep step2 = new StopGlobusStep(this.helper);
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }

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
        StopGlobusStep step2 = new StopGlobusStep(this.helper);
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        CleanupGlobusStep step3 = new CleanupGlobusStep(this.helper);
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
        TestResult result = runner.doRun(new TestSuite(UpgradesTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
