package gov.nih.nci.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.NotificationTestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.PersistentTestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.steps.AddBookstoreSchemaStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddBookResourcePropertyStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddSetBookMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.InvokeClientStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;

import java.util.Vector;

import com.atomicobject.haste.framework.Story;


public class PersistenceTest extends Story {
    
    private ServiceContainer container;
    private PersistentTestCaseInfo tci;

    public PersistenceTest() {
        this.setName("Introduce Persistence System Test");
    }


    public String getName() {
        return "Introduce Persistence System Test";
    }


    public String getDescription() {
        return "Testing the Introduce Persistence support";
    }


    @Override
    protected Vector steps() {
        // init the container
        try {
            container = ServiceContainerFactory.createContainer(
                ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
        
        tci = new PersistentTestCaseInfo();
        Vector steps = new Vector();
        try {
            steps.add(new UnpackContainerStep(container));
            steps.add(new CreateSkeletonStep(tci, true));
            steps.add(new AddBookstoreSchemaStep(tci,false));
            steps.add(new AddBookResourcePropertyStep(tci,false));
            steps.add(new AddSetBookMethodStep(tci,false));
            //steps.add(new AddPersistenceMethodImplStep(tci,true));
            steps.add(new DeployServiceStep(container,tci.getDir()));
            steps.add(new StartContainerStep(container));
            steps.add(new InvokeClientStep(container,tci));
            steps.add(new StopContainerStep(container));
            //steps.add(new AddPersistenceMethodImplStep(tci,true));
            steps.add(new StartContainerStep(container));
            steps.add(new InvokeClientStep(container,tci));
            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return steps;
    }
    

    protected boolean storySetUp() throws Throwable {
        super.storySetUp();
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci);
        try {
            step1.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci);
        try {
            step1.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StopContainerStep step2 = new StopContainerStep(container);
        try {
            step2.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DestroyContainerStep step3 = new DestroyContainerStep(container);
        try {
            step3.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // used to make sure that if we are going to use a junit testsuite to test
    // this
    // that the test suite will not error out looking for a single test......
    public void testDummy() throws Throwable {
    }

}
