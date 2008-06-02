package org.cagrid.data.test.system.enumeration;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.util.Vector;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.data.test.creation.enumeration.CreateEnumerationTests;
import org.cagrid.data.test.system.AddBookstoreStep;
import org.cagrid.data.test.system.AddTestingJarToServiceStep;
import org.cagrid.data.test.system.BaseSystemTest;
import org.cagrid.data.test.system.EnableValidationStep;
import org.cagrid.data.test.system.RebuildServiceStep;
import org.cagrid.data.test.system.SetQueryProcessorStep;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


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
    
    private DataTestCaseInfo info;
    private ServiceContainer container;

	public EnumerationSystemTests() {
        super();
		this.setName("Enumeration Data Service System Tests");
	}
    
    
    public String getName() {
        return "Enumeration Data Service System Tests";
    }


	public String getDescription() {
		return "Testing the data service infrastructure";
	}


	protected boolean storySetUp() {
        info = new CreateEnumerationTests.TestEnumerationDataServiceInfo();
        // obtain a new container instance
        try {
            container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
        
		// unpack the service container
        Step unpack = new UnpackContainerStep(container);
        try {
            unpack.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
		return true;
	}


	protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
		// an enumeration supporting data service is presumed to have been
		// created by a previous testing process
        // 1) Add the data tests jar to the service lib
        steps.add(new AddTestingJarToServiceStep(info));
		// 2) Add the bookstore schema to the data service
		steps.add(new AddBookstoreStep(info));
		// 3) change out query processor
		steps.add(new SetQueryProcessorStep(info.getDir()));
		// 4) Turn on query validation
		steps.add(new EnableValidationStep(info.getDir()));
		// 5) Rebuild the service to pick up the bookstore beans
		steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
		// 6) deploy data service
		steps.add(new DeployServiceStep(container, info.getDir()));
		// 7) start container
		steps.add(new StartContainerStep(container));
		// 8) test data service
		steps.add(new InvokeEnumerationDataServiceStep(container, info.getName()));
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
		Step destroyStep = new DestroyContainerStep(container);
		try {
			destroyStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
        // 11) throw away service
        Step deleteServiceStep = new DeleteOldServiceStep(info);
        try {
            deleteServiceStep.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
        }
	}


	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(EnumerationSystemTests.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
