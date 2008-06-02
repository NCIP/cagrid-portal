package org.cagrid.data.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.cagrid.data.test.creation.CreationTests;
import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;


/**
 * SystemTests 
 * Story for data service system tests
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 7, 2006
 * @version $Id: SystemTests.java,v 1.3 2008-06-02 20:34:18 dervin Exp $
 */
public class SystemTests extends BaseSystemTest {
    
    private static File auditorLogFile = new File("./dataServiceAuditing.log").getAbsoluteFile();
    
    private DataTestCaseInfo info;
    private ServiceContainer container;
    
    
    public SystemTests() {
        super();
        this.setName("Data Service System Tests");
    }
    
    
    public String getName() {
        return "Data Service System Tests";
    }


    public String getDescription() {
        return "Testing the data service infrastructure";
    }


    protected boolean storySetUp() {
        // init the test info
        info = new CreationTests.TestDataServiceInfo();
        
        // initialize the service container instance
        try {
            container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
        
        // 1) set up a clean, temporary service container
        Step step = new UnpackContainerStep(container);
        try {
            step.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
        return true;
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        // data service presumed to have been created
        // by the data service creation tests
        // 2) Add the data tests jar to the service lib
        steps.add(new AddTestingJarToServiceStep(info));
        // 3) Add the bookstore schema to the data service
        steps.add(new AddBookstoreStep(info));
        // 4) change out query processor
        steps.add(new SetQueryProcessorStep(info.getDir()));
        // 5) Turn on query validation
        steps.add(new EnableValidationStep(info.getDir()));
        // 6) Turn on and configure auditing
        steps.add(new AddFileSystemAuditorStep(info.getDir(), auditorLogFile.getAbsolutePath()));
        // 7) Rebuild the service to pick up the bookstore beans
        steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
        // 8) deploy data service
        steps.add(new DeployServiceStep(container, info.getDir()));
        // 9) start globus
        steps.add(new StartContainerStep(container));
        // 10) test data service
        steps.add(new InvokeDataServiceStep(container, info.getName()));
        // 11) verify the audit log
        steps.add(new VerifyAuditLogStep(auditorLogFile.getAbsolutePath()));
        return steps;
    }


    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        List<Throwable> exceptions = new ArrayList<Throwable>();
        // 12) stop globus
        Step stopStep = new StopContainerStep(container);
        try {
            stopStep.runStep();
        } catch (Throwable ex) {
            exceptions.add(ex);
        }
        // 13) throw away auditor log
        if (auditorLogFile.exists()) {
            auditorLogFile.deleteOnExit();
        }
        // 14) throw away globus
        Step destroyStep = new DestroyContainerStep(container);
        try {
            destroyStep.runStep();
        } catch (Throwable ex) {
            exceptions.add(ex);
        }
        // 15) Delete the old service
        Step deleteServiceStep = new DeleteOldServiceStep(info);
        try {
            deleteServiceStep.runStep();
        } catch (Throwable th) {
            exceptions.add(th);
        }
        
        // check on exceptions
        if (exceptions.size() != 0) {
            // uh oh
            for (Throwable th : exceptions) {
                System.err.println("EXCEPTION THROWN DURING TEAR DOWN:");
                th.printStackTrace();
            }
            throw new Exception("Error during tear down, see logs");
        }
    }


    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(SystemTests.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
