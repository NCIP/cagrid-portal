package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.util.List;

import org.apache.axis.message.MessageElement;

/** 
 *  DataServiceCodegenPostProcessor
 *  Post-processor for dataservice code generation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 29, 2006 
 * @version $Id$ 
 */
public class DataServiceCodegenPostProcessor implements CodegenExtensionPostProcessor {
	public static final String METHOD_START = "public " + DataServiceConstants.QUERY_METHOD_RETURN_TYPE + " " 
		+ DataServiceConstants.QUERY_METHOD_NAME + "(" + DataServiceConstants.QUERY_METHOD_PARAMETER_TYPE + " " 
		+ DataServiceConstants.QUERY_METHOD_PARAMETER_NAME + ")";
	
	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info) throws CodegenExtensionException {
		modifyQueryMethod(desc, info);
	}
	
	
	private void modifyQueryMethod(ServiceExtensionDescriptionType desc, ServiceInformation info) throws CodegenExtensionException {
		// get the processor implementation out of the service properties
		String implementationClassName = getQueryProcesorClass(desc, info);
		if (implementationClassName == null || implementationClassName.length() == 0) {
			System.out.println("No CQL Processor implementation specified");
			return;
		}
		
		// Find the service implementation file
		File serviceSrcDir = new File(info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "src");
		final String implFileName = info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME) + "Impl.java";
		// search the service dir for the impl file
		/*
		File[] implFiles = serviceSrcDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().equals(implFileName);
			}
		});
		*/
		List files = Utils.recursiveListFiles(serviceSrcDir, new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().equals(implFileName);
			}
		});
		File[] implFiles = new File[files.size()];
		files.toArray(implFiles);
		File implFile = null;
		if (implFiles != null && implFiles.length != 0) {
			implFile = implFiles[0];
		} else {
			// no impl file found
			throw new CodegenExtensionException("No service implementation found: " + implFileName + " in " + serviceSrcDir.getAbsolutePath());
		}
		
		// read the file in to a string buffer
		StringBuffer implementation = null;
		try {
			implementation = Utils.fileToStringBuffer(implFile);
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error reading implementation file: " + ex.getMessage(), ex);
		}
		
		// put the implementation in the class
		dropInImplementation(implementation, implementationClassName);
		
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
	
	
	private void dropInImplementation(StringBuffer implClass, String implClassName) {
		int startIndex = implClass.indexOf(METHOD_START);
		// move to the end of the signature
		startIndex = implClass.indexOf("{", startIndex);
		startIndex++;
		// find the end of the method body
		int endIndex = implClass.indexOf("}", startIndex);
		// trim out the existing method body
		implClass.delete(startIndex, endIndex);
		// insert the new method body
		StringBuffer body = new StringBuffer();
		body.append("\n");
		body.append("\t\t").append("gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;").append("\n");
		body.append("\t\t").append("try {").append("\n");
		body.append("\t\t\t").append("processor = new ").append(implClassName).append("();").append("\n");
		body.append("\t\t").append("} catch (gov.nih.nci.cagrid.data.InitializationException ex) {").append("\n");
		body.append("\t\t\t").append("throw new gov.nih.nci.cagrid.data.QueryProcessingException(\"Error initializing the query processor: \" + ex.getMessage(), ex);").append("\n");
		body.append("\t\t").append("}").append("\n");
		body.append("\t\t").append("return processor.processQuery(cqlQuery);").append("\n");
		implClass.insert(startIndex, body);
	}
	
	
	private String getQueryProcesorClass(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		ExtensionTypeExtensionData data = getExtensionData(desc, info);
		MessageElement[] dataEntries = data.get_any();
		for (int i = 0; dataEntries != null && i < dataEntries.length; i++) {
			if (dataEntries[i].getLocalName().equals(DataServiceConstants.QUERY_PROCESSOR_ELEMENT_NAME)) {
				String queryProcessorClass = dataEntries[i].getValue();
				return queryProcessorClass;
			}
		}
		return null;
	}
	
	
	private ExtensionTypeExtensionData getExtensionData(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		String extensionName = desc.getName();
		ExtensionType[] extensions = info.getServiceDescriptor().getExtensions().getExtension();
		for (int i = 0; extensions != null && i < extensions.length; i++) {
			if (extensions[i].getName().equals(extensionName)) {
				if (extensions[i].getExtensionData() == null) {
					extensions[i].setExtensionData(new ExtensionTypeExtensionData());
				}
				return extensions[i].getExtensionData();
			}
		}
		return null;
	}
}
