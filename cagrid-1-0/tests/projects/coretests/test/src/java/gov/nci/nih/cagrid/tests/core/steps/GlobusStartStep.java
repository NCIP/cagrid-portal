/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;


/**
 * This step starts a temporary globus container by issuing a java command on
 * org.globus.wsrf.container.ServiceContainer in the temporary globus container
 * directory.
 * 
 * @author Patrick McConnell
 */
public class GlobusStartStep extends Step {
	private GlobusHelper globus;


	public GlobusStartStep(GlobusHelper globus) {
		super();
		this.globus = globus;
	}


	@Override
	public void runStep() throws Throwable {
		this.globus.startGlobus();
	}
}