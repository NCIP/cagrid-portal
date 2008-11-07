package org.cagrid.fqp.test.remote.secure;

import gov.nih.nci.cagrid.testing.system.deployment.SecureContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyCAStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.util.Vector;

import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;
import org.cagrid.fqp.test.common.ServiceContainerSource;

/**
 * SecureStandardQueryStory
 * Executes basic queries against a secure Federated Query Processor Service
 * 
 * @author David
 */
public class SecureStandardQueryStory extends Story {
    
    private ServiceContainerSource[] dataServiceContainers = null;
    private ServiceContainerSource fqpContainerSource = null;
    private FederatedQueryProcessorHelper queryHelper = null;
    
    public String getName() {
        return "Secure Standard Query Story";
    }
    

    public String getDescription() {
        return "Executes basic queries against a secure Federated Query Processor Service";
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        
        assertTrue("FQP service container was not secure!", 
            fqpContainerSource.getServiceContainer() instanceof SecureContainer);
        
        ServiceContainer fqpContainer = fqpContainerSource.getServiceContainer();
        
        // stop the FQP container
        steps.add(new StopContainerStep(fqpContainer));
        
        // set up the FQP container to trust the data services
        for (ServiceContainerSource dataContainerSource : dataServiceContainers) {
            ServiceContainer dataContainer = dataContainerSource.getServiceContainer();
            assertTrue("Data Service container was not secure!",
                dataContainer instanceof SecureContainer);
        }
        
        return steps;
    }
}
