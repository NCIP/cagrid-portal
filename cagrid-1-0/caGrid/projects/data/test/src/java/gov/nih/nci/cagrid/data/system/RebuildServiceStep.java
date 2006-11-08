package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.CreationTests;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/** 
 *  RebuildServiceStep
 *  Rebuilds the stubs, invokes post-processing extensions,
 *  and compiles the service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: RebuildServiceStep.java,v 1.1 2006-11-08 18:09:38 dervin Exp $ 
 */
public class RebuildServiceStep extends Step {
	
	private String introduceDir;
	
	public RebuildServiceStep(String introduceDir) {
		super();
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {		
		System.out.println("Running step: " + getClass().getName());
		
		System.out.println("Invoking post creation processes...");
		String cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, CreationTests.SERVICE_NAME,
			CreationTests.SERVICE_DIR, CreationTests.PACKAGE_NAME, CreationTests.SERVICE_NAMESPACE, "data");
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creations completed successfully", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(CreationTests.SERVICE_DIR);
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process completed successfully", p.exitValue() == 0);
	}
}
