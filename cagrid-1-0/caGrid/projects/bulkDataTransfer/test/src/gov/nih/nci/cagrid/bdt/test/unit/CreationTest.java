package gov.nih.nci.cagrid.bdt.test.unit;

import gov.nih.nci.cagrid.bdt.test.steps.CreationStep;
import gov.nih.nci.cagrid.bdt.test.steps.DeleteOldServiceStep;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

/** 
 *  CreationTests
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id: CreationTest.java,v 1.2 2007-03-20 18:50:21 hastings Exp $ 
 */
public class CreationTest extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
	public static final String SERVICE_NAME = "TestBDTService";
	public static final String SERVICE_DIR = (new File("..")).getAbsolutePath() + File.separator + "bulkDataTransfer" + File.separator + "test" + File.separator + "TestBDTService";
	public static final String PACKAGE_NAME = "gov.nih.nci.cagrid.testbdt";
	public static final String SERVICE_NAMESPACE = "http://" + PACKAGE_NAME + "/" + SERVICE_NAME;
	
	public String getDescription() {
		return "Testing the data service creation extension for Introduce"; 
	}
	

	protected Vector steps() {
		Vector steps = new Vector();
		// delete any existing service
		steps.add(new DeleteOldServiceStep());
		// create a new enumeration supporting data service
		steps.add(new CreationStep(getIntroduceBaseDir()));
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


	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(CreationTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
