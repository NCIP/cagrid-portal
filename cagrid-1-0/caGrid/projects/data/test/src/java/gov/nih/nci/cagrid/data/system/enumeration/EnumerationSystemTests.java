package gov.nih.nci.cagrid.data.system.enumeration;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.creation.enumeration.CreateEnumerationTests;
import gov.nih.nci.cagrid.data.system.AddBookstoreStep;
import gov.nih.nci.cagrid.data.system.BaseSystemTest;
import gov.nih.nci.cagrid.data.system.CreateCleanContainerStep;
import gov.nih.nci.cagrid.data.system.DeployDataServiceStep;
import gov.nih.nci.cagrid.data.system.DestroyTempContainerStep;
import gov.nih.nci.cagrid.data.system.EnableValidationStep;
import gov.nih.nci.cagrid.data.system.RebuildServiceStep;
import gov.nih.nci.cagrid.data.system.SetQueryProcessorStep;
import gov.nih.nci.cagrid.data.system.StartContainerStep;
import gov.nih.nci.cagrid.data.system.StopContainerStep;
import gov.nih.nci.cagrid.testing.core.TestingConstants;
import gov.nih.nci.cagrid.testing.system.deployment.PortPreference;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;

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
                Integer.valueOf(TestingConstants.TEST_PORT_LOWER_BOUND.intValue() + 701), 
                Integer.valueOf(TestingConstants.TEST_PORT_UPPER_BOUND.intValue() + 701), null);
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
        Step unpack = new CreateCleanContainerStep(container);
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
		steps.add(new StartContainerStep(container));
		// 7) test data service
		steps.add(new InvokeEnumerationDataServiceStep("localhost", info.getName(), 
            container.getProperties().getPortPreference()));
		return steps;
	}


	protected void storyTearDown() throws Throwable {
		super.storyTearDown();
		// 9) stop globus
		Step stopStep = new StopContainerStep(container);
		try {
			stopStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		// 10) throw away globus
		Step destroyStep = new DestroyTempContainerStep(container);
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
