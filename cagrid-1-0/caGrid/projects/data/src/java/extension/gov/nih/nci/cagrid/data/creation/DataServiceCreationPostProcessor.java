package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.common.DataServiceConstants;
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
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.utils.TemplateUtils;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
		copySchema(DataServiceConstants.CQL_QUERY_SCHEMA, schemaDir);
		copySchema(DataServiceConstants.CQL_RESULT_SET_SCHEMA, schemaDir);
		copySchema(DataServiceConstants.COMMON_METADATA_SCHEMA, schemaDir);
		copySchema(DataServiceConstants.DATA_METADATA_SCHEMA, schemaDir);
		copySchema(DataServiceConstants.CADSR_DOMAIN_SCHEMA, schemaDir);
		copySchema(DataServiceConstants.CADSR_UMLPROJECT_SCHEMA, schemaDir);
		// copy libraries for data services into the new DS's lib directory
		copyLibraries(props);
		// namespaces
		System.out.println("Modifying namespace definitions");
		NamespacesType namespaces = description.getNamespaces();
		if (namespaces == null) {
			namespaces = new NamespacesType();
		}
		// add three namespaces to the service
		NamespaceType[] dsNamespaces = new NamespaceType[namespaces.getNamespace().length + 3];
		System.arraycopy(namespaces.getNamespace(), 0, dsNamespaces, 0, namespaces.getNamespace().length);
		// query namespace
		dsNamespaces[dsNamespaces.length - 3] = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.CQL_QUERY_SCHEMA);
		dsNamespaces[dsNamespaces.length - 3].setLocation("." + File.separator + DataServiceConstants.CQL_QUERY_SCHEMA);
		// query result namespace
		dsNamespaces[dsNamespaces.length - 2] = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
		dsNamespaces[dsNamespaces.length - 2].setLocation("." + File.separator + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
		// ds metadata namespace
		dsNamespaces[dsNamespaces.length - 1] = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.DATA_METADATA_SCHEMA);
		dsNamespaces[dsNamespaces.length - 1].setLocation("." + File.separator + DataServiceConstants.DATA_METADATA_SCHEMA);
		namespaces.setNamespace(dsNamespaces);
		description.setNamespaces(namespaces);
		// add the metadata namespace to the nsexcludes property
		System.out.println("Excludig metadata namespace from processing");
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
		QName queryQname = new QName(dsNamespaces[dsNamespaces.length - 3].getNamespace(), 
			dsNamespaces[dsNamespaces.length - 3].getSchemaElement(0).getType());
		queryInput.setQName(queryQname);
		inputs.setInput(new MethodTypeInputsInput[] {queryInput});
		queryMethod.setInputs(inputs);
		// method output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setIsArray(false);
		QName resultSetQName = new QName(dsNamespaces[dsNamespaces.length - 2].getNamespace(),
			dsNamespaces[dsNamespaces.length - 2].getSchemaElement(0).getType());
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
				return (name.endsWith(".jar") && name.startsWith("caGrid-data-1.0"));
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
		String classpathText = XMLUtilities.elementToString(cpElement);
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