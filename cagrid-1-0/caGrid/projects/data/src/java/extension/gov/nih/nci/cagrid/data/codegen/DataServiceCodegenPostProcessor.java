package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;


/**
 * DataServiceCodegenPostProcessor Post-processor for dataservice code
 * generation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Mar 29, 2006
 * @version $Id$
 */
public class DataServiceCodegenPostProcessor implements CodegenExtensionPostProcessor {
	public static final String METHOD_START = "public " + DataServiceConstants.QUERY_METHOD_RETURN_TYPE + " "
		+ DataServiceConstants.QUERY_METHOD_NAME + "(" + DataServiceConstants.QUERY_METHOD_PARAMETER_TYPE + " "
		+ DataServiceConstants.QUERY_METHOD_PARAMETER_NAME + ")";


	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		try {
			String serviceDir = info.getIntroduceServiceProperties().getProperty(
				IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR);
			// get the eclipse classpath document
			File classpathFile = new File(serviceDir + File.separator + ".classpath");
			Set libs = new HashSet();
			ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
			MessageElement qpLibsElement = ExtensionTools.getExtensionDataElement(data,
				DataServiceConstants.QUERY_PROCESSOR_ADDITIONAL_JARS_ELEMENT);
			if (qpLibsElement != null) {
				Element qpLibs = AxisJdomUtils.fromMessageElement(qpLibsElement);
				Iterator jarElemIter = qpLibs.getChildren(DataServiceConstants.QUERY_PROCESSOR_JAR_ELEMENT,
					qpLibs.getNamespace()).iterator();
				while (jarElemIter.hasNext()) {
					String jarFilename = ((Element) jarElemIter.next()).getText();
					libs.add(new File(serviceDir + File.separator + "lib" + File.separator + jarFilename));
				}
			}
			File[] libFiles = new File[libs.size()];
			libs.toArray(libFiles);
			ExtensionUtilities.syncEclipseClasspath(classpathFile, libFiles);
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error syncing eclipse .classpath file: " + ex.getMessage(), ex);
		}
		modifyQueryMethod(info);
	}


	private void modifyQueryMethod(ServiceInformation info) throws CodegenExtensionException {
		// Find the service implementation file
		File serviceSrcDir = new File(info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
			+ File.separator + "src");
		final String implFileName = info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
			+ "Impl.java";
		List files = Utils.recursiveListFiles(serviceSrcDir, new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().equals(implFileName);
			}
		});
		File[] implFiles = new File[files.size()];
		files.toArray(implFiles);
		File implFile = null;
		if (implFiles.length != 0) {
			implFile = implFiles[0];
		} else {
			// no impl file found
			throw new CodegenExtensionException("No service implementation found: " + implFileName + " in "
				+ serviceSrcDir.getAbsolutePath());
		}

		// read the file in to a string buffer
		StringBuffer implementation = null;
		try {
			implementation = Utils.fileToStringBuffer(implFile);
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error reading implementation file: " + ex.getMessage(), ex);
		}

		// put the implementation in the class
		dropInImplementation(implementation);

		// save the source file.
		try {
			implFile.delete();
			implFile.createNewFile();
			FileWriter writer = new FileWriter(implFile);
			writer.write(implementation.toString());
			writer.close();
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error writing implementation to disk: " + ex.getMessage(), ex);
		}
	}


	private void dropInImplementation(StringBuffer implClass) {
		int startIndex = implClass.indexOf(METHOD_START);
		// move to the end of the signature
		startIndex = implClass.indexOf("{", startIndex);
		startIndex++;
		// find the end of the method body
		// start counting open and closed brackets until I reach zero. That's
		// the end of the query method
		int endIndex = startIndex;
		int brackets = 1;
		while (brackets != 0) {
			char c = implClass.charAt(endIndex);
			if (c == '{') {
				brackets++;
			} else if (c == '}') {
				brackets--;
			}
			endIndex++;
		}
		// trim out the existing method body
		implClass.delete(startIndex, endIndex);
		// insert the new method body
		StringBuffer body = new StringBuffer();
		body.append("\n");
		body.append("\t\t").append("gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;").append("\n");
		body.append("\t\t").append("try {").append("\n");
		body.append("\t\t\t").append("Class qpClass = Class.forName(getConfiguration().get").append(DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY).append("());").append("\n");
		body.append("\t\t\t").append("processor = qpClass.newInstance();").append("\n");
		body.append("\t\t\t").append("processor.initialize(configurationToMap());").append("\n");
		body.append("\t\t").append("} catch (Exception ex) {").append("\n");
		// body.append("\t\t\t").append("throw new
		// gov.nih.nci.cagrid.data.QueryProcessingException(\"Error initializing
		// the query processor: \" + ex.getMessage(), ex);").append("\n");
		body.append("\t\t\t").append("return null;").append("\n");
		body.append("\t\t").append("}").append("\n");
		body.append("\t\t").append("try {").append("\n");
		body.append("\t\t\t").append("return processor.processQuery(cqlQuery);").append("\n");
		body.append("\t\t").append("} catch (Exception ex) {").append("\n");
		body.append("\t\t\t").append("return null;").append("\n");
		body.append("\t\t").append("}").append("\n");
		body.append("\t}\n\n\n");
		// do we need to add the getMapperFunction() ?
		String mapperFunc = getMapperFunction();
		if (body.indexOf(mapperFunc) != -1) {
			body.append(mapperFunc);
		}
		implClass.insert(startIndex, body);
	}


	private String getMapperFunction() {
		StringBuffer func = new StringBuffer();
		/*
		 * private java.util.Map configurationToMap() throws Exception {
		 * java.util.Map map = new java.util.HashMap(); Class configClass =
		 * getConfiguration().getClass(); for (int i = 0; i <
		 * configClass.getMethods().length; i++) { if
		 * (configClass.getMethods()[i].getName().startsWith("get")) { String
		 * value = (String)
		 * configClass.getMethods()[i].invoke(getConfiguration(), null);
		 * map.put(configClass.getMethods()[i].getName().substring(3), value); } }
		 * return map; }
		 */
		func.append("\t\t").append("private ").append(Map.class.getName()).append(
			" configurationToMap() throws Exception {").append("\n");
		func.append("\t\t\t").append(Map.class.getName()).append(" map = new ").append(HashMap.class.getName()).append(
			"();").append("\n");
		func.append("\t\t\t").append(Class.class.getName()).append(" configClass = getConfiguration().getClass();")
			.append("\n");
		func.append("\t\t\t").append("for (int i = 0; i < configClass.getMethods().length; i++) {").append("\n");
		func.append("\t\t\t\t").append("if (configClass.getMethods()[i].getName().startsWith(\"get\")) {").append("\n");
		func.append("\t\t\t\t\t").append(
			"String value = (String) configClass.getMethods()[i].invoke(getConfiguration(), null);").append("\n");
		func.append("\t\t\t\t\t").append("map.put(configClass.getMethods()[i].getName().substring(3), value);").append(
			"\n");
		func.append("\t\t\t\t").append("}\n");
		func.append("\t\t\t").append("}\n");
		func.append("return map;").append("\n");
		func.append("}\n");
		return func.toString();
	}
}
