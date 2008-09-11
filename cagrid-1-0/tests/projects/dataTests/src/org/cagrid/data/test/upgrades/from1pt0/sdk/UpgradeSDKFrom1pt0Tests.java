package org.cagrid.data.test.upgrades.from1pt0.sdk;

import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.data.test.upgrades.UnpackOldServiceStep;
import org.cagrid.data.test.upgrades.UpgradeTestConstants;
import org.cagrid.data.test.upgrades.from1pt0.BuildUpgradedServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.UpgradeIntroduceServiceStep;

/** 
 *  UpgradeSDKFrom1pt0Tests
 *  Tests to upgrade a data service backed by caCORE SDK 3.1 from 1.0 to current
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeSDKFrom1pt0Tests.java,v 1.1 2008-09-11 17:47:50 dervin Exp $ 
 */
public class UpgradeSDKFrom1pt0Tests extends Story {
	public static final String SERVICE_ZIP_NAME = "DataServiceBackedBySDK_1-0.zip";
    public static final String SERVICE_NAME = "DataServiceBackedBySDK";
    public static final String SERVICE_PACKAGE = "gov.nih.nci.cagrid.test.sdkds";
    public static final String SERVICE_NAMESPACE = "http://sdkds.test.cagrid.nci.nih.gov/DataServiceBackedBySDK";
    
	
	public String getDescription() {
		return "Tests upgrade of a data service backed by the SDK from version 1.0 to " + UpgradeTestConstants.DATA_CURRENT_VERSION;
	}
    
    
    public String getName() {
        return "Data Service backed by the SDK from 1_0 to " 
            + UpgradeTestConstants.DATA_CURRENT_VERSION.replaceAll(".", "_") 
            + " Upgrade Tests";
    }
	

	protected Vector steps() {
        DataTestCaseInfo info = new UpgradeSDK1pt0to1pt1TestServiceInfo();
		Vector<Step> steps = new Vector<Step>();
		// steps to unpack and upgrade the old service
		steps.add(new DeleteOldServiceStep(info));
		steps.add(new UnpackOldServiceStep(SERVICE_ZIP_NAME));
		steps.add(new UpgradeIntroduceServiceStep(info.getDir()));
		steps.add(new BuildUpgradedServiceStep(info.getDir()));
		
		return steps;
	}


	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(UpgradeSDKFrom1pt0Tests.class));
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
