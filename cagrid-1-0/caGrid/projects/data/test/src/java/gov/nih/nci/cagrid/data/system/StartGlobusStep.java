package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.tests.deployment.ServiceContainer;

import com.atomicobject.haste.framework.Step;

/**
 * StartGlobusStep Step to start the Globus container with my data service
 * deployed to it
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: StartGlobusStep.java,v 1.5 2007-10-18 18:57:44 dervin Exp $
 */
public class StartGlobusStep extends Step {

	private ServiceContainer container;

	public StartGlobusStep(ServiceContainer container) {
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
