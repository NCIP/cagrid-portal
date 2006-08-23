package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.service.globus.EnumerationQueryProviderImpl;
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
import gov.nih.nci.cagrid.wsenum.common.WsEnumConstants;

import java.io.File;
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

	public WsEnumerationFeatureCreator(ServiceDescription desc, ServiceType mainService, Properties serviceProps) {
		super(desc, mainService, serviceProps);
	}
	
	
	public void addFeature() throws CreationExtensionException {
		installWsEnumExtension();
		copySchemas();
		addEnumerationQueryMethod();
	}
	
	
	private void installWsEnumExtension() throws CreationExtensionException {
		// verify the ws-enum extension is installed
		if (!wsEnumExtensionInstalled()) {
			throw new CreationExtensionException("The required extension " + WS_ENUM_EXTENSION_NAME 
				+ " was not found to be installed.  Please install it and try creating your service again");
		}
		if (!wsEnumExtensionUsed()) {
			// add the ws Enumeration extension
			ExtensionDescription ext = 
				ExtensionsLoader.getInstance().getExtension(WS_ENUM_EXTENSION_NAME);
			ExtensionType extType = new ExtensionType();
			extType.setName(ext.getServiceExtensionDescription().getName());
			extType.setExtensionType(ext.getExtensionType());
			ExtensionType[] serviceExtensions = getServiceDescription().getExtensions().getExtension();
			ExtensionType[] allExtensions = new ExtensionType[serviceExtensions.length + 1];
			System.arraycopy(serviceExtensions, 0, allExtensions, 0, serviceExtensions.length);
			allExtensions[allExtensions.length - 1] = extType;
			getServiceDescription().getExtensions().setExtension(allExtensions);
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
		enumImport.setPortTypeName("EnumerationQueryPortType");
		enumImport.setWsdlFile("EnumerationQuery.wsdl");
		enumImport.setInputMessage(new QName(ENUMERATION_DATA_SERVICE_NAMESPACE, "EnumerationQueryRequest"));
		enumImport.setOutputMessage(new QName(ENUMERATION_DATA_SERVICE_NAMESPACE, "EnumerationQueryResponse"));
		enumImport.setNamespace(ENUMERATION_DATA_SERVICE_NAMESPACE);
		enumImport.setPackageName(DataServiceConstants.DATA_SERVICE_PACKAGE);
		enumerateMethod.setImportInformation(enumImport);
		// provider info
		MethodTypeProviderInformation enumProvider = new MethodTypeProviderInformation();
		enumProvider.setProviderClass(EnumerationQueryProviderImpl.class.getName());
		enumerateMethod.setProviderInformation(enumProvider);
		// exceptions
		MethodTypeExceptions methodExceptions = new MethodTypeExceptions();
		MethodTypeExceptionsException[] exceptions = 
			new MethodTypeExceptionsException[DataServiceConstants.QUERY_METHOD_EXCEPTIONS.length];
		for (int i = 0; i < DataServiceConstants.QUERY_METHOD_EXCEPTIONS.length; i++) {
			exceptions[i] = new MethodTypeExceptionsException("Enumeration" + DataServiceConstants.QUERY_METHOD_EXCEPTIONS[i]);
		}
		methodExceptions.setException(exceptions);
		enumerateMethod.setExceptions(methodExceptions);
		// add the method to the service
		CommonTools.addMethod(getMainService(), enumerateMethod);
	}
	
	
	private void copySchemas() throws CreationExtensionException {		
		// copy over the EnumerationQuery.wsdl file
		String schemaDir = getServiceSchemaDir();
		File extensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data"
			+ File.separator + "schema");
		File wsdlFile = new File(extensionSchemaDir.getAbsolutePath() 
			+ File.separator + "Data" + File.separator + "EnumerationQuery.wsdl");
		File wsdlOutFile = new File(schemaDir + File.separator + wsdlFile.getName());
		try {
			Utils.copyFile(wsdlFile, wsdlOutFile);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error copying data service schemas: " + ex.getMessage(), ex);
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
		ServiceDescription desc = getServiceDescription();
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
