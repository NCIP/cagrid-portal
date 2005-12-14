package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsTypeMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axis.wsdl.symbolTable.SymbolTable;
import org.apache.axis.wsdl.symbolTable.Type;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.Parameter;

/**
 * SyncMethodsOnDeployment TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncMethods {

	private String serviceClient;

	private String serviceInterface;

	private String serviceImpl;

	private String serviceProviderImpl;

	private Properties deploymentProperties;

	private String packageName;

	private SymbolTable table;

	private File baseDir;

	public SyncMethods(SymbolTable table, File baseDir,
			Properties deploymentProperties) {
		this.table = table;
		this.baseDir = baseDir;
		this.deploymentProperties = deploymentProperties;
		this.packageName = (String) this.deploymentProperties
				.get("introduce.skeleton.package")
				+ ".stubs";
		serviceClient = baseDir.getAbsolutePath()
				+ File.separator
				+ "src"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.package.dir")
				+ File.separator
				+ "client"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.service.name") + "Client.java";
		serviceInterface = baseDir.getAbsolutePath()
				+ File.separator
				+ "src"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.package.dir")
				+ File.separator
				+ "common"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.service.name") + "I.java";
		serviceImpl = baseDir.getAbsolutePath()
				+ File.separator
				+ "src"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.package.dir")
				+ File.separator
				+ "service"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.service.name") + "Impl.java";
		serviceProviderImpl = baseDir.getAbsolutePath()
				+ File.separator
				+ "src"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.package.dir")
				+ File.separator
				+ "service"
				+ File.separator
				+ "globus"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.service.name")
				+ "ProviderImpl.java";
	}

	private String getClassNameFromElement(MethodTypeInputsInput method) {
		if (method.getClassName().equals("void")) {
			return "void";
		}
		table.dump(System.out);
		Type type = table.getType(new QName(method.getNamespace(), method
				.getType()));
		return type.getName();
	}

	private String getClassNameFromElement(MethodTypeOutput method) {
		if (method.getClassName().equals("void")) {
			return "void";
		}
		table.dump(System.out);
		Type type = table.getType(new QName(method.getNamespace(), method
				.getType()));
		return type.getName();
	}

	private String createExceptions(MethodsTypeMethod method) {
		String exceptions = "";
		// process the faults for this method...
		MethodTypeExceptions exceptionsEl = method.getExceptions();
		exceptions += "RemoteException";
		if (exceptionsEl != null) {
			if (exceptionsEl.getException().length > 0) {
				exceptions += ", ";
			}
			for (int i = 0; i < exceptionsEl.getException().length; i++) {
				MethodTypeExceptionsException fault = exceptionsEl
						.getException(i);
				// hack for now, should look at the namespace in the
				// element.....
				exceptions += this.packageName + "."
						+ capatilzeFirstLetter(fault.getName());
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

	private String createUnBoxedSignatureStringFromMethod(MethodsTypeMethod method) {
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String methodName = method.getName();
		String returnType = getClassNameFromElement(returnTypeEl);
		methodString += "\tpublic " + returnType + " " + methodName + "(";
		if (method.getInputs() != null) {
			for (int j = 0; j < method.getInputs().getInput().length; j++) {
				String classType = getClassNameFromElement(method.getInputs()
						.getInput(j));
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

	private String createUnBoxedSignatureStringFromMethod(JavaMethod method) {
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
		methodString += "\tpublic " + returnType + " " + methodName + "(";
		Parameter[] inputs = method.getParams();
		for (int j = 0; j < inputs.length; j++) {
			String classType = inputs[j].getType().getPackageName() + "."
					+ inputs[j].getType().getClassName();
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

	private String getBoxedOutputTypeName(String input) {
		String returnType = capatilzeFirstLetter(input) + "Response";
		return returnType;
	}

	private String capatilzeFirstLetter(String input) {
		String returnType = input.toUpperCase().toCharArray()[0]
				+ input.substring(1, input.length());
		return returnType;
	}

	private String createBoxedSignatureStringFromMethod(MethodsTypeMethod method) {
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String methodName = method.getName();
		String returnType = getClassNameFromElement(returnTypeEl);

		returnType = this.packageName + "."
				+ getBoxedOutputTypeName(methodName);

		methodString += "\tpublic " + returnType + " " + methodName + "(";

		// boxed
		methodString += this.packageName + "."
				+ capatilzeFirstLetter(methodName) + " params";

		methodString += ")";
		return methodString;
	}

	private String createBoxedSignatureStringFromMethod(JavaMethod method) {
		String methodString = "";
		String methodName = method.getName();
		String returnType = "";

		// need to box the output type
		returnType = this.packageName + "."
				+ getBoxedOutputTypeName(method.getName());

		methodString += "\tpublic " + returnType + " " + methodName + "(";
		Parameter[] inputs = method.getParams();
		// always boxed for now
		// if (inputs.length > 1 || inputs.length == 0) {

		// boxed
		methodString += this.packageName + "."
				+ capatilzeFirstLetter(methodName) + " params";

		methodString += ")";
		return methodString;
	}

	private boolean isPrimitive(String type) {
		if (type.equals("int") || type.equals("double") || type.equals("float")
				|| type.equals("boolean") || type.equals("short")
				|| type.equals("byte") || type.equals("char")
				|| type.equals("long")) {
			return true;
		}
		return false;
	}

	private String createPrimitiveReturn(String type) {
		if (type.equals("int") || type.equals("double") || type.equals("float")
				|| type.equals("short") || type.equals("byte")
				|| type.equals("char") || type.equals("long")) {
			return "0";
		} else if (type.equals("boolean")) {
			return "false";
		} else {
			return "RETURN VALUE";
		}
	}

	public void addMethods(List additions) {
		for (int i = 0; i < additions.size(); i++) {
			// add it to the interface
			MethodsTypeMethod method = (MethodsTypeMethod) additions.get(i);

			StringBuffer fileContent = null;
			try {
				fileContent = CommonTools.fileToStringBuffer(new File(
						this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// insert the new client method
			int endOfClass = fileContent.lastIndexOf("}");
			String clientMethod = createUnBoxedSignatureStringFromMethod(method)
					+ " " + createExceptions(method);
			clientMethod += ";\n";

			fileContent.insert(endOfClass - 1, clientMethod);
			try {
				FileWriter fw = new FileWriter(new File(this.serviceInterface));
				fw.write(fileContent.toString());
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// populate the impl method
			addImpl(method);
			// populate the provider impl method
			addProviderImpl(method);
			// populate the client method
			addClientImpl(method);
		}
	}

	private void addClientImpl(MethodsTypeMethod method) {
		StringBuffer fileContent = null;
		String methodName = method.getName();
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(
					this.serviceClient));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		String clientMethod = createUnBoxedSignatureStringFromMethod(method)
				+ " " + createExceptions(method);
		clientMethod += "{\n\t\t";
		// clientMethod += "try{\n";
		clientMethod += "\t\t\t";
		
		String secureValue = "SECURITY_PROPERTY_NONE";
		if (method.getSecure() != null) {
			secureValue = "SECURITY_PROPERTY_"
					+ method.getSecure();
		}
		
		// get the port
		// TODO: handle security here
		clientMethod += this.deploymentProperties
				.get("introduce.skeleton.service.name")
				+ "PortType port = this.getPortType();\n";

		clientMethod += "";

		// put in the call to the client
		String var = "port";

		String lineStart = "\t\t\t";

		String methodString = lineStart;
		MethodTypeOutput returnTypeEl = method.getOutput();
		String returnType = getClassNameFromElement(returnTypeEl);

		// always a boxed call now becuase using complex types in the wsdl
		// create handle for the boxed wrapper
		methodString += this.packageName + "."
				+ capatilzeFirstLetter(methodName) + " params = new "
				+ this.packageName + "." + capatilzeFirstLetter(methodName)
				+ "();\n";
		// set the values fo the boxed wrapper
		if (method.getInputs() != null) {
			for (int j = 0; j < method.getInputs().getInput().length; j++) {
				String paramName = method.getInputs().getInput(j).getName();
				methodString += lineStart;
				methodString += "params.set" + capatilzeFirstLetter(paramName)
						+ "(" + paramName + ");\n";
			}
		}
		// make the call
		methodString += lineStart;

		// always boxed returns now because of complex types in wsdl
		String returnTypeBoxed = getBoxedOutputTypeName(methodName);
		methodString += this.packageName + "." + returnTypeBoxed
				+ " boxedResult = " + var + "." + methodName + "(params);\n";
		methodString += lineStart;
		if (!returnType.equals("void")) {
			methodString += "return boxedResult.getValue();\n";
		}

		clientMethod += methodString;

		// clientMethod += "\t\t} catch(Exception e)
		// {\n\t\t\te.printStackTrace();\n\t\t}\n";
		// Element methodReturn = method.getChild("output",
		// method.getNamespace());
		// if (!methodReturn.getAttributeValue("className").equals("void")) {
		// if (!isPrimitive(returnType)) {
		// clientMethod += "\t\treturn null;";
		// } else if (isPrimitive(returnType)) {
		// clientMethod += "\t\treturn "
		// + createPrimitiveReturn(methodReturn
		// .getAttributeValue("className")) + ";\n";
		// }
		// }

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

	private void addImpl(MethodsTypeMethod method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(
					this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		String clientMethod = createUnBoxedSignatureStringFromMethod(method)
				+ " " + createExceptions(method);

		clientMethod += "{\n";
		clientMethod += "\t\t//TODO: Implement this autogenerated method\n";
		MethodTypeOutput methodReturn = method.getOutput();
		if (!getClassNameFromElement(methodReturn).equals("void")
				&& !isPrimitive(getClassNameFromElement(methodReturn))) {
			clientMethod += "\t\treturn null;\n";
		} else if (isPrimitive(getClassNameFromElement(methodReturn))) {
			clientMethod += "\t\treturn "
					+ createPrimitiveReturn(getClassNameFromElement(methodReturn))
					+ ";\n";
		}
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

	private void addProviderImpl(MethodsTypeMethod method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(
					this.serviceProviderImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		// slh -- in migration to globus 4 we need to check here for autoboxing
		// and get appropriate
		String clientMethod = createBoxedSignatureStringFromMethod(method)
				+ " " + createExceptions(method);

		// clientMethod += " throws RemoteException";
		clientMethod += "{\n";

		// create the unboxed call to the implementation
		String var = "impl";
		String lineStart = "\t\t";

		String methodName = method.getName();
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String returnType = getClassNameFromElement(returnTypeEl);

		// unbox the params
		String params = "";

		if (method.getInputs() != null) {
			// always unbox now
			if (method.getInputs().getInput().length >= 1) {
				// inputs were boxed and need to be unboxed
				for (int j = 0; j < method.getInputs().getInput().length; j++) {
					String paramName = method.getInputs().getInput(j).getName();
					params += "params.get" + capatilzeFirstLetter(paramName)
							+ "()";
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
		if (returnType.equals("void")) {
			// just call it is void
			methodString += lineStart;
			methodString += var + "." + methodName + "(" + params + ");\n";
			methodString += lineStart;
			methodString += "return new " + this.packageName + "."
					+ returnTypeBoxed + "();\n";
		} else {
			// need to unbox on the way out
			methodString += lineStart;
			methodString += this.packageName + "." + returnTypeBoxed
					+ " boxedResult = new " + this.packageName + "."
					+ returnTypeBoxed + "();\n";
			methodString += lineStart;
			methodString += "boxedResult.setValue(" + var + "." + methodName
					+ "(" + params + "));\n";
			methodString += lineStart;
			methodString += "return boxedResult;\n";
		}

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

	public void removeMethods(List removals) {
		for (int i = 0; i < removals.size(); i++) {
			JavaMethod method = (JavaMethod) removals.get(i);

			StringBuffer fileContent = null;
			try {
				fileContent = CommonTools.fileToStringBuffer(new File(
						this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// remove the method
			String clientMethod = createUnBoxedSignatureStringFromMethod(method);
			clientMethod += " throws RemoteException ;\n";
			int startOfMethod = fileContent.indexOf(clientMethod);
			int endOfMethod = startOfMethod + clientMethod.length();

			if (startOfMethod == -1 || endOfMethod == -1) {
				System.err.println("WARNING: Unable to locate method in I : "
						+ method.getName());
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

			// remove the impl method
			removeImpl(method);
			// remove the provider impl method
			removeProviderImpl(method);
			// remove the client method
			removeClientImpl(method);
		}
	}

	private void removeClientImpl(JavaMethod method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(
					this.serviceClient));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createUnBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod
				+ clientMethod.length());

		if (startOfMethod == -1 || endOfMethod == -1) {
			System.err
					.println("WARNING: Unable to locate method in clientImpl : "
							+ method.getName());
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

	private void removeProviderImpl(JavaMethod method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(
					this.serviceProviderImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod
				+ clientMethod.length());

		if (startOfMethod == -1 || endOfMethod == -1) {
			System.err
					.println("WARNING: Unable to locate method in providerImpl : "
							+ method.getName());
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

	private void removeImpl(JavaMethod method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(
					this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createUnBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod
				+ clientMethod.length());

		if (startOfMethod == -1 || endOfMethod == -1) {
			System.err.println("WARNING: Unable to locate method in Impl : "
					+ method.getName());
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

	private int parenMatch(StringBuffer sb, int startingIndex) {
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
		return index;
	}
}
