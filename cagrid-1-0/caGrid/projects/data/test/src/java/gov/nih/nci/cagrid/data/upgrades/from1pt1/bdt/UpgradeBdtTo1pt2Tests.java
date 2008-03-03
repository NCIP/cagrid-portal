package gov.nih.nci.cagrid.data.upgrades.from1pt1.bdt;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.creation.DeleteOldServiceStep;
import gov.nih.nci.cagrid.data.upgrades.from1pt0.BuildUpgradedServiceStep;
import gov.nih.nci.cagrid.data.upgrades.from1pt0.UnzipOldServiceStep;
import gov.nih.nci.cagrid.data.upgrades.from1pt0.UpgradeIntroduceServiceStep;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  UpgradeSDKTo1pt1Tests
 *  Tests to upgrade a data service using BDT from 1.1 to 1.2
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeBdtTo1pt2Tests.java,v 1.2 2008-03-03 14:49:26 dervin Exp $ 
 */
public class UpgradeBdtTo1pt2Tests extends Story {
	public static final String TEST_DIR = ".." + File.separator + "data" + File.separator + "test";
    public static final String SERVICE_ZIP_NAME = "DataServiceWithBdt_1-1.zip";
    
	public static final String SERVICE_DIR = TEST_DIR + File.separator + "DataServiceWithBdt_1-1";
    public static final String SERVICE_NAME = "DataServiceWithBdt";
    public static final String SERVICE_PACKAGE = "gov.nih.nci.cagrid.data.bdt";
    public static final String SERVICE_NAMESPACE = "http://bdt.data.cagrid.nci.nih.gov/DataServiceWithBdt";
    
	
	public String getDescription() {
		return "Tests upgrade of a data service using BDT from version 1.1 to 1.2";
	}
    
    
    public String getName() {
        return "Data Service with BDT from 1_1 to 1_2 Upgrade Tests";
    }
	

	protected Vector steps() {
        TestCaseInfo info = new DataTestCaseInfo() {
        	public String getDir() {
    	        return SERVICE_DIR;
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
        };
		Vector<Step> steps = new Vector<Step>();
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
		TestResult result = runner.doRun(new TestSuite(UpgradeBdtTo1pt2Tests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
