package gov.nih.nci.cagrid.testing.system.deployment.steps;

import org.apache.log4j.Logger;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * UnpackContainerStep
 * Sets up a new service container environment
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: UnpackContainerStep.java,v 1.1 2007-11-01 19:37:22 dervin Exp $
 */
public class UnpackContainerStep extends Step {
    private static final Logger LOG = Logger.getLogger(UnpackContainerStep.class);
    
	private ServiceContainer container;

	public UnpackContainerStep(ServiceContainer container) {
		this.container = container;
	}

    
	public void runStep() throws Throwable {
		LOG.debug("Unpacking service container");
		container.unpackContainer();
	}
}
