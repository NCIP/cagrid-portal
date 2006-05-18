package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ImportInformation;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;


public class AddImportedSimpleMethodStep extends BaseStep {
	private TestCaseInfo tci;
	private TestCaseInfo importedTCI;
	private String methodName;


	public AddImportedSimpleMethodStep(TestCaseInfo tci, TestCaseInfo importedTCI, String methodName, boolean build) throws Exception {
		super(tci.getDir(),build);
		this.tci = tci;
		this.importedTCI = importedTCI;
		this.methodName = methodName;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding an imported simple method.");

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(getBaseDir() + File.separator
			+ tci.getDir() + File.separator + "introduce.xml", ServiceDescription.class);
		MethodsType methodsType = CommonTools.getService(introService.getServices(),tci.getName()).getMethods();

		MethodType method = new MethodType();
		method.setName(this.methodName);
		MethodTypeOutput output = new MethodTypeOutput();
		output.setQName(new QName("","void"));
		method.setOutput(output);

		// create a new input param
		MethodTypeInputsInput input = new MethodTypeInputsInput();
		input.setQName(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		input.setName("foo");
		input.setIsArray(false);
		MethodTypeInputsInput[] newInputs = new MethodTypeInputsInput[1];
		newInputs[0] = input;
		MethodTypeInputs inputs = new MethodTypeInputs();
		inputs.setInput(newInputs);
		method.setInputs(inputs);
		
		method.setIsImported(true);
		MethodTypeImportInformation ii = new MethodTypeImportInformation();
		ii.setNamespace(importedTCI.getNamespace());
		ii.setOperationName(methodName);
		ii.setPortTypeName(importedTCI.getName() + "PortType");
		ii.setPackageName(importedTCI.getPackageName() + ".stubs");
		ii.setWsdlFile(importedTCI.getName() + ".wsdl");
		method.setImportInformation(ii);
		
		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		MethodType[] newMethods;
		int newLength = 0;
		if (methodsType!=null && methodsType.getMethod() != null) {
			newLength = methodsType.getMethod().length + 1;
			newMethods = new MethodType[newLength];
			System.arraycopy(methodsType.getMethod(), 0, newMethods, 0, methodsType.getMethod().length);
		} else {
			newLength = 1;
			newMethods = new MethodType[newLength];
		}
		MethodsType newmethodsType = new MethodsType();
		newMethods[newLength - 1] = method;
		newmethodsType.setMethod(newMethods);
		CommonTools.getService(introService.getServices(),tci.getName()).setMethods(newmethodsType);

		Utils.serializeDocument(getBaseDir() + File.separator + tci.getDir() + File.separator + "introduce.xml",
			introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));

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

		String cmd = CommonTools.getAntAllCommand(getBaseDir() + File.separator + tci.getDir());

		buildStep();
	}

}
