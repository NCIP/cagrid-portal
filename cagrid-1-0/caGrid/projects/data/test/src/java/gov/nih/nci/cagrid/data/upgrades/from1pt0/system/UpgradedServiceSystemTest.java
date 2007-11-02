package gov.nih.nci.cagrid.data.upgrades.from1pt0.system;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.system.AddBookstoreStep;
import gov.nih.nci.cagrid.data.system.CreateCleanContainerStep;
import gov.nih.nci.cagrid.data.system.DeployDataServiceStep;
import gov.nih.nci.cagrid.data.system.DestroyTempContainerStep;
import gov.nih.nci.cagrid.data.system.EnableValidationStep;
import gov.nih.nci.cagrid.data.system.InvokeDataServiceStep;
import gov.nih.nci.cagrid.data.system.RebuildServiceStep;
import gov.nih.nci.cagrid.data.system.SetQueryProcessorStep;
import gov.nih.nci.cagrid.data.system.StartContainerStep;
import gov.nih.nci.cagrid.data.system.StopContainerStep;
import gov.nih.nci.cagrid.data.upgrades.from1pt0.UpgradeTo1pt2Tests;
import gov.nih.nci.cagrid.testing.core.TestingConstants;
import gov.nih.nci.cagrid.testing.system.deployment.PortPreference;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;

import java.util.Vector;

import com.atomicobject.haste.framework.Step;
import com.atomicobject.haste.framework.Story;

/** 
 *  UpgradedServiceSystemTest
 *  Tests the upgraded data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: UpgradedServiceSystemTest.java,v 1.11 2007-11-02 15:25:40 dervin Exp $ 
 */
public class UpgradedServiceSystemTest extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
    private static ServiceContainer container = null;
    
    static {
        try {
            PortPreference ports = new PortPreference(
                Integer.valueOf(TestingConstants.TEST_PORT_LOWER_BOUND.intValue() + 801), 
                Integer.valueOf(TestingConstants.TEST_PORT_UPPER_BOUND.intValue() + 801), null);
            container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER, null, ports);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }
    
    public String getName() {
        return "Data Service 1_0 to 1_2 Upgraded System Tests";
    }
    
	
	protected boolean storySetUp() {
		// unpack container
        Step unpack = new CreateCleanContainerStep(container);
        try {
            unpack.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
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
		// 6) deploy data service
		steps.add(new DeployDataServiceStep(container, info.getDir()));
		// 7) start globus
		steps.add(new StartContainerStep(container));
		// 8) test data service
		steps.add(new InvokeDataServiceStep(
			"localhost", info.getName(), container.getProperties().getPortPreference()));
		return steps;
	}
	
	
	protected void storyTearDown() throws Throwable {
		super.storyTearDown();
        // 10) stop globus
		Step stopStep = new StopContainerStep(container);
		try {
			stopStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		// 11) throw away globus
		Step destroyStep = new DestroyTempContainerStep(container);
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
