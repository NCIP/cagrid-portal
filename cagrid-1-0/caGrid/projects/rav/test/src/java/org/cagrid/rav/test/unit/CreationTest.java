/**
 * 
 */
package org.cagrid.rav.test.unit;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.cagrid.rav.test.steps.CreationStep;
import org.cagrid.rav.test.steps.DeleteOldServiceStep;

import com.atomicobject.haste.framework.Story;

/**
 * @author madduri
 *
 */
public class CreationTest extends Story {

	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
	public static final String SERVICE_NAME = "TestRAVService";
	public static final String SERVICE_DIR = 
		(new File("..")).getAbsolutePath() + File.separator + "rav" 
		+ File.separator + "test" + File.separator + "TestRAVService";
	public static final String PACKAGE_NAME = "org.cagrid.testrav";
	public static final String SERVICE_NAMESPACE = 
		"http://" + PACKAGE_NAME + "/" + SERVICE_NAME;
	/**
	 * 
	 */
	public CreationTest() {
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see com.atomicobject.haste.framework.Story#getDescription()
	 */
	public String getDescription() {
		return "Testing the RAV service creation extension for Introduce";
	}

	/* (non-Javadoc)
	 * @see com.atomicobject.haste.framework.Story#steps()
	 */
	protected Vector steps() {
		Vector steps = new Vector();
		steps.add(new DeleteOldServiceStep());
		steps.add(new CreationStep(getIntroduceBaseDir()));
		steps.add(new VerifyRAVImplAddedStep());
		return steps;
	}

	/**
	 * @return
	 */
	private String getIntroduceBaseDir() {
		String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
		if (dir == null) {
			dir= "C:\\final2\\cagrid-1-0\\caGrid\\projects\\introduce";
			//fail("Introduce base dir environment variable " + INTRODUCE_DIR_PROPERTY + " is required");
		}
		return dir;
	}
	
	//	 used to make sure that if we are going to use a junit testsuite to 
	// test this that the test suite will not error out 
	// looking for a single test..
	public void testDummy() throws Throwable {
	}
	
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(CreationTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
