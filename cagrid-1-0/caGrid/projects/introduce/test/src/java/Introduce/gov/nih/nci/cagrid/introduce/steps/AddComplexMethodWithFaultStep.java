package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;

public class AddComplexMethodWithFaultStep extends Step {
	private TestCaseInfo tci;

	private String methodName;

	public AddComplexMethodWithFaultStep(TestCaseInfo tci, String methodName) {
		this.tci = tci;
		this.methodName = methodName;
	}

	public void runStep() throws Throwable {
		System.out.println("Adding a complex method with fault.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}

		// copy over the bookstore schema to be used with the test
		CommonTools.copyFile(new File(pathtobasedir + File.separator
				+ TestCaseInfo.GOLD_SCHEMA_DIR + File.separator
				+ "bookstore.xsd"), new File(pathtobasedir + File.separator
				+ tci.getDir() + File.separator + "schema" + File.separator
				+ tci.getName() + File.separator + "bookstore.xsd"));

		ServiceDescription introService = (ServiceDescription) CommonTools
				.deserializeDocument(pathtobasedir + File.separator
						+ tci.getDir() + File.separator + "introduce.xml",
						ServiceDescription.class);
		MethodsType methodsType = introService.getMethods();

		MethodType method = new MethodType();
		method.setName(methodName);

		// set the output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setLocation("./bookstore.xsd");
		output.setType("BookType");
		output.setIsArray(new Boolean(false));
		output.setNamespace("gme://projectmobius.org/1/BookStore");

		// set some parameters
		MethodTypeInputs inputs = new MethodTypeInputs();
		MethodTypeInputsInput[] inputsArray = new MethodTypeInputsInput[1];
		MethodTypeInputsInput input = new MethodTypeInputsInput();
		input.setName("inputOne");
		input.setType("BookType");
		input.setLocation("./bookstore.xsd");
		input.setIsArray(new Boolean(true));
		input.setNamespace("gme://projectmobius.org/1/BookStore");
		inputsArray[0] = input;
		inputs.setInput(inputsArray);
		method.setInputs(inputs);

		// set a fault
		MethodTypeExceptionsException[] exceptionsArray = new MethodTypeExceptionsException[1];
		MethodTypeExceptionsException exception = new MethodTypeExceptionsException();
		exception.setName("testFault");
		exceptionsArray[0] = exception;
		MethodTypeExceptions exceptions = new MethodTypeExceptions();
		exceptions.setException(exceptionsArray);
		method.setExceptions(exceptions);

		method.setOutput(output);

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

		CommonTools.serializeDocument(pathtobasedir + File.separator
				+ tci.getDir() + File.separator + "introduce.xml",
				introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce",
						"ServiceSkeleton"));

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir
				+ File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals(0, p.exitValue());

		// look at the interface to make sure method exists.......
		String serviceInterface = pathtobasedir + File.separator + tci.dir
				+ File.separator + "src" + File.separator + tci.getPackageDir()
				+ "/common/" + tci.getName() + "I.java";
		assertTrue(StepTools.methodExists(serviceInterface, methodName));

		cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator
				+ tci.getDir());

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals(0, p.exitValue());
	}

}
