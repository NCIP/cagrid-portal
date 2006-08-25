/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import com.atomicobject.haste.framework.Step;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.IntroduceServiceInfo;

/**
 * This step deploys a service to a temporary globus container by running the deployGlobus ant task
 * in the service directory.
 * @author Patrick McConnell
 */
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
		this(globus, null, serviceDir, target);
		
		this.globus = globus;
		this.serviceDir = serviceDir;
		this.target = target;
	}
	
	public GlobusDeployServiceStep(GlobusHelper globus, GlobusHelper secureGlobus, File serviceDir)
	{
		this(globus, secureGlobus, serviceDir,  null);
	}
	
	public GlobusDeployServiceStep(GlobusHelper globus, GlobusHelper secureGlobus, File serviceDir, String target)
	{
		super();
		
		this.serviceDir = serviceDir;
		this.target = target;
		
		if (globus == null) {
			this.globus = globus;
		} else if (secureGlobus == null) {
			this.globus = secureGlobus;
		} else {
			try {
				IntroduceServiceInfo introduce = new IntroduceServiceInfo(new File(serviceDir, "introduce.xml"));
				if (introduce.isTransportSecurity()) this.globus = secureGlobus;
				else this.globus = globus;
			} catch (Exception e) {
				throw new RuntimeException("unable to get service info from introduce.xml", e);
			}
		}
	}
	
	public void runStep() throws Throwable
	{
		if (target == null) globus.deployService(serviceDir);
		else globus.deployService(serviceDir, target);
	}
}