/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;

public class GlobusCleanupStep
	extends Step
{
	private GlobusHelper globus;
	
	public GlobusCleanupStep(GlobusHelper globus)
	{
		super();
		
		this.globus = globus;
	}
	
	public void runStep() throws Throwable
	{
		globus.cleanupTempGlobus();
	}
}