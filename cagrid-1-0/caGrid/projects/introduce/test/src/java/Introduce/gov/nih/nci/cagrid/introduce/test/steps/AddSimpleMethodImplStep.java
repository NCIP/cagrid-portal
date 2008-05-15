package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.util.SourceUtils;

import java.io.File;


public class AddSimpleMethodImplStep extends BaseStep {
	private TestCaseInfo tci;
	private String methodName;


	public AddSimpleMethodImplStep(TestCaseInfo tci, String methodName, boolean build) throws Exception {
		super(tci.getDir(),build);
		this.tci = tci;
		this.methodName = methodName;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding a simple methods implementation.");

		File inFileClient = new File(this.getClass().getResource(File.separator +"gold" + File.separator + "simple" + File.separator + "SimpleMethodClient.java").getFile());
		File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir() + File.separator + "client" + File.separator  + tci.getName() + "Client.java");
		
		SourceUtils.modifyImpl(inFileClient, outFileClient, "main");
		
		File inFileImpl = new File(this.getClass().getResource(File.separator +"gold" + File.separator  + "simple" + File.separator + "SimpleMethodImpl.java").getFile());
		File outFileImpl = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir() + File.separator + "service" + File.separator  + tci.getName() + "Impl.java");
		
		SourceUtils.modifyImpl(inFileImpl, outFileImpl, methodName);
			
		buildStep();
	}

}
