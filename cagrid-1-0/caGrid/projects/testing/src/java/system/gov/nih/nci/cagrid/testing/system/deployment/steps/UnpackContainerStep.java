package gov.nih.nci.cagrid.testing.system.deployment.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * UnpackContainerStep
 * Sets up a new service container environment
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: UnpackContainerStep.java,v 1.3 2008-04-17 19:19:01 dervin Exp $
 */
public class UnpackContainerStep extends Step {
    private static final Log LOG = LogFactory.getLog(UnpackContainerStep.class);
    
	private ServiceContainer container;

	public UnpackContainerStep(ServiceContainer container) {
		this.container = container;
	}

    
	public void runStep() throws Throwable {
		LOG.debug("Unpacking service container");
		container.unpackContainer();
	}
}
