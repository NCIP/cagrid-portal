package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * DestroyTempGlobusStep 
 * Step to destroy the temporary testing container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: DestroyTempGlobusStep.java,v 1.6 2007-10-31 19:32:05 dervin Exp $
 */
public class DestroyTempGlobusStep extends Step {

	private ServiceContainer container;

	public DestroyTempGlobusStep(ServiceContainer container) {
		this.container = container;
	}

    
	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		container.deleteContainer();
	}
}
