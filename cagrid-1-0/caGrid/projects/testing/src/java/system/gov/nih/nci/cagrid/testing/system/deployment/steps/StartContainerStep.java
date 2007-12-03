package gov.nih.nci.cagrid.testing.system.deployment.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import org.apache.log4j.Logger;

/** 
 *  StartContainerStep
 *  Step starts a service container
 * 
 * @author David Ervin
 * 
 * @created Nov 1, 2007 3:28:58 PM
 * @version $Id: StartContainerStep.java,v 1.2 2007-12-03 16:27:18 hastings Exp $ 
 */
public class StartContainerStep extends Step {
    private static final Logger LOG = Logger.getLogger(StartContainerStep.class);
    
    private ServiceContainer container;
    
    public StartContainerStep(ServiceContainer container) {
        this.container = container;
    }
    

    public void runStep() throws Throwable {
        LOG.debug("Starting service container");
        container.startContainer();
    }
}
