package org.cagrid.data.test.upgrades.from1pt0.sdk;

import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.BuildUpgradedServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.UnzipOldServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.UpgradeIntroduceServiceStep;

/** 
 *  UpgradeSDKTo1pt1Tests
 *  Tests to upgrade a data service backed by caCORE SDK 3.1 from 1.0 to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeSDKTo1pt2Tests.java,v 1.2 2008-05-21 19:51:14 dervin Exp $ 
 */
public class UpgradeSDKTo1pt2Tests extends Story {
	public static final String SERVICE_ZIP_NAME = "DataServiceBackedBySDK_1-0.zip";
    
	public static final String SERVICE_NAME = "DataServiceBackedBySDK";
    public static final String SERVICE_PACKAGE = "gov.nih.nci.cagrid.test.sdkds";
    public static final String SERVICE_NAMESPACE = "http://sdkds.test.cagrid.nci.nih.gov/DataServiceBackedBySDK";
    
	
	public String getDescription() {
		return "Tests upgrade of a data service backed by the SDK from version 1.0 to 1.2";
	}
    
    
    public String getName() {
        return "Data Service backed by the SDK from 1_0 to 1_2 Upgrade Tests";
    }
	

	protected Vector steps() {
        DataTestCaseInfo info = new UpgradeSDK1pt0to1pt1TestServiceInfo();
		Vector<Step> steps = new Vector<Step>();
		// steps to unpack and upgrade the old service
		steps.add(new DeleteOldServiceStep(info));
		steps.add(new UnzipOldServiceStep(DataTestCaseInfo.getTempDir(), SERVICE_ZIP_NAME));
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
		TestResult result = runner.doRun(new TestSuite(UpgradeSDKTo1pt2Tests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
	
	
	public static class UpgradeSDK1pt0to1pt1TestServiceInfo extends DataTestCaseInfo {
	    public String getServiceDirName() {
	        return SERVICE_NAME;
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
