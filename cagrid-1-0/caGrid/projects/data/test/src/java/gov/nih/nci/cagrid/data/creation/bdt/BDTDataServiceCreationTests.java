package gov.nih.nci.cagrid.data.creation.bdt;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.creation.DeleteOldServiceStep;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  BDTDataServiceCreationTests
 *  Tests for creating a BDT Data Service
 * 
 * @author David Ervin
 * 
 * @created Mar 13, 2007 2:41:25 PM
 * @version $Id: BDTDataServiceCreationTests.java,v 1.7 2007-12-03 16:27:20 hastings Exp $ 
 */
public class BDTDataServiceCreationTests extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
	public static final String SERVICE_NAME = "TestBDTDataService";
	public static final String SERVICE_DIR = (new File("..")).getAbsolutePath() + File.separator + "data" 
		+ File.separator + "test" + File.separator + SERVICE_NAME;
	public static final String PACKAGE_NAME = "gov.nih.nci.cagrid.testbdtds";
	public static final String SERVICE_NAMESPACE = "http://" + PACKAGE_NAME + "/" + SERVICE_NAME;
    
    public String getName() {
        return "BDT Data Service Creation Tests";
    }
	

	public String getDescription() {
		return "Tests for creating a BDT Data Service";
	}


	protected Vector steps() {
        DataTestCaseInfo info = new TestBDTDataServiceInfo();
		Vector steps = new Vector();
		steps.add(new DeleteOldServiceStep(info));
		steps.add(new CreateBDTServiceStep(info, getIntroduceBaseDir()));
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
		TestResult result = runner.doRun(new TestSuite(BDTDataServiceCreationTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
    
    
    public static class TestBDTDataServiceInfo extends DataTestCaseInfo {
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
