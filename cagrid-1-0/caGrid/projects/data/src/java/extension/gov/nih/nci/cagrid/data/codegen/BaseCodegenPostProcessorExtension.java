package gov.nih.nci.cagrid.data.codegen;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.axis.message.MessageElement;
import org.apache.log4j.Logger;
import org.jdom.Element;

/** 
 *  BaseCodegenPostProcessorExtension
 *  Base class for the DS codegen post processor extension
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 16, 2006 
 * @version $Id$ 
 */
public abstract class BaseCodegenPostProcessorExtension implements CodegenExtensionPostProcessor {
	private static Logger logger = Logger.getLogger(DataServiceOperationProviderCodegenPostProcessor.class);

	protected void modifyEclipseClasspath(ServiceExtensionDescriptionType desc, ServiceInformation info) throws CodegenExtensionException {
		String serviceDir = info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR);
		// get the eclipse classpath document
		File classpathFile = new File(serviceDir + File.separator + ".classpath");
		if (classpathFile.exists()) {
			logger.info("Modifying eclipse .classpath file");
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
			try {
				logger.info("Adding libraries to classpath file:");
				for (int i = 0; i < libFiles.length; i++) {
					logger.info("\t" + libFiles[i].getAbsolutePath());
				}
				ExtensionUtilities.syncEclipseClasspath(classpathFile, libFiles);
			} catch (Exception ex) {
				throw new CodegenExtensionException("Error modifying Eclipse .classpath file: " + ex.getMessage(), ex);
			}
		} else {
			logger.warn("Eclipse .classpath file " + classpathFile.getAbsolutePath() + " not found!");
		}
	}
}
