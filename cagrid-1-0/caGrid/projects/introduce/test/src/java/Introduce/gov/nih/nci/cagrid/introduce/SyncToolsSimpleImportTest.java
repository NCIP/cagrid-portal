package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.steps.AddImportedMethodStep;
import gov.nih.nci.cagrid.introduce.steps.AddSimpleMethodStep;
import gov.nih.nci.cagrid.introduce.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.steps.RemoveSkeletonStep;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;


public class SyncToolsSimpleImportTest extends Story {
	private TestCaseInfo tci1;

	private TestCaseInfo tci3;


	public SyncToolsSimpleImportTest() {
		this.setName("IntroduceCodegenSimpleImportSystemTest");
	}


	protected Vector steps() {
		this.tci1 = new TestCaseInfo1();
		this.tci3 = new TestCaseInfo3();
		Vector steps = new Vector();

		try {
			steps.add(new CreateSkeletonStep(tci1, true));
			steps.add(new CreateSkeletonStep(tci3, true));

			steps.add(new AddSimpleMethodStep(tci3, "newMethod", true));
			steps.add(new AddImportedMethodStep(tci1, tci3, "newMethod", true, true));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return steps;
	}


	public String getDescription() {
		return "Testing the Introduce code generation tools";
	}
	

	protected boolean storySetUp() throws Throwable {
		// TODO Auto-generated method stub
		super.storySetUp();
		RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci1);
		try {
			step1.runStep();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RemoveSkeletonStep step2 = new RemoveSkeletonStep(tci3);
		try {
			step2.runStep();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


	protected void storyTearDown() throws Throwable {
		RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci1);
		try {
			step1.runStep();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RemoveSkeletonStep step2 = new RemoveSkeletonStep(tci3);
		try {
			step2.runStep();
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
		TestResult result = runner.doRun(new TestSuite(SyncToolsSimpleImportTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
