package gov.nih.nci.cagrid.data.system.bdt;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.creation.bdt.BDTDataServiceCreationTests;
import gov.nih.nci.cagrid.data.system.AddBookstoreStep;
import gov.nih.nci.cagrid.data.system.BaseSystemTest;
import gov.nih.nci.cagrid.data.system.CreateCleanGlobusStep;
import gov.nih.nci.cagrid.data.system.DeployDataServiceStep;
import gov.nih.nci.cagrid.data.system.DestroyTempGlobusStep;
import gov.nih.nci.cagrid.data.system.EnableValidationStep;
import gov.nih.nci.cagrid.data.system.RebuildServiceStep;
import gov.nih.nci.cagrid.data.system.SetQueryProcessorStep;
import gov.nih.nci.cagrid.data.system.StartGlobusStep;
import gov.nih.nci.cagrid.data.system.StopGlobusStep;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Step;

/** 
 *  BDTDataServiceSystemTests
 *  System tests for BDT Data Service
 * 
 * @author David Ervin
 * 
 * @created Mar 14, 2007 2:19:42 PM
 * @version $Id: BDTDataServiceSystemTests.java,v 1.5 2007-09-28 20:06:52 dervin Exp $ 
 */
public class BDTDataServiceSystemTests extends BaseSystemTest {
    private static int TEST_PORT = IntroduceTestConstants.TEST_PORT + 501;
	private static GlobusHelper globusHelper = new GlobusHelper(
        false, new File(IntroduceTestConstants.TEST_TEMP), TEST_PORT);
	
	public String getDescription() {
		return "System tests for BDT Data Service";
	}
    
    
    public String getName() {
        return "BDT Data Service System Tests";
    }
	
	
	protected boolean storySetUp() {
		// make sure globus isn't running
		assertFalse("Globus should NOT be running yet", globusHelper.isGlobusRunning());
		// verify the BDT service has been built
		File serviceDir = new File(BDTDataServiceCreationTests.SERVICE_DIR);
		assertTrue("BDT Data Service directory NOT FOUND", serviceDir.exists());
		File serviceModel = new File(serviceDir.getAbsolutePath() 
			+ File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
		assertTrue("BDT Data Service directory does not appear to be an Introduce service", 
			serviceModel.exists());
		return true;
	}


	protected Vector steps() {
        DataTestCaseInfo info = new BDTDataServiceCreationTests.TestBDTDataServiceInfo();
		Vector steps = new Vector();
		// assumes the BDT service has been created already
		// 1) Add the bookstore schema to the data service
		steps.add(new AddBookstoreStep(info));
		// 2) change out query processor
		steps.add(new SetQueryProcessorStep(BDTDataServiceCreationTests.SERVICE_DIR));
		// 3) Turn on query validation
		steps.add(new EnableValidationStep(BDTDataServiceCreationTests.SERVICE_DIR));
		// 4) Rebuild the service to pick up the bookstore beans
		steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
		// 5) set up a clean, temporary Globus
		steps.add(new CreateCleanGlobusStep(globusHelper));
		// 6) deploy data service
		steps.add(new DeployDataServiceStep(globusHelper, BDTDataServiceCreationTests.SERVICE_DIR));
		// 7) start globus
		steps.add(new StartGlobusStep(globusHelper));
		// 8) test bdt data service
		steps.add(new InvokeBDTDataServiceStep("localhost", TEST_PORT,
			BDTDataServiceCreationTests.SERVICE_NAME));
		return steps;
	}
	
	
	protected void storyTearDown() throws Throwable {
		super.storyTearDown();
		// 9) stop globus
		Step stopStep = new StopGlobusStep(globusHelper);
		try {
			stopStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		// 10) throw away globus
		Step destroyStep = new DestroyTempGlobusStep(globusHelper);
		try {
			destroyStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(
				BDTDataServiceSystemTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
