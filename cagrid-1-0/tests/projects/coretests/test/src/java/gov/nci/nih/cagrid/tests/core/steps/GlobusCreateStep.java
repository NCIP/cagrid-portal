/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;

/**
 * This step creates a temporary globus container by copying GLOBUS_LOCATION to a temp directory.
 * @author Patrick McConnell
 */
public class GlobusCreateStep
	extends Step
{
	private GlobusHelper globus;
	
	public GlobusCreateStep(GlobusHelper globus)
	{
		super();
		
		this.globus = globus;
	}
	
	public void runStep() throws Throwable
	{
		globus.createTempGlobus();
	}
}