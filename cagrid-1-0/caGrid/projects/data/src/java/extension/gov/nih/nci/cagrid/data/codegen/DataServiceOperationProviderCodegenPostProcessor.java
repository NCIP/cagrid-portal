package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.utilities.CastorMappingUtil;
import gov.nih.nci.cagrid.data.utilities.WsddUtil;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;


/**
 * DataServiceCodegenPostProcessor Post-processor for dataservice code
 * generation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Mar 29, 2006
 * @version $Id$
 */
public class DataServiceOperationProviderCodegenPostProcessor extends BaseCodegenPostProcessorExtension {

	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		// add the necessary jars to the eclipse .classpath
		modifyEclipseClasspath(desc, info);
		
		// get the data service extension data
		Data extensionData = getExtensionData(desc, info);

		// create the XSD with the group of allowable return types for the service
		ResultTypeGeneratorInformation typeInfo = new ResultTypeGeneratorInformation();
		typeInfo.setServiceInfo(info);
		typeInfo.setCadsrInfo(extensionData.getCadsrInformation());
		CQLResultTypesGenerator.generateCQLResultTypesXSD(typeInfo);
		
		// create the class to QName mapping
		generateClassToQnameMapping(extensionData, info);
		
		// if the service has a caCORE sdk data source, the namespaces for every
		// object in the domain model probably need fixed
		if (extensionData.getServiceFeatures() != null 
			&& extensionData.getServiceFeatures().isUseSdkDataSource()) {
			rebuildCastorMappings(extensionData, info);
		}
		
		// handle service feature modifications
		if (extensionData.getServiceFeatures() != null &&
			extensionData.getServiceFeatures().isUseBdt()) {
			BDTFeatureCodegen bdtCodegen = new BDTFeatureCodegen(
				info, info.getServices().getService(0), info.getIntroduceServiceProperties());
			bdtCodegen.codegenFeature();
		}
	}
	
	
	private Data getExtensionData(ServiceExtensionDescriptionType desc, ServiceInformation info) 
		throws CodegenExtensionException {
		ExtensionTypeExtensionData extData = ExtensionTools.getExtensionData(desc, info);
		Data data = null;
		try {
			data = ExtensionDataUtils.getExtensionData(extData);
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error getting extension data: " + ex.getMessage(), ex);
		}		
		return data;
	}
	
	
	private void rebuildCastorMappings(Data extensionData, ServiceInformation info) throws CodegenExtensionException {
		// ensure the original castor mapping file from the client.jar
		// has been extracted to the service's base directory
		File mappingFile = new File(CastorMappingUtil.getCustomCastorMappingFileName(info));
		if (!mappingFile.exists()) {
			throw new CodegenExtensionException("Castor mapping file " + mappingFile.getAbsolutePath() + " not found");
		}
		// read in the file
		String mappingText = null;
		try {
			mappingText = Utils.fileToStringBuffer(mappingFile).toString();
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error reading castor mapping file: " + ex.getMessage(), ex);
		}
		// for each package in the extension data, fix the namespace mapping
		CadsrInformation cadsrInfo = extensionData.getCadsrInformation();
		if (cadsrInfo != null) {
			CadsrPackage[] packages = cadsrInfo.getPackages();
			try {
				for (int i = 0; packages != null && i < packages.length; i++) {
					mappingText = CastorMappingUtil.changeNamespaceOfPackage(mappingText, 
						packages[i].getName(), packages[i].getMappedNamespace());
				}
			} catch (Exception ex) {
				throw new CodegenExtensionException("Error changing namespaces in castor mapping: " + ex.getMessage(), ex);
			}
		}
		// write the mapping back out to disk
		try {
			Utils.stringBufferToFile(new StringBuffer(mappingText), mappingFile.getAbsolutePath());
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error saving castor mapping to disk: " + ex.getMessage(), ex);
		}
		
		// change the castor mapping property in the client-config.wsdd and
		// the server-config.wsdd files.
		String mainServiceName = info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		ServiceType mainService = CommonTools.getService(info.getServices(), mainServiceName);
		String servicePackageName = mainService.getPackageName();
		String packageDir = servicePackageName.replace('.', File.separatorChar);
		// find the client source directory, where the client-config will be located
		File clientConfigFile = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "src" 
			+ File.separator + packageDir + File.separator + "client" + File.separator + "client-config.wsdd");
		if (!clientConfigFile.exists()) {
			throw new CodegenExtensionException("Client config file " + clientConfigFile.getAbsolutePath() + " not found!");
		}
		// fine the server-config.wsdd, located in the service's root directory
		File serverConfigFile = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "server-config.wsdd");
		if (!serverConfigFile.exists()) {
			throw new CodegenExtensionException("Server config file " + serverConfigFile.getAbsolutePath() + " not found!");
		}
		
		// edit the client file
		try {
			WsddUtil.setGlobalClientParameter(clientConfigFile.getAbsolutePath(), 
				DataServiceConstants.CASTOR_MAPPING_WSDD_PARAMETER, 
				CastorMappingUtil.getCustomCastorMappingName(info));
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error setting castor mapping parameter in client-config.wsdd: " + ex.getMessage(), ex);
		}
		// edit the server file
		try {
			WsddUtil.setServiceParameter(serverConfigFile.getAbsolutePath(),
				info.getServices().getService(0).getName(), DataServiceConstants.CASTOR_MAPPING_WSDD_PARAMETER,
				CastorMappingUtil.getCustomCastorMappingName(info));
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error setting castor mapping parameter in server-config.wsdd: " + ex.getMessage(), ex);
		}
	}
}
