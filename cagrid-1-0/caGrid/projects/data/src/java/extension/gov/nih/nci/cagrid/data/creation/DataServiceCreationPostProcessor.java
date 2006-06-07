package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
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
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.utils.TemplateUtils;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription;
import gov.nih.nci.cagrid.metadata.service.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.tools.common.viewer.XSDFileFilter;

/** 
 *  DataServiceCreationPostProcessor
 *  Creation post-processor for data services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 29, 2006 
 * @version $Id$ 
 */
public class DataServiceCreationPostProcessor implements CreationExtensionPostProcessor {
	
	public void postCreate(ServiceDescription serviceDescription, Properties serviceProperties) throws CreationExtensionException {
		// apply data service requirements to it
		try {
			System.out.println("Adding data service components to template");
			makeDataService(serviceDescription, serviceProperties);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException("Error adding data service components to template!", ex);
		}
	}
	
	
	private void makeDataService(ServiceDescription description, Properties props) throws Exception {
		// get the data service itself
		String serviceName = props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		ServiceType dataService = CommonTools.getService(description.getServices(), serviceName);
		
		// grab cql query and result set schemas and move them into the service's directory
		String schemaDir = getServiceSchemaDir(props);
		System.out.println("Copying schemas to " + schemaDir);
		File extensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data" + File.separator + "schema"); 
		List schemaFiles = Utils.recursiveListFiles(extensionSchemaDir, new XSDFileFilter());
		for (int i = 0; i < schemaFiles.size(); i++) {
			File schemaFile = (File) schemaFiles.get(i);
			String subname = schemaFile.getCanonicalPath().substring(extensionSchemaDir.getCanonicalPath().length() + File.separator.length());
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
		NamespaceType queryNamespace = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.CQL_QUERY_SCHEMA);
		queryNamespace.setLocation("." + File.separator + DataServiceConstants.CQL_QUERY_SCHEMA);
		// query result namespace
		NamespaceType resultNamespace = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
		resultNamespace.setLocation("." + File.separator + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
		// ds metadata namespace
		NamespaceType dsMetadataNamespace = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.DATA_METADATA_SCHEMA);
		dsMetadataNamespace.setLocation("." + File.separator + DataServiceConstants.DATA_METADATA_SCHEMA);
		// caGrid metadata namespace
		NamespaceType cagridMdNamespace = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.CAGRID_METADATA_SCHEMA);
		cagridMdNamespace.setLocation("." + File.separator + DataServiceConstants.CAGRID_METADATA_SCHEMA);
		cagridMdNamespace.setGenerateStubs(Boolean.FALSE); // prevent these beans from being built!
		// add those new namespaces to the list of namespace types
		dsNamespaces.add(queryNamespace);
		dsNamespaces.add(resultNamespace);
		dsNamespaces.add(dsMetadataNamespace);
		dsNamespaces.add(cagridMdNamespace);
		NamespaceType[] nsArray = new NamespaceType[dsNamespaces.size()];
		dsNamespaces.toArray(nsArray);
		namespaces.setNamespace(nsArray);
		description.setNamespaces(namespaces);
		// add the metadata namespace to the nsexcludes property
		System.out.println("Excluding metadata namespace from processing");
		String excludes = props.getProperty(IntroduceConstants.INTRODUCE_NS_EXCLUDES);
		Set excludeNamespaces = new HashSet();
		File dataSchemaFile = new File(schemaDir + File.separator + DataServiceConstants.DATA_METADATA_SCHEMA);
		TemplateUtils.walkSchemasGetNamespaces(dataSchemaFile.getCanonicalPath(), excludeNamespaces);
		Iterator excludeIter = excludeNamespaces.iterator();
		while (excludeIter.hasNext()) {
			String namespace = (String) excludeIter.next();
			excludes += " -x " + namespace;
		}
		props.setProperty(IntroduceConstants.INTRODUCE_NS_EXCLUDES, excludes);
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
		QName queryQname = new QName(queryNamespace.getNamespace(), 
			queryNamespace.getSchemaElement(0).getType());
		queryInput.setQName(queryQname);
		inputs.setInput(new MethodTypeInputsInput[] {queryInput});
		queryMethod.setInputs(inputs);
		// method output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setIsArray(false);
		QName resultSetQName = new QName(resultNamespace.getNamespace(),
			resultNamespace.getSchemaElement(0).getType());
		output.setQName(resultSetQName);
		queryMethod.setOutput(output);
		// exceptions on query method
		MethodTypeExceptions queryExceptions = new MethodTypeExceptions();
		MethodTypeExceptionsException[] exceptions = {
			new MethodTypeExceptionsException(DataServiceConstants.QUERY_METHOD_EXCEPTIONS[0]),
			new MethodTypeExceptionsException(DataServiceConstants.QUERY_METHOD_EXCEPTIONS[1])
		};
		queryExceptions.setException(exceptions);
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
		
