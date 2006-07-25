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

/**
 * This step runs the syncOnce ant task in the gts project directory to synchronize with the 
 * central GTS service.
 * @author Patrick McConnell
 */
public class GTSSyncOnceStep
	extends Step
{
	private GlobusHelper globus;
	private File gtsProject;
	
	public GTSSyncOnceStep()
	{
		this(new File(System.getProperty("syncgts.project.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + "caGrid" + File.separator + "projects" + File.separator + "syncgts"
		)));
	}
	
	public GTSSyncOnceStep(GlobusHelper globus)
	{
		this(new File(System.getProperty("syncgts.project.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + "caGrid" + File.separator + "projects" + File.separator + "syncgts"
		)), globus);
	}
	
	public GTSSyncOnceStep(File gtsProject)
	{
		this(gtsProject, null);
	}
	
	public GTSSyncOnceStep(File gtsProject, GlobusHelper globus)
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
