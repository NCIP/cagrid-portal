package gov.nih.nci.cabig.introduce.steps;

import java.io.File;

import gov.nih.nci.cabig.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.common.CommonTools;

import com.atomicobject.haste.framework.Step;

public class BuildSkeletonStep extends Step {
	private TestCaseInfo tci;
	
	public BuildSkeletonStep(TestCaseInfo tci){
		this.tci = tci;
	}

	public void runStep() throws Throwable {
		System.out.println("Building the service skeleton");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}

		String cmd = CommonTools.getAntAllCommand(pathtobasedir
				+ File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals(0, p.exitValue());
	}

}
