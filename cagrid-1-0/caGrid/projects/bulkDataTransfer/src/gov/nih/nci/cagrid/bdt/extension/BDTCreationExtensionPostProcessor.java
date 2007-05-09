package gov.nih.nci.cagrid.bdt.extension;

import gov.nih.nci.cagrid.bdt.service.BDTServiceConstants;
import gov.nih.nci.cagrid.bdt.templates.BDTResourceTemplate;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;


public class BDTCreationExtensionPostProcessor implements CreationExtensionPostProcessor {

	private ServiceInformation info;
	private Properties serviceProperties;


	public void postCreate(ServiceExtensionDescriptionType desc, ServiceInformation serviceInfo)
		throws CreationExtensionException {

		ServiceDescription serviceDescription = serviceInfo.getServiceDescriptor();
		serviceProperties = serviceInfo.getIntroduceServiceProperties();

		info = new ServiceInformation(serviceDescription, serviceProperties, new File(serviceProperties
			.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)));

		// apply BDT service requirements to it
		try {
			System.out.println("Adding BDT service components to template");
			makeBDTService(serviceDescription, serviceProperties);
			addResourceImplStub(serviceDescription, serviceProperties);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException(
				"Error adding BDT service components to template! " + ex.getMessage(), ex);
		}
		// add the proper deployment metadata
		try {
			System.out.println("Modifying metadata");
			modifyServiceProperties(serviceDescription);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException(
                "Error modifying metadata: " + ex.getMessage(), ex);
		}
	}


	private void makeBDTService(ServiceDescription description, Properties props) throws Exception {
		String schemaDir = getServiceSchemaDir(props);
		File schemaDirFile = new File(schemaDir);
		System.out.println("Copying schemas to " + schemaDir);
		File extensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "bdt"
			+ File.separator + "schema");
		File extensionDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "bdt");
		List schemaFiles = Utils.recursiveListFiles(extensionSchemaDir, new FileFilters.XSDFileFilter());
		for (int i = 0; i < schemaFiles.size(); i++) {
			File schemaFile = (File) schemaFiles.get(i);
			String subname = schemaFile.getCanonicalPath().substring(
				extensionSchemaDir.getCanonicalPath().length() + File.separator.length());
			copySchema(subname, schemaDir);
		}

		List wsdlFiles = Utils.recursiveListFiles(extensionSchemaDir, new FileFilters.WSDLFileFilter());
		for (int i = 0; i < wsdlFiles.size(); i++) {
			File wsdlFile = (File) wsdlFiles.get(i);
			String subname = wsdlFile.getCanonicalPath().substring(
				extensionSchemaDir.getCanonicalPath().length() + File.separator.length());
			copySchema(subname, schemaDir);
		}

		// copy libraries for data services into the new bdt lib directory
		copyLibraries(props);
		// namespaces
		System.out.println("Modifying namespace definitions");
		NamespacesType namespaces = description.getNamespaces();
		if (namespaces == null) {
			namespaces = new NamespacesType();
		}

		// add some namespaces to the service
		List bdtNamespaces = new ArrayList(Arrays.asList(namespaces.getNamespace()));
		// metadata
		NamespaceType metadataNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ BDTServiceConstants.METADATA_SCHEMA, schemaDirFile);
		// metadataNamespace.setGenerateStubs(new Boolean(false));
		metadataNamespace.setPackageName("gov.nih.nci.cagrid.bdt.beans.metadata");
		//base reference
		NamespaceType refNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ BDTServiceConstants.BDT_REF_SCHEMA, schemaDirFile);
		refNamespace.setGenerateStubs(new Boolean(false));
		refNamespace.setPackageName("gov.nih.nci.cagrid.bdt.stubs.reference");
		// transfer
		NamespaceType transferNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ BDTServiceConstants.TRANSFER_SCHEMA, schemaDirFile);
		transferNamespace.setGenerateStubs(new Boolean(false));
		transferNamespace.setPackageName("org.globus.transfer");
		// enumeration
		NamespaceType enumerationNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ BDTServiceConstants.ENUMERATION_SCHEMA, schemaDirFile);
		enumerationNamespace.setGenerateStubs(new Boolean(false));
		enumerationNamespace.setPackageName("org.xmlsoap.schemas.ws._2004._09.enumeration");
		// new addressing
		NamespaceType addressingNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ BDTServiceConstants.ADDRESSING_SCHEMA, schemaDirFile);
		addressingNamespace.setGenerateStubs(new Boolean(false));
		addressingNamespace.setPackageName("org.globus.addressing");
        // enumeration response container
        NamespaceType responseContainerNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
            + BDTServiceConstants.ENUMERATION_RESPONSE_CONTAINER_SCHEMA, schemaDirFile);
        // responseContainerNamespace.setGenerateStubs(new Boolean(true));
        responseContainerNamespace.setPackageName("gov.nih.nci.cagrid.enumeration.stubs.response");
        
		bdtNamespaces.add(metadataNamespace);
		bdtNamespaces.add(refNamespace);
		bdtNamespaces.add(transferNamespace);
		bdtNamespaces.add(enumerationNamespace);
		bdtNamespaces.add(addressingNamespace);
        bdtNamespaces.add(responseContainerNamespace);

		NamespaceType[] nsArray = new NamespaceType[bdtNamespaces.size()];
		bdtNamespaces.toArray(nsArray);
		namespaces.setNamespace(nsArray);
		description.setNamespaces(namespaces);

		ServiceType mainService = description.getServices().getService(0);

		// add the bdt subservice
		ServiceDescription desc = (ServiceDescription) Utils.deserializeDocument(extensionDir + File.separator
			+ "introduce.xml", ServiceDescription.class);
		ServiceType bdtService = desc.getServices().getService(0);
		bdtService.setName(mainService.getName() + bdtService.getName());
		bdtService.setNamespace(mainService.getNamespace() + "BDT");
		bdtService.setPackageName(mainService.getPackageName() + ".bdt");
		bdtService.setResourceFrameworkType(IntroduceConstants.INTRODUCE_CUSTOM_RESOURCE);
		MethodType[] methods = bdtService.getMethods().getMethod();
		for (int i = 0; i < methods.length; i++) {
			MethodType method = methods[i];
			if (method.getName().equals("Get")) {
				method.setIsProvided(true);
				MethodTypeProviderInformation mpi = new MethodTypeProviderInformation();
				mpi.setProviderClass("gov.nih.nci.cagrid.bdt.service.globus.BulkDataHandlerProviderImpl");
				method.setProviderInformation(mpi);
			} else if (method.getName().equals("CreateEnumeration")) {
				method.setIsProvided(true);
				MethodTypeProviderInformation mpi = new MethodTypeProviderInformation();
				mpi.setProviderClass("gov.nih.nci.cagrid.bdt.service.globus.BulkDataHandlerProviderImpl");
				method.setProviderInformation(mpi);
				method.setIsImported(true);
				MethodTypeImportInformation mii = new MethodTypeImportInformation();
				mii.setFromIntroduce(Boolean.TRUE);
				mii.setInputMessage(new QName("http://cagrid.nci.nih.gov/BulkDataHandler", "CreateEnumerationRequest"));
				mii.setOutputMessage(new QName("http://cagrid.nci.nih.gov/BulkDataHandler",
					"CreateEnumerationResponse"));
				mii.setPackageName("gov.nih.nci.cagrid.bdt.stubs");
				mii.setNamespace("http://cagrid.nci.nih.gov/BulkDataHandler");
				mii.setPortTypeName("BulkDataHandlerPortType");
				mii.setWsdlFile("./BulkDataHandler.wsdl");
				method.setImportInformation(mii);
			} else if (method.getName().equals("GetGridFTPURLs")) {
				method.setIsProvided(true);
				MethodTypeProviderInformation mpi = new MethodTypeProviderInformation();
				mpi.setProviderClass("gov.nih.nci.cagrid.bdt.service.globus.BulkDataHandlerProviderImpl");
				method.setProviderInformation(mpi);
				method.setIsImported(true);
				MethodTypeImportInformation mii = new MethodTypeImportInformation();
				mii.setFromIntroduce(Boolean.TRUE);
				mii.setInputMessage(new QName("http://cagrid.nci.nih.gov/BulkDataHandler", "GetGridFTPURLsRequest"));
				mii.setOutputMessage(new QName("http://cagrid.nci.nih.gov/BulkDataHandler", "GetGridFTPURLsResponse"));
				mii.setPackageName("gov.nih.nci.cagrid.bdt.stubs");
				mii.setNamespace("http://cagrid.nci.nih.gov/BulkDataHandler");
				mii.setPortTypeName("BulkDataHandlerPortType");
				mii.setWsdlFile("./BulkDataHandler.wsdl");
				method.setImportInformation(mii);
			}
		}

		ServiceType[] services = description.getServices().getService();
        services = (ServiceType[]) Utils.appendToArray(services, bdtService);
		description.getServices().setService(services);
	}


	private void addResourceImplStub(ServiceDescription desc, Properties props) throws Exception {
		BDTResourceTemplate resourceTemplate = new BDTResourceTemplate();
		String resourceS = resourceTemplate.generate(
            new SpecificServiceInformation(info, info.getServices().getService(0)));
		File resourceF = new File(props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
			+ File.separator + "src" + File.separator + CommonTools.getPackageDir(desc.getServices().getService(0))
			+ File.separator + "service" + File.separator + "BDTResource.java");
		FileWriter resourceFW = new FileWriter(resourceF);
		resourceFW.write(resourceS);
		resourceFW.close();
	}


	private void addServiceMetadata(ServiceDescription desc) throws CreationExtensionException {
		ResourcePropertyType serviceMetadata = new ResourcePropertyType();
		serviceMetadata.setPopulateFromFile(true); // no metadata file yet...
		serviceMetadata.setRegister(true);
		serviceMetadata.setFileLocation("./BulkDataHandler-metadata.xml");
		serviceMetadata.setQName(BDTServiceConstants.METADATA_QNAME);
		ResourcePropertiesListType propsList = desc.getServices().getService(0).getResourcePropertiesList();
		if (propsList == null) {
			propsList = new ResourcePropertiesListType();
			desc.getServices().getService(0).setResourcePropertiesList(propsList);
		}
		ResourcePropertyType[] metadataArray = propsList.getResourceProperty();
		if ((metadataArray == null) || (metadataArray.length == 0)) {
			metadataArray = new ResourcePropertyType[]{serviceMetadata};
		} else {
            metadataArray = (ResourcePropertyType[]) Utils.appendToArray(metadataArray, serviceMetadata);
		}
		propsList.setResourceProperty(metadataArray);

		try {
            File baseBdtMetadata = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "bdt" + File.separator
                + "etc" + File.separator + "BulkDataHandler-metadata.xml");
            File serviceBdtMetadata = new File(serviceProperties.getProperty(
                IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
                + File.separator + "etc" + File.separator + "BulkDataHandler-metadata.xml");
			Utils.copyFile(baseBdtMetadata, serviceBdtMetadata);
		} catch (IOException ex) {
			throw new CreationExtensionException("Error copying BDT metadata to service: " 
                + ex.getMessage(), ex);
		}
	}


	private boolean serviceMetadataExists(ServiceDescription desc) {
		ResourcePropertiesListType propsList = desc.getServices().getService(0).getResourcePropertiesList();
		if (propsList == null) {
			return false;
		}
		ResourcePropertyType[] props = propsList.getResourceProperty();
		if ((props == null) || (props.length == 0)) {
			return false;
		}
		for (int i = 0; i < props.length; i++) {
			if (props[i].getQName().equals(BDTServiceConstants.METADATA_QNAME)) {
				return true;
			}
		}
		return false;
	}


	private String getServiceSchemaDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "schema"
			+ File.separator + props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	}


	private String getServiceLibDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
	}


	private void copySchema(String schemaName, String outputDir) throws Exception {
		File schemaFile = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "bdt" + File.separator
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
				return (name.endsWith(".jar") && (name.startsWith("caGrid-1.1-BulkDataHandler")
					|| name.startsWith("wsrf_core_enum") || name.startsWith("wsrf_core_stubs_enum")));
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
		if (!serviceMetadataExists(desc)) {
			addServiceMetadata(desc);
		}
	}
}
