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
    protected TestServiceInfo serviceInfo;
    protected String introduceDir;
	
	public CreationStep(TestServiceInfo serviceInfo, String introduceDir) {
		super();
        this.serviceInfo = serviceInfo;
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Creating service...");

		String cmd = CommonTools.getAntSkeletonCreationCommand(introduceDir, serviceInfo.getName(), 
			serviceInfo.getDir(), serviceInfo.getPackage(), serviceInfo.getNamespace(), serviceInfo.getExtensions());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Creating new data service failed", p.exitValue() == 0);
        
        postSkeletonCreation();
		
		System.out.println("Invoking post creation processes...");
		cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, serviceInfo.getName(),
			serviceInfo.getDir(), serviceInfo.getPackage(), serviceInfo.getNamespace(), serviceInfo.getExtensions());
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);
        
        postSkeletonPostCreation();

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(serviceInfo.getDir());
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
    
    
    protected void postSkeletonCreation() throws Throwable {
        // subclasses can hook in here to do things post-skeleton creation
    }
    
    
    protected void postSkeletonPostCreation() throws Throwable {
        // subclasses can hook in here to do things after post-creation process has run
    }
}
