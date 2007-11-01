package gov.nih.nci.cagrid.testing.system.deployment.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import org.apache.log4j.Logger;

import com.atomicobject.haste.framework.Step;

/**
 * DestroyContainerStep 
 * Step to destroy the temporary testing container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: DestroyContainerStep.java,v 1.1 2007-11-01 19:37:22 dervin Exp $
 */
public class DestroyContainerStep extends Step {
    private static final Logger LOG = Logger.getLogger(DestroyContainerStep.class);

	private ServiceContainer container;

	public DestroyContainerStep(ServiceContainer container) {
		this.container = container;
	}

    
	public void runStep() throws Throwable {
		LOG.debug("Destroying service container");
		container.deleteContainer();
	}
}
