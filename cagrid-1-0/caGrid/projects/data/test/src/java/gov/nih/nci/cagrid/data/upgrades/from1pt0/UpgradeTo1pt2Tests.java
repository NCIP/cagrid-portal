package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.creation.DeleteOldServiceStep;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

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
 * @version $Id: UpgradeTo1pt2Tests.java,v 1.1 2007-10-01 17:04:49 hastings Exp $ 
 */
public class UpgradeTo1pt2Tests extends Story {
	public static final String TEST_DIR = "../data/test";
    public static final String SERVICE_ZIP_NAME = "BasicDataService_1-0.zip";
    
	public static final String SERVICE_DIR = TEST_DIR + File.separator + "BasicDataService";
    public static final String SERVICE_NAME = "BasicDataService";
    public static final String SERVICE_PACKAGE = "basicdataservice.cagrid.nci.nih.gov";
    public static final String SERVICE_NAMESPACE = "http://basicdataservice.cagrid.nci.nih.gov/BasicDataService";
    
	
	public String getDescription() {
		return "Tests upgrade of a data service from version 1.0 to 1.1";
	}
    
    
    public String getName() {
        return "Data Service 1_0 to 1_1 Upgrade Tests";
    }
	

	protected Vector steps() {
        TestCaseInfo info = new Upgrade1pt0to1pt1TestServiceInfo();
		Vector steps = new Vector();
		// steps to unpack and upgrade the old service
		steps.add(new DeleteOldServiceStep(info));
		steps.add(new UnzipOldServiceStep(TEST_DIR, SERVICE_ZIP_NAME));
		steps.add(new UpgradeIntroduceServiceStep(info.getDir()));
		steps.add(new BuildUpgradedServiceStep(info.getDir()));
		
		return steps;
	}
	
	
	// used to make sure that if we are going to use a junit testsuite to 
	// test this that the test suite will not error out 
	// looking for a single test......
	public void testDummy() throws Throwable {
	}


	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(UpgradeTo1pt2Tests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
	
	
	public static class Upgrade1pt0to1pt1TestServiceInfo extends DataTestCaseInfo {
	    public String getDir() {
	        return UpgradeTo1pt2Tests.SERVICE_DIR;
	    }


	    public String getName() {
	        return SERVICE_NAME;
	    }


	    public String getNamespace() {
	        return SERVICE_NAMESPACE;
	    }


	    public String getPackageName() {
	        return SERVICE_PACKAGE;
	    }
	}
}
