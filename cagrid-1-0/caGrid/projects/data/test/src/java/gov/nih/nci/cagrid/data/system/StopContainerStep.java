package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * StopContainerStep 
 * Shuts down the service container I've been testing against
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: StopContainerStep.java,v 1.1 2007-10-31 19:32:05 dervin Exp $
 */
public class StopContainerStep extends Step {

	private ServiceContainer container;

	public StopContainerStep(ServiceContainer container) {
		this.container = container;
	}

    
	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		System.out.println("stopping temporary container");

		container.stopContainer();

		// assertFalse("Globus should no longer be running, yet it is", helper.isGlobusRunning());
	}
}
