package org.cagrid.data.test.upgrades.from1pt0;

import gov.nih.nci.cagrid.introduce.common.AntTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

/** 
 *  BuildUpgradedServiceStep
 *  Builds the upgraded data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: BuildUpgradedServiceStep.java,v 1.2 2008-09-11 17:47:50 dervin Exp $ 
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
		String cmd = AntTools.getAntCommand("clean", new File(serviceDir).getAbsolutePath());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Call to '" + cmd + "' failed", p.exitValue() == 0);
	}
	
	
	private void invokeBuildProcess() throws Exception {
		System.out.println("Building upgraded service...");
		String cmd = AntTools.getAntAllCommand(serviceDir);
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Call to '" + cmd + "' failed", p.exitValue() == 0);	
	}
}
