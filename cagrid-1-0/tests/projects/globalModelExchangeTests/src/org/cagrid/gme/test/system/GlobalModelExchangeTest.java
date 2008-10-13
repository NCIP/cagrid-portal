package org.cagrid.gme.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.story.ServiceStoryBase;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.util.Vector;

import org.cagrid.gme.test.system.steps.CreateDatabaseStep;
import org.cagrid.gme.test.system.steps.SetDatabasePropertiesStep;


public class GlobalModelExchangeTest extends ServiceStoryBase {

    public static final String GME_DIR_PROPERTY = "gme.service.dir";


    public GlobalModelExchangeTest(ServiceContainer container) {
        super(container);
    }


    public GlobalModelExchangeTest() {

        // init the container
        try {
            this.setContainer(ServiceContainerFactory.createContainer(ServiceContainerType.TOMCAT_CONTAINER));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }


    @Override
    public String getName() {
        return getDescription();
    }


    @Override
    public String getDescription() {
        return "Global Model Exchange Service Test";
    }


    protected File getGMEDir() {
        String value = System.getProperty(GME_DIR_PROPERTY, "../../../caGrid/projects/globalModelExchange");
        assertNotNull("System property " + GME_DIR_PROPERTY + " was not set!", value);
        File dir = new File(value);
        return dir;
    }


    @Override
    protected Vector<Step> steps() {
        Vector<Step> steps = new Vector<Step>();
        File tempGMEServiceDir = new File("tmp/TempGME");

        // SETUP
        steps.add(new UnpackContainerStep(getContainer()));
        steps.add(new CopyServiceStep(getGMEDir(), tempGMEServiceDir));

        // CONFIGURE
        steps.add(new SetDatabasePropertiesStep(tempGMEServiceDir));
        steps.add(new CreateDatabaseStep(tempGMEServiceDir));

        // STARTUP
        steps.add(new DeployServiceStep(getContainer(), tempGMEServiceDir.getAbsolutePath()));
        steps.add(new StartContainerStep(getContainer()));

        // TEST

        return steps;
    }


    @Override
    protected void storyTearDown() throws Throwable {

        StopContainerStep step2 = new StopContainerStep(getContainer());
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        DestroyContainerStep step3 = new DestroyContainerStep(getContainer());
        try {
            step3.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
