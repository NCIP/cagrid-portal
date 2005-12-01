package gov.nih.nci.cabig.introduce;

import gov.nih.nci.cagrid.introduce.CommonTools;

import com.atomicobject.haste.framework.Step;

public class CreateSkeletonStep extends Step {
	
	public void runStep() throws Throwable {
		System.out.println("Creating the service skeleton");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if(pathtobasedir == null){
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		
		String cmd = CommonTools.getAntSkeletonCreationCommand(pathtobasedir,
				TestCaseInfo.name, TestCaseInfo.dir, TestCaseInfo.packageName,
				TestCaseInfo.namespaceDomain);

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals(0,p.exitValue());
	}

}
