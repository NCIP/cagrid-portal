package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.CreationTests;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

/** 
 *  SystemTests
 *  Story for data service system tests
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: SystemTests.java,v 1.2 2006-11-09 15:27:52 dervin Exp $ 
 */
public class SystemTests extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
	
	public SystemTests() {
		this.setName("Data Service System Tests");
	}
	

	public String getDescription() {
		return "Testing the data service infrastructure";
	}


	protected Vector steps() {
		GlobusHelper globusHelper = new GlobusHelper(
			false, new File(IntroduceTestConstants.TEST_TEMP), IntroduceTestConstants.TEST_PORT);
		Vector steps = new Vector();
		// data service presumed to have been created 
		// by the data service creation tests
		// 1) Add the bookstore schema to the data service
		steps.add(new AddBookstoreStep(CreationTests.SERVICE_DIR, CreationTests.SERVICE_NAME));
		// 2) change out query processor
		steps.add(new SetQueryProcessorStep(CreationTests.SERVICE_DIR));
		// 3) Turn on query validation
		steps.add(new EnableValidationStep(CreationTests.SERVICE_DIR));
		// 4) Rebuild the service to pick up the bookstore beans
		steps.add(new RebuildServiceStep(getIntroduceBaseDir()));
		// 5) set up a clean, temporary Globus
		steps.add(new CreateCleanGlobusStep(globusHelper));
		// 6) deploy data service
		steps.add(new DeployDataServiceStep(globusHelper, CreationTests.SERVICE_DIR));
		// 7) start globus
		steps.add(new StartGlobusStep(globusHelper));
		// 8) test data service
		steps.add(new InvokeDataServiceStep(
			"localhost", IntroduceTestConstants.TEST_PORT, CreationTests.SERVICE_NAME));
		// 9) stop globus
		steps.add(new StopGlobusStep(globusHelper));
		// 10) throw away globus
		steps.add(new DestroyTempGlobusStep(globusHelper));
		return steps;
	}
	
	
	private String getIntroduceBaseDir() {
		String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
		if (dir == null) {
			fail("Introduce base dir environment variable " + INTRODUCE_DIR_PROPERTY + " is required");
		}
		return dir;
	}
	

	// used to make sure that if we are going to use a junit testsuite to 
	// test this that the test suite will not error out 
	// looking for a single test......
	public void testDummy() throws Throwable {
	}


	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(SystemTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
