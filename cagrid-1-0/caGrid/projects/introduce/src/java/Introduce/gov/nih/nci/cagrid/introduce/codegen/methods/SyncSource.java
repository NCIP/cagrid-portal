package gov.nih.nci.cagrid.introduce.codegen.methods;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.ServiceSecurityConfiguration;
import gov.nih.nci.cagrid.introduce.beans.method.AnonymousClientsType;
import gov.nih.nci.cagrid.introduce.beans.method.AuthenticationMethodType;
import gov.nih.nci.cagrid.introduce.beans.method.ClientAuthorizationType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.SecureCommunicationConfiguration;
import gov.nih.nci.cagrid.introduce.beans.method.SecureCommunicationMethodType;
import gov.nih.nci.cagrid.introduce.codegen.TemplateUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.Parameter;


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
	private Properties deploymentProperties;
	private String packageName;
	private ServiceInformation serviceInfo;


	public SyncSource(File baseDir, ServiceInformation info) {
		// this.baseDir = baseDir;
		this.serviceInfo = info;
		this.deploymentProperties = this.serviceInfo.getServiceProperties();
		this.packageName = (String) this.deploymentProperties.get("introduce.skeleton.package") + ".stubs";
		serviceClient = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ this.deploymentProperties.get("introduce.skeleton.package.dir") + File.separator + "client"
			+ File.separator + this.deploymentProperties.get("introduce.skeleton.service.name") + "Client.java";
		serviceInterface = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ this.deploymentProperties.get("introduce.skeleton.package.dir") + File.separator + "common"
			+ File.separator + this.deploymentProperties.get("introduce.skeleton.service.name") + "I.java";
		serviceImpl = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ this.deploymentProperties.get("introduce.skeleton.package.dir") + File.separator + "service"
			+ File.separator + this.deploymentProperties.get("introduce.skeleton.service.name") + "Impl.java";
		serviceProviderImpl = baseDir.getAbsolutePath() + File.separator + "src" + File.separator
			+ this.deploymentProperties.get("introduce.skeleton.package.dir") + File.separator + "service"
			+ File.separator + "globus" + File.separator
			+ this.deploymentProperties.get("introduce.skeleton.service.name") + "ProviderImpl.java";
	}


	private String createExceptions(MethodType method) {
		String exceptions = "";
		// process the faults for this method...
		MethodTypeExceptions exceptionsEl = method.getExceptions();
		exceptions += "RemoteException";
		if (exceptionsEl != null && exceptionsEl.getException() != null) {
			if (exceptionsEl.getException().length > 0) {
				exceptions += ", ";
			}
			for (int i = 0; i < exceptionsEl.getException().length; i++) {
				MethodTypeExceptionsException fault = exceptionsEl.getException(i);
				// hack for now, should look at the namespace in the
				// element.....
				exceptions += this.packageName + "." + TemplateUtils.upperCaseFirstCharacter(fault.getName());
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


	private String createUnBoxedSignatureStringFromMethod(MethodType method) {
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String methodName = method.getName();
		String returnType = returnTypeEl.getClassName();
		if (returnTypeEl.getPackageName() != null && returnTypeEl.getPackageName().length() > 0) {
			returnType = returnTypeEl.getPackageName() + "." + returnType;
		}
		methodString += "     public " + returnType + " " + methodName + "(";
		if (method.getInputs() != null && method.getInputs().getInput() != null) {
			for (int j = 0; j < method.getInputs().getInput().length; j++) {
				String packageName = method.getInputs().getInput(j).getPackageName();
				String classType = null;
				if (packageName.length() > 0) {
					classType = packageName + "." + method.getInputs().getInput(j).getClassName();
				} else {
					classType = method.getInputs().getInput(j).getClassName();
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


	private String getBoxedOutputTypeName(String input) {
		String returnType = TemplateUtils.upperCaseFirstCharacter(input) + "Response";
		return returnType;
	}


	private String createBoxedSignatureStringFromMethod(MethodType method) {
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String methodName = method.getName();
		String returnType = returnTypeEl.getClassName();

		returnType = this.packageName + "." + getBoxedOutputTypeName(methodName);

		methodString += "     public " + returnType + " " + methodName + "(";

		// boxed
		methodString += this.packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + " params";

		methodString += ")";
		return methodString;
	}


	private String createBoxedSignatureStringFromMethod(JavaMethod method) {
		String methodString = "";
		String methodName = method.getName();
		String returnType = "";

		// need to box the output type
		returnType = this.packageName + "." + getBoxedOutputTypeName(method.getName());

		methodString += "     public " + returnType + " " + methodName + "(";
		// Parameter[] inputs = method.getParams();
		// always boxed for now
		// if (inputs.length > 1 || inputs.length == 0) {

		// boxed
		methodString += this.packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + " params";

		methodString += ")";
		return methodString;
	}


	private boolean isPrimitive(String type) {
		if (type.equals("int") || type.equals("double") || type.equals("float") || type.equals("boolean")
			|| type.equals("short") || type.equals("byte") || type.equals("char") || type.equals("long")) {
			return true;
		}
		return false;
	}


	private String createPrimitiveReturn(String type) {
		if (type.equals("int") || type.equals("double") || type.equals("float") || type.equals("short")
			|| type.equals("byte") || type.equals("char") || type.equals("long")) {
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
			MethodType method = (MethodType) additions.get(i);

			StringBuffer fileContent = null;
			try {
				fileContent = CommonTools.fileToStringBuffer(new File(this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// insert the new client method
			int endOfClass = fileContent.lastIndexOf("}");
			String clientMethod = createUnBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);
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


	public void modifyMethods(List modifiedMethods) {
		for (int i = 0; i < modifiedMethods.size(); i++) {
			// add it to the interface
			Modification mod = (Modification)modifiedMethods.get(i);
			MethodType method = mod.getMethodType();

			StringBuffer fileContent = null;
			try {
				fileContent = CommonTools.fileToStringBuffer(new File(this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// remove the old interface method
			String clientMethod = createUnBoxedSignatureStringFromMethod(mod.getJavaMethod());
			System.err.println("Looking to remove method: |" + clientMethod + "|");
			int startOfMethod = fileContent.indexOf(clientMethod);
			String restOfFile = fileContent.substring(startOfMethod);
			int endOfMethod = startOfMethod + restOfFile.indexOf(";\n") + 2;

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

			// just clean up the modified impl
			modifyImpl(mod);
			// redo the provider impl method
			removeProviderImpl(mod.getJavaMethod());
			addProviderImpl(method);
			// redo the client method
			removeClientImpl(mod.getJavaMethod());
			addClientImpl(method);
		}
	}


	private String getClientSecurityCode(SecureCommunicationConfiguration scc, boolean isService) {
		StringBuffer sec = new StringBuffer();
		if (scc != null) {
			SecureCommunicationMethodType comm = scc.getSecureCommunication();
			if (comm != null) {

				if (comm.equals(SecureCommunicationMethodType.Default)) {
					ServiceSecurityConfiguration serviceConf = this.serviceInfo.getServiceSecurityConfiguration();
					if (serviceConf != null) {
						if (!isService) {
							return getClientSecurityCode(serviceConf.getServiceCommunicationSecurity(), true);
						}
					}
				} else if (comm.equals(SecureCommunicationMethodType.GSI_Transport_Level_Security)) {
					sec.append("Util.registerTransport();\n");
					if (scc.getAuthenticationMethod().equals(AuthenticationMethodType.Integrity)) {
						sec
							.append("stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT, org.globus.wsrf.security.Constants.SIGNATURE);\n");
					} else {
						sec
							.append("stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT, org.globus.wsrf.security.Constants.ENCRYPTION);\n");
					}

					if (scc.getAnonymousClients().equals(AnonymousClientsType.Yes)) {
						sec
							.append("	stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS,Boolean.TRUE);\n");
					} else {
						sec.append("	if (proxy != null) {\n");
						sec.append("try{\n");
						sec
							.append("		org.ietf.jgss.GSSCredential gss = new org.globus.gsi.gssapi.GlobusGSSCredentialImpl(proxy,org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT);\n");
						sec.append("		stub._setProperty(org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss);\n");
						sec.append("}catch(org.ietf.jgss.GSSException ex){\n");
						sec.append("throw new RemoteException(ex.getMessage());\n");
						sec.append("}\n");
						sec.append("}\n");
					}

					if (scc.getClientAuthorization() != null) {
						if (scc.getClientAuthorization().equals(ClientAuthorizationType.None)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.NoAuthorization.getInstance());\n");
						} else if (scc.getClientAuthorization().equals(ClientAuthorizationType.Host)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.HostAuthorization.getInstance());\n");
						} else if (scc.getClientAuthorization().equals(ClientAuthorizationType.Self)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.SelfAuthorization.getInstance());\n");
						}
					}

				} else if (comm.equals(SecureCommunicationMethodType.GSI_Secure_Conversation)) {
					if (scc.getAuthenticationMethod().equals(AuthenticationMethodType.Integrity)) {
						sec
							.append("stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_CONV, org.globus.wsrf.security.Constants.SIGNATURE);\n");
					} else {
						sec
							.append("stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_CONV, org.globus.wsrf.security.Constants.ENCRYPTION);\n");
					}

					if (scc.getAnonymousClients().equals(AnonymousClientsType.Yes)) {
						sec
							.append("	stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS,Boolean.TRUE);\n");
					} else {
						sec.append("	if (proxy != null) {\n");
						sec.append("try{\n");
						sec
							.append("		org.ietf.jgss.GSSCredential gss = new org.globus.gsi.gssapi.GlobusGSSCredentialImpl(proxy,org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT);\n");
						sec.append("		stub._setProperty(org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss);\n");
						sec.append("}catch(org.ietf.jgss.GSSException ex){\n");
						sec.append("throw new RemoteException(ex.getMessage());\n");
						sec.append("}\n");
						sec.append("}\n");
					}

					if (scc.getClientAuthorization() != null) {
						if (scc.getClientAuthorization().equals(ClientAuthorizationType.None)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.NoAuthorization.getInstance());\n");
						} else if (scc.getClientAuthorization().equals(ClientAuthorizationType.Host)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.HostAuthorization.getInstance());\n");
						} else if (scc.getClientAuthorization().equals(ClientAuthorizationType.Self)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.SelfAuthorization.getInstance());\n");
						}
					}

				} else if (comm.equals(SecureCommunicationMethodType.GSI_Secure_Message)) {
					if (scc.getAuthenticationMethod().equals(AuthenticationMethodType.Integrity)) {
						sec
							.append("stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_MSG, org.globus.wsrf.security.Constants.SIGNATURE);\n");
					} else {
						sec
							.append("stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_MSG, org.globus.wsrf.security.Constants.ENCRYPTION);\n");
					}

					sec.append("	if (proxy != null) {\n");
					sec.append("try{\n");
					sec
						.append("		org.ietf.jgss.GSSCredential gss = new org.globus.gsi.gssapi.GlobusGSSCredentialImpl(proxy,org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT);\n");
					sec.append("		stub._setProperty(org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss);\n");
					sec.append("}catch(org.ietf.jgss.GSSException ex){\n");
					sec.append("throw new RemoteException(ex.getMessage());\n");
					sec.append("}\n");
					sec.append("}\n");

					if (scc.getClientAuthorization() != null) {
						if (scc.getClientAuthorization().equals(ClientAuthorizationType.None)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.NoAuthorization.getInstance());\n");
						} else if (scc.getClientAuthorization().equals(ClientAuthorizationType.Host)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.HostAuthorization.getInstance());\n");
						} else if (scc.getClientAuthorization().equals(ClientAuthorizationType.Self)) {
							sec
								.append("stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, org.globus.wsrf.impl.security.authorization.SelfAuthorization.getInstance());\n");
						}
					}
				}
			}
		}
		return sec.toString();
	}


	private void addClientImpl(MethodType method) {
		StringBuffer fileContent = null;
		String methodName = method.getName();
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceClient));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		String clientMethod = createUnBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);
		clientMethod += "{\n          ";
		// clientMethod += "try{\n";
		clientMethod += "               ";
		clientMethod += this.deploymentProperties.get("introduce.skeleton.service.name")
			+ "PortType port = this.getPortType();\n";

		clientMethod += "";
		clientMethod += "org.apache.axis.client.Stub stub = (org.apache.axis.client.Stub) port;\n";
		SecureCommunicationConfiguration scc = method.getMethodSecurity();

		clientMethod += "\n" + getClientSecurityCode(scc, false);
		// put in the call to the client
		String var = "port";

		String lineStart = "               ";

		String methodString = lineStart;
		MethodTypeOutput returnTypeEl = method.getOutput();
		String returnType = returnTypeEl.getClassName();

		// always a boxed call now becuase using complex types in the wsdl
		// create handle for the boxed wrapper
		methodString += this.packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + " params = new "
			+ this.packageName + "." + TemplateUtils.upperCaseFirstCharacter(methodName) + "();\n";
		// set the values fo the boxed wrapper
		if (method.getInputs() != null && method.getInputs().getInput() != null) {
			for (int j = 0; j < method.getInputs().getInput().length; j++) {
				String paramName = method.getInputs().getInput(j).getName();
				methodString += lineStart;
				methodString += "params.set" + TemplateUtils.upperCaseFirstCharacter(paramName) + "(" + paramName
					+ ");\n";
			}
		}
		// make the call
		methodString += lineStart;

		// always boxed returns now because of complex types in wsdl
		String returnTypeBoxed = getBoxedOutputTypeName(methodName);
		methodString += this.packageName + "." + returnTypeBoxed + " boxedResult = " + var + "." + methodName
			+ "(params);\n";
		methodString += lineStart;
		if (!returnType.equals("void")) {
			methodString += "return boxedResult.getResponse();\n";
		}

		clientMethod += methodString;

		// clientMethod += " } catch(Exception e)
		// {\n e.printStackTrace();\n }\n";
		// Element methodReturn = method.getChild("output",
		// method.getNamespace());
		// if (!methodReturn.getAttributeValue("className").equals("void")) {
		// if (!isPrimitive(returnType)) {
		// clientMethod += " return null;";
		// } else if (isPrimitive(returnType)) {
		// clientMethod += " return "
		// + createPrimitiveReturn(methodReturn
		// .getAttributeValue("className")) + ";\n";
		// }
		// }

		clientMethod += "\n     }\n\n";

		fileContent.insert(endOfClass - 1, clientMethod);
		try {
			FileWriter fw = new FileWriter(new File(this.serviceClient));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}


	private void addImpl(MethodType method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		String clientMethod = createUnBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);

		clientMethod += "{\n";
		clientMethod += "          //TODO: Implement this autogenerated method\n";
		MethodTypeOutput methodReturn = method.getOutput();
		if (!methodReturn.getClassName().equals("void") && !isPrimitive(methodReturn.getClassName())) {
			clientMethod += "          return null;\n";
		} else if (isPrimitive(methodReturn.getClassName())) {
			clientMethod += "          return " + createPrimitiveReturn(methodReturn.getClassName()) + ";\n";
		}
		clientMethod += "     }\n";

		fileContent.insert(endOfClass - 1, clientMethod);
		try {
			FileWriter fw = new FileWriter(new File(this.serviceImpl));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	private void addProviderImpl(MethodType method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceProviderImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert the new client method
		int endOfClass = fileContent.lastIndexOf("}");
		// slh -- in migration to globus 4 we need to check here for autoboxing
		// and get appropriate
		String clientMethod = createBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);

		// clientMethod += " throws RemoteException";
		clientMethod += "{\n";

		// create the unboxed call to the implementation
		String var = "impl";
		String lineStart = "          ";

		String methodName = method.getName();
		String methodString = "";
		MethodTypeOutput returnTypeEl = method.getOutput();
		String returnType = returnTypeEl.getClassName();

		// unbox the params
		String params = "";

		if (method.getInputs() != null && method.getInputs().getInput() != null) {
			// always unbox now
			if (method.getInputs().getInput().length >= 1) {
				// inputs were boxed and need to be unboxed
				for (int j = 0; j < method.getInputs().getInput().length; j++) {
					String paramName = method.getInputs().getInput(j).getName();
					params += "params.get" + TemplateUtils.upperCaseFirstCharacter(paramName) + "()";
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
			methodString += "return new " + this.packageName + "." + returnTypeBoxed + "();\n";
		} else {
			// need to unbox on the way out
			methodString += lineStart;
			methodString += this.packageName + "." + returnTypeBoxed + " boxedResult = new " + this.packageName + "."
				+ returnTypeBoxed + "();\n";
			methodString += lineStart;
			methodString += "boxedResult.setResponse(" + var + "." + methodName + "(" + params + "));\n";
			methodString += lineStart;
			methodString += "return boxedResult;\n";
		}

		clientMethod += methodString;
		clientMethod += "     }\n";
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
				fileContent = CommonTools.fileToStringBuffer(new File(this.serviceInterface));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// remove the method
			String clientMethod = createUnBoxedSignatureStringFromMethod(method);
			System.err.println("Looking to remove method: |" + clientMethod + "|");
			int startOfMethod = fileContent.indexOf(clientMethod);
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
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceClient));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createUnBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod + clientMethod.length());

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


	private void removeClientImpl(MethodType method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceClient));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createUnBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod + clientMethod.length());

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


	private void removeProviderImpl(JavaMethod method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceProviderImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod + clientMethod.length());

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


	private void removeProviderImpl(MethodType method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceProviderImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod + clientMethod.length());

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


	private void modifyImpl(Modification mod) {
		MethodType method = mod.getMethodType();
		JavaMethod oldMethod = mod.getJavaMethod();
		
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the old method signature
		String clientMethod = createUnBoxedSignatureStringFromMethod(oldMethod);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfSignature = endOfSignature(fileContent, startOfMethod + clientMethod.length());

		if (startOfMethod == -1 || endOfSignature == -1) {
			System.err.println("WARNING: Unable to locate method in Impl : " + oldMethod.getName());
			return;
		}

		fileContent.delete(startOfMethod, endOfSignature);

		// add in the new modified signature
		clientMethod = createUnBoxedSignatureStringFromMethod(method) + " " + createExceptions(method);
		clientMethod += "{\n";
		fileContent.insert(startOfMethod, clientMethod);

		try {
			FileWriter fw = new FileWriter(new File(this.serviceImpl));
			fw.write(fileContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}


	private void removeImpl(JavaMethod method) {
		StringBuffer fileContent = null;
		try {
			fileContent = CommonTools.fileToStringBuffer(new File(this.serviceImpl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// remove the method
		String clientMethod = createUnBoxedSignatureStringFromMethod(method);
		int startOfMethod = fileContent.indexOf(clientMethod);
		int endOfMethod = parenMatch(fileContent, startOfMethod + clientMethod.length());

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


	private int endOfSignature(StringBuffer sb, int startingIndex) {
		int index = startingIndex;
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
}
