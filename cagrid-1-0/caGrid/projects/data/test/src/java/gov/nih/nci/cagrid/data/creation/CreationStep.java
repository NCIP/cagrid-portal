package gov.nih.nci.cagrid.data.creation;


import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/** 
 *  CreationStep
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id$ 
 */
public class CreationStep extends Step {
	private String introduceDir;
	
	public CreationStep(String introduceDir) {
		super();
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Creating service...");

		String cmd = CommonTools.getAntSkeletonCreationCommand(introduceDir, CreationTests.SERVICE_NAME, 
			CreationTests.SERVICE_DIR, CreationTests.PACKAGE_NAME, CreationTests.SERVICE_NAMESPACE, "data");
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Creating new data service completed successfully", p.exitValue() == 0);
		
		System.out.println("Invoking post creation processes...");
		cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, CreationTests.SERVICE_NAME,
			CreationTests.SERVICE_DIR, CreationTests.PACKAGE_NAME, CreationTests.SERVICE_NAMESPACE, "data");
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creations completed successfully", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(CreationTests.SERVICE_DIR);
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process completed successfully", p.exitValue() == 0);
	}
}
