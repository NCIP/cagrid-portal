package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import java.io.File;

import com.atomicobject.haste.framework.Step;


public class DeployServiceToContainerStep extends Step {
	private ServiceContainer container;
	private TestCaseInfo info;

	public DeployServiceToContainerStep(ServiceContainer container, TestCaseInfo info) throws Exception {
		super();
		this.info = info;
		this.container = container;
	}


	public void runStep() throws Throwable {
		System.out.println("Deploying service " + info.getName());

		container.deployService(new File(info.getDir()));
	}
}
