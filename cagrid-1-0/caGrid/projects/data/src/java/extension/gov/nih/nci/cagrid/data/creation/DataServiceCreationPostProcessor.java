package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.projectmobius.tools.common.viewer.XSDFileFilter;


/**
 * DataServiceCreationPostProcessor Creation post-processor for data services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Mar 29, 2006
 * @version $Id$
 */
public class DataServiceCreationPostProcessor implements CreationExtensionPostProcessor {

	public void postCreate(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CreationExtensionException {
		// apply data service requirements to it
		try {
			System.out.println("Adding data service components to template");
			makeDataService(info.getServiceDescriptor(), info.getIntroduceServiceProperties());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException(
				"Error adding data service components to template! " + ex.getMessage(), ex);
		}
		// add the proper deployment properties
		try {
			System.out.println("Adding deploy property for query processor class");
			modifyServiceProperties(info.getServiceDescriptor());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException("Error adding query processor parameter to service! "
				+ ex.getMessage(), ex);
		}
	}


	private void makeDataService(ServiceDescription description, Properties props) throws Exception {
		// get the data service itself
		String serviceName = props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		ServiceType dataService = CommonTools.getService(description.getServices(), serviceName);

		// grab cql query and result set schemas and move them into the
		// service's directory
		String schemaDir = getServiceSchemaDir(props);
		File schemaDirFile = new File(schemaDir);
		System.out.println("Copying schemas to " + schemaDir);
		File extensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "dataFS"
			+ File.separator + "schema");
		List schemaFiles = Utils.recursiveListFiles(extensionSchemaDir, new XSDFileFilter());
		for (int i = 0; i < schemaFiles.size(); i++) {
			File schemaFile = (File) schemaFiles.get(i);
			String subname = schemaFile.getCanonicalPath().substring(
				extensionSchemaDir.getCanonicalPath().length() + File.separator.length());
			copySchema(subname, schemaDir);
		}
		// copy libraries for data services into the new DS's lib directory
		copyLibraries(props);
		// namespaces
		System.out.println("Modifying namespace definitions");
		NamespacesType namespaces = description.getNamespaces();
		if (namespaces == null) {
			namespaces = new NamespacesType();
		}
		// add some namespaces to the service
		List dsNamespaces = new ArrayList(Arrays.asList(namespaces.getNamespace()));
		// query namespace
		NamespaceType queryNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ DataServiceConstants.CQL_QUERY_SCHEMA, schemaDirFile);
		queryNamespace.setLocation("./" + DataServiceConstants.CQL_QUERY_SCHEMA);
		// query result namespace
		NamespaceType resultNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ DataServiceConstants.CQL_RESULT_SET_SCHEMA, schemaDirFile);
		resultNamespace.setLocation("./" + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
		// ds metadata namespace
		NamespaceType dsMetadataNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ DataServiceConstants.DATA_METADATA_SCHEMA, schemaDirFile);
		dsMetadataNamespace.setLocation("./" + DataServiceConstants.DATA_METADATA_SCHEMA);
		// caGrid metadata namespace
		NamespaceType cagridMdNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ DataServiceConstants.CAGRID_METADATA_SCHEMA, schemaDirFile);
		cagridMdNamespace.setLocation("./" + DataServiceConstants.CAGRID_METADATA_SCHEMA);
		// prevent metadata beans from being built
		cagridMdNamespace.setGenerateStubs(Boolean.FALSE);
		// add those new namespaces to the list of namespace types
		dsNamespaces.add(queryNamespace);
		dsNamespaces.add(resultNamespace);
		dsNamespaces.add(dsMetadataNamespace);
		dsNamespaces.add(cagridMdNamespace);
		NamespaceType[] nsArray = new NamespaceType[dsNamespaces.size()];
		dsNamespaces.toArray(nsArray);
		namespaces.setNamespace(nsArray);
		description.setNamespaces(namespaces);
		// add the CQL and CQLResult namespaces to the ns excludes
		/*
		 * String excludes =
		 * props.getProperty(IntroduceConstants.INTRODUCE_NS_EXCLUDES); excludes += "
		 * -x " + DataServiceConstants.CQL_QUERY_URI; excludes += " -x " +
		 * DataServiceConstants.CQL_RESULT_SET_URI;
		 * props.setProperty(IntroduceConstants.INTRODUCE_NS_EXCLUDES,
		 * excludes);
		 */
		// query method
		System.out.println("Building query method");
		MethodsType methods = dataService.getMethods();
		if (methods == null) {
			methods = new MethodsType();
		}
		MethodType queryMethod = new MethodType();
		queryMethod.setName(DataServiceConstants.QUERY_METHOD_NAME);
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
		QName resultSetQName = new QName(resultNamespace.getNamespace(), resultNamespace.getSchemaElement(0).getType());
		output.setQName(resultSetQName);
		queryMethod.setOutput(output);
		// exceptions on query method
		MethodTypeExceptions queryExceptions = new MethodTypeExceptions();
		MethodTypeExceptionsException qpException = new MethodTypeExceptionsException("",
			DataServiceConstants.QUERY_PROCESSING_EXCEPTION_NAME, DataServiceConstants.QUERY_PROCESSING_EXCEPTION_QNAME);
		MethodTypeExceptionsException mqException = new MethodTypeExceptionsException("",
			DataServiceConstants.MALFORMED_QUERY_EXCEPTION_NAME, DataServiceConstants.MALFORMED_QUERY_EXCEPTION_QNAME);
		queryExceptions.setException(new MethodTypeExceptionsException[]{qpException, mqException});
		queryMethod.setExceptions(queryExceptions);
		// add query method to methods array
		MethodType[] dsMethods = null;
		if (methods.getMethod() != null) {
			dsMethods = new MethodType[methods.getMethod().length + 1];
			System.arraycopy(methods.getMethod(), 0, dsMethods, 0, methods.getMethod().length);
		} else {
			dsMethods = new MethodType[1];
		}
		dsMethods[dsMethods.length - 1] = queryMethod;
		methods.setMethod(dsMethods);
		dataService.setMethods(methods);

	}


	private String getServiceSchemaDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "schema"
			+ File.separator + props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	}


	private String getServiceLibDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
	}


	private void copySchema(String schemaName, String outputDir) throws Exception {
		File schemaFile = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "dataFS" + File.separator
			+ "schema" + File.separator + schemaName);
		System.out.println("Copying schema from " + schemaFile.getAbsolutePath());
		File outputFile = new File(outputDir + File.separator + schemaName);
		System.out.println("Saving schema to " + outputFile.getAbsolutePath());
		Utils.copyFile(schemaFile, outputFile);
	}


	private void copyLibraries(Properties props) throws Exception {
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
		if (libs != null) {
			for (int i = 0; i < libs.length; i++) {
				File outFile = new File(toDir + File.separator + libs[i].getName());
				copiedLibs[i] = outFile;
				Utils.copyFile(libs[i], outFile);
			}
		}
		modifyClasspathFile(copiedLibs, props);
	}


	private void modifyClasspathFile(File[] libs, Properties props) throws Exception {
		File classpathFile = new File(props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
			+ File.separator + ".classpath");
		ExtensionUtilities.syncEclipseClasspath(classpathFile, libs);
	}


	private void modifyServiceProperties(ServiceDescription desc) throws Exception {
		// does the query processor class property exist?
		if (!CommonTools.servicePropertyExists(desc, DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY)) {
			CommonTools.setServiceProperty(desc, DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, "", false, "");
		} else {
			String value = CommonTools.getServicePropertyValue(desc,
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
			System.out.println(DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY + " property is already defined as "
				+ value);
		}
		// does the server config location property exist?
		if (!CommonTools.servicePropertyExists(desc, DataServiceConstants.SERVER_CONFIG_LOCATION)) {
			CommonTools.setServiceProperty(desc, DataServiceConstants.SERVER_CONFIG_LOCATION, "server-config.wsdd",
				true, "");
		}
	}
}