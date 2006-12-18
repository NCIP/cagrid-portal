package gov.nih.nci.cagrid.data.creation.enumeration;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

/** 
 *  CreateEnumerationTests
 *  Tests creation of an enumeration supporting caGrid 1.0 Data Service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 30, 2006 
 * @version $Id: CreateEnumerationTests.java,v 1.1 2006-12-18 14:48:47 dervin Exp $ 
 */
public class CreateEnumerationTests extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
	public static final String SERVICE_NAME = "TestEnumerationDataService";
	public static final String SERVICE_DIR = (new File("..")).getAbsolutePath() + File.separator + "data" + File.separator 
	+ "test" + File.separator + "TestEnumerationDataService";
	public static final String PACKAGE_NAME = "gov.nih.nci.cagrid.test.enumds";
	public static final String SERVICE_NAMESPACE = "http://" + PACKAGE_NAME + "/" + SERVICE_NAME;
	
	public String getDescription() {
		return "Tests creation of an enumeration supporting caGrid 1.0 Data Service";
	}


	protected Vector steps() {
		Vector steps = new Vector();
		// 1. delete any existing enumeration data service directory
		steps.add(new DeleteOldServiceStep());
		// 2. create a new enumeration data service
		steps.add(new CreateEnumerationDataServiceStep(getIntroduceBaseDir()));
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
		TestResult result = runner.doRun(new TestSuite(CreateEnumerationTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
