package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainer;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/**
 * DeployDataServiceStep Deploys the data service to the globus container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: DeployDataServiceStep.java,v 1.3 2007-10-18 18:57:44 dervin Exp $
 */
public class DeployDataServiceStep extends Step {

	private ServiceContainer container;
	private String serviceBase;

	public DeployDataServiceStep(ServiceContainer container, String serviceBaseDir) {
		this.container = container;
		serviceBase = serviceBaseDir;
	}

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		File serviceBaseDir = new File(serviceBase);
		container.deployService(serviceBaseDir);
	}
}
