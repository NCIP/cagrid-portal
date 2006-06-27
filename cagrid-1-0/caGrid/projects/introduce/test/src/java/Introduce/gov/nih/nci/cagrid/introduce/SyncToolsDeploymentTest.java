package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.steps.StartGlobusStep;
import gov.nih.nci.cagrid.introduce.steps.CleanupGlobusStep;
import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

public class SyncToolsDeploymentTest extends Story {
	private GlobusHelper helper;
	
	public SyncToolsDeploymentTest(){
		super();
	}

	protected Vector steps() {
		Vector steps = new Vector();
		this.setName("Introduce Codegen System Test");
		this.helper = new GlobusHelper(false,new File("tmp"), 8081);
		try {
			steps.add(new StartGlobusStep(this.helper));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return steps;
	}

	public String getDescription() {
		return "Testing the Introduce code generation tools";
	}

	protected void storyTearDown() throws Throwable {
		CleanupGlobusStep step = new CleanupGlobusStep(this.helper);
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
		TestResult result = runner.doRun(new TestSuite(SyncToolsDeploymentTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
