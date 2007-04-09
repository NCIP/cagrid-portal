package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  BuildUpgradedServiceStep
 *  Builds the service upgraded from version 1.0 to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: BuildUpgradedServiceStep.java,v 1.2 2007-04-09 15:26:14 dervin Exp $ 
 */
public class BuildUpgradedServiceStep extends Step {
	
	private String serviceDir;
	
	public BuildUpgradedServiceStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		cleanService();
		
		invokeBuildProcess();
	}
	
	
	private void cleanService() throws Exception {
		String cmd = CommonTools.getAntCommand("clean", new File(serviceDir).getAbsolutePath());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Call to '" + cmd + "' failed", p.exitValue() == 0);
	}
	
	
	private void invokeBuildProcess() throws Exception {
		System.out.println("Building created service...");
		String cmd = CommonTools.getAntAllCommand(serviceDir);
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);	
	}
}
