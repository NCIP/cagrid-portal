package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * DestroyTempGlobusStep 
 * Step to destroy the temporary testing container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: DestroyTempContainerStep.java,v 1.1 2007-11-02 15:25:40 dervin Exp $
 */
public class DestroyTempContainerStep extends Step {

	private ServiceContainer container;

	public DestroyTempContainerStep(ServiceContainer container) {
		this.container = container;
	}

    
	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		container.deleteContainer();
	}
}
