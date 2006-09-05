package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.example.ExampleCodegenPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.example.ExampleCodegenPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.example.ExampleCreationPostProcessor;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

import java.io.File;

import com.atomicobject.haste.framework.Step;

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
						.getNamespace(), "service_example");

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking creation status", 0, p.exitValue());

		cmd = CommonTools.getAntSkeletonPostCreationCommand(getBaseDir(), tci
				.getName(), tci.getDir(), tci.getPackageName(), tci
				.getNamespace(), "service_example");

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking creation status", 0, p.exitValue());

		buildStep();

		// check to see that the extensions ran ok
		File createFile = new File(tci.getDir() + File.separator
				+ ExampleCreationPostProcessor.class.getName());
		if (!createFile.exists()) {
			fail("Creation Extension was not ran");
		}
		File syncPreFile = new File(tci.getDir() + File.separator
				+ ExampleCodegenPreProcessor.class.getName());
		if (!syncPreFile.exists()) {
			fail("Codegen Pre Extension was not ran");
		}
		File syncPostFile = new File(tci.getDir() + File.separator
				+ ExampleCodegenPostProcessor.class.getName());
		if (!syncPostFile.exists()) {
			fail("Codegen Post Extension was not ran");
		}

	}

}
