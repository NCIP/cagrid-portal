package gov.nih.nci.cagrid.data.upgrades.from1pt0.system;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.system.AddBookstoreStep;
import gov.nih.nci.cagrid.data.system.CreateCleanGlobusStep;
import gov.nih.nci.cagrid.data.system.DeployDataServiceStep;
import gov.nih.nci.cagrid.data.system.DestroyTempGlobusStep;
import gov.nih.nci.cagrid.data.system.EnableValidationStep;
import gov.nih.nci.cagrid.data.system.InvokeDataServiceStep;
import gov.nih.nci.cagrid.data.system.RebuildServiceStep;
import gov.nih.nci.cagrid.data.system.SetQueryProcessorStep;
import gov.nih.nci.cagrid.data.system.StartGlobusStep;
import gov.nih.nci.cagrid.data.system.StopGlobusStep;
import gov.nih.nci.cagrid.data.upgrades.from1pt0.UpgradeTo1pt2Tests;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import com.atomicobject.haste.framework.Step;
import com.atomicobject.haste.framework.Story;

/** 
 *  UpgradedServiceSystemTest
 *  Tests the upgraded data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: UpgradedServiceSystemTest.java,v 1.8 2007-10-02 14:39:10 hastings Exp $ 
 */
public class UpgradedServiceSystemTest extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
	private static GlobusHelper globusHelper = 
		new GlobusHelper(false, 
			new File(IntroduceTestConstants.TEST_TEMP), IntroduceTestConstants.TEST_PORT + 5);
    
    public String getName() {
        return "Data Service 1_0 to 1_2 Upgraded System Tests";
    }
    
	
	protected boolean storySetUp() {
		assertFalse("Globus should NOT be running yet", globusHelper.isGlobusRunning());
		return true;
	}


	public String getDescription() {
		return "Deploys and invokes the upgraded data service";
	}


	protected Vector steps() {
        DataTestCaseInfo info = new UpgradeTo1pt2Tests.Upgrade1pt0to1pt1TestServiceInfo();
		Vector steps = new Vector();
		// steps to invoke the upgraded service 
		// by the data service creation tests
		// 1) Add the bookstore schema to the data service
		steps.add(new AddBookstoreStep(info));
		// 2) change out query processor
		steps.add(new SetQueryProcessorStep(info.getDir()));
		// 3) remove anything thats not the query method
		// steps.add(new RemoveNonQueryMethodsStep(info.getDir()));
		// 4) Turn on query validation
		steps.add(new EnableValidationStep(info.getDir()));
		// 5) Rebuild the service to pick up the bookstore beans
		steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
		// 6) set up a clean, temporary Globus
		steps.add(new CreateCleanGlobusStep(globusHelper));
		// 7) deploy data service
		steps.add(new DeployDataServiceStep(globusHelper, info.getDir()));
		// 8) start globus
		steps.add(new StartGlobusStep(globusHelper));
		// 9) test data service
		steps.add(new InvokeDataServiceStep(
			"localhost", IntroduceTestConstants.TEST_PORT + 5, info.getName()));
		return steps;
	}
	
	
	protected void storyTearDown() throws Throwable {
		super.storyTearDown();
		// 10) stop globus
		Step stopStep = new StopGlobusStep(globusHelper);
		try {
			stopStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		// 11) throw away globus
		Step destroyStep = new DestroyTempGlobusStep(globusHelper);
		try {
			destroyStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
	
	
	// used to make sure that if we are going to use a junit testsuite to 
	// test this that the test suite will not error out 
	// looking for a single test......
	public void testDummy() throws Throwable {
	}
	
	
	private String getIntroduceBaseDir() {
		String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
		if (dir == null) {
			fail("Introduce base dir environment variable " + INTRODUCE_DIR_PROPERTY + " is required");
		}
		return dir;
	}
}
