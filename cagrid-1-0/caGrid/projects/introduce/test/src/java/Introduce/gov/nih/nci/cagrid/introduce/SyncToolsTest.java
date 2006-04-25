package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.steps.AddComplexMethodWithFaultStep;
import gov.nih.nci.cagrid.introduce.steps.AddComplexMethodWithFaulsAndArraysStep;
import gov.nih.nci.cagrid.introduce.steps.AddMetadataStep;
import gov.nih.nci.cagrid.introduce.steps.AddMetadatatWithLoadFromFileStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodWithArraysStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodWithReturnStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodWithFaultStep;
import gov.nih.nci.cagrid.introduce.steps.AddSServicePropertiesStep;
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
	private TestCaseInfo tci;


	protected Vector steps() {
		this.tci = new TestCaseInfo();
		Vector steps = new Vector();

		steps.add(new CreateSkeletonStep(tci));
		steps.add(new AddSimpleMethodStep(tci, "newMethod"));
		steps.add(new ModifySimpleMethodStep(tci, "newMethod"));
		steps.add(new RemoveMethodStep(tci, "newMethod"));
		steps.add(new AddSimpleMethodWithFaultStep(tci, "newMethodWithFault"));
		steps.add(new AddSimpleMethodWithReturnStep(tci, "newMethodWithReturn"));
		steps.add(new AddSimpleMethodWithArraysStep(tci, "newMethodWithArrays"));
		steps.add(new RollBackStep(tci));
		steps.add(new AddComplexMethodWithFaultStep(tci, "newComplexMethodWithFault"));
		steps.add(new AddComplexMethodWithFaulsAndArraysStep(tci, "newComplexMethodWithFaultStepsAndArrays"));
		steps.add(new AddMetadataStep(tci));
		steps.add(new AddMetadatatWithLoadFromFileStep(tci));
		steps.add(new RemoveAllMetadataStep(tci));
		steps.add(new AddSServicePropertiesStep(tci));
		steps.add(new RemoveAllServicePropertiesStep(tci));
		
		return steps;
	}


	public String getDescription() {
		return "Tests the code generation tools";
	}


	protected void storyTearDown() throws Throwable {
		RemoveSkeletonStep step = new RemoveSkeletonStep(tci);
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
