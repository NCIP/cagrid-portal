package gov.nih.nci.cagrid.testing.system.deployment.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import java.io.File;

import org.apache.log4j.Logger;

import com.atomicobject.haste.framework.Step;

/**
 * DeployServiceStep 
 * Deploys a caGrid service to the container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: DeployServiceStep.java,v 1.1 2007-11-01 19:37:22 dervin Exp $
 */
public class DeployServiceStep extends Step {
    private static final Logger LOG = Logger.getLogger(DeployServiceStep.class);

	private ServiceContainer container;
	private String serviceBase;

	public DeployServiceStep(ServiceContainer container, String serviceBaseDir) {
		this.container = container;
		serviceBase = serviceBaseDir;
	}

	
    public void runStep() throws Throwable {
		LOG.debug("Running step: " + getClass().getName());
		File serviceBaseDir = new File(serviceBase);
		container.deployService(serviceBaseDir);
	}
}
