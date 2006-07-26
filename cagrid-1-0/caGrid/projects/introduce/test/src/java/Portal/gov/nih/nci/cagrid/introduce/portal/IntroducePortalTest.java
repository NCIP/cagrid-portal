package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.portal.steps.AddMultipleOperationsStep;
import gov.nih.nci.cagrid.introduce.portal.steps.CreateServiceStep;
import gov.nih.nci.cagrid.introduce.portal.steps.AddSchemaStep;
import gov.nih.nci.cagrid.introduce.portal.steps.AddOperationStep;
import gov.nih.nci.cagrid.introduce.portal.steps.GeneralModifyServiceStep;
import gov.nih.nci.cagrid.introduce.portal.steps.ModifyMultipleOperationsStep;
import gov.nih.nci.cagrid.introduce.portal.steps.ModifyOperationStep;
import gov.nih.nci.cagrid.introduce.portal.steps.RemoveMultipleOperationsStep;
import gov.nih.nci.cagrid.introduce.portal.steps.RemoveOperationStep;
import gov.nih.nci.cagrid.introduce.portal.steps.SecurityTestStep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

public class IntroducePortalTest extends Story {
	private TestCaseInfo tci;
	private static final String testDir = "test/resources/TestService";
	private	static final String tempDir = "test/TempIntroduceTestFiles";
	private static final String testSchema = "test/resources/abbot/1_TestString.xsd";
	private static final String tempTestSchema = tempDir + "/1_TestString.xsd";

	protected Vector steps() {

		Vector steps = new Vector();
		try {
			steps.add(new CreateServiceStep());
			steps.add(new AddSchemaStep());
			steps.add(new AddOperationStep());
			steps.add(new ModifyOperationStep());
			steps.add(new RemoveOperationStep());
			steps.add(new AddMultipleOperationsStep());
			steps.add(new ModifyMultipleOperationsStep());
			steps.add(new RemoveMultipleOperationsStep());
			steps.add(new GeneralModifyServiceStep());
			steps.add(new SecurityTestStep());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return steps;
	}

	public String getDescription() {
		return "Tests the code generation tools";
	}
	
	protected boolean storySetUp() throws Throwable {
		System.out.println("Initializing the testing environment.");
		File tempTestDirectory = new File(testDir);
		if(tempTestDirectory.exists()){
			if(!(Utils.deleteDir(tempTestDirectory)))
				fail("Unable to delete /test/resources/testService");
		}
		
		File tempDirectory = new File(tempDir);
		if(tempDirectory.exists()){
			if(!(Utils.deleteDir(tempDirectory)))
				fail("Unable to delete TempIntroduceTestFiles");
		}
		
		boolean success = (tempTestDirectory).mkdir();
	    if (!success) {
	        fail("Error creating test service directory.");
	    }
	    
	    success = (tempDirectory).mkdir();
	    if (!success) {
	        fail("Error creating temp directory.");
	    }
	    
	    this.copy(new File(testSchema), new File (tempTestSchema));
		return true;
	}

	protected void storyTearDown() throws Throwable {
		System.out.println("Cleaning up the testing environment.");
		if(!(Utils.deleteDir(new File(testDir))))
			fail("Unable to delete /test/resources/testService");
		if(!(Utils.deleteDir(new File(tempDir))))
			fail("Unable to delete TempIntroduceTestFiles");
	}

	public void testDummy() throws Throwable {
	}
	
	private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(
				IntroducePortalTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
	
	

}
