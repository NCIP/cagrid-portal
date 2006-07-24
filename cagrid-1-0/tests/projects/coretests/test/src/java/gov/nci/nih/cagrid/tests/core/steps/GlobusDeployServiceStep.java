/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;

public class GlobusDeployServiceStep
	extends Step
{
	private GlobusHelper globus;
	private File serviceDir;
	private String target;
	
	public GlobusDeployServiceStep(GlobusHelper globus, File serviceDir)
	{
		this(globus, serviceDir, null);
	}
	
	public GlobusDeployServiceStep(GlobusHelper globus, File serviceDir, String target)
	{
		super();
		
		this.globus = globus;
		this.serviceDir = serviceDir;
		this.target = target;
	}
	
	public void runStep() throws Throwable
	{
		if (target == null) globus.deployService(serviceDir);
		else globus.deployService(serviceDir, target);
	}
}