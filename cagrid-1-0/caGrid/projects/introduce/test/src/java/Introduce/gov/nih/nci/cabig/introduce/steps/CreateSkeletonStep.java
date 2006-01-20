package gov.nih.nci.cabig.introduce.steps;

import gov.nih.nci.cabig.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.common.CommonTools;

import java.io.File;

import com.atomicobject.haste.framework.Step;


public class CreateSkeletonStep extends Step {
	private TestCaseInfo tci;


	public CreateSkeletonStep(TestCaseInfo tci) {
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Creating the service skeleton");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}

		String cmd = CommonTools.getAntSkeletonCreationCommand(pathtobasedir, tci.getName(), tci.getDir(), tci
			.getPackageName(), tci.getNamespaceDomain());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals(0, p.exitValue());

		cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals(0, p.exitValue());
	}

}
