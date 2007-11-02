package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.common.AntTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;


public class InvokeSimpleMethodImplStep extends BaseStep {
    public static final String TEST_URL_PREFIX = "http://localhost:"; 
    public static final String TEST_URL_SUFFIX = "/wsrf/services/cagrid/IntroduceTestService";
    
	private TestCaseInfo tci;
	private String methodName;
    private ServiceContainer container;


	public InvokeSimpleMethodImplStep(ServiceContainer container, TestCaseInfo tci, String methodName, boolean build) throws Exception {
		super(tci.getDir(),build);
		this.tci = tci;
		this.methodName = methodName;
        this.container = container;
	}


	public void runStep() throws Throwable {
		System.out.println("Invoking a simple methods implementation.");

		String cmd = AntTools.getAntCommand("runClient", tci.getDir());
		cmd += " -Dservice.url=" + TEST_URL_PREFIX 
            + container.getProperties().getPortPreference().getPort() + TEST_URL_SUFFIX;
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		
		assertTrue(p.exitValue()==0);
		
		buildStep();
	}

}
