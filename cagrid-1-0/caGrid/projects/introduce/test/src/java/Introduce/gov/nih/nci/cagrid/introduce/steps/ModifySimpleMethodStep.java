package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;

public class ModifySimpleMethodStep extends Step {
	private TestCaseInfo tci;
	private String methodName;

	public ModifySimpleMethodStep(TestCaseInfo tci, String methodName) {
		this.tci = tci;
		this.methodName = methodName;
	}

	public void runStep() throws Throwable {
		System.out.println("modifying the simple method.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}

		ServiceDescription introService = (ServiceDescription) Utils
				.deserializeDocument(pathtobasedir + File.separator
						+ tci.getDir() + File.separator + "introduce.xml",
						ServiceDescription.class);
		MethodsType methodsType = introService.getMethods();
		MethodType method = methodsType.getMethod(0);
		
		//create a new input param
		MethodTypeInputsInput input = new MethodTypeInputsInput();
		input.setNamespace("http://www.w3.org/2001/XMLSchema");
		input.setName("bar");
		input.setType("int");
		input.setIsArray(new Boolean(false));
		
		// add new input to array in bean
		// this seems to be a wierd way be adding things....
		MethodTypeInputsInput[] newInputs;
		int newLength = 0;
		if (method.getInputs()!=null && method.getInputs().getInput()!=null) {
			newLength = method.getInputs().getInput().length + 1;
			newInputs = new MethodTypeInputsInput[newLength];
			System.arraycopy(method.getInputs().getInput(), 0,newInputs, 0,
					method.getInputs().getInput().length);
		} else {
			newLength = 1;
			newInputs = new MethodTypeInputsInput[newLength];
		}
		newInputs[newLength - 1] = input;
		MethodTypeInputs inputs = new MethodTypeInputs();
		inputs.setInput(newInputs);
		method.setInputs(inputs);

		Utils.serializeDocument(pathtobasedir + File.separator
				+ tci.getDir() + File.separator + "introduce.xml",
				introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce",
						"ServiceSkeleton"));

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir
				+ File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals("Checking resync status", 0, p.exitValue());

		
		// look at the interface to make sure method exists.......
		String serviceInterface = pathtobasedir + File.separator + tci.dir
				+ File.separator + "src" + File.separator
				+ tci.getPackageDir() + "/common/" + tci.getName() + "I.java";
		assertTrue(StepTools.methodExists(serviceInterface,methodName));
	

		cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator
				+ tci.getDir());

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
	}

}
