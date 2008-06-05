package org.cagrid.data.styles.cacore4.test;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.data.styles.cacore4.test.steps.InvokeSDK4DataServiceStep;
import org.cagrid.data.styles.cacore4.test.steps.SDK4StyleCreationStep;
import org.cagrid.data.test.creation.DataTestCaseInfo;

/** 
 *  SDK4ServiceStyleInvocationTest
 *  Test to stand up an SDK 4 style data service and invoke it.
 * 
 * @author David Ervin
 * 
 * @created Feb 1, 2008 7:49:44 AM
 * @version $Id: SDK4ServiceStyleInvocationTest.java,v 1.1 2008-06-05 18:02:23 dervin Exp $ 
 */
public class SDK4ServiceStyleInvocationTest extends Story {
    
    private DataTestCaseInfo serviceTestInfo = null;
    private ServiceContainer container = null;

    public SDK4ServiceStyleInvocationTest() {
        super();
    }


    public String getDescription() {
        return "Test to stand up an SDK 4 style data service and invoke it";
    }
    
    
    public String getName() {
        return "SDK 4 Data Service style Invocation test";
    }
    
    
    public boolean storySetUp() throws Throwable {
        serviceTestInfo = SDK4ServiceStyleSystemTestConstants.SERVICE_TEST_CASE_INFO;
        
        // TODO: use "container of the day"
        container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER);
        
        File serviceDir = new File(serviceTestInfo.getDir());
        serviceDir.mkdirs();
        
        return container != null && serviceDir.exists() && serviceDir.isDirectory();
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        steps.add(new SDK4StyleCreationStep(serviceTestInfo, getIntroduceBaseDir()));
        steps.add(new UnpackContainerStep(container));
        steps.add(new DeployServiceStep(container, serviceTestInfo.getDir()));
        steps.add(new StartContainerStep(container));
        steps.add(new InvokeSDK4DataServiceStep(container, serviceTestInfo));
        return steps;
    }
    
    
    public void storyTearDown() throws Throwable {
        Utils.deleteDir(new File(serviceTestInfo.getDir()));
        new StopContainerStep(container).runStep();
        new DestroyContainerStep(container).runStep();
    }
    
    
    public String getIntroduceBaseDir() {
        String dir = System.getProperty(SDK4ServiceStyleSystemTestConstants.INTRODUCE_DIR_PROPERTY);
        if (dir == null) {
            fail("Introduce base dir system property " + 
                SDK4ServiceStyleSystemTestConstants.INTRODUCE_DIR_PROPERTY + " is required");
        }
        return dir;
    }
}
