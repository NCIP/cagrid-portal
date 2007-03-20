package gov.nih.nci.cagrid.bdt.test.steps;


import gov.nih.nci.cagrid.bdt.test.unit.CreationTest;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/** 
 *  CreationStep
 *  TODO:DOCUMENT ME
 * 
 * 
 * @created Aug 22, 2006 
 * @version $Id: CreationStep.java,v 1.1 2007-03-20 18:44:46 hastings Exp $ 
 */
public class CreationStep extends Step {
	private String introduceDir;
	
	public CreationStep(String introduceDir) {
		super();
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Creating service...");

		String cmd = CommonTools.getAntSkeletonCreationCommand(introduceDir, CreationTest.SERVICE_NAME, 
			CreationTest.SERVICE_DIR, CreationTest.PACKAGE_NAME, CreationTest.SERVICE_NAMESPACE, "data");
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Creating new data service failed", p.exitValue() == 0);
		
		System.out.println("Invoking post creation processes...");
		cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, CreationTest.SERVICE_NAME,
			CreationTest.SERVICE_DIR, CreationTest.PACKAGE_NAME, CreationTest.SERVICE_NAMESPACE, "bdt");
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(CreationTest.SERVICE_DIR);
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
}
