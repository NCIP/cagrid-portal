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
 * @version $Id: UpgradeTo1pt1Tests.java,v 1.3 2007-02-26 21:43:11 dervin Exp $ 
 */
public class UpgradeTo1pt1Tests extends Story {
	public static final String TEST_DIR = "../data/test";
	public static final String SERVICE_DIR = TEST_DIR + File.separator + "BasicDataService";
	
	public String getDescription() {
		return "Tests upgrade of a data service from version 1.0 to 1.1";
	}
	

	protected Vector steps() {
		Vector steps = new Vector();
		// steps to unpack and upgrade the old service
		steps.add(new DeleteServiceDirectoryStep(SERVICE_DIR));
		steps.add(new UnzipOldServiceStep(TEST_DIR));
		steps.add(new UpgradeIntroduceServiceStep(SERVICE_DIR));
		steps.add(new UpgradeDataServiceExtensionStep(SERVICE_DIR));
		steps.add(new BuildUpgradedServiceStep(SERVICE_DIR));
		
		return steps;
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
	
	
	public static class ServiceInfoHolder {
		public String serviceName;
		public String servicePackage;
		public String serviceNamespace;
	}
}
