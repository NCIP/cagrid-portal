package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.steps.AddComplexMethodWithFaultStep;
import gov.nih.nci.cagrid.introduce.steps.AddComplexMethodWithFaulsAndArraysStep;
import gov.nih.nci.cagrid.introduce.steps.AddMetadataStep;
import gov.nih.nci.cagrid.introduce.steps.AddMetadatatWithLoadFromFileStep;
import gov.nih.nci.cagrid.introduce.steps.AddServiceStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodWithArraysStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodWithReturnStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodWithFaultStep;
import gov.nih.nci.cagrid.introduce.steps.AddServicePropertiesStep;
import gov.nih.nci.cagrid.introduce.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.steps.ModifySimpleMethodStep;
import gov.nih.nci.cagrid.introduce.steps.RemoveAllMetadataStep;
import gov.nih.nci.cagrid.introduce.steps.RemoveAllServicePropertiesStep;
import gov.nih.nci.cagrid.introduce.steps.RemoveMethodStep;
import gov.nih.nci.cagrid.introduce.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.steps.RollBackStep;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;


public class SyncToolsTest extends Story {
	private TestCaseInfo tci1;
	private TestCaseInfo tci2;


	protected Vector steps() {
		this.tci1 = new TestCaseInfo1();
		this.tci2 = new TestCaseInfo2();
		Vector steps = new Vector();

		steps.add(new CreateSkeletonStep(tci1));
		steps.add(new AddServiceStep(tci2));
		steps.add(new AddSimpleMethodStep(tci1, "newMethod"));
		steps.add(new AddSimpleMethodStep(tci2, "newMethod2"));
		steps.add(new ModifySimpleMethodStep(tci1, "newMethod"));
		steps.add(new ModifySimpleMethodStep(tci2, "newMethod2"));
		steps.add(new RemoveMethodStep(tci1, "newMethod"));
		steps.add(new RemoveMethodStep(tci2, "newMethod2"));
		steps.add(new AddSimpleMethodWithFaultStep(tci1, "newMethodWithFault"));
		steps.add(new AddSimpleMethodWithReturnStep(tci1, "newMethodWithReturn"));
		steps.add(new AddSimpleMethodWithArraysStep(tci1, "newMethodWithArrays"));
		steps.add(new RollBackStep(tci1));
		steps.add(new AddComplexMethodWithFaultStep(tci1, "newComplexMethodWithFault"));
		steps.add(new AddComplexMethodWithFaulsAndArraysStep(tci1, "newComplexMethodWithFaultStepsAndArrays"));
		steps.add(new AddMetadataStep(tci1));
		steps.add(new AddMetadatatWithLoadFromFileStep(tci1));
		steps.add(new RemoveAllMetadataStep(tci1));
		steps.add(new AddServicePropertiesStep(tci1));
		steps.add(new RemoveAllServicePropertiesStep(tci1));
		return steps;
	}


	public String getDescription() {
		return "Tests the code generation tools";
	}


	protected void storyTearDown() throws Throwable {
		RemoveSkeletonStep step = new RemoveSkeletonStep(tci1);
		step.runStep();
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
