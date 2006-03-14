package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

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
			System.err.println("basedir system property not set");
			throw new Exception("basedir system property not set");
		}

		// copy over the bookstore schema to be used with the test
		Utils.copyFile(new File(pathtobasedir + File.separator + TestCaseInfo.GOLD_SCHEMA_DIR + File.separator
			+ "bookstore.xsd"), new File(pathtobasedir + File.separator + tci.getDir() + File.separator + "schema"
			+ File.separator + tci.getName() + File.separator + "bookstore.xsd"));

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(pathtobasedir + File.separator
			+ tci.getDir() + File.separator + "introduce.xml", ServiceDescription.class);
		MethodsType methodsType = introService.getMethods();

		MethodType method = new MethodType();
		method.setName(methodName);

		// set the output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setLocation("./bookstore.xsd");
		output.setType("Book");
		output.setPackageName("bookstore");
		output.setIsArray(new Boolean(false));
		output.setNamespace("gme://projectmobius.org/1/BookStore");

		// set some parameters
		MethodTypeInputs inputs = new MethodTypeInputs();
		MethodTypeInputsInput[] inputsArray = new MethodTypeInputsInput[1];
		MethodTypeInputsInput input = new MethodTypeInputsInput();
		input.setName("inputOne");
		input.setType("Book");
		input.setLocation("./bookstore.xsd");
		input.setPackageName("bookstore");
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
			System.arraycopy(methodsType.getMethod(), 0, newMethods, 0, methodsType.getMethod().length);
		} else {
			newLength = 1;
			newMethods = new MethodType[newLength];
		}
		newMethods[newLength - 1] = method;
		methodsType.setMethod(newMethods);

		Utils.serializeDocument(pathtobasedir + File.separator + tci.getDir() + File.separator + "introduce.xml",
			introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));

		try {
			SyncTools sync = new SyncTools(new File(pathtobasedir + File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// look at the interface to make sure method exists.......
		String serviceInterface = pathtobasedir + File.separator + tci.dir + File.separator + "src" + File.separator
			+ tci.getPackageDir() + "/common/" + tci.getName() + "I.java";
		assertTrue(StepTools.methodExists(serviceInterface, methodName));

		String cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
	}

}
