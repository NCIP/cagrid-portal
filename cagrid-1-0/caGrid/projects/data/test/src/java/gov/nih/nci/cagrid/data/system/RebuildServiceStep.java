package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.TestServiceInfo;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/** 
 *  RebuildServiceStep
 *  Rebuilds the stubs, invokes post-processing extensions,
 *  and compiles the service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: RebuildServiceStep.java,v 1.4 2007-03-22 14:21:25 dervin Exp $ 
 */
public class RebuildServiceStep extends Step {
	
    private TestServiceInfo serviceInfo;
	private String introduceDir;
	
	public RebuildServiceStep(TestServiceInfo serviceInfo, String introduceDir) {
		super();
        this.serviceInfo = serviceInfo;
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {		
		System.out.println("Running step: " + getClass().getName());
		
		System.out.println("Invoking post creation processes...");
		String cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, 
            serviceInfo.getName(), serviceInfo.getDir(), serviceInfo.getPackage(), 
            serviceInfo.getNamespace(), serviceInfo.getExtensions());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(serviceInfo.getDir());
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
}
