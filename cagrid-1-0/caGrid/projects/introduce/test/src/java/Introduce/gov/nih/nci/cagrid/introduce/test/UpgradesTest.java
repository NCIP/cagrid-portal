package gov.nih.nci.cagrid.introduce.test;

import gov.nih.nci.cagrid.introduce.steps.CleanupGlobusStep;
import gov.nih.nci.cagrid.introduce.steps.CreateGlobusStep;
import gov.nih.nci.cagrid.introduce.steps.DeployGlobusServiceStep;
import gov.nih.nci.cagrid.introduce.steps.InvokeSimpleMethodImplStep;
import gov.nih.nci.cagrid.introduce.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.steps.StartGlobusStep;
import gov.nih.nci.cagrid.introduce.steps.StopGlobusStep;
import gov.nih.nci.cagrid.introduce.steps.UnzipOldServiceStep;
import gov.nih.nci.cagrid.introduce.steps.UpgradesStep;
import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

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

	protected Vector steps() {
		tci1 = new TestCaseInfo1();
		helper = new GlobusHelper(false, new File(
				IntroduceTestConstants.TEST_TEMP),
				IntroduceTestConstants.TEST_PORT);
		Vector steps = new Vector();

		try {
			steps.add(new CreateGlobusStep(helper));
			steps.add(new UnzipOldServiceStep("." + File.separator + "test"
					+ File.separator + "resources" + File.separator
					+ "serviceVersions" + File.separator + "IntroduceTest.zip",
					tci1));
			steps.add(new UpgradesStep(tci1, true));
			steps.add(new DeployGlobusServiceStep(helper, tci1));
			steps.add(new StartGlobusStep(helper));
			steps.add(new InvokeSimpleMethodImplStep(tci1, "newMethod", false));
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

		super.storySetUp();
		RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci1);
		try {
			step1.runStep();
		} catch (Throwable e) {

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
			e.printStackTrace();
		}
		StopGlobusStep step2 = new StopGlobusStep(helper);
		try {
			step2.runStep();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		CleanupGlobusStep step3 = new CleanupGlobusStep(helper);
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
