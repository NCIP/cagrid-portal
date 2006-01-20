package gov.nih.nci.cabig.introduce.steps;

import gov.nih.nci.cabig.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.IntroduceService;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;

import com.atomicobject.haste.framework.Step;

public class RollBackStep extends Step {
	private TestCaseInfo tci;
	
	public RollBackStep(TestCaseInfo tci){
		this.tci = tci;
	}

	public void runStep() throws Throwable {
		System.out.println("Rolling back to previous version.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		
		ResourceManager.restoreLatest(String.valueOf(System.currentTimeMillis()),tci.getName(),tci.getDir());

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir
				+ File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals(0, p.exitValue());
		
		//check to see that this is same as before....
	}

}
