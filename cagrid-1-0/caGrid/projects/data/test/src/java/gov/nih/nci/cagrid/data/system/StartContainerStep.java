package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * StartContainerStep 
 * Step to start the service container with my data service
 * deployed to it
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: StartContainerStep.java,v 1.1 2007-10-31 19:32:05 dervin Exp $
 */
public class StartContainerStep extends Step {

	private ServiceContainer container;

	public StartContainerStep(ServiceContainer container) {
		this.container = container;
	}

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
        /*
		assertFalse("Globus found to be running and should not be", 
			helper.isGlobusRunning());
            */
		// helper.startGlobus();
        container.startContainer();
        /*
		assertTrue("Globus should be running at this point and is not", 
			helper.isGlobusRunning());
            */
	}
}
