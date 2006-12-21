package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.creation.templates.EnumerationServiceClientTemplate;
import gov.nih.nci.cagrid.data.enumeration.service.globus.EnumerationDataServiceProviderImpl;
import gov.nih.nci.cagrid.data.enumeration.stubs.EnumerationDataServicePortType;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.wsenum.common.WsEnumConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

/** 
 *  WsEnumerationFeatureCreator
 *  Adds the components needed for the WS-Enumeration feature of data services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id$ 
 */
public class WsEnumerationFeatureCreator extends FeatureCreator {
	public static final String ENUMERATION_DATA_SERVICE_NAMESPACE = "http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService";
	public static final String WS_ENUM_EXTENSION_NAME = "cagrid_wsEnum";

	public WsEnumerationFeatureCreator(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
		super(info, mainService, serviceProps);
	}
	
	
	public void addFeature() throws CreationExtensionException {
		installWsEnumExtension();
		copySchemas();
		addEnumerationQueryMethod();
		createDataSourceClient();
	}
	
	
	private void installWsEnumExtension() throws CreationExtensionException {
		// verify the ws-enum extension is installed
		if (!wsEnumExtensionInstalled()) {
			throw new CreationExtensionException("The required extension " + WS_ENUM_EXTENSION_NAME 
				+ " was not found to be installed.  Please install it and try creating your service again");
		}
		
		if (!wsEnumExtensionUsed()) {
			System.out.println("Adding the WS-Enumeration extension to the service");
			// add the ws Enumeration extension
			ExtensionDescription ext = ExtensionsLoader.getInstance()
				.getExtension(WsEnumerationFeatureCreator.WS_ENUM_EXTENSION_NAME);
			ExtensionType extType = new ExtensionType();
			extType.setName(ext.getServiceExtensionDescription().getName());
			extType.setExtensionType(ext.getExtensionType());
			ExtensionType[] serviceExtensions = getServiceInformation()
				.getServiceDescriptor().getExtensions().getExtension();
			ExtensionType[] allExtensions = new ExtensionType[serviceExtensions.length + 1];
			System.arraycopy(serviceExtensions, 0, allExtensions, 0, serviceExtensions.length);
			allExtensions[allExtensions.length - 1] = extType;
			getServiceInformation().getServiceDescriptor().getExtensions().setExtension(allExtensions);
			// wsEnum extension copies libraries into the service on its own
		}
	}
	
	
	private void addEnumerationQueryMethod() {
		// add the enumerationQuery method to the data service
		MethodType enumerateMethod = new MethodType();
		enumerateMethod.setName("enumerationQuery");
		enumerateMethod.setIsImported(true);
		enumerateMethod.setIsProvided(true);
		MethodTypeInputs enumInputs = new MethodTypeInputs();
		MethodTypeInputsInput queryParam = new MethodTypeInputsInput();
		queryParam.setName(DataServiceConstants.QUERY_METHOD_PARAMETER_NAME);
		queryParam.setIsArray(false);
		QName queryQname = new QName(DataServiceConstants.CQL_QUERY_URI, CQLQuery.class.getSimpleName());
		queryParam.setQName(queryQname);
		enumInputs.setInput(new MethodTypeInputsInput[]{queryParam});
		enumerateMethod.setInputs(enumInputs);
		MethodTypeOutput enumOutput = new MethodTypeOutput();
		enumOutput.setIsArray(false);
		enumOutput.setQName(new QName(WsEnumConstants.WS_ENUMERATION_URI, WsEnumConstants.ENUMERATE_RESPONSE_TYPE));
		enumerateMethod.setOutput(enumOutput);
		// import info
		MethodTypeImportInformation enumImport = new MethodTypeImportInformation();
		enumImport.setPortTypeName(EnumerationDataServicePortType.class.getSimpleName());
		enumImport.setWsdlFile("EnumerationDataService.wsdl");
		enumImport.setInputMessage(new QName(ENUMERATION_DATA_SERVICE_NAMESPACE, "EnumerationQueryRequest"));
		enumImport.setOutputMessage(new QName(ENUMERATION_DATA_SERVICE_NAMESPACE, "EnumerationQueryResponse"));
		enumImport.setNamespace(ENUMERATION_DATA_SERVICE_NAMESPACE);
		enumImport.setPackageName(DataServiceConstants.ENUMERATION_DATA_SERVICE_PACKAGE);
		enumerateMethod.setImportInformation(enumImport);
		// provider info
		MethodTypeProviderInformation enumProvider = new MethodTypeProviderInformation();
		enumProvider.setProviderClass(EnumerationDataServiceProviderImpl.class.getName());
		enumerateMethod.setProviderInformation(enumProvider);
		// exceptions
		MethodTypeExceptions methodExceptions = new MethodTypeExceptions();
		MethodTypeExceptionsException qpException = new MethodTypeExceptionsException(
			DataServiceConstants.QUERY_PROCESSING_EXCEPTION_NAME, DataServiceConstants.QUERY_PROCESSING_EXCEPTION_QNAME);
		MethodTypeExceptionsException mqException = new MethodTypeExceptionsException(
			DataServiceConstants.MALFORMED_QUERY_EXCEPTION_NAME, DataServiceConstants.MALFORMED_QUERY_EXCEPTION_QNAME);
		methodExceptions.setException(new MethodTypeExceptionsException[] {qpException, mqException});
		enumerateMethod.setExceptions(methodExceptions);
		// add the method to the service
		CommonTools.addMethod(getMainService(), enumerateMethod);
	}
	
	
	private void copySchemas() throws CreationExtensionException {		
		// copy over the EnumerationQuery.wsdl file
		String schemaDir = getServiceSchemaDir();
		File dataExtensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator 
			+ "data" + File.separator + "schema");
		File wsEnumExtensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator
			+ WS_ENUM_EXTENSION_NAME + File.separator + "schema");
		File wsdlFile = new File(dataExtensionSchemaDir.getAbsolutePath() 
			+ File.separator + "Data" + File.separator + "EnumerationDataService.wsdl");
		File enumWsdlFile = new File(wsEnumExtensionSchemaDir.getAbsolutePath()
			+ File.separator + WsEnumConstants.ENUMERATION_WSDL_NAME);
		File enumXsdFile = new File(wsEnumExtensionSchemaDir.getAbsolutePath()
			+ File.separator + WsEnumConstants.ENUMERATION_XSD_NAME);
		File addressingXsdFile = new File(wsEnumExtensionSchemaDir.getAbsolutePath()
			+ File.separator + WsEnumConstants.ADDRESSING_XSD_NAME);
		File wsdlOutFile = new File(schemaDir + File.separator + wsdlFile.getName());
		File enumWsdlOutFile = new File(schemaDir + File.separator + enumWsdlFile.getName());
		File enumXsdOutFile = new File(schemaDir + File.separator + enumXsdFile.getName());
		File addressingXsdOutFile = new File(schemaDir + File.separator + addressingXsdFile.getName());
		try {
			Utils.copyFile(wsdlFile, wsdlOutFile);
			Utils.copyFile(enumWsdlFile, enumWsdlOutFile);
			Utils.copyFile(enumXsdFile, enumXsdOutFile);
			Utils.copyFile(addressingXsdFile, addressingXsdOutFile);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error copying data service schemas: " + ex.getMessage(), ex);
		}
	}
	
	
	private void createDataSourceClient() throws CreationExtensionException {
		EnumerationServiceClientTemplate template = new EnumerationServiceClientTemplate();
		String clientClassContents = template.generate(getServiceInformation());
		// figgure out the class name
		String classStart = "public class ";
		int nameStart = clientClassContents.indexOf(classStart) + classStart.length();
		int nameEnd = clientClassContents.indexOf(' ', nameStart);
		String clientClassName = clientClassContents.substring(nameStart, nameEnd);
		// and the package name
		String pack = "package ";
		int packNameStart = clientClassContents.indexOf(pack) + pack.length();
		int packNameEnd = clientClassContents.indexOf(';', packNameStart);
		String clientPackage = clientClassContents.substring(packNameStart, packNameEnd);
		// write it out to disk
		File clientClassFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath()
			+ File.separator + "src" + File.separator + clientPackage.replace('.', File.separatorChar)
			+ File.separator + clientClassName + ".java");
		try {
			FileWriter classWriter = new FileWriter(clientClassFile);
			classWriter.write(clientClassContents);
			classWriter.flush();
			classWriter.close();
		} catch (IOException ex) {
			throw new CreationExtensionException("Error creating Data Source enumeration client class: " 
				+ ex.getMessage(), ex);
		}		
	}
	
	
	private boolean wsEnumExtensionInstalled() {
		List extensionDescriptors = ExtensionsLoader.getInstance().getServiceExtensions();
		for (int i = 0; i < extensionDescriptors.size(); i++) {
			ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) extensionDescriptors.get(i);
			if (ex.getName().equals(WS_ENUM_EXTENSION_NAME)) {
				return true;
			}
		}
		return false;
	}
	
	
	private boolean wsEnumExtensionUsed() {
		ServiceDescription desc = getServiceInformation().getServiceDescriptor();
		if (desc.getExtensions() != null && desc.getExtensions().getExtension() != null) {
			for (int i = 0; i < desc.getExtensions().getExtension().length; i++) {
				if (desc.getExtensions().getExtension(i).getName().equals(WS_ENUM_EXTENSION_NAME)) {
					return true;
				}
			}
		}		
		return false;
	}
	
	
	private String getServiceSchemaDir() {
		return getServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "schema"
			+ File.separator + getServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	}
}
