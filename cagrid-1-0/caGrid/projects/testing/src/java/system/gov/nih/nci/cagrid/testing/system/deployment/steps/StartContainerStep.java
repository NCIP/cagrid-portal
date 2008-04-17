package gov.nih.nci.cagrid.testing.system.deployment.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 *  StartContainerStep
 *  Step starts a service container
 * 
 * @author David Ervin
 * 
 * @created Nov 1, 2007 3:28:58 PM
 * @version $Id: StartContainerStep.java,v 1.3 2008-04-17 19:19:01 dervin Exp $ 
 */
public class StartContainerStep extends Step {
    private static final Log LOG = LogFactory.getLog(StartContainerStep.class);
    
    private ServiceContainer container;
    
    public StartContainerStep(ServiceContainer container) {
        this.container = container;
    }
    

    public void runStep() throws Throwable {
        LOG.debug("Starting service container");
        container.startContainer();
    }
}
