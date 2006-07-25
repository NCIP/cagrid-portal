/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;

/**
 * This step attempts to stop a running temporary globus container by first running the 
 * org.globus.wsrf.container.ShutdownClient and then killing the process. 
 * @author Patrick McConnell
 */
public class GlobusStopStep
	extends Step
{
	private GlobusHelper globus;
	private int port;
	
	public GlobusStopStep(GlobusHelper globus, int port)
	{
		super();
		
		this.globus = globus;
		this.port = port;
	}
	
	public void runStep() throws Throwable
	{
		globus.stopGlobus(port);
	}
}