package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * DataServiceCodegenPreProcessor Preprocessor for data service codegen
 * operations.
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created May 11, 2006
 * @version $Id$
 */
public class DataServiceCodegenPreProcessor implements CodegenExtensionPreProcessor {

	private static final String DEFAULT_DOMAIN_MODEL_XML_FILE = "domainModel.xml";
	private static final Logger LOG = Logger.getLogger(DataServiceCodegenPreProcessor.class);


	public void preCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		if (stubQueryProcessorSelected(info)) {
			addQueryProcessorStub(info, ExtensionDataUtils.getQueryProcessorStubClassName(info));
		}
		modifyMetadata(desc, info);
	}
	
	
	private boolean stubQueryProcessorSelected(ServiceInformation info) throws CodegenExtensionException {
		try {
			String selectedQpClassName = CommonTools.getServicePropertyValue(info.getServiceDescriptor(), 
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
			if (selectedQpClassName != null) {
				String stubName = ExtensionDataUtils.getQueryProcessorStubClassName(info);
				return stubName.equals(selectedQpClassName);
			}
			return true;
		} catch (Exception ex) {
			throw new CodegenExtensionException(ex.getMessage(), ex);
		}
	}
	
	
	private CadsrInformation getCadsrInformation(ServiceExtensionDescriptionType desc, ServiceInformation info) throws Exception {
		ExtensionTypeExtensionData extData = ExtensionTools.getExtensionData(desc, info);
		Data data = ExtensionDataUtils.getExtensionData(extData);
		CadsrInformation cadsrInfo = data.getCadsrInformation();
		if (cadsrInfo == null) {
			System.out.println("NO CADSR INFORMATION FOUND, USING DEFAULTS");
			cadsrInfo = new CadsrInformation();
			cadsrInfo.setNoDomainModel(true);
			data.setCadsrInformation(cadsrInfo);
			ExtensionDataUtils.storeExtensionData(extData, data);
		}
		return cadsrInfo;
	}


	private void modifyMetadata(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		String localDomainModelFilename = getDestinationDomainModelFilename(info);

		// find the service's etc directory, where the domain model goes
		String domainModelFile = info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
			+ File.separator + "etc" + File.separator + localDomainModelFilename;

		// get the caDSR information
		CadsrInformation cadsrInfo = null;
		try {
			cadsrInfo = getCadsrInformation(desc, info);
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error loading Cadsr Information from extension data", ex);
		}
		
		// get the resource property for the domain model
		ResourcePropertyType dmResourceProp = getDomainModelResourceProp(info);
		
		if (cadsrInfo.isUseSuppliedModel()) {
			// the model is already in the service's etc dir with the name specified
			// in the resource property.  Make sure the resource property has
			// the populate from file flag set, and we're done
			dmResourceProp.setPopulateFromFile(true);
		} else if (!cadsrInfo.isNoDomainModel()) {
			// the domain model is to be generated from the caDSR
			LOG.info("No domain model supplied, generating from caDSR");
			generateDomainModel(cadsrInfo, info, domainModelFile);
			// set the domain model file name
			dmResourceProp.setFileLocation(localDomainModelFilename);
		}

		// if the domainModel.xml doesn't exist, don't try to populate the
		// domain model metadata on service startup
		File dmFile = new File(domainModelFile);
		dmResourceProp.setPopulateFromFile(dmFile.exists());
	}


	/**
	 * Gets the local part of the filename from which the Domain Model resource
	 * property (metadata) will be populated at runtime.  This value is configured
	 * by the user when selecting a pre-built domain model in the data service
	 * creation GUI in Introduce, or the default (domainModel.xml) value
	 * if a domain model is to be generated from the caDSR
	 * 
	 * @param info
	 * 		The service information
	 * @return
	 * 		The local domain model xml file name
	 */
	private String getDestinationDomainModelFilename(ServiceInformation info) {
		ServiceType mainServ = info.getServiceDescriptor().getServices().getService()[0];
		if (mainServ.getResourcePropertiesList() != null
			&& mainServ.getResourcePropertiesList().getResourceProperty() != null) {
			ResourcePropertyType[] resourceProperty = mainServ.getResourcePropertiesList().getResourceProperty();
			for (int i = 0; i < resourceProperty.length; i++) {
				ResourcePropertyType rp = resourceProperty[i];
				if (rp.getQName().equals(DataServiceConstants.DOMAIN_MODEL_QNAME)) {
					String fileLocation = rp.getFileLocation();
					if (fileLocation == null || fileLocation.trim().equals("")) {
						rp.setFileLocation(DEFAULT_DOMAIN_MODEL_XML_FILE);
					}
					return rp.getFileLocation();
				}
			}
		}
		return null;
	}


	private void generateDomainModel(CadsrInformation cadsrInfo, ServiceInformation info, String domainModelFile) throws CodegenExtensionException {		
		// init the cadsr service client
		String cadsrUrl = cadsrInfo.getServiceUrl();
		LOG.info("Initializing caDSR client (URL = " + cadsrUrl + ")");
		System.out.println("Initializing caDSR client (URL = " + cadsrUrl + ")");
		CaDSRServiceClient cadsrClient = null;
		try {
			cadsrClient = new CaDSRServiceClient(cadsrUrl);
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error initializing caDSR client: " + ex.getMessage(), ex);
		}

		// create the prototype project
		Project proj = new Project();
		proj.setLongName(cadsrInfo.getProjectLongName());
		proj.setVersion(cadsrInfo.getProjectVersion());

		// sets for holding all selected classes and associations
		Set selectedClasses = new HashSet();

		// walk through the selected packages
		for (int i = 0; cadsrInfo.getPackages() != null && i < cadsrInfo.getPackages().length; i++) {
			CadsrPackage packageInfo = cadsrInfo.getPackages(i);
			String packName = packageInfo.getName();
			// get selected classes from the package
			if (packageInfo.getCadsrClass() != null) {
				for (int j = 0; j < packageInfo.getCadsrClass().length; j++) {
					selectedClasses.add(packName + "." + packageInfo.getCadsrClass(j).getClassName());
				}
			}
		}

		// get the data service's description
		ServiceType dataService = null;
		String serviceName = info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		ServiceType[] services = info.getServices().getService();
		for (int i = 0; i < services.length; i++) {
			if (services[i].getName().equals(serviceName)) {
				dataService = services[i];
				break;
			}
		}
		if (dataService == null) {
			// this REALLY should never happen...
			throw new CodegenExtensionException("No data service found in service information");
		}

		// build the domain model
		LOG.info("Contacting caDSR to build domain model.  This might take a while...");
		System.out.println("Contacting caDSR to build domain model.  This might take a while...");
		DomainModel model = null;
		try {
			// TODO; change this to use EXCLUDED associations
			String classNames[] = new String[selectedClasses.size()];
			selectedClasses.toArray(classNames);
			model = cadsrClient.generateDomainModelForClasses(proj, classNames);
			if (model == null) {
				throw new CodegenExtensionException("caDSR returned a null domain model.");
			}
			System.out.println("Created data service Domain Model!");
			LOG.info("Created data service Domain Model!");
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error connecting to caDSR for metadata: " + ex.getMessage(), ex);
		}

		LOG.debug("Serializing domain model to file " + domainModelFile);
		try {
			FileWriter domainModelFileWriter = new FileWriter(domainModelFile);
			MetadataUtils.serializeDomainModel(model, domainModelFileWriter);
			domainModelFileWriter.flush();
			domainModelFileWriter.close();
			LOG.debug("Serialized domain model");
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error serializing the domain model to disk: " 
				+ ex.getMessage(), ex);
		}

		// add the metadata to the service information as a resource property
		ResourcePropertyType domainModelResourceProperty = new ResourcePropertyType();
		domainModelResourceProperty.setPopulateFromFile(true);
		domainModelResourceProperty.setQName(DataServiceConstants.DOMAIN_MODEL_QNAME);
		domainModelResourceProperty.setRegister(true);
		ResourcePropertiesListType propertyList = dataService.getResourcePropertiesList();
		if (propertyList == null) {
			propertyList = new ResourcePropertiesListType();
		}
		ResourcePropertyType[] propertyArray = propertyList.getResourceProperty();
		if (propertyArray == null) {
			propertyArray = new ResourcePropertyType[]{domainModelResourceProperty};
		} else {
			ResourcePropertyType[] newProperties = new ResourcePropertyType[propertyArray.length];
			System.arraycopy(propertyArray, 0, newProperties, 0, propertyArray.length);
			propertyArray = newProperties;
		}
		// start packing up the resource property info
		propertyList.setResourceProperty(propertyArray);
		dataService.setResourcePropertiesList(propertyList);
	}
	
	
	/**
	 * Adds the CQL Query processor stub to the service
	 * 
	 * @param info
	 * 		The service model
	 * @param className
	 * 		The name to give to the stub class
	 * @throws CodegenExtensionException
	 */
	private void addQueryProcessorStub(ServiceInformation info, String className) throws CodegenExtensionException {
		try {
			// find / create the output directory
			int index = className.lastIndexOf('.');
			String basePackage = className.substring(0, index);
			String outSrcDir = info.getIntroduceServiceProperties().getProperty(
				IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "src";
			outSrcDir += File.separator + basePackage.replace('.', File.separatorChar);
			File outSrcDirFile = new File(outSrcDir);
			outSrcDirFile.mkdirs();
			File outSourceFile = new File(outSrcDir + File.separator + DataServiceConstants.QUERY_PROCESSOR_STUB_NAME + ".java");
			// read the code file
			InputStream codeStream = getClass().getResourceAsStream("/resources/StubCQLQueryProcessor.java");
			StringBuffer code = Utils.inputStreamToStringBuffer(codeStream);
			// build the whole java file
			StringBuffer processorStub = new StringBuffer();
			processorStub.append("package ").append(basePackage).append(";").append("\n");
			processorStub.append(code);
			// output the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(outSourceFile));
			writer.write(processorStub.toString());
			writer.flush();
			writer.close();		
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error providing stub CQL implementation: " + ex.getMessage(), ex);
		}
	}
	
	
	private ResourcePropertyType getDomainModelResourceProp(ServiceInformation info) {
		ServiceType baseService = info.getServices().getService(0);
		
		ResourcePropertyType[] typedProps = CommonTools.getResourcePropertiesOfType(
			info.getServices().getService(0), DataServiceConstants.DOMAIN_MODEL_QNAME);
		if (typedProps == null || typedProps.length == 0) {
			ResourcePropertyType dmProp = new ResourcePropertyType();
			dmProp.setQName(DataServiceConstants.DOMAIN_MODEL_QNAME);
			dmProp.setRegister(true);
			CommonTools.addResourcePropety(baseService, dmProp);
			return dmProp;
		} else {
			return typedProps[0];
		}
	}
}
