package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.CreationTests;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import com.atomicobject.haste.framework.Step;

/** 
 *  PlainDataServiceSystemTests
 *  System test to just create a data service, deploy it, 
 *  and make sure it can start up.
 * 
 * @author David Ervin
 * 
 * @created Sep 28, 2007 12:22:29 PM
 * @version $Id: PlainDataServiceSystemTests.java,v 1.1 2007-09-28 20:09:52 dervin Exp $ 
 */
public class PlainDataServiceSystemTests extends BaseSystemTest {
    private static int TEST_PORT = IntroduceTestConstants.TEST_PORT + 510;
    private static GlobusHelper globusHelper = new GlobusHelper(
        false, new File(IntroduceTestConstants.TEST_TEMP), TEST_PORT);
    
    
    public PlainDataServiceSystemTests() {
        setName("Plain Data Service System Test");
    }
    
    
    public String getName() {
        return "Plain Data Service System Test";
    }


    public String getDescription() {
        return "System test to just create a data service, deploy it, " +
                "and make sure it can start up.";
    }
    
    
    protected boolean storySetUp() {
        assertFalse("Globus should NOT be running yet", globusHelper.isGlobusRunning());
        return true;
    }


    protected Vector steps() {
        DataTestCaseInfo info = new CreationTests.PlainDataServiceInfo();
        Vector<Step> steps = new Vector<Step>();
        // data service presumed to have been created
        // by the data service creation tests
        // 1) Rebuild the service
        steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
        // 2) set up a clean, temporary Globus
        steps.add(new CreateCleanGlobusStep(globusHelper));
        // 3) deploy data service
        steps.add(new DeployDataServiceStep(globusHelper, info.getDir()));
        // 4) start globus
        steps.add(new StartGlobusStep(globusHelper));
        return steps;
    }
    
    
    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        // 5) stop globus
        Step stopStep = new StopGlobusStep(globusHelper);
        try {
            stopStep.runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        // 6) throw away globus
        Step destroyStep = new DestroyTempGlobusStep(globusHelper);
        try {
            destroyStep.runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
