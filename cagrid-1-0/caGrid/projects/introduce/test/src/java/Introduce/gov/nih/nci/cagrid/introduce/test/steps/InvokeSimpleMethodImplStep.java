package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.common.AntTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.test.IntroduceTestConstants;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;


public class InvokeSimpleMethodImplStep extends BaseStep {
	private TestCaseInfo tci;
	private String methodName;


	public InvokeSimpleMethodImplStep(TestCaseInfo tci, String methodName, boolean build) throws Exception {
		super(tci.getDir(),build);
		this.tci = tci;
		this.methodName = methodName;
	}


	public void runStep() throws Throwable {
		System.out.println("Invoking a simple methods implementation.");

		String cmd = AntTools.getAntCommand("runClient",tci.getDir());
		cmd += " -Dservice.url=" + IntroduceTestConstants.TEST_URL ;
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		
		assertTrue(p.exitValue()==0);
		
		buildStep();
	}

}
