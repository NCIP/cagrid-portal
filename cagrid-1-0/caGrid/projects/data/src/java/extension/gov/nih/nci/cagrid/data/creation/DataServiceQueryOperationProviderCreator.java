package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.codegen.CQLResultTypesGenerator;
import gov.nih.nci.cagrid.data.codegen.ResultTypeGeneratorInformation;
import gov.nih.nci.cagrid.data.cql.validation.DomainModelValidator;
import gov.nih.nci.cagrid.data.cql.validation.ObjectWalkingCQLValidator;
import gov.nih.nci.cagrid.data.extension.ServiceFeatures;
import gov.nih.nci.cagrid.data.service.globus.DataServiceProviderImpl;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.wsenum.common.WsEnumConstants;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.projectmobius.common.MobiusException;


/**
 * DataServiceQueryOperationProviderCreator Adds the operation provider for the
 * data service query operation to an introduce created service at post-create
 * time
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 15, 2006
 * @version $Id$
 */
public class DataServiceQueryOperationProviderCreator implements CreationExtensionPostProcessor {
	public static final String DEFAULT_CQL_VALIDATOR_CLASS = ObjectWalkingCQLValidator.class.getName();
	public static final String DEFAULT_DOMAIN_MODEL_VALIDATOR = DomainModelValidator.class.getName();

	private static Logger log = Logger.getLogger(DataServiceQueryOperationProviderCreator.class);


	public void postCreate(ServiceExtensionDescriptionType desc, ServiceInformation serviceInfo)
		throws CreationExtensionException {
		ServiceType mainService = serviceInfo.getServices().getService(0);
		Properties serviceProperties = serviceInfo.getIntroduceServiceProperties();

		copyDataServiceSchemas(serviceProperties);
		copyDataServiceLibraries(serviceProperties);
		ResultTypeGeneratorInformation typeInfo = new ResultTypeGeneratorInformation();
		typeInfo.setServiceInfo(serviceInfo);
		try {
			CQLResultTypesGenerator.generateCQLResultTypesXSD(typeInfo);
		} catch (CodegenExtensionException e) {
			throw new CreationExtensionException("Problen creating initial permissible result type XSD.", e);
		}
		addDataServiceNamespaces(serviceInfo.getServiceDescriptor(), serviceProperties);
		modifyServiceProperties(serviceInfo.getServiceDescriptor());
		addQueryMethod(serviceInfo.getServiceDescriptor(), mainService);
		processFeatures(serviceInfo.getServiceDescriptor(), mainService, serviceProperties);
	}


