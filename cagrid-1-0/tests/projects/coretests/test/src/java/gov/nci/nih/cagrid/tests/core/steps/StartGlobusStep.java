/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;

public class StartGlobusStep
	extends Step
{
	private GlobusHelper globus;
	private int port;
	
	public StartGlobusStep(GlobusHelper globus, int port)
	{
		super();
		
		this.globus = globus;
		this.port = port;
	}
	
	public void runStep() throws Throwable
	{

		globus.startGlobus(port);
	}
}