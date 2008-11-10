package org.cagrid.fqp.test.remote;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.fqp.test.common.ServiceContainerSource;
import org.cagrid.fqp.test.remote.steps.ChangeJndiSweeperDelayStep;

/** 
 *  FQPServiceDeploymentStory
 *  Deploys the federated query service to a local service 
 *  container and gets it running
 * 
 * @author David Ervin
 * 
 * @created Jul 15, 2008 12:46:02 PM
 * @version $Id: FQPServiceDeploymentStory.java,v 1.6 2008-11-10 21:00:13 dervin Exp $ 
 */
public class FQPServiceDeploymentStory extends Story implements ServiceContainerSource {
    
    private File fqpServiceDirectory;
    
    private ServiceContainer fqpServiceContainer;
    private boolean secureDeployment;
    private boolean complete;
    
    public FQPServiceDeploymentStory(File fqpDir, boolean secureDeployment) {
        this.fqpServiceDirectory = fqpDir;
        this.secureDeployment = secureDeployment;
        this.complete = false;
    }
    
    
    public String getName() {
        return (secureDeployment ? "Secure " : "") + "FQP Service Deployment";
    }
    

    public String getDescription() {
        return "Deploys the FQP service to a " + (secureDeployment ? "secure " : "") +
                "local service container and starts it";
    }
    
    
    public boolean storySetUp() {
        try {
            // must be tomcat container for transfer to work
            ServiceContainerType containerType = 
                secureDeployment ? ServiceContainerType.SECURE_TOMCAT_CONTAINER : ServiceContainerType.TOMCAT_CONTAINER;
            fqpServiceContainer = ServiceContainerFactory.createContainer(containerType);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
        
    /**
     * Overridden to run tests, and on successful completion set 
     * the 'complete' flag to true so the service container can be returned
     */
    protected void runTest() throws Throwable {
        super.runTest();
        complete = true;
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        File tempFqpServiceDir = new File("tmp/Temp" + (secureDeployment ? "Secure" : "") + "FQP");
        steps.add(new UnpackContainerStep(fqpServiceContainer));
        steps.add(new CopyServiceStep(fqpServiceDirectory, tempFqpServiceDir));
        steps.add(new ChangeJndiSweeperDelayStep(tempFqpServiceDir, 2000));
        steps.add(new DeployServiceStep(fqpServiceContainer, tempFqpServiceDir.getAbsolutePath()));
        steps.add(new StartContainerStep(fqpServiceContainer));
        return steps;
    }
    
    
    public ServiceContainer getServiceContainer() {
        if (fqpServiceContainer == null || !complete) {
            throw new IllegalStateException(
                "Deployment Story has not completed to create a working service container!");
        }
        return fqpServiceContainer;
    }
}
