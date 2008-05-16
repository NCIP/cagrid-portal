package org.cagrid.data.test.creation.enumeration;

import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  CreateEnumerationTests
 *  Tests creation of an enumeration supporting caGrid 1.0 Data Service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 30, 2006 
 * @version $Id: CreateEnumerationTests.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class CreateEnumerationTests extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
	public static final String SERVICE_NAME = "TestEnumerationDataService";
	public static final String SERVICE_DIR = (new File("..")).getAbsolutePath() + File.separator + "data" + File.separator 
	+ "test" + File.separator + "TestEnumerationDataService";
	public static final String PACKAGE_NAME = "gov.nih.nci.cagrid.test.enumds";
	public static final String SERVICE_NAMESPACE = "http://" + PACKAGE_NAME + "/" + SERVICE_NAME;
    
    public String getName() {
        return "Enumeration Data Service Creation Tests";
    }
    
	
	public String getDescription() {
		return "Tests creation of an enumeration supporting caGrid 1.0 Data Service";
	}


	protected Vector steps() {
        DataTestCaseInfo info = new TestEnumerationDataServiceInfo();
		Vector<Step> steps = new Vector<Step>();
		// 1. delete any existing enumeration data service directory
		steps.add(new DeleteOldServiceStep(info));
		// 2. create a new enumeration data service
		steps.add(new CreateEnumerationDataServiceStep(info, getIntroduceBaseDir()));
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
		TestResult result = runner.doRun(new TestSuite(CreateEnumerationTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
    
    
    public static class TestEnumerationDataServiceInfo extends DataTestCaseInfo {
        public String getName() {
            return SERVICE_NAME;
        }


        public String getDir() {
            return SERVICE_DIR;
        }


        public String getNamespace() {
            return SERVICE_NAMESPACE;
        }


        public String getPackageName() {
            return PACKAGE_NAME;
        }
    }
}
