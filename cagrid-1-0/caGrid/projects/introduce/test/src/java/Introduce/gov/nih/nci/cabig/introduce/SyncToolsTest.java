package gov.nih.nci.cabig.introduce;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

public class SyncToolsTest extends Story {

	protected Vector steps() {
		Vector steps = new Vector();

		steps.add(new CreateSkeletonStep());
		steps.add(new BuildSkeletonStep());
		steps.add(new AddSimpleMethodStep());
		steps.add(new BuildSkeletonStep());
		steps.add(new RemoveSimpleMethodStep());
		steps.add(new BuildSkeletonStep());

		return steps;
	}

	public String getDescription() {
		return "Tests the code generation tools";
	}

	protected boolean storySetUp() throws Throwable {
		return true;
	}

	protected void storyTearDown() throws Throwable {
		RemoveSkeletonStep step = new RemoveSkeletonStep();
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
