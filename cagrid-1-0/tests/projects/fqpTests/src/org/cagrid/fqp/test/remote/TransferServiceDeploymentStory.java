package org.cagrid.fqp.test.remote;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.TomcatServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.fqp.test.common.ServiceContainerSource;

/**
 * TransferServiceDeploymentStory
 * Deploys the caGrid transfer service to the same service container that
 * the FederatedQueryProcessor service lives in
 * 
 * @author David
 */
public class TransferServiceDeploymentStory extends Story {
    
    private File transferServiceDirectory;
    private ServiceContainerSource containerSource;
    
    
    public TransferServiceDeploymentStory(File transferServiceDirectory, ServiceContainerSource containerSource) {
        this.transferServiceDirectory = transferServiceDirectory;
        this.containerSource = containerSource;
    }
    
    
    public String getName() {
        return "Transfer Service Deployment Story";
    }
    

    public String getDescription() {
        return "Deploys the caGrid transfer service";
    }


    protected Vector steps() {
        ServiceContainer container = containerSource.getServiceContainer();
        assertTrue("CaGrid transfer service only works with Tomcat service container " +
                "(found " + container.getClass().getName() + ")", 
            container instanceof TomcatServiceContainer);
        Vector<Step> steps = new Vector<Step>();
        File tempTransferDir = new File("tmp/TempTransfer");
        steps.add(new CopyServiceStep(transferServiceDirectory, tempTransferDir));
        steps.add(new StopContainerStep(container));
        steps.add(new DeployServiceStep(container, tempTransferDir.getAbsolutePath()));
        steps.add(new StartContainerStep(container));
        return steps;
    }
}
