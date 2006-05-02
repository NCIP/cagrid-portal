package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.example.ExampleCodegenPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.example.ExampleCodegenPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.example.ExampleCreationPostProcessor;

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
			System.out.println("basedir system property not set");
			throw new Exception("basedir system property not set");
		}

		String cmd = CommonTools.getAntSkeletonCreationCommand(pathtobasedir, tci.getName(), tci.getDir(), tci
			.getPackageName(), tci.getNamespaceDomain(),"service_example");

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking creation status", 0, p.exitValue());

		cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
		
		//check to see that the extensions ran ok
		File createFile = new File(tci.getDir() + File.separator + ExampleCreationPostProcessor.class.getName());
		if(!createFile.exists()){
			fail("Creation Extension was not ran");
		} 
		File syncPreFile = new File(tci.getDir() + File.separator + ExampleCodegenPreProcessor.class.getName());
		if(!syncPreFile.exists()){
			fail("Codegen Pre Extension was not ran");
		}
		File syncPostFile = new File(tci.getDir() + File.separator + ExampleCodegenPostProcessor.class.getName());
		if(!syncPostFile.exists()){
			fail("Codegen Post Extension was not ran");
		}
	
	}

}
