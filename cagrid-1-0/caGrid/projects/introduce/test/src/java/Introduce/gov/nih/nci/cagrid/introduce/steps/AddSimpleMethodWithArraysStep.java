package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;


public class AddSimpleMethodWithArraysStep extends BaseStep {
	private TestCaseInfo tci;
	private String methodName;


	public AddSimpleMethodWithArraysStep(TestCaseInfo tci, String methodName, boolean build) throws Exception {
		super(tci.getDir(),build);
		this.tci = tci;
		this.methodName = methodName;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding a simple method.");

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(getBaseDir() + File.separator
			+ tci.getDir() + File.separator + "introduce.xml", ServiceDescription.class);
		MethodsType methodsType = CommonTools.getService(introService.getServices(),tci.getName()).getMethods();

		MethodType method = new MethodType();
		method.setName(this.methodName);
		MethodTypeOutput output = new MethodTypeOutput();
		output.setQName(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		output.setIsArray(true);
		method.setOutput(output);

		// create a new input param
		MethodTypeInputsInput input1 = new MethodTypeInputsInput();
		input1.setQName(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		input1.setName("foo");
		input1.setIsArray(true);
		// create a new input param
		MethodTypeInputsInput input2 = new MethodTypeInputsInput();
		input2.setQName(new QName("http://www.w3.org/2001/XMLSchema", "integer"));
		input2.setName("bar");
		input2.setIsArray(true);
		//create a new input param
		MethodTypeInputsInput input3 = new MethodTypeInputsInput();
		input3.setQName(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		input3.setName("who");
		input3.setIsArray(true);
		MethodTypeInputsInput[] newInputs = new MethodTypeInputsInput[3];
		newInputs[0] = input1;
		newInputs[1] = input2;
		newInputs[2] = input3;
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
			System.arraycopy(methodsType.getMethod(), 0, newMethods, 0, methodsType.getMethod().length);
		} else {
			newLength = 1;
			newMethods = new MethodType[newLength];
		}
		newMethods[newLength - 1] = method;
		methodsType.setMethod(newMethods);

		Utils.serializeDocument(getBaseDir() + File.separator + tci.getDir() + File.separator + "introduce.xml",
			introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);

		try {
			SyncTools sync = new SyncTools(new File(getBaseDir() + File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		// look at the interface to make sure method exists.......
		String serviceInterface = getBaseDir() + File.separator + tci.getDir() + File.separator + "src" + File.separator
			+ tci.getPackageDir() + File.separator + "common" + File.separator + tci.getName() + "I.java";
		assertTrue(StepTools.methodExists(serviceInterface, methodName));

		buildStep();
	}

}
