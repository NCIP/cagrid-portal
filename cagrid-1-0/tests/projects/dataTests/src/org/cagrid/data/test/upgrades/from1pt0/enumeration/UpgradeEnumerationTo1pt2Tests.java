package org.cagrid.data.test.upgrades.from1pt0.enumeration;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.BuildUpgradedServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.UnzipOldServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.UpgradeIntroduceServiceStep;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  UpgradeEnumerationTo1pt1Tests
 *  Tests to upgrade a data service with enumeration from 1.0 to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeEnumerationTo1pt2Tests.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class UpgradeEnumerationTo1pt2Tests extends Story {
    public static final String TEST_DIR = ".." + File.separator + "data" + File.separator + "test";
    public static final String SERVICE_ZIP_NAME = "DataServiceWithEnumeration_1-0.zip";
    
	public static final String SERVICE_DIR = TEST_DIR + File.separator + "DataServiceWithEnumeration";
    public static final String SERVICE_NAME = "DataServiceWithEnumeration";
    public static final String SERVICE_PACKAGE = "gov.nih.nci.cagrid.test.dswenum";
    public static final String SERVICE_NAMESPACE = "http://dswenum.test.cagrid.nci.nih.gov/DataServiceWithEnumeration";
    
	
	public String getDescription() {
		return "Tests upgrade of a data service with WS-Enumeration from version 1.0 to 1.2";
	}
    
    
    public String getName() {
        return "Data Service with WS-Enumeration from 1_0 to 1_2 Upgrade Tests";
    }
	

	protected Vector steps() {
        TestCaseInfo info = new UpgradeEnumeration1pt0to1pt1TestServiceInfo();
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
		TestResult result = runner.doRun(new TestSuite(UpgradeEnumerationTo1pt2Tests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
	
	
	public static class UpgradeEnumeration1pt0to1pt1TestServiceInfo extends DataTestCaseInfo {
	    public String getDir() {
	        return UpgradeEnumerationTo1pt2Tests.SERVICE_DIR;
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
