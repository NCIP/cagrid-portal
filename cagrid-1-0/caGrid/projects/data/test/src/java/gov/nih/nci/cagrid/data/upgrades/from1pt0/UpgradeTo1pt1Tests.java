package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

/** 
 *  UpgradeTo1pt1Tests
 *  Tests to upgrade a data service from 1.0 to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeTo1pt1Tests.java,v 1.2 2007-02-21 16:09:50 dervin Exp $ 
 */
public class UpgradeTo1pt1Tests extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
	public static final String TEST_DIR = "../data/test";
	public static final String SERVICE_DIR = TEST_DIR + File.separator + "CaGridTutorialService";

	public String getDescription() {
		return "Tests upgrade of a data service from version 1.0 to 1.1";
	}


	protected Vector steps() {
		Vector steps = new Vector();
		steps.add(new DeleteServiceDirectoryStep(SERVICE_DIR));
		steps.add(new UnzipOldServiceStep(TEST_DIR));
		steps.add(new UpgradeIntroduceServiceStep(SERVICE_DIR));
		steps.add(new UpgradeDataServiceExtensionStep(SERVICE_DIR));
		steps.add(new BuildUpgradedServiceStep(SERVICE_DIR));
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
		TestResult result = runner.doRun(new TestSuite(UpgradeTo1pt1Tests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
