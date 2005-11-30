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
		steps.add(new AddSimpleMethodStep());
		steps.add(new RemoveSimpleMethodStep());

		return steps;
	}

	public String getDescription() {
		return "Tests the code generation tools";
	}

	protected boolean storySetUp() throws Throwable {
		// TODO Auto-generated method stub
		return true;
	}

	protected void storyTearDown() throws Throwable {
		// TODO Auto-generated method stub

	}

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
