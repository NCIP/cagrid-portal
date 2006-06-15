package gov.nih.nci.cagrid.introduce.codegen.services.methods;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.utils.TemplateUtils;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.SchemaInformation;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.axis.client.Stub;
import org.apache.axis.utils.JavaUtils;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.Parameter;
import org.apache.ws.jaxme.js.util.JavaParser;


/**
 * SyncSource
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncSource {

	private String serviceClient;

	private String serviceInterface;

	private String serviceImpl;

	private String serviceProviderImpl;

	// private String packageName;

	private ServiceInformation serviceInfo;

	private ServiceType service;


	public SyncSource(File baseDir, ServiceInformation info, ServiceType service) {
		// this.baseDir = baseDir;
		this.service = service;
		this.serviceInfo = info;
		// this.packageName = service.getPackageName() + ".stubs";
		serviceClient = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ CommonTools.getPackageDir(service) + File.separator + "client" + File.separator + service.getName()
			+ "Client.java";
		serviceInterface = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ CommonTools.getPackageDir(service) + File.separator + "common" + File.separator + service.getName()
			+ "I.java";
		serviceImpl = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + service.getName()
			+ "Impl.java";
		serviceProviderImpl = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
			+ File.separator + service.getName() + "ProviderImpl.java";
	}


	public String createClientExceptions(MethodType method) {
		String exceptions = "";
		// process the faults for this method...
		MethodTypeExceptions exceptionsEl = method.getExceptions();
		String packageName = service.getPackageName() + ".stubs";
		if (method.isIsImported()) {
			packageName = method.getImportInformation().getPackageName();
		}
		exceptions += "RemoteException";
		if (method.getOutput().getIsClientHandle() != null && method.getOutput().getIsClientHandle().booleanValue()) {
			exceptions += ",org.apache.axis.types.URI.MalformedURIException";
		}
		if (exceptionsEl != null && exceptionsEl.getException() != null) {
			if (exceptionsEl.getException().length > 0) {
				exceptions += ", ";
			}
			for (int i = 0; i < exceptionsEl.getException().length; i++) {
				MethodTypeExceptionsException fault = exceptionsEl.getException(i);
				// hack for now, should look at the namespace in the
				// element.....
				exceptions += packageName + "." + TemplateUtils.upperCaseFirstCharacter(fault.getName());
				if (i < exceptionsEl.getException().length - 1) {
					exceptions += ", ";
				}
			}
		}
		if (exceptions.length() > 0) {
			exceptions = "throws " + exceptions + " ";
		}
		return exceptions;
	}


	public String createExceptions(MethodType method) {
		String exceptions = "";
		// process the faults for this method...
		MethodTypeExceptions exceptionsEl = method.getExceptions();
		String packageName = service.getPackageName() + ".stubs";
		if (method.isIsImported()) {
			packageName = method.getImportInformation().getPackageName();
		}
		exceptions += "RemoteException";
		if (exceptionsEl != null && exceptionsEl.getException() != null) {
			if (exceptionsEl.getException().length > 0) {
				exceptions += ", ";
			}
			for (int i = 0; i < exceptionsEl.getException().length; i++) {
				MethodTypeExceptionsException fault = exceptionsEl.getException(i);
				// hack for now, should look at the namespace in the
				// element.....
				exceptions += packageName + "." + TemplateUtils.upperCaseFirstCharacter(fault.getName());
				if (i < exceptionsEl.getException().length - 1) {
					exceptions += ", ";
				}
			}
		}
		if (exceptions.length() > 0) {
			exceptions = "throws " + exceptions + " ";
		}
		return exceptions;
	}


	public String createClientUnBoxedSignatureStringFromMethod(MethodType method) {
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String methodName = method.getName();
		String returnType = null;
		if (returnTypeEl.getQName().getNamespaceURI().equals("")
			&& returnTypeEl.getQName().getLocalPart().equals("void")) {
			returnType = "void";
		} else {
			SchemaInformation info = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(), returnTypeEl
				.getQName());
			returnType = info.getType().getClassName();
			if (info.getType().getPackageName() != null && info.getType().getPackageName().length() > 0) {
				if (returnTypeEl.getIsClientHandle() != null && returnTypeEl.getIsClientHandle().booleanValue()) {
					returnType = returnTypeEl.getClientHandleClass();
				} else {
					returnType = info.getType().getPackageName() + "." + returnType;
				}
			}
			if (returnTypeEl.isIsArray()) {
				returnType += "[]";
			}
		}
		methodString += "     public " + returnType + " " + methodName + "(";
		if (method.getInputs() != null && method.getInputs().getInput() != null) {
			for (int j = 0; j < method.getInputs().getInput().length; j++) {
				SchemaInformation info = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(), method
					.getInputs().getInput(j).getQName());
				String packageName = info.getType().getPackageName();
				String classType = null;
				if (packageName != null && packageName.length() > 0) {
					classType = packageName + "." + info.getType().getClassName();
				} else {
					classType = info.getType().getClassName();
				}
				if (method.getInputs().getInput(j).isIsArray()) {
					classType += "[]";
				}
				String paramName = method.getInputs().getInput(j).getName();
				methodString += classType + " " + paramName;
				if (j < method.getInputs().getInput().length - 1) {
					methodString += ",";
				}
			}
		}
		methodString += ")";

		return methodString;
	}


	public String createUnBoxedSignatureStringFromMethod(MethodType method) {
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String methodName = method.getName();
		String returnType = null;
		if (returnTypeEl.getQName().getNamespaceURI().equals("")
			&& returnTypeEl.getQName().getLocalPart().equals("void")) {
			returnType = "void";
		} else {
			SchemaInformation info = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(), returnTypeEl
				.getQName());
			returnType = info.getType().getClassName();
			if (info.getType().getPackageName() != null && info.getType().getPackageName().length() > 0) {
				returnType = info.getType().getPackageName() + "." + returnType;
			}
			if (returnTypeEl.isIsArray()) {
				returnType += "[]";
			}
		}
		methodString += "     public " + returnType + " " + methodName + "(";
		if (method.getInputs() != null && method.getInputs().getInput() != null) {
			for (int j = 0; j < method.getInputs().getInput().length; j++) {
				SchemaInformation info = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(), method
					.getInputs().getInput(j).getQName());
				String packageName = info.getType().getPackageName();
				String classType = null;
				if (packageName != null && packageName.length() > 0) {
					classType = packageName + "." + info.getType().getClassName();
				} else {
					classType = info.getType().getClassName();
				}
				if (method.getInputs().getInput(j).isIsArray()) {
					classType += "[]";
				}
				String paramName = method.getInputs().getInput(j).getName();
				methodString += classType + " " + paramName;
				if (j < method.getInputs().getInput().length - 1) {
					methodString += ",";
				}
			}
		}
		methodString += ")";

		return methodString;
	}


	public List buildServicesClientHandleClassNameList() {
		List list = new ArrayList();
		if (serviceInfo.getServices() != null && serviceInfo.getServices().getService() != null) {
			for (int i = 0; i < serviceInfo.getServices().getService().length; i++) {
				ServiceType service = serviceInfo.getServices().getService(i);
				list.add(service.getPackageName() + ".client." + service.getName() + "Client");
			}
		}
		return list;
	}


	public String createUnBoxedSignatureStringFromMethod(JavaMethod method) throws Exception {
		String methodString = "";
		String methodName = method.getName();
		String returnType = "";
		if (buildServicesClientHandleClassNameList().contains(
			method.getType().getPackageName() + "." + method.getType().getClassName())) {
			returnType += IntroduceConstants.WSADDRESSING_EPR_CLASSNAME;
		} else {
			if (method.getType().getPackageName().length() > 0) {
				returnType += method.getType().getPackageName() + ".";
			}
			returnType += method.getType().getClassName();
		}
		if (method.getType().isArray()) {
			returnType += "[]";
		}
		methodString += "     public " + returnType + " " + methodName + "(";
		Parameter[] inputs = method.getParams();
		for (int j = 0; j < inputs.length; j++) {
			String classType = null;
			if (inputs[j].getType().getPackageName().length() > 0) {
				classType = inputs[j].getType().getPackageName() + "." + inputs[j].getType().getClassName();
			} else {
				classType = inputs[j].getType().getClassName();
			}
			if (inputs[j].getType().isArray()) {
				classType += "[]";
			}
			String paramName = inputs[j].getName();
			methodString += classType + " " + paramName;
			if (j < inputs.length - 1) {
				methodString += ",";
			}
		}
		methodString += ")";
		return methodString;
	}


	public String createClientUnBoxedSignatureStringFromMethod(JavaMethod method) throws Exception {
		String methodString = "";
		String methodName = method.getName();
		String returnType = "";
		if (method.getType().getPackageName().length() > 0) {
			returnType += method.getType().getPackageName() + ".";
		}
		returnType += method.getType().getClassName();
		if (method.getType().isArray()) {
			returnType += "[]";
		}
		methodString += "     public " + returnType + " " + methodName + "(";
		Parameter[] inputs = method.getParams();
		for (int j = 0; j < inputs.length; j++) {
			String classType = null;
			if (inputs[j].getType().getPackageName().length() > 0) {
				classType = inputs[j].getType().getPackageName() + "." + inputs[j].getType().getClassName();
			} else {
				classType = inputs[j].getType().getClassName();
			}
			if (inputs[j].getType().isArray()) {
				classType += "[]";
			}
			String paramName = inputs[j].getName();
			methodString += classType + " " + paramName;
			if (j < inputs.length - 1) {
				methodString += ",";
			}
		}
		methodString += ")";
		return methodString;
	}


	public String getBoxedOutputTypeName(String input) {
		String returnType = TemplateUtils.upperCaseFirstCharacter(input) + "Response";
		return returnType;
	}


	public String createBoxedSignatureStringFromMethod(MethodType method) throws Exception {
		String packageName = service.getPackageName() + ".stubs";
		if (method.isIsImported()) {
			packageName = method.getImportInformation().getPackageName();
		}

		String methodString = "";

		String methodName = method.getName();
		String returnType = null;
		returnType = packageName + "." + getBoxedOutputTypeName(methodName);

		methodString += "public " + returnType + " " + methodName + "(";

		// boxed
		methodString += packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + "Request params";

		methodString += ")";
		return methodString;
	}


	public String createBoxedSignatureStringFromMethod(JavaMethod method) throws Exception {
		String methodString = "";
		String methodName = method.getName();
		String returnType = "";

		JavaSource sourceI;
		JavaSourceFactory jsf;
		JavaParser jp;

		jsf = new JavaSourceFactory();
		jp = new JavaParser(jsf);

		jp.parse(new File(serviceProviderImpl));
		sourceI = (JavaSource) jsf.getJavaSources().next();
		sourceI.setForcingFullyQualifiedName(true);

		System.out.println(sourceI.getClassName());

		JavaMethod[] methods = sourceI.getMethods();

		String packageName = "";
		for (int i = 0; i < methods.length; i++) {
			;
			if (methods[i].getName().equals(methodName)) {
				packageName = methods[i].getType().getPackageName();
				break;
			}
		}

		// need to box the output type
		returnType = packageName + "." + getBoxedOutputTypeName(methodName);

		methodString += "public " + returnType + " " + methodName + "(";
		// Parameter[] inputs = method.getParams();
		// always boxed for now
		// if (inputs.length > 1 || inputs.length == 0) {

		// boxed
		methodString += packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + "Request params";

		methodString += ")";

		jsf = null;
		jp = null;
		sourceI = null;
		System.gc();

		return methodString;
	}


	public void addMethods(List additions) throws Exception {
		for (int i = 0; i < additions.size(); i++) {
			// add it to the interface
			MethodType method = (MethodType) additions.get(i);

			StringBuffer fileContent = null;
			try {
				fileContent = Utils.fileToStringBuffer(new File(this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// insert the new client method
			int endOfClass = fileContent.lastIndexOf("}");
			String clientMethod = "\n\t" + createClientUnBoxedSignatureStringFromMethod(method) + " "
				+ createClientExceptions(method);
			clientMethod += ";\n";

			fileContent.insert(endOfClass - 1, clientMethod);
			try {
				FileWriter fw = new FileWriter(new File(this.serviceInterface));
				fw.write(fileContent.toString());
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			if (!method.isIsProvided()) {
				// populate the impl method
				addImpl(method);
				// populate the provider impl method
				addProviderImpl(method);
				// populate the client method
			}

			addClientImpl(method);
		}
	}


	public void modifyMethods(List modifiedMethods) throws Exception {
		for (int i = 0; i < modifiedMethods.size(); i++) {
			// add it to the interface
			Modification mod = (Modification) modifiedMethods.get(i);
			MethodType method = mod.getMethodType();

			StringBuffer fileContent = null;
			try {
				fileContent = Utils.fileToStringBuffer(new File(this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// remove the old interface method
			String clientMethod = createClientUnBoxedSignatureStringFromMethod(mod.getJavaMethod());
			int startOfMethod = startOfSignature(fileContent, clientMethod);
			String restOfFile = fileContent.substring(startOfMethod);
			int endOfMethod = startOfMethod + restOfFile.indexOf(";") + 1;

			if (startOfMethod == -1 || endOfMethod == -1) {
				System.err.println("WARNING: Unable to locate method in I : " + method.getName());
				return;
			}

			fileContent.delete(startOfMethod, endOfMethod);

			// insert the new client method
			int endOfClass = fileContent.lastIndexOf("}");
			clientMethod = createUnBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);
			clientMethod += ";\n";

			fileContent.insert(endOfClass - 1, clientMethod);
			try {
				FileWriter fw = new FileWriter(new File(this.serviceInterface));
				fw.write(fileContent.toString());
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// if the method was not provided
			if (!method.isIsProvided()) {
				// just clean up the modified impl
				modifyImpl(mod);
				// redo the provider impl method
				removeProviderImpl(mod.getJavaMethod());
				addProviderImpl(method);
			}
			// redo the client method

			removeClientImpl(mod.getJavaMethod());
			addClientImpl(method);
		}
	}


	public void addClientImpl(MethodType method) {
		String packageName = service.getPackageName() + ".stubs";
		if (method.isIsImported()) {
			packageName = method.getImportInformation().getPackageName();
		}

		StringBuffer fileContent = null;
		String methodName = method.getName();
		try {
			fileContent = Utils.fileToStringBuffer(new File(this.serviceClient));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String lineStart = "               ";

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		String clientMethod = "\n\t" + createClientUnBoxedSignatureStringFromMethod(method) + " "
			+ createClientExceptions(method);
		clientMethod += "{\n";
		if (!method.getName().equals("getServiceSecurityMetadata")) {
			clientMethod += lineStart
				+ "ServiceSecurityClient.configurePortType(this.securityMetadata,(org.apache.axis.client.Stub)this.portType,\""
				+ method.getName() + "\");\n";
		}

		// put in the call to the client
		String var = "portType";

		String methodString = lineStart;
		MethodTypeOutput returnTypeEl = method.getOutput();

		// always a boxed call now becuase using complex types in the wsdl
		// create handle for the boxed wrapper
		methodString += packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + "Request params = new "
			+ packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + "Request();\n";
		// set the values fo the boxed wrapper
		if (method.getInputs() != null && method.getInputs().getInput() != null) {
			for (int j = 0; j < method.getInputs().getInput().length; j++) {
				SchemaInformation inNamespace = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(), method
					.getInputs().getInput(j).getQName());
				String paramName = method.getInputs().getInput(j).getName();
				String containerClassName = method.getInputs().getInput(j).getContainerClassName();
				String containerMethodCall = TemplateUtils.upperCaseFirstCharacter(JavaUtils.xmlNameToJava(inNamespace
					.getType().getType()));
				methodString += lineStart;
				if (inNamespace.getNamespace().getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
					methodString += "params.set" + TemplateUtils.upperCaseFirstCharacter(paramName) + "(" + paramName
						+ ");\n";
				} else {
					methodString += containerClassName + " " + paramName + "Container = new " + containerClassName
						+ "();\n";
					methodString += lineStart;
					methodString += paramName + "Container.set" + containerMethodCall + "(" + paramName + ");\n";
					methodString += lineStart;
					methodString += "params.set" + TemplateUtils.upperCaseFirstCharacter(paramName) + "(" + paramName
						+ "Container);\n";
				}
			}
		}
		// make the call
		methodString += lineStart;

		// always boxed returns now because of complex types in wsdl
		String returnTypeBoxed = getBoxedOutputTypeName(methodName);
		methodString += packageName + "." + returnTypeBoxed + " boxedResult = " + var + "." + methodName
			+ "(params);\n";
		methodString += lineStart;
		if (!returnTypeEl.getQName().getNamespaceURI().equals("")
			&& !returnTypeEl.getQName().getLocalPart().equals("void")) {
			SchemaInformation info = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(), returnTypeEl
				.getQName());
			if (info.getNamespace().getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
				if (info.getType().getType().equals("boolean") && !returnTypeEl.isIsArray()) {
					methodString += "return boxedResult.isResponse();\n";
				} else {
					methodString += "return boxedResult.getResponse();\n";
				}
			} else {
				if (returnTypeEl.getIsClientHandle() != null && returnTypeEl.getIsClientHandle().booleanValue()) {
					// create the client handle and put the EPR in it
					// then return the client handle...
					if (returnTypeEl.isIsArray()) {
						methodString += returnTypeEl.getClientHandleClass() + "[] clientArray = null;\n";
						methodString += lineStart + "if(boxedResult.getEndpointReference()!=null){\n";
						methodString += lineStart + "  clientArray = new " + returnTypeEl.getClientHandleClass()
							+ "[boxedResult.getEndpointReference().length];\n";
						methodString += lineStart
							+ "  for(int i = 0; i < boxedResult.getEndpointReference().length; i++){\n";
						methodString += lineStart + "	   clientArray[i] = new " + returnTypeEl.getClientHandleClass()
							+ "(boxedResult.getEndpointReference(i));\n";
						methodString += lineStart + "  }\n";
						methodString += lineStart + "}\n";
						methodString += lineStart + "return clientArray;\n";
					} else {
						methodString += "EndpointReferenceType ref = boxedResult.get";
						methodString += TemplateUtils.upperCaseFirstCharacter(info.getType().getType()) + "();\n";
						methodString += lineStart + "return new " + returnTypeEl.getClientHandleClass() + "(ref);\n";
					}
				} else {
					methodString += "return boxedResult.get"
						+ TemplateUtils.upperCaseFirstCharacter(info.getType().getType()) + "();\n";
				}
			}
		}

		clientMethod += methodString;

		clientMethod += "\n\t}\n\n";

		fileContent.insert(endOfClass - 1, clientMethod);
		try {
			FileWriter fw = new FileWriter(new File(this.serviceClient));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}


	public void addImpl(MethodType method) {
		StringBuffer fileContent = null;
		try {
			fileContent = Utils.fileToStringBuffer(new File(this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		String clientMethod = "\n\t" + createUnBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);

		clientMethod += "{\n";
		clientMethod += "\t\t//TODO: Implement this autogenerated method\n";
		clientMethod += "\t\tthrow new RemoteException(\"Not yet implemented\");\n";
		clientMethod += "\t}\n";

		fileContent.insert(endOfClass - 1, clientMethod);
		try {
			FileWriter fw = new FileWriter(new File(this.serviceImpl));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public void addProviderImpl(MethodType method) throws Exception {
		String packageName = service.getPackageName() + ".stubs";
		if (method.isIsImported()) {
			packageName = method.getImportInformation().getPackageName();
		}

		StringBuffer fileContent = null;
		try {
			fileContent = Utils.fileToStringBuffer(new File(this.serviceProviderImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		// slh -- in migration to globus 4 we need to check here for autoboxing
		// and get appropriate
		String clientMethod = "\n\t" + createBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);

		// clientMethod += " throws RemoteException";
		clientMethod += "{\n";

		// create the unboxed call to the implementation
		String var = "impl";
		String lineStart = "\t\t";

		String methodName = method.getName();
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();

		// unbox the params
		String params = "";

		if (method.getInputs() != null && method.getInputs().getInput() != null) {
			// always unbox now
			if (method.getInputs().getInput().length >= 1) {
				// inputs were boxed and need to be unboxed
				for (int j = 0; j < method.getInputs().getInput().length; j++) {
					SchemaInformation inNamespace = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(),
						method.getInputs().getInput(j).getQName());
					String paramName = method.getInputs().getInput(j).getName();
					if (inNamespace.getNamespace().getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
						if (inNamespace.getType().getType().equals("boolean")
							&& !method.getInputs().getInput(j).isIsArray()) {
							params += "params.is" + TemplateUtils.upperCaseFirstCharacter(paramName) + "()";
						} else {
							params += "params.get" + TemplateUtils.upperCaseFirstCharacter(paramName) + "()";
						}
					} else {
						params += "params.get"
							+ TemplateUtils.upperCaseFirstCharacter(paramName)
							+ "().get"
							+ TemplateUtils.upperCaseFirstCharacter(JavaUtils.xmlNameToJava(inNamespace.getType()
								.getType())) + "()";
					}
					if (j < method.getInputs().getInput().length - 1) {
						params += ",";
					}
				}
			} else {
				// inputs are not boxed and can just be passed through
				for (int j = 0; j < method.getInputs().getInput().length; j++) {
					String paramName = method.getInputs().getInput(j).getName();
					params += paramName;
					if (j < method.getInputs().getInput().length - 1) {
						params += ",";
					}
				}
			}
		}

		// return the boxed type
		String returnTypeBoxed = getBoxedOutputTypeName(methodName);

		// need to unbox on the way out
		methodString += lineStart;
		methodString += packageName + "." + returnTypeBoxed + " boxedResult = new " + packageName + "."
			+ returnTypeBoxed + "();\n";
		methodString += lineStart;
		if (returnTypeEl.getQName().getNamespaceURI().equals("")
			&& returnTypeEl.getQName().getLocalPart().equals("void")) {
			// just call but dont set anything
			methodString += var + "." + methodName + "(" + params + ");\n";
		} else {
			SchemaInformation outputNamespace = CommonTools.getSchemaInformation(serviceInfo.getNamespaces(),
				returnTypeEl.getQName());
			if (outputNamespace.getNamespace().getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
				methodString += "boxedResult.setResponse(" + var + "." + methodName + "(" + params + "));\n";
			} else {
				methodString += "boxedResult.set"
					+ TemplateUtils.upperCaseFirstCharacter(outputNamespace.getType().getType()) + "(" + var + "."
					+ methodName + "(" + params + "));\n";
			}
		}
		methodString += lineStart;
		methodString += "return boxedResult;\n";

		clientMethod += methodString;
		clientMethod += "\t}\n";
		fileContent.insert(endOfClass - 1, clientMethod);

		try {
			FileWriter fw = new FileWriter(new File(this.serviceProviderImpl));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}


	public void removeMethods(List removals) throws Exception {
		for (int i = 0; i < removals.size(); i++) {
			JavaMethod method = (JavaMethod) removals.get(i);

			StringBuffer fileContent = null;
			try {
				fileContent = Utils.fileToStringBuffer(new File(this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// remove the method
			String clientMethod = createClientUnBoxedSignatureStringFromMethod(method);
			System.err.println("Looking to remove method: |" + clientMethod + "|");
			int startOfMethod = startOfSignature(fileContent, clientMethod);
			String restOfFile = fileContent.substring(startOfMethod);
			int endOfMethod = startOfMethod + restOfFile.indexOf(";\n") + 2;

			if (startOfMethod == -1 || endOfMethod == -1) {
				System.err.println("WARNING: Unable to locate method in I : " + method.getName());
				return;
			}

			fileContent.delete(startOfMethod, endOfMethod);

			try {
				FileWriter fw = new FileWriter(new File(this.serviceInterface));
				fw.write(fileContent.toString());
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// fail silent here in caase the method was not implemented
			try {
				// remove the impl method
				removeImpl(method);
				// remove the provider impl method
				removeProviderImpl(method);
			} catch (Exception e) {
				System.out.println("WARNING: " + e.getMessage()
					+ "\n might be due to method implementation provided by another service");
			}
			// remove the client method
			removeClientImpl(method);
		}
	}


	public void removeClientImpl(JavaMethod method) throws Exception {
		StringBuffer fileContent = null;
		try {
			fileContent = Utils.fileToStringBuffer(new File(this.serviceClient));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createClientUnBoxedSignatureStringFromMethod(method);
		int startOfMethod = startOfSignature(fileContent, clientMethod);
		int endOfMethod = bracketMatch(fileContent, startOfMethod);

		if (startOfMethod == -1 || endOfMethod == -1) {
			System.err.println("WARNING: Unable to locate method in clientImpl : " + method.getName());
			return;
		}

		fileContent.delete(startOfMethod, endOfMethod);

		try {
			FileWriter fw = new FileWriter(new File(this.serviceClient));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public void removeProviderImpl(JavaMethod method) throws Exception {
		StringBuffer fileContent = null;
		try {
			fileContent = Utils.fileToStringBuffer(new File(this.serviceProviderImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createBoxedSignatureStringFromMethod(method);
		int startOfMethod = startOfSignature(fileContent, clientMethod);
		int endOfMethod = bracketMatch(fileContent, startOfMethod);

		if (startOfMethod == -1 || endOfMethod == -1) {
			System.err.println("WARNING: Unable to locate method in providerImpl : " + method.getName());
			return;
		}

		fileContent.delete(startOfMethod, endOfMethod);

		try {
			FileWriter fw = new FileWriter(new File(this.serviceProviderImpl));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}


	public void modifyImpl(Modification mod) throws Exception {
		MethodType method = mod.getMethodType();
		JavaMethod oldMethod = mod.getJavaMethod();

		StringBuffer fileContent = null;
		try {
			fileContent = Utils.fileToStringBuffer(new File(this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the old method signature
		String clientMethod = createUnBoxedSignatureStringFromMethod(oldMethod);
		int startOfMethod = startOfSignature(fileContent, clientMethod);
		int endOfSignature = endOfSignature(fileContent, startOfMethod);

		if (startOfMethod == -1 || endOfSignature == -1) {
			System.err.println("WARNING: Unable to locate method in Impl : " + oldMethod.getName());
			return;
		}

		fileContent.delete(startOfMethod, endOfSignature);

		// add in the new modified signature
		clientMethod = "\t" + createUnBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);
		clientMethod += "{";
		fileContent.insert(startOfMethod, clientMethod);

		try {
			FileWriter fw = new FileWriter(new File(this.serviceImpl));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}


	public void removeImpl(JavaMethod method) throws Exception {
		StringBuffer fileContent = null;
		try {
			fileContent = Utils.fileToStringBuffer(new File(this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createUnBoxedSignatureStringFromMethod(method);
		int startOfMethod = startOfSignature(fileContent, clientMethod);
		int endOfMethod = bracketMatch(fileContent, startOfMethod);

		if (startOfMethod == -1 || endOfMethod == -1) {
			System.err.println("WARNING: Unable to locate method in Impl : " + method.getName());
			return;
		}

		fileContent.delete(startOfMethod, endOfMethod);

		try {
			FileWriter fw = new FileWriter(new File(this.serviceImpl));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}


	public int bracketMatch(StringBuffer sb, int startingIndex) {
		// System.out.println("Starting to look for brackets on this string:");
		// System.out.println(sb.toString().substring(startingIndex));
		int parenCount = 0;
		int index = startingIndex;
		boolean found = false;
		boolean canFind = false;
		while (!found) {
			char ch = sb.charAt(index);
			if (ch == '{') {
				canFind = true;
				parenCount++;
			} else if (ch == '}') {
				parenCount--;
				if (canFind == true) {
					if (parenCount == 0) {
						found = true;
					}
				}
			}
			index++;
		}
		if (found) {
			char ch = sb.charAt(index);
			while (ch == '\t' || ch == ' ') {
				ch = sb.charAt(++index);
			}
		}
		return index;
	}


	public int endOfSignature(StringBuffer sb, int startingIndex) {
		int index = startingIndex;
		if (index < 0) {
			return index;
		}
		boolean found = false;
		while (!found) {
			char ch = sb.charAt(index);
			if (ch == '{') {
				found = true;
			}
			index++;
		}
		return index;
	}


	public int startOfSignature(StringBuffer sb, String searchString) {
		BufferedReader br = new BufferedReader(new StringReader(sb.toString()));
		// tokenizer to compress all parts, then start matching the parts
		int charsRead = 0;
		try {
			String line1 = null;
			String line2 = null;
			String line3 = null;

			line1 = br.readLine();
			if (line1 != null) {
				line1 += "\n";
				line2 = br.readLine();
				if (line2 != null) {
					line2 += "\n";
					line3 = br.readLine();
					if (line3 != null) {
						line3 += "\n";
					}
				}
			}

			String matchedLine = null;
			boolean found = false;

			while (line1 != null && !found) {
				matchedLine = line1;
				// if the line is empty just skip it...
				if (!line1.equals("\n")) {
					if (line2 != null) {
						matchedLine += line2;
						if (line3 != null) {
							matchedLine += line3;
						}
					}

					StringTokenizer searchStringTokenizer = new StringTokenizer(searchString, " \t\n\r\f(),");
					StringTokenizer lineTokenizer = new StringTokenizer(matchedLine, " \t\n\r\f(),");
					int matchCount = 0;
					// this could be advanced to support multiple lines......
					while (searchStringTokenizer.hasMoreTokens() && lineTokenizer.hasMoreTokens()) {
						String searchToken = searchStringTokenizer.nextToken();
						String lineToken = lineTokenizer.nextToken();
						if (searchToken.equals(lineToken)) {
							matchCount++;
							if (matchCount == 3) {
								found = true;
								break;
							}
						} else {
							break;
						}
					}
				}
				if (!found) {
					charsRead += line1.length();
					line1 = line2;
					line2 = line3;
					line3 = br.readLine();
					if (line3 != null) {
						line3 += "\n";
					}
				}
			}
			if (found) {
				// System.out.println("Found start of method: " + matchedLine);
			} else {
				System.out.println("Did not find the appropriate match");
			}
			// if the last line i found the match then lets look for the start
			// of the method
			if (found) {
				StringTokenizer searchStringTokenizer = new StringTokenizer(searchString);
				String startToken = searchStringTokenizer.nextToken();
				int index = charsRead + matchedLine.indexOf(startToken);

				char prevChar = sb.toString().charAt(--index);
				while (prevChar != '\n' && (prevChar == ' ' || prevChar == '\t')) {
					prevChar = sb.toString().charAt(--index);
				}
				index++;
				return index;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}


	public ServiceType getService() {
		return service;
	}


	public void setService(ServiceType service) {
		this.service = service;
	}


	public String getServiceClient() {
		return serviceClient;
	}


	public void setServiceClient(String serviceClient) {
		this.serviceClient = serviceClient;
	}


	public String getServiceImpl() {
		return serviceImpl;
	}


	public void setServiceImpl(String serviceImpl) {
		this.serviceImpl = serviceImpl;
	}


	public ServiceInformation getServiceInfo() {
		return serviceInfo;
	}


	public void setServiceInfo(ServiceInformation serviceInfo) {
		this.serviceInfo = serviceInfo;
	}


	public String getServiceInterface() {
		return serviceInterface;
	}


	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}


	public String getServiceProviderImpl() {
		return serviceProviderImpl;
	}


	public void setServiceProviderImpl(String serviceProviderImpl) {
		this.serviceProviderImpl = serviceProviderImpl;
	}
}
