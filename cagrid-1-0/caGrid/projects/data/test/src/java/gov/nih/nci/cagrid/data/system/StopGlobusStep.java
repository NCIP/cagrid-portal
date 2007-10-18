package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * StopGlobusStep Shuts down the Globus container I've been testing against
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: StopGlobusStep.java,v 1.5 2007-10-18 18:57:44 dervin Exp $
 */
public class StopGlobusStep extends Step {

	private ServiceContainer container;

	public StopGlobusStep(ServiceContainer container) {
		this.container = container;
	}

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		System.out.println("stopping temporary globus");

		container.stopContainer();

		// assertFalse("Globus should no longer be running, yet it is", helper.isGlobusRunning());
	}
}
