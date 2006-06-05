/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;

public class DeployGlobusServiceStep
	extends Step
{
	private GlobusHelper globus;
	private File serviceDir;
	
	public DeployGlobusServiceStep(GlobusHelper globus, File serviceDir)
	{
		super();
		
		this.globus = globus;
		this.serviceDir = serviceDir;
	}
	
	public void runStep() throws Throwable
	{
		globus.deployService(serviceDir);
	}
}