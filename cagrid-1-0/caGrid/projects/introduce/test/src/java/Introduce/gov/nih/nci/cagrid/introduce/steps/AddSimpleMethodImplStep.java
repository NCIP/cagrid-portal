package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.util.SourceUtils;

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

		File inFileClient = new File("test" + File.separator + "resources" + File.separator + "gold" + File.separator + "SimpleMethodClient.java");
		File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir() + File.separator + "client" + File.separator  + tci.getName() + "Client.java");
		
		SourceUtils.modifyImpl(inFileClient, outFileClient, "main");
		
		File inFileImpl = new File("test" + File.separator + "resources" + File.separator + "gold" + File.separator + "SimpleMethodImpl.java");
		File outFileImpl = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir() + File.separator + "service" + File.separator  + tci.getName() + "Impl.java");
		
		SourceUtils.modifyImpl(inFileImpl, outFileImpl, methodName);
			
		buildStep();
	}

}
