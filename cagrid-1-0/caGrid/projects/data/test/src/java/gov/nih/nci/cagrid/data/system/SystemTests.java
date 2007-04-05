package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.CreationTests;
import gov.nih.nci.cagrid.data.creation.TestServiceInfo;
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
 * @version $Id: SystemTests.java,v 1.14 2007-04-05 16:57:58 dervin Exp $
 */
public class SystemTests extends BaseSystemTest {
    private static GlobusHelper globusHelper = new GlobusHelper(false, new File(IntroduceTestConstants.TEST_TEMP),
        IntroduceTestConstants.TEST_PORT + 1);


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
        TestServiceInfo info = new CreationTests.TestDataServiceInfo();
        Vector steps = new Vector();
        // data service presumed to have been created
        // by the data service creation tests
        // 1) Add the bookstore schema to the data service
        steps.add(new AddBookstoreStep(info));
        // 2) change out query processor
        steps.add(new SetQueryProcessorStep(info.getDir()));
        // 3) Turn on query validation
        steps.add(new EnableValidationStep(info.getDir()));
        // 4) Rebuild the service to pick up the bookstore beans
        steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
        // 5) set up a clean, temporary Globus
        steps.add(new CreateCleanGlobusStep(globusHelper));
        // 6) deploy data service
        steps.add(new DeployDataServiceStep(globusHelper, info.getDir()));
        // 7) start globus
        steps.add(new StartGlobusStep(globusHelper));
        // 8) test data service
        steps.add(new InvokeDataServiceStep("localhost", IntroduceTestConstants.TEST_PORT + 1, info.getName()));

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


    // used to make sure that if we are going to use a junit testsuite to
    // test this that the test suite will not error out
    // looking for a single test......
    public void testDummy() throws Throwable {
    }


    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(SystemTests.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