		// add the service metadata
		addServiceMetadata(description);
	}
	
	
	private void addServiceMetadata(ServiceDescription desc) {
		ResourcePropertyType serviceMetadata = new ResourcePropertyType();
		serviceMetadata.setPopulateFromFile(false); // no metadata file yet...
		serviceMetadata.setRegister(true);
		serviceMetadata.setQName(DataServiceConstants.SERVICE_METADATA_QNAME);
		ServiceMetadata smd = new ServiceMetadata();
		ServiceMetadataServiceDescription des = new ServiceMetadataServiceDescription();
		Service service = new Service();
		des.setService(service);
		smd.setServiceDescription(des);
		ResourcePropertiesListType propsList = desc.getServices().getService()[0].getResourcePropertiesList();
		if (propsList == null) {
			propsList = new ResourcePropertiesListType();
			desc.getServices().getService()[0].setResourcePropertiesList(propsList);
		}
		ResourcePropertyType[] metadataArray = propsList.getResourceProperty();
		if (metadataArray == null || metadataArray.length == 0) {
			metadataArray = new ResourcePropertyType[] {serviceMetadata};
		} else {
			ResourcePropertyType[] tmpArray = new ResourcePropertyType[metadataArray.length + 1];
			System.arraycopy(metadataArray, 0, tmpArray, 0, metadataArray.length);
			tmpArray[metadataArray.length] = serviceMetadata;
			metadataArray = tmpArray;
		}
		propsList.setResourceProperty(metadataArray);
	}
	
	
	private String getServiceSchemaDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator 
			+ "schema" + File.separator + props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	}
	
	
	private String getServiceLibDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
	}
	
	
	private void copySchema(String schemaName, String outputDir) throws Exception {
		File schemaFile = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data" + File.separator + "schema" + File.separator + schemaName);
		System.out.println("Copying schema from " + schemaFile.getAbsolutePath());
		File outputFile = new File(outputDir + File.separator + schemaName);
		System.out.println("Saving schema to " + outputFile.getAbsolutePath());
		copyFile(schemaFile, outputFile);
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
				return (name.endsWith(".jar") && (
					name.startsWith("caGrid-data-1.0") || name.startsWith("castor") ||
					name.startsWith("client") || name.startsWith("caGrid-caDSR") ||
					name.startsWith("caGrid-metadata") || name.startsWith("caGrid-core")));
			}
		});
		if (libs != null) {
			for (int i = 0; i < libs.length; i++) {
				File outFile = new File(toDir + File.separator + libs[i].getName());
				copyFile(libs[i], outFile);
			}
		}
		modifyClasspathFile(libs, props);
	}
	
	
	private void modifyClasspathFile(File[] libs, Properties props) throws Exception {
		String classpathFilename = props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + ".classpath";
		Element cpElement = XMLUtilities.fileNameToDocument(classpathFilename).getRootElement();
		for (int i = 0; i < libs.length; i++) {
			Element entryElement = new Element("classpathentry");
			entryElement.setAttribute("kind", "lib");
			entryElement.setAttribute("path", "lib" + File.separator + libs[i].getName());
			cpElement.addContent(entryElement);
		}
		// write the classpath back out
		String classpathText = XMLUtilities.formatXML(XMLUtilities.elementToString(cpElement));
		FileWriter writer = new FileWriter(classpathFilename);
		writer.write(classpathText);
		writer.flush();
		writer.close();
	}
	
	
	private void copyFile(File inputFile, File outputFile) throws FileNotFoundException, IOException {
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
		byte[] buff = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(buff)) != -1) {
			outputStream.write(buff, 0, len);
		}
		inputStream.close();
		outputStream.flush();
		outputStream.close();
	}
	
	
	private Set getSchemaNamespaces(File schemaDir) throws CreationExtensionException {
		Set namespaces = new HashSet();
		List xsdFiles = Utils.recursiveListFiles(schemaDir, new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".xsd");
			}
		});
		Iterator fileIter = xsdFiles.iterator();
		while (fileIter.hasNext()) {
			String filename = ((File) fileIter.next()).getAbsolutePath();
			System.out.println("Looking at schema " + filename);
			try {
				Document schema = XMLUtilities.fileNameToDocument(filename);
				String targetNs = schema.getRootElement().getAttributeValue("targetNamespace");
				if (namespaces.add(targetNs)) {
					System.out.println("Adding namespace " + targetNs);
				}
				List importEls = schema.getRootElement().getChildren("import",
					schema.getRootElement().getNamespace(IntroduceConstants.W3CNAMESPACE));
				for (int i = 0; i < importEls.size(); i++) {
					org.jdom.Element importEl = (org.jdom.Element) importEls.get(i);
					String namespace = importEl.getAttributeValue("namespace");
					if (namespaces.add(namespace)) {
						System.out.println("Adding namepace " + namespace);
					}
				}
			} catch (MobiusException ex) {
				throw new CreationExtensionException("Error parsing schema for namespaces: " + ex.getMessage(), ex);
			}
		}
		return namespaces;
	}
}