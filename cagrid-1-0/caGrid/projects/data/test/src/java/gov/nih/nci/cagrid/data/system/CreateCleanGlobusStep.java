package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * CreateCleanGlobusStep Sets up a new globus environment
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: CreateCleanGlobusStep.java,v 1.3 2007-10-18 18:57:44 dervin Exp $
 */
public class CreateCleanGlobusStep extends Step {
	private ServiceContainer container;

	public CreateCleanGlobusStep(ServiceContainer container) {
		this.container = container;
	}

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		container.unpackContainer();
	}
}
