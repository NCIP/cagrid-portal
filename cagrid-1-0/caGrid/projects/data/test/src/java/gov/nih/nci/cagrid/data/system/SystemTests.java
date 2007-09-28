package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.CreationTests;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

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
 * @version $Id: SystemTests.java,v 1.17 2007-09-28 20:06:52 dervin Exp $
 */
public class SystemTests extends BaseSystemTest {
    private static int TEST_PORT = IntroduceTestConstants.TEST_PORT + 500;
    private static GlobusHelper globusHelper = new GlobusHelper(
        false, new File(IntroduceTestConstants.TEST_TEMP), TEST_PORT);
    
    private static File auditorLogFile = new File("./dataServiceAuditing.log").getAbsoluteFile();


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
        assertFalse("Globus should NOT be running yet", globusHelper.isGlobusRunning());
        return true;
    }


    protected Vector steps() {
        DataTestCaseInfo info = new CreationTests.TestDataServiceInfo();
        Vector steps = new Vector();
        // data service presumed to have been created
        // by the data service creation tests
        // 1) Add the bookstore schema to the data service
        steps.add(new AddBookstoreStep(info));
        // 2) change out query processor
        steps.add(new SetQueryProcessorStep(info.getDir()));
        // 3) Turn on query validation
        steps.add(new EnableValidationStep(info.getDir()));
        // 4) Turn on and configure auditing
        steps.add(new AddFileSystemAuditorStep(info.getDir(), auditorLogFile.getAbsolutePath()));
        // 5) Rebuild the service to pick up the bookstore beans
        steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
        // 6) set up a clean, temporary Globus
        steps.add(new CreateCleanGlobusStep(globusHelper));
        // 7) deploy data service
        steps.add(new DeployDataServiceStep(globusHelper, info.getDir()));
        // 8) start globus
        steps.add(new StartGlobusStep(globusHelper));
        // 9) test data service
        steps.add(new InvokeDataServiceStep("localhost", TEST_PORT, info.getName()));
        // 10) verify the audit log
        steps.add(new VerifyAuditLogStep(auditorLogFile.getAbsolutePath()));
        return steps;
    }


    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        // 11) stop globus
        Step stopStep = new StopGlobusStep(globusHelper);
        try {
            stopStep.runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        // 12) throw away globus
        Step destroyStep = new DestroyTempGlobusStep(globusHelper);
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
