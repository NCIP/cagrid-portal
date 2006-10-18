package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.mapping.ClassToQname;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

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
			AdditionalLibraries additionalLibs = null;
			try {
				additionalLibs = ExtensionDataUtils.getExtensionData(data).getAdditionalLibraries();
			} catch (Exception ex) {
				throw new CodegenExtensionException("Error retrieving extension data");
			}
			if (additionalLibs != null && additionalLibs.getJarName() != null) {
				for (int i = 0; i < additionalLibs.getJarName().length; i++) {
					String jarFilename = additionalLibs.getJarName(i);
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
	
	
	protected void generateClassToQnameMapping(ExtensionTypeExtensionData extDesc, ServiceInformation info)
		throws CodegenExtensionException {
		try {
			// load the caDSR package to namespace mapping information
			Data data = ExtensionDataUtils.getExtensionData(extDesc);
			CadsrInformation cadsrInfo = data.getCadsrInformation();
			if (cadsrInfo != null) {
				Mappings mappings = new Mappings();
				List classMappings = new ArrayList();
				for (int pack = 0; cadsrInfo.getPackages() != null && pack < cadsrInfo.getPackages().length; pack++) {
					CadsrPackage currentPackage = cadsrInfo.getPackages(pack);
					for (int clazz = 0; currentPackage.getCadsrClass() != null 
						&& clazz < currentPackage.getCadsrClass().length; clazz++) {
						ClassMapping map = currentPackage.getCadsrClass(clazz);
						ClassToQname toQname = new ClassToQname();
						toQname.setClassName(currentPackage.getName() + "." + map.getClassName());
						toQname.setQname((new QName(currentPackage.getMappedNamespace(), map.getElementName()).toString()));
						classMappings.add(toQname);
					}
				}
				ClassToQname[] mapArray = new ClassToQname[classMappings.size()];
				classMappings.toArray(mapArray);
				mappings.setMapping(mapArray);
				// create the filename where the mapping will be stored
				String mappingFilename = info.getBaseDirectory().getAbsolutePath() + File.separator + "etc" 
					+ File.separator + DataServiceConstants.CLASS_TO_QNAME_XML;
				// serialize the mapping to that file
				Utils.serializeDocument(mappingFilename, mappings, DataServiceConstants.MAPPING_QNAME);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CodegenExtensionException("Error generating class to QName mapping", ex);
		}		
	}
}
