package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.CreationTests;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.tests.deployment.PortPreference;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainer;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainerType;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Step;


/**
 * SystemTests 
 * Story for data service system tests
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 7, 2006
 * @version $Id: SystemTests.java,v 1.18 2007-10-18 18:57:44 dervin Exp $
 */
public class SystemTests extends BaseSystemTest {
    
    private static File auditorLogFile = new File("./dataServiceAuditing.log").getAbsoluteFile();
    
    private static ServiceContainer container = null;
    
    static {
        try {
            PortPreference ports = new PortPreference(
                Integer.valueOf(IntroduceTestConstants.TEST_PORT + 501), 
                Integer.valueOf(IntroduceTestConstants.TEST_PORT + 1001), null);
            container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER, null, ports);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }
    
    
    public SystemTests() {
        this.setName("Data Service System Tests");
    }
    
    
    public String getName() {
        return "Data Service System Tests";
    }


    public String getDescription() {
        return "Testing the data service infrastructure";
    }


    protected boolean storySetUp() {
        // 1) set up a clean, temporary Globus
        Step step = new CreateCleanGlobusStep(container);
        try {
            step.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
        return true;
    }


    protected Vector steps() {
        DataTestCaseInfo info = new CreationTests.TestDataServiceInfo();
        Vector steps = new Vector();
        // data service presumed to have been created
        // by the data service creation tests
        // 2) Add the bookstore schema to the data service
        steps.add(new AddBookstoreStep(info));
        // 3) change out query processor
        steps.add(new SetQueryProcessorStep(info.getDir()));
        // 4) Turn on query validation
        steps.add(new EnableValidationStep(info.getDir()));
        // 5) Turn on and configure auditing
        steps.add(new AddFileSystemAuditorStep(info.getDir(), auditorLogFile.getAbsolutePath()));
        // 6) Rebuild the service to pick up the bookstore beans
        steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
        // 7) deploy data service
        steps.add(new DeployDataServiceStep(container, info.getDir()));
        // 8) start globus
        steps.add(new StartGlobusStep(container));
        // 9) test data service
        steps.add(new InvokeDataServiceStep("localhost", info.getName(), 
            container.getProperties().getPortPreference()));
        // 10) verify the audit log
        steps.add(new VerifyAuditLogStep(auditorLogFile.getAbsolutePath()));
        return steps;
    }


    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        // 11) stop globus
        Step stopStep = new StopGlobusStep(container);
        try {
            stopStep.runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        // 12) throw away globus
        Step destroyStep = new DestroyTempGlobusStep(container);
        try {
            destroyStep.runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        // 13) throw away auditor log
        if (auditorLogFile.exists()) {
            auditorLogFile.delete();
        }
    }


    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(SystemTests.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
