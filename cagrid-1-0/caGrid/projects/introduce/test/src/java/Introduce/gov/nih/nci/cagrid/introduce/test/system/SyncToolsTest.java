package gov.nih.nci.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo1;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo2;
import gov.nih.nci.cagrid.introduce.test.steps.AddComplexMethodWithFaulsAndArraysStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddComplexMethodWithFaultStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddImportedMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddMetadataStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddMetadatatWithLoadFromFileStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddMethodReturningClientHandleMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddServicePropertiesStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddSimpleMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddSimpleMethodWithArraysStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddSimpleMethodWithFaultStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddSimpleMethodWithReturnStep;
import gov.nih.nci.cagrid.introduce.test.steps.CleanupGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.DeployGlobusServiceStep;
import gov.nih.nci.cagrid.introduce.test.steps.InvokeSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.ModifySimpleMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveAllMetadataStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveAllServicePropertiesStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.RollBackStep;
import gov.nih.nci.cagrid.introduce.test.steps.StartGlobusStep;
import gov.nih.nci.cagrid.introduce.test.steps.StopGlobusStep;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;


public class SyncToolsTest extends Story {
    private TestCaseInfo tci1;

    private TestCaseInfo tci2;

    private GlobusHelper helper;


    public SyncToolsTest() {
        this.setName("Introduce Codegen System Test");
    }
    
    
    public String getName() {
        return "Introduce Codegen System Test";
    }
    
    
    public String getDescription() {
        return "Testing the Introduce code generation tools";
    }


    protected Vector steps() {
        tci1 = new TestCaseInfo1();
        tci2 = new TestCaseInfo2();
        helper = new GlobusHelper(false, new File(IntroduceTestConstants.TEST_TEMP), IntroduceTestConstants.TEST_PORT);
        Vector steps = new Vector();

        try {
            steps.add(new CreateGlobusStep(helper));
            steps.add(new CreateSkeletonStep(tci1, true));
            steps.add(new AddServiceStep(tci2, true));
            steps.add(new AddMethodReturningClientHandleMethodStep(tci1, tci2, "testClientReturn", false, true));
            steps
                .add(new AddMethodReturningClientHandleMethodStep(tci1, tci2, "testClientReturnWithArray", true, true));
            steps.add(new AddMetadataStep(tci1, true));
            steps.add(new AddServicePropertiesStep(tci1, true));
            steps.add(new AddSimpleMethodStep(tci1, "newMethod", false));
            steps.add(new AddSimpleMethodImplStep(tci1, "newMethod", true));
            steps.add(new AddSimpleMethodStep(tci2, "newMethod2", true));
            steps.add(new DeployGlobusServiceStep(helper, tci1));
            steps.add(new StartGlobusStep(helper));
            steps.add(new InvokeSimpleMethodImplStep(tci1, "newMethod", false));
            steps.add(new RemoveSimpleMethodImplStep(tci1, "newMethod", true));
            steps.add(new ModifySimpleMethodStep(tci1, "newMethod", false));
            steps.add(new ModifySimpleMethodStep(tci2, "newMethod2", true));
            steps.add(new RemoveMethodStep(tci1, "newMethod", false));
            steps.add(new RemoveMethodStep(tci2, "newMethod2", false));
            steps.add(new AddSimpleMethodStep(tci2, "newMethod1", true));
            steps.add(new AddImportedMethodStep(tci1, tci2, "newMethod1", true, false));
            steps.add(new AddSimpleMethodWithFaultStep(tci1, "newMethodWithFault", false));
            steps.add(new AddSimpleMethodWithReturnStep(tci1, "newMethodWithReturn", false));
            steps.add(new AddSimpleMethodWithArraysStep(tci1, "newMethodWithArrays", true));
            steps.add(new RollBackStep(tci1));
            steps.add(new AddComplexMethodWithFaultStep(tci1, "newComplexMethodWithFault", false));
            steps
                .add(new AddComplexMethodWithFaulsAndArraysStep(tci1, "newComplexMethodWithFaultStepsAndArrays", true));
            steps.add(new AddMetadatatWithLoadFromFileStep(tci1, true));
            steps.add(new RemoveAllMetadataStep(tci1, true));
            steps.add(new RemoveAllServicePropertiesStep(tci1, true));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
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
        TestResult result = runner.doRun(new TestSuite(SyncToolsTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
