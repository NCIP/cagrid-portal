package gov.nih.nci.cagrid.data.system.enumeration;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.creation.enumeration.CreateEnumerationTests;
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
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.tests.deployment.PortPreference;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainer;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainerType;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Step;


/**
 * EnumerationSystemTests 
 * Story for WS-Enumeration data service system tests
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 7, 2006
 * @version $Id: EnumerationSystemTests.java,v 1.1 2006/12/18 14:48:47 dervin
 *          Exp $
 */
public class EnumerationSystemTests extends BaseSystemTest {
    
    private static ServiceContainer container = null;
    
    static {
        try {
            PortPreference ports = new PortPreference(
                Integer.valueOf(IntroduceTestConstants.TEST_PORT + 701), 
                Integer.valueOf(IntroduceTestConstants.TEST_PORT + 1201), null);
            container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER, null, ports);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }

	public EnumerationSystemTests() {
		this.setName("Enumeration Data Service System Tests");
	}
    
    
    public String getName() {
        return "Enumeration Data Service System Tests";
    }


	public String getDescription() {
		return "Testing the data service infrastructure";
	}


	protected boolean storySetUp() {
		// unpack the service container
        Step unpack = new CreateCleanGlobusStep(container);
        try {
            unpack.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
		return true;
	}


	protected Vector steps() {
        DataTestCaseInfo info = new CreateEnumerationTests.TestEnumerationDataServiceInfo();
		Vector steps = new Vector();
		// an enumeration supporting data service is presumed to have been
		// created by a previous testing process
		// 1) Add the bookstore schema to the data service
		steps.add(new AddBookstoreStep(info));
		// 2) change out query processor
		steps.add(new SetQueryProcessorStep(info.getDir()));
		// 3) Turn on query validation
		steps.add(new EnableValidationStep(info.getDir()));
		// 4) Rebuild the service to pick up the bookstore beans
		steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
		// 5) deploy data service
		steps.add(new DeployDataServiceStep(container, info.getDir()));
		// 6) start container
		steps.add(new StartGlobusStep(container));
		// 7) test data service
		steps.add(new InvokeEnumerationDataServiceStep("localhost", info.getName(), 
            container.getProperties().getPortPreference()));
		return steps;
	}


	protected void storyTearDown() throws Throwable {
		super.storyTearDown();
		// 9) stop globus
		Step stopStep = new StopGlobusStep(container);
		try {
			stopStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		// 10) throw away globus
		Step destroyStep = new DestroyTempGlobusStep(container);
		try {
			destroyStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}


	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(EnumerationSystemTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
