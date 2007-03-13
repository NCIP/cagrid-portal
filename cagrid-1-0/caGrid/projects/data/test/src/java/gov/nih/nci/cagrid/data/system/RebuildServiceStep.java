package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/** 
 *  RebuildServiceStep
 *  Rebuilds the stubs, invokes post-processing extensions,
 *  and compiles the service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: RebuildServiceStep.java,v 1.3 2007-03-13 19:34:15 dervin Exp $ 
 */
public class RebuildServiceStep extends Step {
	
	private String introduceDir;
	private String serviceBaseDir;
	private String serviceName;
	private String packageName;
	private String namespace;
	
	public RebuildServiceStep(String introduceDir, String serviceBaseDir, 
		String serviceName, String packageName, String namespace) {
		super();
		this.introduceDir = introduceDir;
		this.serviceBaseDir = serviceBaseDir;
		this.serviceName = serviceName;
		this.packageName = packageName;
		this.namespace = namespace;
	}
	

	public void runStep() throws Throwable {		
		System.out.println("Running step: " + getClass().getName());
		
		System.out.println("Invoking post creation processes...");
		String cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, serviceName,
			serviceBaseDir, packageName, namespace, "data");
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(serviceBaseDir);
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
}
