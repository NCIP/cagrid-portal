package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.data.creation.DeleteOldServiceStep;
import gov.nih.nci.cagrid.data.creation.TestServiceInfo;

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
 * @version $Id: UpgradeTo1pt1Tests.java,v 1.5 2007-04-05 16:59:23 dervin Exp $ 
 */
public class UpgradeTo1pt1Tests extends Story {
	public static final String TEST_DIR = "../data/test";
    
	public static final String SERVICE_DIR = TEST_DIR + File.separator + "BasicDataService";
    public static final String SERVICE_NAME = "BasicDataService";
    public static final String SERVICE_PACKAGE = "basicdataservice.cagrid.nci.nih.gov";
    public static final String SERVICE_NAMESPACE = "http://basicdataservice.cagrid.nci.nih.gov/BasicDataService";
    
	
	public String getDescription() {
		return "Tests upgrade of a data service from version 1.0 to 1.1";
	}
    
    
    public String getName() {
        return "Data Service 1.0 to 1.1 Upgrade Tests";
    }
	

	protected Vector steps() {
        TestServiceInfo info = new Upgrade1pt0to1pt1TestServiceInfo();
		Vector steps = new Vector();
		// steps to unpack and upgrade the old service
		steps.add(new DeleteOldServiceStep(info));
		steps.add(new UnzipOldServiceStep(TEST_DIR));
		steps.add(new UpgradeIntroduceServiceStep(info.getDir()));
		steps.add(new UpgradeDataServiceExtensionStep(info.getDir()));
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
		TestResult result = runner.doRun(new TestSuite(UpgradeTo1pt1Tests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
	
	
	public static class Upgrade1pt0to1pt1TestServiceInfo implements TestServiceInfo {
	    public String getDir() {
	        return UpgradeTo1pt1Tests.SERVICE_DIR;
	    }


	    public String getName() {
	        return SERVICE_NAME;
	    }


	    public String getNamespace() {
	        return SERVICE_NAMESPACE;
	    }


	    public String getPackage() {
	        return SERVICE_PACKAGE;
	    }


	    public String getExtensions() {
	        return "data";
	    }
	}
}