	private void copyDataServiceSchemas(Properties props) throws CreationExtensionException {
		// grab cql query and result set schemas and move them into the
		// service's directory
		String schemaDir = getServiceSchemaDir(props);
		System.out.println("Copying schemas to " + schemaDir);
		File extensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data"
			+ File.separator + "schema" + File.separator + "Data");
		List schemaFiles = Utils.recursiveListFiles(extensionSchemaDir, new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.isDirectory() || pathname.getName().endsWith(".xsd")) {
					return !pathname.getName().equals(WsEnumConstants.ENUMERATION_WSDL_NAME)
						&& !pathname.getName().equals(WsEnumConstants.ENUMERATION_XSD_NAME)
						&& !pathname.getName().equals(WsEnumConstants.ADDRESSING_XSD_NAME);
				}
				return false;
			}
		});
		// also copy the WSDL for data services
		// schemaFiles.add(new File(getWsdlFileName(props)));
		schemaFiles.add(new File(extensionSchemaDir + File.separator + "DataService.wsdl"));
		try {
			for (int i = 0; i < schemaFiles.size(); i++) {
				File schemaFile = (File) schemaFiles.get(i);
				String subname = schemaFile.getAbsolutePath().substring(
					extensionSchemaDir.getAbsolutePath().length() + File.separator.length());
				File schemaOut = new File(schemaDir + File.separator + subname);
				Utils.copyFile(schemaFile, schemaOut);
			}
		} catch (Exception ex) {
			throw new CreationExtensionException("Error copying data service schemas: " + ex.getMessage(), ex);
		}
	}


	private void addDataServiceNamespaces(ServiceDescription description, Properties properties)
		throws CreationExtensionException {
		String schemaDir = getServiceSchemaDir(properties);
		NamespacesType namespaces = description.getNamespaces();
		if (namespaces == null) {
			namespaces = new NamespacesType();
		}
		// add some namespaces to the service
		List dsNamespaces = new ArrayList(Arrays.asList(namespaces.getNamespace()));
		NamespaceType queryNamespace = null;
		NamespaceType resultNamespace = null;
		NamespaceType resultRestrictionNamespace = null;
		NamespaceType dsMetadataNamespace = null;
		NamespaceType dsExceptionsNamespace = null;
		NamespaceType cagridMdNamespace = null;
		try {
			// query namespace
			queryNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
				+ DataServiceConstants.CQL_QUERY_SCHEMA);
			queryNamespace.setLocation("./" + DataServiceConstants.CQL_QUERY_SCHEMA);
			// query result namespace
			resultNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
				+ DataServiceConstants.CQL_RESULT_SET_SCHEMA);
			resultRestrictionNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
				+ CQLResultTypesGenerator.getResultTypeXSDFileName(getDataService(description)));
			resultNamespace.setLocation("./" + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
			// ds metadata namespace
			dsMetadataNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
				+ DataServiceConstants.DATA_METADATA_SCHEMA);
			dsMetadataNamespace.setLocation("./" + DataServiceConstants.DATA_METADATA_SCHEMA);
			// ds exceptions namespace
			dsExceptionsNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
				+ DataServiceConstants.DATA_SERVICE_EXCEPTIONS_SCHEMA);
			dsExceptionsNamespace.setLocation("./" + DataServiceConstants.DATA_SERVICE_EXCEPTIONS_SCHEMA);
			// caGrid metadata namespace
			cagridMdNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
				+ DataServiceConstants.CAGRID_METADATA_SCHEMA);
			cagridMdNamespace.setLocation("./" + DataServiceConstants.CAGRID_METADATA_SCHEMA);
		} catch (MobiusException ex) {
			throw new CreationExtensionException("Error creating namespace for data service: " + ex.getMessage(), ex);
		}
		// prevent the metadata beans from being generated
		cagridMdNamespace.setGenerateStubs(Boolean.FALSE);
		dsExceptionsNamespace.setGenerateStubs(Boolean.FALSE);

		// set package mappings for result restriction types
		resultRestrictionNamespace.setPackageName(description.getServices().getService(0).getPackageName()
			+ ".cqlresulttypes");

		// set package mappings for exceptions
		dsExceptionsNamespace.setPackageName(DataServiceConstants.DATA_SERVICE_PACKAGE + ".faults");
		// add those new namespaces to the list of namespace types
		dsNamespaces.add(queryNamespace);
		dsNamespaces.add(resultNamespace);
		dsNamespaces.add(resultRestrictionNamespace);
		dsNamespaces.add(dsMetadataNamespace);
		dsNamespaces.add(dsExceptionsNamespace);
		dsNamespaces.add(cagridMdNamespace);
		// add the namespaces back to the service description
		NamespaceType[] nsArray = new NamespaceType[dsNamespaces.size()];
		dsNamespaces.toArray(nsArray);
		namespaces.setNamespace(nsArray);
		description.setNamespaces(namespaces);
	}


	private ServiceType getDataService(ServiceDescription serviceDescription) throws CreationExtensionException {
		String serviceName = serviceDescription.getServices().getService(0).getName();
		ServiceType mainService = CommonTools.getService(serviceDescription.getServices(), serviceName);
		if (mainService == null) {
			throw new CreationExtensionException("No service could be located!");
		}
		return mainService;
	}


	private void addQueryMethod(ServiceDescription description, ServiceType service) throws CreationExtensionException {
		MethodType queryMethod = new MethodType();
		queryMethod.setName(DataServiceConstants.QUERY_METHOD_NAME);
		// get namespaces needed out of the service description
		NamespaceType queryNamespace = getNamespaceType(description, DataServiceConstants.CQL_QUERY_URI);
		// method input parameters
		MethodTypeInputs inputs = new MethodTypeInputs();
		MethodTypeInputsInput queryInput = new MethodTypeInputsInput();
		queryInput.setName(DataServiceConstants.QUERY_METHOD_PARAMETER_NAME);
		queryInput.setIsArray(false);
		QName queryQname = new QName(queryNamespace.getNamespace(), queryNamespace.getSchemaElement(0).getType());
		queryInput.setQName(queryQname);
		inputs.setInput(new MethodTypeInputsInput[]{queryInput});
		queryMethod.setInputs(inputs);
		// method output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setIsArray(false);
		output.setQName(DataServiceConstants.CQL_RESULT_COLLECTION_QNAME);
		queryMethod.setOutput(output);
		// exceptions on query method
		MethodTypeExceptions queryExceptions = new MethodTypeExceptions();
		MethodTypeExceptionsException qpException = new MethodTypeExceptionsException(
			DataServiceConstants.QUERY_PROCESSING_EXCEPTION_NAME, DataServiceConstants.QUERY_PROCESSING_EXCEPTION_QNAME);
		MethodTypeExceptionsException mqException = new MethodTypeExceptionsException(
			DataServiceConstants.MALFORMED_QUERY_EXCEPTION_NAME, DataServiceConstants.MALFORMED_QUERY_EXCEPTION_QNAME);
		queryExceptions.setException(new MethodTypeExceptionsException[]{qpException, mqException});
		queryMethod.setExceptions(queryExceptions);
		// query method is imported
		MethodTypeImportInformation importInfo = new MethodTypeImportInformation();
		importInfo.setNamespace(DataServiceConstants.DATA_SERVICE_NAMESPACE);
		importInfo.setPackageName(DataServiceConstants.DATA_SERVICE_PACKAGE);
		importInfo.setPortTypeName(DataServiceConstants.DATA_SERVICE_PORT_TYPE_NAME);
		importInfo.setWsdlFile("DataService.wsdl");
		importInfo.setInputMessage(new QName(DataServiceConstants.DATA_SERVICE_NAMESPACE, "QueryRequest"));
		importInfo.setOutputMessage(new QName(DataServiceConstants.DATA_SERVICE_NAMESPACE, "QueryResponse"));
		queryMethod.setIsImported(true);
		queryMethod.setImportInformation(importInfo);
		// query method is provided
		MethodTypeProviderInformation providerInfo = new MethodTypeProviderInformation();
		providerInfo.setProviderClass(DataServiceProviderImpl.class.getName());
		queryMethod.setProviderInformation(providerInfo);
		queryMethod.setIsProvided(true);
		// add the query method to the service
		MethodType[] methods = service.getMethods().getMethod();
		if (methods != null) {
			MethodType[] tmpMethods = new MethodType[methods.length + 1];
			System.arraycopy(methods, 0, tmpMethods, 0, methods.length);
			tmpMethods[tmpMethods.length - 1] = queryMethod;
			methods = tmpMethods;
		} else {
			methods = new MethodType[]{queryMethod};
		}
		service.getMethods().setMethod(methods);
	}


	private void copyDataServiceLibraries(Properties props) throws CreationExtensionException {
		String toDir = getServiceLibDir(props);
		File directory = new File(toDir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		// from the lib directory
		File libDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "lib");
		File[] libs = libDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				String name = pathname.getName();
				return (name.endsWith(".jar") && (name.startsWith("caGrid-1.0-data")
					|| name.startsWith("caGrid-1.0-core") || name.startsWith("caGrid-1.0-caDSR")
					|| name.startsWith("caGrid-1.0-metadata") || name.startsWith("castor") || name.startsWith("client")
					|| name.startsWith("hibernate") || name.startsWith("spring") || name.startsWith("cglib")));
			}
		});
		File[] copiedLibs = new File[libs.length];
		try {
			if (libs != null) {
				for (int i = 0; i < libs.length; i++) {
					File outFile = new File(toDir + File.separator + libs[i].getName());
					copiedLibs[i] = outFile;
					Utils.copyFile(libs[i], outFile);
				}
			}
		} catch (Exception ex) {
			throw new CreationExtensionException("Error copying data service libraries: " + ex.getMessage(), ex);
		}
		try {
			modifyClasspathFile(copiedLibs, props);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error modifying eclipse .classpath file: " + ex.getMessage(), ex);
		}
	}


	private String getServiceSchemaDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "schema"
			+ File.separator + props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	}


	private String getServiceLibDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
	}


	private void modifyClasspathFile(File[] libs, Properties props) throws Exception {
		File classpathFile = new File(props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
			+ File.separator + ".classpath");
		if (classpathFile.exists()) {
			ExtensionUtilities.syncEclipseClasspath(classpathFile, libs);
		} else {
			log.warn("The eclipse classpath file " + classpathFile.getAbsolutePath()
				+ " was not found.  Was it deleted?");
		}
	}


	private void modifyServiceProperties(ServiceDescription desc) throws CreationExtensionException {
		// does the query processor class property exist?
		if (!CommonTools.servicePropertyExists(desc, DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY)) {
			CommonTools.setServiceProperty(desc, DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, "", false);
		} else {
			try {
				String value = CommonTools.getServicePropertyValue(desc,
					DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
				System.out.println(DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY
					+ " property is already defined as " + value);
			} catch (Exception ex) {
				// ?
			}
		}
		CommonTools.setServiceProperty(desc, DataServiceConstants.CQL_VALIDATOR_CLASS, DEFAULT_CQL_VALIDATOR_CLASS,
			false);
		CommonTools.setServiceProperty(desc, DataServiceConstants.DOMAIN_MODEL_VALIDATOR_CLASS,
			DEFAULT_DOMAIN_MODEL_VALIDATOR, false);
		CommonTools.setServiceProperty(desc, DataServiceConstants.VALIDATE_CQL_FLAG, String.valueOf(false), false);
		CommonTools.setServiceProperty(desc, DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG, String.valueOf(false),
			false);
	}


	private NamespaceType getNamespaceType(ServiceDescription description, String nsUri) {
		NamespaceType[] namespaces = description.getNamespaces().getNamespace();
		for (int i = 0; i < namespaces.length; i++) {
			if (namespaces[i].getNamespace().equals(nsUri)) {
				return namespaces[i];
			}
		}
		return null;
	}


	private void processFeatures(ServiceDescription desc, ServiceType service, Properties serviceProps)
		throws CreationExtensionException {
		ExtensionTypeExtensionData extensionData = getExtensionData(desc);
		ServiceFeatures features = null;
		try {
			features = ExtensionDataUtils.getExtensionData(extensionData).getServiceFeatures();
		} catch (Exception ex) {
			ex.printStackTrace(); // TODO: remove me
			throw new CreationExtensionException("Error getting service features: " + ex.getMessage(), ex);
		}
		if (features != null) {
			// ws-enumeration
			if (features.isUseWsEnumeration()) {
				FeatureCreator wsEnumCreator = new WsEnumerationFeatureCreator(desc, service, serviceProps);
				wsEnumCreator.addFeature();
			}
		} else {
			log.warn("No data service features information could be found!");
		}
	}


	private ExtensionTypeExtensionData getExtensionData(ServiceDescription desc) {
		for (int i = 0; i < desc.getExtensions().getExtension().length; i++) {
			ExtensionType ext = desc.getExtensions().getExtension(i);
			if (ext.getName().equals("data")) {
				if (ext.getExtensionData() == null) {
					ext.setExtensionData(new ExtensionTypeExtensionData());
				}
				return ext.getExtensionData();
			}
		}
		return null;
	}
}
