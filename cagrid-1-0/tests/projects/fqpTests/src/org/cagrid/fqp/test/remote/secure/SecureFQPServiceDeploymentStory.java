package org.cagrid.fqp.test.remote.secure;

import gov.nih.nci.cagrid.testing.system.deployment.SecureContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyCAStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyProxyStep;
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
 * SecureFQPServiceDeploymentStory
 * Deploys a secure FQP service which trusts communication with several data services
 * 
 * @author David
 */
public class SecureFQPServiceDeploymentStory extends Story implements ServiceContainerSource {

    private ServiceContainerSource[] trustedDataServices;
    
    private File fqpServiceDirectory;
    
    private ServiceContainer fqpServiceContainer;
    private boolean complete;
    
    public SecureFQPServiceDeploymentStory(File fqpDir, ServiceContainerSource[] dataServices) {
        this.fqpServiceDirectory = fqpDir;
        this.trustedDataServices = dataServices;
        this.complete = false;
    }


    public String getName() {
        return "Secure FQP Service Deployment Story";
    }
    
    
    public String getDescription() {
        return "Deploys a secure FQP service";
    }
    
    
    public boolean storySetUp() {
        try {
            // must be tomcat container for transfer to work
            fqpServiceContainer = ServiceContainerFactory.createContainer(
                ServiceContainerType.SECURE_TOMCAT_CONTAINER);
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
        File tempFqpServiceDir = new File("tmp/TempSecureFQP");
        // prepare the container and service
        steps.add(new UnpackContainerStep(fqpServiceContainer));
        steps.add(new CopyServiceStep(fqpServiceDirectory, tempFqpServiceDir));
        steps.add(new ChangeJndiSweeperDelayStep(tempFqpServiceDir, 2000));
        // set the temp FQP service to trust the data services
        for (ServiceContainerSource trustedService : trustedDataServices) {
            steps.add(new CopyCAStep((SecureContainer) trustedService.getServiceContainer(), tempFqpServiceDir));
            steps.add(new CopyProxyStep((SecureContainer) trustedService.getServiceContainer(), tempFqpServiceDir));
        }
        // deploy and start the FQP service
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
