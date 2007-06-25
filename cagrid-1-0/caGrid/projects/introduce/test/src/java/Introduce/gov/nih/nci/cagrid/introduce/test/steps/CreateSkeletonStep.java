package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

public class CreateSkeletonStep extends BaseStep {
	private TestCaseInfo tci;

	public CreateSkeletonStep(TestCaseInfo tci, boolean build) throws Exception {
		super(tci.getDir(), build);
		this.tci = tci;
	}

	public void runStep() throws Throwable {
		System.out.println("Creating the service skeleton");

		String cmd = CommonTools.getAntSkeletonCreationCommand(getBaseDir(),
				tci.getName(), tci.getDir(), tci.getPackageName(), tci
						.getNamespace(), "");

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking creation status", 0, p.exitValue());

		cmd = CommonTools.getAntSkeletonPostCreationCommand(getBaseDir(), tci
				.getName(), tci.getDir(), tci.getPackageName(), tci
				.getNamespace(), "");

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking creation status", 0, p.exitValue());

		buildStep();
	}

}
