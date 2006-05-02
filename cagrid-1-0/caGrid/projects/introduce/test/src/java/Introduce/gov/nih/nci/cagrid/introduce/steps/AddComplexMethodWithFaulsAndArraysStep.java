package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;


public class AddComplexMethodWithFaulsAndArraysStep extends Step {
	private TestCaseInfo tci;

	private String methodName;


	public AddComplexMethodWithFaulsAndArraysStep(TestCaseInfo tci, String methodName) {
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
		
		
		int currentLength = 0;
		NamespacesType namespaces = introService.getNamespaces();
		if (namespaces.getNamespace() != null) {
			currentLength = namespaces.getNamespace().length;
		}
		NamespaceType[] newNamespaceTypes = new NamespaceType[currentLength + 1];
		if (currentLength > 0) {
			System.arraycopy(namespaces.getNamespace(), 0, newNamespaceTypes, 0, currentLength);
		}
		NamespaceType type = new NamespaceType();
		type.setLocation("." + File.separator + "bookstore.xsd");
		type.setNamespace("gme://projectmobius.org/1/BookStore");
		SchemaElementType etype = new SchemaElementType();
		etype.setType("Book");
		SchemaElementType[] etypeArr = new SchemaElementType[1];
		etypeArr[0] = etype;
		type.setSchemaElement(etypeArr);
		newNamespaceTypes[currentLength] = type;
		namespaces.setNamespace(newNamespaceTypes);
		
		MethodsType methodsType =  CommonTools.getService(introService.getServices(),tci.getName()).getMethods();

		MethodType method = new MethodType();
		method.setName(methodName);

		// set the output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setQName(new QName("gme://projectmobius.org/1/BookStore","Book"));
		output.setIsArray(true);

		// set some parameters
		MethodTypeInputs inputs = new MethodTypeInputs();
		MethodTypeInputsInput[] inputsArray = new MethodTypeInputsInput[1];
		MethodTypeInputsInput input = new MethodTypeInputsInput();
		input.setName("inputOne");
		input.setIsArray(true);
		input.setQName(new QName("gme://projectmobius.org/1/BookStore","Book"));
		input.setIsArray(true);
		inputsArray[0] = input;
		inputs.setInput(inputsArray);
		method.setInputs(inputs);

		// set a fault
		MethodTypeExceptionsException[] exceptionsArray = new MethodTypeExceptionsException[2];
		MethodTypeExceptionsException exception1 = new MethodTypeExceptionsException();
		exception1.setName("testFault1");
		MethodTypeExceptionsException exception2 = new MethodTypeExceptionsException();
		exception2.setName("testFault2");
		exceptionsArray[0] = exception1;
		exceptionsArray[1] = exception2;
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
			introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
		
		try {
			SyncTools sync = new SyncTools(new File(pathtobasedir + File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// look at the interface to make sure method exists.......
		String serviceInterface = pathtobasedir + File.separator + tci.getDir() + File.separator + "src" + File.separator
			+ tci.getPackageDir()+ File.separator + introService.getServices().getService(0).getName().toLowerCase() + File.separator + "common" + File.separator + tci.getName() + "I.java";
		assertTrue(StepTools.methodExists(serviceInterface, methodName));

		String cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
	}
}
