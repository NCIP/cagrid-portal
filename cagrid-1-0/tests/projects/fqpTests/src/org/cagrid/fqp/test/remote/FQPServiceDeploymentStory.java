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
 * @version $Id: FQPServiceDeploymentStory.java,v 1.3 2008-09-03 17:28:16 dervin Exp $ 
 */
public class FQPServiceDeploymentStory extends Story implements ServiceContainerSource {
    
    private File fqpServiceDirectory;
    
    private ServiceContainer fqpServiceContainer;
    private boolean complete;
    
    public FQPServiceDeploymentStory(File fqpDir) {
        this.fqpServiceDirectory = fqpDir;
        this.complete = false;
    }
    
    
    public String getName() {
        return "FQP Service Deployment";
    }
    

    public String getDescription() {
        return "Deploys the FQP service to a local service container and starts it";
    }
    
    
    public boolean storySetUp() {
        try {
            fqpServiceContainer = 
                ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER);
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
        File tempFqpServiceDir = new File("tmp/TempFQP");
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
