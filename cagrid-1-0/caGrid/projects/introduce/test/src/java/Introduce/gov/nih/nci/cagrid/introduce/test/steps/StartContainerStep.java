package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;


public class StartContainerStep extends Step {
	private ServiceContainer container;

	public StartContainerStep(ServiceContainer container) throws Exception {
		super();
		this.container = container;
	}


	public void runStep() throws Throwable {
		System.out.println("Starting temporary service container");
		
		container.startContainer();
	}
}
