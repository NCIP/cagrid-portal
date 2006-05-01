package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.InputStreamReader;

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
		modifyQueryMethod(info);
	}
	
	
	private void modifyQueryMethod(ServiceInformation info) throws CodegenExtensionException {
		// Find the service implementation file
		File serviceSrcDir = new File(info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "src");
		final String implFileName = info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME) + "Impl.java";
		// search the service dir for the impl file
		File[] implFiles = serviceSrcDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().equals(implFileName);
			}
		});
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
		
		// read template for implementation's body
		StringBuffer template = new StringBuffer();
		try {
			BufferedReader reader =  new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("QueryTemplate.java")));
			String line = null;
			while ((line = reader.readLine()) != null) {
				template.append(line).append("\n");
			}
			reader.close();
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error reading the implementation template: " + ex.getMessage(), ex);
		}
		String body = extractMethodBody(template);
		
		// get the processor implementation out of the service properties
		String implementationClassName = info.getIntroduceServiceProperties().getProperty(DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
		// replace the placeholder in the method body
		body.replace(DataServiceConstants.QUERY_PROCESSOR_PLACEHOLDER, implementationClassName);
		
		// put the implementation in the class
		dropInImplementation(implementation, body);
		
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
	
	
	private String extractMethodBody(StringBuffer template) {
		// find the method signature's line
		int startIndex = template.indexOf(METHOD_START);
		// move to the end of the signature
		startIndex = template.indexOf("{", startIndex);
		startIndex++;
		// start counting open and closed brackets until I reach zero.  That's the end of the query method
		int endIndex = startIndex;
		int brackets = 1;
		while (brackets != 0) {
			char c = template.charAt(endIndex);
			if (c == '{') {
				brackets++;
			} else if (c == '}') {
				brackets--;
			}
			endIndex++;
		}
		endIndex--; // get off the last bracket
		String body = template.substring(startIndex, endIndex);
		return body;
	}
	
	
	private void dropInImplementation(StringBuffer implClass, String methodBody) {
		int startIndex = implClass.indexOf(METHOD_START);
		// move to the end of the signature
		startIndex = implClass.indexOf("{", startIndex);
		startIndex++;
		// insert the method body
		implClass.insert(startIndex, methodBody);
	}
}
