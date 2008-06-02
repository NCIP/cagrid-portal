package org.cagrid.data.test.upgrades.from1pt0.enumeration;

import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.data.test.upgrades.UnpackOldServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.BuildUpgradedServiceStep;
import org.cagrid.data.test.upgrades.from1pt0.UpgradeIntroduceServiceStep;

/** 
 *  UpgradeEnumerationTo1pt1Tests
 *  Tests to upgrade a data service with enumeration from 1.0 to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeEnumerationTo1pt2Tests.java,v 1.4 2008-06-02 20:34:18 dervin Exp $ 
 */
public class UpgradeEnumerationTo1pt2Tests extends Story {
    public static final String SERVICE_ZIP_NAME = "DataServiceWithEnumeration_1-0.zip";
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
        DataTestCaseInfo info = new UpgradeEnumeration1pt0to1pt1TestServiceInfo();
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
		TestResult result = runner.doRun(new TestSuite(UpgradeEnumerationTo1pt2Tests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
	
	
	public static class UpgradeEnumeration1pt0to1pt1TestServiceInfo extends DataTestCaseInfo {
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
