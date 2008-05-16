package org.cagrid.data.test.creation;

import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  CreationTests
 *  Tests for creation of a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id: CreationTests.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class CreationTests extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
	public static final String SERVICE_NAME = "TestDataService";
	public static final String SERVICE_DIR = (new File("..")).getAbsolutePath() + File.separator + "data" + File.separator + "test" + File.separator + SERVICE_NAME;
	public static final String PACKAGE_NAME = "gov.nih.nci.cagrid.testds";
	public static final String SERVICE_NAMESPACE = "http://" + PACKAGE_NAME + "/" + SERVICE_NAME;
    
    public static final String PLAIN_SERVICE_NAME = "PlainTestDataService";
    public static final String PLAIN_SERVICE_DIR = (new File("..")).getAbsolutePath() + File.separator + "data" + File.separator + "test" + File.separator + PLAIN_SERVICE_NAME;
    public static final String PLAIN_PACKAGE_NAME = "gov.nih.nci.cagrid.plainds";
    public static final String PLAIN_SERVICE_NAMESPACE = "http://" + PLAIN_PACKAGE_NAME + "/" + PLAIN_SERVICE_NAME;
    
    public String getName() {
        return "Data Service Creation Tests";
    }
    
	
	public String getDescription() {
		return "Testing the data service creation extension for Introduce"; 
	}
	

	protected Vector steps() {
        DataTestCaseInfo info = new TestDataServiceInfo();
        DataTestCaseInfo plainInfo = new PlainDataServiceInfo();
        
		Vector<Step> steps = new Vector<Step>();
		// delete any existing service dirs
		steps.add(new DeleteOldServiceStep(info));
        steps.add(new DeleteOldServiceStep(plainInfo));
		// create a new data service
		steps.add(new CreationStep(info, getIntroduceBaseDir()));
        steps.add(new CreationStep(plainInfo, getIntroduceBaseDir()));
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


	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(CreationTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
    
    
	public static class TestDataServiceInfo extends DataTestCaseInfo {
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
    
    
    public static class PlainDataServiceInfo extends DataTestCaseInfo {
        public String getName() {
            return PLAIN_SERVICE_NAME;
        }


        public String getDir() {
            return PLAIN_SERVICE_DIR;
        }


        public String getNamespace() {
            return PLAIN_SERVICE_NAMESPACE;
        }


        public String getPackageName() {
            return PLAIN_PACKAGE_NAME;
        }
        
    }
}
