package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;


public class StopContainerStep extends Step {
	private ServiceContainer container;


	public StopContainerStep(ServiceContainer container) throws Exception {
		super();
		this.container = container;
	}


	public void runStep() throws Throwable {
		System.out.println("stopping temporary service container");

		container.stopContainer();

		assertFalse(container.isStarted());
	}
}
