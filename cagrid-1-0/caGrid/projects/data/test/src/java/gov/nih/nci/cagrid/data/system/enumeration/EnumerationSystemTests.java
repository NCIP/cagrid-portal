package gov.nih.nci.cagrid.data.system.enumeration;

import gov.nih.nci.cagrid.data.creation.enumeration.CreateEnumerationTests;
import gov.nih.nci.cagrid.data.system.AddBookstoreStep;
import gov.nih.nci.cagrid.data.system.BaseSystemTest;
import gov.nih.nci.cagrid.data.system.CreateCleanGlobusStep;
import gov.nih.nci.cagrid.data.system.DeployDataServiceStep;
import gov.nih.nci.cagrid.data.system.DestroyTempGlobusStep;
import gov.nih.nci.cagrid.data.system.EnableValidationStep;
import gov.nih.nci.cagrid.data.system.RebuildServiceStep;
import gov.nih.nci.cagrid.data.system.SetQueryProcessorStep;
import gov.nih.nci.cagrid.data.system.StartGlobusStep;
import gov.nih.nci.cagrid.data.system.StopGlobusStep;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Step;


/**
 * EnumerationSystemTests 
 * Story for WS-Enumeration data service system tests
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 7, 2006
 * @version $Id: EnumerationSystemTests.java,v 1.1 2006/12/18 14:48:47 dervin
 *          Exp $
 */
public class EnumerationSystemTests extends BaseSystemTest {

	private static GlobusHelper globusHelper = new GlobusHelper(false, new File(IntroduceTestConstants.TEST_TEMP),
		IntroduceTestConstants.TEST_PORT + 2);


	public EnumerationSystemTests() {
		this.setName("Data Service System Tests");
	}


	public String getDescription() {
		return "Testing the data service infrastructure";
	}


	protected boolean storySetUp() {
		assertFalse("Globus should NOT be running yet", globusHelper.isGlobusRunning());
		return true;
	}


	protected Vector steps() {
		Vector steps = new Vector();
		// an enumeration supporting data service is presumed to have been
		// created by a previous testing process
		// 1) Add the bookstore schema to the data service
		steps.add(new AddBookstoreStep(CreateEnumerationTests.SERVICE_DIR, 
			CreateEnumerationTests.SERVICE_NAME));
		// 2) change out query processor
		steps.add(new SetQueryProcessorStep(CreateEnumerationTests.SERVICE_DIR));
		// 3) Turn on query validation
		steps.add(new EnableValidationStep(CreateEnumerationTests.SERVICE_DIR));
		// 4) Rebuild the service to pick up the bookstore beans
		steps.add(new RebuildServiceStep(getIntroduceBaseDir(), CreateEnumerationTests.SERVICE_DIR,
			CreateEnumerationTests.SERVICE_NAME, CreateEnumerationTests.PACKAGE_NAME,
			CreateEnumerationTests.SERVICE_NAMESPACE));
		// 5) set up a clean, temporary Globus
		steps.add(new CreateCleanGlobusStep(globusHelper));
		// 6) deploy data service
		steps.add(new DeployDataServiceStep(globusHelper, CreateEnumerationTests.SERVICE_DIR));
		// 7) start globus
		steps.add(new StartGlobusStep(globusHelper));
		// 8) test data service
		steps.add(new InvokeEnumerationDataServiceStep("localhost", IntroduceTestConstants.TEST_PORT + 2,
			CreateEnumerationTests.SERVICE_NAME));

		return steps;
	}


	protected void storyTearDown() throws Throwable {
		super.storyTearDown();
		// 9) stop globus
		Step stopStep = new StopGlobusStep(globusHelper);
		try {
			stopStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		// 10) throw away globus
		Step destroyStep = new DestroyTempGlobusStep(globusHelper);
		try {
			destroyStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}


	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(EnumerationSystemTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
