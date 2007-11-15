package gov.nih.nci.cagrid.introduce.test.system;

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
import gov.nih.nci.cagrid.introduce.test.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.InvokeSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.ModifySimpleMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveAllServicePropertiesStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Step;
import com.atomicobject.haste.framework.Story;


public class SyncToolsTest extends Story {
    private TestCaseInfo tci1;

    private TestCaseInfo tci2;

    private ServiceContainer container;

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
        // init the container
        try {
            container = ServiceContainerFactory.createContainer(
                ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
        tci1 = new TestCaseInfo1();
        tci2 = new TestCaseInfo2();
        Vector<Step> steps = new Vector<Step>();

        try {
            steps.add(new UnpackContainerStep(container));
            steps.add(new CreateSkeletonStep(tci1, true));
            steps.add(new AddServiceStep(tci2, true));
            steps.add(new AddMethodReturningClientHandleMethodStep(
                tci1, tci2, "testClientReturn", false, true));
            steps.add(new AddMethodReturningClientHandleMethodStep(
                tci1, tci2, "testClientReturnWithArray", true, true));
            steps.add(new AddMetadataStep(tci1, true));
            steps.add(new AddServicePropertiesStep(tci1, true));
            steps.add(new AddSimpleMethodStep(tci1, "newMethod", false));
            steps.add(new AddSimpleMethodImplStep(tci1, "newMethod", true));
            steps.add(new AddSimpleMethodStep(tci2, "newMethod2", true));
            steps.add(new DeployServiceStep(container, tci1.getDir()));
            steps.add(new StartContainerStep(container));
            steps.add(new InvokeSimpleMethodImplStep(container, tci1, "newMethod", false));
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
            //archiving has been moved to the GUI
            //steps.add(new RollBackStep(tci1));
            steps.add(new AddComplexMethodWithFaultStep(tci1, "newComplexMethodWithFault", false));
            steps.add(new AddComplexMethodWithFaulsAndArraysStep(
                tci1, "newComplexMethodWithFaultStepsAndArrays", true));
            steps.add(new AddMetadatatWithLoadFromFileStep(tci1, true));
            //can't do now because main has required resource properties...
            //steps.add(new RemoveAllMetadataStep(tci1, true));
            steps.add(new RemoveAllServicePropertiesStep(tci1, true));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
        super.storySetUp();
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
        StopContainerStep step2 = new StopContainerStep(container);
        try {
            step2.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DestroyContainerStep step3 = new DestroyContainerStep(container);
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
