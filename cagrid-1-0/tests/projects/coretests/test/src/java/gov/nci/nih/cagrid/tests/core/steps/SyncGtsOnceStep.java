/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.AntUtils;
import gov.nci.nih.cagrid.tests.core.util.EnvUtils;

import java.io.File;
import java.util.Properties;

import com.atomicobject.haste.framework.Step;

public class SyncGtsOnceStep
	extends Step
{
	private GlobusHelper globus;
	private File gtsProject;
	
	public SyncGtsOnceStep()
	{
		this(new File(System.getProperty("syncgts.project.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + "caGrid" + File.separator + "projects" + File.separator + "syncgts"
		)));
	}
	
	public SyncGtsOnceStep(GlobusHelper globus)
	{
		this(new File(System.getProperty("syncgts.project.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + "caGrid" + File.separator + "projects" + File.separator + "syncgts"
		)), globus);
	}
	
	public SyncGtsOnceStep(File gtsProject)
	{
		this(gtsProject, null);
	}
	
	public SyncGtsOnceStep(File gtsProject, GlobusHelper globus)
	{
		super();
		
		this.globus = globus;
		this.gtsProject = gtsProject;
	}
	
	public void runStep() 
		throws Throwable
	{
		Properties sysProps = null;
		String[] envp = null;
		
		if (globus != null) {
			sysProps = new Properties();
			sysProps.setProperty("GLOBUS_LOCATION", globus.getTempGlobusLocation().toString());
			
			envp = new String[] {
				"GLOBUS_LOCATION=" + globus.getTempGlobusLocation(),
			};
			envp = EnvUtils.overrideEnv(envp);
		}
		
		AntUtils.runAnt(gtsProject, "build.xml", "syncOnce", sysProps, envp);
	}
}
