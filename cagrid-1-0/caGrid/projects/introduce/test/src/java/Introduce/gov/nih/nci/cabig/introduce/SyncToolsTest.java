package gov.nih.nci.cabig.introduce;

import gov.nih.nci.cabig.introduce.steps.AddSimpleMethodStep;
import gov.nih.nci.cabig.introduce.steps.AddSimpleMethodWithFaultStep;
import gov.nih.nci.cabig.introduce.steps.CreateSkeletonStep;
import gov.nih.nci.cabig.introduce.steps.RemoveSimpleMethodStep;
import gov.nih.nci.cabig.introduce.steps.RemoveSkeletonStep;
import gov.nih.nci.cabig.introduce.steps.RollBackStep;

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
		steps.add(new AddSimpleMethodStep(tci));
		steps.add(new AddSimpleMethodWithFaultStep(tci));
		//steps.add(new RollBackStep(tci));
		//steps.add(new AddSimpleMethodStep(tci));
		steps.add(new RemoveSimpleMethodStep(tci));
		
		return steps;
	}

	public String getDescription() {
		return "Tests the code generation tools";
	}

	protected void storyTearDown() throws Throwable {
		RemoveSkeletonStep step = new RemoveSkeletonStep(tci);
		step.runStep();
	}

	// used to make sure that if we are going to use a junit testsuite to test this 
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
