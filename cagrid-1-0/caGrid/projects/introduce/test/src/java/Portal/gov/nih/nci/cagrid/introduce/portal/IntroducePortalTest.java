package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.TestCaseInfo1;
import gov.nih.nci.cagrid.introduce.portal.steps.CreateServiceStep;
import gov.nih.nci.cagrid.introduce.portal.steps.ModifyServiceStep;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.projectmobius.portal.GridPortal;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import com.atomicobject.haste.framework.Story;


public class IntroducePortalTest extends Story {
	private TestCaseInfo tci;
	private Window mainWindow = null;
	private GridPortal myApp;
	private CreateServiceStep createService;
	private ModifyServiceStep modifyService;


	protected Vector steps() {
		System.out.println("TRYING TO SET THIS TEST UP.......................................");
		UISpec4J.init();

		mainWindow = WindowInterceptor.run(new Trigger() {
			public void run() {
				try {

					String pathtobasedir = System.getProperty("basedir");
					System.out.println(pathtobasedir);
					if (pathtobasedir == null) {
						System.err.println("basedir system property not set");
						throw new Exception("basedir system property not set");
					}

					System.out.println("TRYING TO CREATE THE PORTAL WITH NO PROBLEMS...................");
					myApp = new GridPortal(pathtobasedir + File.separator
						+ "conf" + File.separator + "introduce" + File.separator + "introduce-portal-conf.xml");
					System.out.println("CREATED THE PORTAL WITH NO PROBLEMS...................");
					myApp.show();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		});

		System.out.println("DONE SETTING THIS TEST UP.......................................");

		createService = new CreateServiceStep(mainWindow);
		modifyService = new ModifyServiceStep(mainWindow);
		this.tci = new TestCaseInfo1();

		Vector steps = new Vector();
		steps.add(createService);
		// steps.add(modifyService);
		return steps;
	}


	public String getDescription() {
		return "Tests the code generation tools";
	}


	protected void storyTearDown() throws Throwable {
		// myApp.setVisible(false);
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
		TestResult result = runner.doRun(new TestSuite(IntroducePortalTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
