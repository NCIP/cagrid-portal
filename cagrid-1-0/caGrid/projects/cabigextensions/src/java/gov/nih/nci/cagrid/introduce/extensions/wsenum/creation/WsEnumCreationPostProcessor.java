package gov.nih.nci.cagrid.introduce.extensions.wsenum.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.wsenum.common.WsEnumConstants;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.globus.ws.enumeration.EnumProvider;
import org.projectmobius.common.MobiusException;

/** 
 *  WsEnumCreationPostProcessor
 *  Creation extension for Introduce to add WS-Enumeration support
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 1, 2006 
 * @version $Id$ 
 */
public class WsEnumCreationPostProcessor implements CreationExtensionPostProcessor {
	
	public void postCreate(ServiceDescription desc, Properties props) throws CreationExtensionException {
		// get the service directory
		File serviceDir = new File(props.getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR));
		// add the namespace and schemas for WS-Enumeration
		addEnumerationNamespace(serviceDir, desc, props);
		// get the service type
		String serviceName = props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		ServiceType service = CommonTools.getService(desc.getServices(), serviceName);
		// add the methods for WS-Enumeration
		addEnumerationMethods(service);
		// add the modified Globus WsCore 4.0.2 jars to the service
		addWsEnumLibraries(serviceDir);
	}
	
	
	/**
	 * Copies the ws-enumeration schema into the service and adds the namespace type for it
	 * @param serviceDir
	 * @param desc
	 * @param props
	 * @throws CreationExtensionException
	 */
	private static void addEnumerationNamespace(File serviceDir, ServiceDescription desc, Properties props) 
		throws CreationExtensionException {
		String serviceName = props.getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		// copy the schemas
		File schemaBaseDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "cagrid_wsEnum"
			+ File.separator + "schema" + File.separator);
		List schemas = Utils.recursiveListFiles(schemaBaseDir, new FileFilters.XSDFileFilter());
		for (int i = 0; i < schemas.size(); i++) {
			File schemaFile = (File) schemas.get(i);
			File schemaOutFile = new File(serviceDir.getAbsolutePath() + File.separator 
				+ "schema" + File.separator + serviceName + File.separator + schemaFile.getName());
			try {
				Utils.copyFile(schemaFile, schemaOutFile);
			} catch (Exception ex) {
				throw new CreationExtensionException("Error copying enumeration schema file: " + ex.getMessage(), ex);
			}
		}
		
		// copy the wsdl
		File enumWsdlFile = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "cagrid_wsEnum"
			+ File.separator + "schema" + File.separator + "enumeration.wsdl");
		File enumWsdlOut = new File(serviceDir.getAbsolutePath() + File.separator 
			+ "schema" + File.separator + serviceName + File.separator + "enumeration.wsdl");
		try {
			Utils.copyFile(enumWsdlFile, enumWsdlOut);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error copying enumeration wsdl file: " + ex.getMessage(), ex);
		}
		
		// add the namespace type for the enumeration schema
		NamespaceType nsType = null;
		try {
			File enumSchema = new File(serviceDir.getAbsolutePath() + File.separator 
				+ "schema" + File.separator + serviceName + File.separator + "enumeration.xsd");
			nsType = CommonTools.createNamespaceType(enumSchema.getAbsolutePath());
			// fix the schema location on the namespace type
			nsType.setLocation("./" + "enumeration.xsd");
			// change the package mapping
			nsType.setPackageName(WsEnumConstants.ENUMERATION_PACKAGE_NAME);
		} catch (MobiusException ex) {
			throw new CreationExtensionException("Error creating namespace type from schema: " + ex.getMessage(), ex);
		}
		CommonTools.addNamespace(desc, nsType);
	}
	
	
	/**
	 * Adds the methods to the service required for WS-Enumeration
	 *  
	 * @param service
	 * @throws CreationExtensionException
	 */
	private static void addEnumerationMethods(ServiceType service) throws CreationExtensionException {
		// Pull method
		MethodType pullMethod = new MethodType();
		pullMethod.setName("PullOp");
		MethodTypeInputs pullInputs = new MethodTypeInputs();
		MethodTypeInputsInput pullParameter = new MethodTypeInputsInput();
		pullParameter.setIsArray(false);
		pullParameter.setName("pull");
		pullParameter.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, "Pull"));
		pullInputs.setInput(new MethodTypeInputsInput[] {pullParameter});
		pullMethod.setInputs(pullInputs);
		MethodTypeOutput pullOutput = new MethodTypeOutput();
		pullOutput.setIsArray(false);
		pullOutput.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, "PullResponse"));
		pullMethod.setOutput(pullOutput);
		setMethodImportInformation(pullMethod, "PullMessage", "PullResponseMessage");
		CommonTools.addMethod(service, pullMethod);
		
		// Renew method
		MethodType renewMethod = new MethodType();
		renewMethod.setName("RenewOp");
		MethodTypeInputs renewInputs = new MethodTypeInputs();
		MethodTypeInputsInput renewParameter = new MethodTypeInputsInput();
		renewParameter.setIsArray(false);
		renewParameter.setName("renew");
		renewParameter.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, "Renew"));
		renewInputs.setInput(new MethodTypeInputsInput[] {renewParameter});
		renewMethod.setInputs(renewInputs);
		MethodTypeOutput renewOutput = new MethodTypeOutput();
		renewOutput.setIsArray(false);
		renewOutput.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, "RenewResponse"));
		renewMethod.setOutput(renewOutput);
		setMethodImportInformation(renewMethod, "RenewMessage", "RenewResponseMessage");
		CommonTools.addMethod(service, renewMethod);
		
		// GetStatus method
		MethodType getStatusMethod = new MethodType();
		getStatusMethod.setName("GetStatusOp");
		MethodTypeInputs getStatusInputs = new MethodTypeInputs();
		MethodTypeInputsInput getStatusParam = new MethodTypeInputsInput();
		getStatusParam.setIsArray(false);
		getStatusParam.setName("status");
		getStatusParam.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, "GetStatus"));
		getStatusInputs.setInput(new MethodTypeInputsInput[] {getStatusParam});
		getStatusMethod.setInputs(getStatusInputs);
		MethodTypeOutput getStatusOutput = new MethodTypeOutput();
		getStatusOutput.setIsArray(false);
		getStatusOutput.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, "GetStatusResponse"));
		getStatusMethod.setOutput(getStatusOutput);
		setMethodImportInformation(getStatusMethod, "GetStatusMessage", "GetStatusResponseMessage");
		CommonTools.addMethod(service, getStatusMethod);
		
		// Release method
		MethodType releaseMethod = new MethodType();
		releaseMethod.setName("ReleaseOp");
		MethodTypeInputs releaseInputs = new MethodTypeInputs();
		MethodTypeInputsInput releaseParameter = new MethodTypeInputsInput();
		releaseParameter.setIsArray(false);
		releaseParameter.setName("release");
		releaseParameter.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, "Release"));
		releaseInputs.setInput(new MethodTypeInputsInput[] {releaseParameter});
		releaseMethod.setInputs(releaseInputs);
		// even void return methods require a method output
		MethodTypeOutput releaseOutput = new MethodTypeOutput();
		releaseOutput.setQName(new QName("", "void"));
		releaseOutput.setIsArray(false);
		releaseMethod.setOutput(releaseOutput);
		setMethodImportInformation(releaseMethod, "ReleaseMessage", "ReleaseResponseMessage");
		CommonTools.addMethod(service, releaseMethod);
	}
	
	
	private static void setMethodImportInformation(MethodType method, String inputName, String outputName) {
		method.setIsImported(true);
		method.setIsProvided(true);
		MethodTypeImportInformation info = new MethodTypeImportInformation();
		info.setNamespace(WsEnumConstants.WS_ENUMERATION_URI);
		info.setWsdlFile("enumeration.wsdl");
		String packName = EnumProvider.class.getPackage().getName();
		info.setPackageName(packName);
		info.setPortTypeName(WsEnumConstants.PORT_TYPE_NAME); // FIXME: ???
		// input and output message types
		QName inputQname = new QName(WsEnumConstants.WS_ENUMERATION_URI, inputName);
		info.setInputMessage(inputQname);
		QName outputQname = new QName(WsEnumConstants.WS_ENUMERATION_URI, outputName);
		info.setOutputMessage(outputQname);
		MethodTypeProviderInformation provider = new MethodTypeProviderInformation();
		provider.setProviderClass(EnumProvider.class.getName());
		method.setImportInformation(info);
		method.setProviderInformation(provider);
	}
	
	
	private static void addWsEnumLibraries(File serviceDir) throws CreationExtensionException {
		File libDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "lib");
		List enumLibs = Utils.recursiveListFiles(libDir, new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith("_enum.jar") || pathname.getName().equals("caGrid-1.0-wsEnum.jar");
			}
		});
		File[] copiedLibs = new File[enumLibs.size()];
		for (int i = 0; i < enumLibs.size(); i++) {
			File lib = (File) enumLibs.get(i);
			File out = new File(serviceDir.getAbsolutePath() + File.separator + "lib" + File.separator + lib.getName());
			copiedLibs[i] = out;
			try {
				Utils.copyFile(lib, out);
			} catch (Exception ex) {
				throw new CreationExtensionException(ex.getMessage(), ex);
			}
		}
		File classpathFile = new File(serviceDir.getAbsolutePath() + File.separator + ".classpath");
		try {
			ExtensionUtilities.syncEclipseClasspath(classpathFile, copiedLibs);
		} catch (Exception ex) {
			throw new CreationExtensionException(ex.getMessage(), ex);
		}
	}
}
