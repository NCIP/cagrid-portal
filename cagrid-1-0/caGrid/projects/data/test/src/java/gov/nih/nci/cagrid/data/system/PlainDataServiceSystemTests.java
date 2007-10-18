package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.CreationTests;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainer;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainerType;

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
 * @version $Id: PlainDataServiceSystemTests.java,v 1.2 2007-10-18 18:57:44 dervin Exp $ 
 */
public class PlainDataServiceSystemTests extends BaseSystemTest {
    
    private static ServiceContainer container = null;
    
    static {
        try {
            container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }
    
    
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
        DataTestCaseInfo info = new CreationTests.PlainDataServiceInfo();
        Vector<Step> steps = new Vector<Step>();
        // data service presumed to have been created
        // by the data service creation tests
        // 2) Rebuild the service
        steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
        // 3) deploy data service
        steps.add(new DeployDataServiceStep(container, info.getDir()));
        // 4) start the container
        steps.add(new StartGlobusStep(container));
        return steps;
    }
    
    
    protected void storyTearDown() throws Throwable {
        // 5) stop globus
        Step stopStep = new StopGlobusStep(container);
        try {
            stopStep.runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        // 6) throw away globus
        Step destroyStep = new DestroyTempGlobusStep(container);
        try {
            destroyStep.runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
