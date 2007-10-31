package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/**
 * DeployDataServiceStep 
 * Deploys the data service to the container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: DeployDataServiceStep.java,v 1.4 2007-10-31 19:32:05 dervin Exp $
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
