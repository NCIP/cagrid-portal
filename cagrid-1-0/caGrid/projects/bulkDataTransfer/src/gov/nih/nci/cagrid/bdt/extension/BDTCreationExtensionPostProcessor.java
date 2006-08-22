package gov.nih.nci.cagrid.bdt.extension;

import gov.nih.nci.cagrid.bdt.service.BDTServiceConstants;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class BDTCreationExtensionPostProcessor implements CreationExtensionPostProcessor {

	public void postCreate(ServiceDescription serviceDescription, Properties serviceProperties)
		throws CreationExtensionException {
		// apply data service requirements to it
		try {
			System.out.println("Adding data service components to template");
			makeBDTService(serviceDescription, serviceProperties);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException(
				"Error adding data service components to template! " + ex.getMessage(), ex);
		}
		// add the proper deployment properties
		try {
			System.out.println("Adding deploy property for query processor class");
			modifyServiceProperties(serviceDescription);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException("Error adding query processor parameter to service! "
				+ ex.getMessage(), ex);
		}
	}


	private void makeBDTService(ServiceDescription description, Properties props) throws Exception {
		String schemaDir = getServiceSchemaDir(props);
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
			+ BDTServiceConstants.METADATA_SCHEMA);
		// transfer
		NamespaceType transferNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ BDTServiceConstants.TRANSFER_SCHEMA);
		// enumeration
		NamespaceType enumerationNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ BDTServiceConstants.ENUMERATION_SCHEMA);

		bdtNamespaces.add(metadataNamespace);
		bdtNamespaces.add(transferNamespace);
		bdtNamespaces.add(enumerationNamespace);

		NamespaceType[] nsArray = new NamespaceType[bdtNamespaces.size()];
		bdtNamespaces.toArray(nsArray);
		namespaces.setNamespace(nsArray);
		description.setNamespaces(namespaces);

		// add the bdt subservice
		ServiceDescription desc = (ServiceDescription) Utils.deserializeDocument(extensionDir + File.separator + "introduce.xml", ServiceDescription.class);
		ServiceType bdtService = desc.getServices().getService(0);
		List services = new ArrayList(Arrays.asList(description.getServices().getService()));
		services.add(bdtService);
		ServiceType[] servicesArr = new ServiceType[services.size()];
		services.toArray(servicesArr);
		description.getServices().setService(servicesArr);

	}


	private void addServiceMetadata(ServiceDescription desc) {
		ResourcePropertyType serviceMetadata = new ResourcePropertyType();
		serviceMetadata.setPopulateFromFile(true); // no metadata file yet...
		serviceMetadata.setRegister(true);
		serviceMetadata.setQName(BDTServiceConstants.METADATA_QNAME);
		ResourcePropertiesListType propsList = desc.getServices().getService()[0].getResourcePropertiesList();
		if (propsList == null) {
			propsList = new ResourcePropertiesListType();
			desc.getServices().getService()[0].setResourcePropertiesList(propsList);
		}
		ResourcePropertyType[] metadataArray = propsList.getResourceProperty();
		if (metadataArray == null || metadataArray.length == 0) {
			metadataArray = new ResourcePropertyType[]{serviceMetadata};
		} else {
			ResourcePropertyType[] tmpArray = new ResourcePropertyType[metadataArray.length + 1];
			System.arraycopy(metadataArray, 0, tmpArray, 0, metadataArray.length);
			tmpArray[metadataArray.length] = serviceMetadata;
			metadataArray = tmpArray;
		}
		propsList.setResourceProperty(metadataArray);
	}


	private boolean serviceMetadataExists(ServiceDescription desc) {
		ResourcePropertiesListType propsList = desc.getServices().getService()[0].getResourcePropertiesList();
		if (propsList == null) {
			return false;
		}
		ResourcePropertyType[] props = propsList.getResourceProperty();
		if (props == null || props.length == 0) {
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
				return (name.endsWith(".jar") && (name.startsWith("caGrid-1.0-bdt")));
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
		if(!serviceMetadataExists(desc)){
			addServiceMetadata(desc);
		}
	}

}
