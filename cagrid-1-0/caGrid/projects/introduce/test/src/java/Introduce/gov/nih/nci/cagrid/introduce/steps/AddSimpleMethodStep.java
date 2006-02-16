package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;

public class AddSimpleMethodStep extends Step {
	private TestCaseInfo tci;
	private String methodName;

	public AddSimpleMethodStep(TestCaseInfo tci, String methodName) {
		this.tci = tci;
		this.methodName = methodName;
	}

	public void runStep() throws Throwable {
		System.out.println("Adding a simple method.");

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

		MethodType method = new MethodType();
		method.setName(this.methodName);
		MethodTypeOutput output = new MethodTypeOutput();
		output.setClassName("void");
		method.setOutput(output);
		
		//create a new input param
		MethodTypeInputsInput input = new MethodTypeInputsInput();
		input.setNamespace("http://www.w3.org/2001/XMLSchema");
		input.setName("foo");
		input.setType("string");
		input.setIsArray(new Boolean(false));
		MethodTypeInputsInput[] newInputs = new MethodTypeInputsInput[1];
		newInputs[0] = input;
		MethodTypeInputs inputs = new MethodTypeInputs();
		inputs.setInput(newInputs);
		method.setInputs(inputs);

		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		MethodType[] newMethods;
		int newLength = 0;
		if (methodsType.getMethod() != null) {
			newLength = methodsType.getMethod().length + 1;
			newMethods = new MethodType[newLength];
			System.arraycopy(methodsType.getMethod(), 0, newMethods, 0,
					methodsType.getMethod().length);
		} else {
			newLength = 1;
			newMethods = new MethodType[newLength];
		}
		newMethods[newLength - 1] = method;
		methodsType.setMethod(newMethods);

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
