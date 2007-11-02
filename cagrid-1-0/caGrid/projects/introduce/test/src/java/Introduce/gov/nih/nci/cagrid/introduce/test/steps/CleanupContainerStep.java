package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;


public class CleanupContainerStep extends Step {
	private ServiceContainer container;


	public CleanupContainerStep(ServiceContainer container) throws Exception {
		super();
		this.container = container;
	}


	public void runStep() throws Throwable {
		System.out.println("removing temporary service container");
		container.deleteContainer();
	}
}
