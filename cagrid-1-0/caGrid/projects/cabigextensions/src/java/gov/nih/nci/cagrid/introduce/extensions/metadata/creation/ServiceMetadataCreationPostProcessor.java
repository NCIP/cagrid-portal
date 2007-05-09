package gov.nih.nci.cagrid.introduce.extensions.metadata.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.extensions.metadata.constants.MetadataConstants;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription;
import gov.nih.nci.cagrid.metadata.service.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.projectmobius.common.MobiusException;
import org.projectmobius.tools.common.viewer.XSDFileFilter;


/**
 * ServiceMetadataCreationPostProcessor adds standard cagrid metadata to
 * services
 */
public class ServiceMetadataCreationPostProcessor implements CreationExtensionPostProcessor {

	private static final String METADATA_JAR_PREFIX = "caGrid-1.1-metadata";


	public void postCreate(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CreationExtensionException {
		try {
			Properties serviceProperties = info.getIntroduceServiceProperties();
			ServiceDescription serviceDescription = info.getServiceDescriptor();

			// grab schemas and copy them into the service's directory
			copyMetadataSchemas(getServiceSchemaDir(serviceProperties));

			// copy libraries into the new lib directory
			copyLibraries(serviceProperties);

			// add the namespace types
			addNamespaces(serviceDescription, getServiceSchemaDir(serviceProperties));

			// add the service metadata
			addServiceMetadata(serviceDescription);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationExtensionException("Error adding service metadata components to template!", ex);
		}
	}


	private void copyMetadataSchemas(String schemaDir) throws Exception {
		System.out.println("Copying schemas to " + schemaDir);
		File extensionSchemaDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator
			+ MetadataConstants.EXTENSION_NAME + File.separator + "schema");
		List schemaFiles = Utils.recursiveListFiles(extensionSchemaDir, new XSDFileFilter());
		for (int i = 0; i < schemaFiles.size(); i++) {
			File schemaFile = (File) schemaFiles.get(i);
			String subname = schemaFile.getCanonicalPath().substring(
				extensionSchemaDir.getCanonicalPath().length() + File.separator.length());
			copySchema(subname, schemaDir);
		}
	}


	private void addNamespaces(ServiceDescription description, String schemaDir) throws MobiusException {
		// namespaces
		System.out.println("Modifying namespace definitions");
		NamespacesType namespaces = description.getNamespaces();
		if (namespaces == null) {
			namespaces = new NamespacesType();
		}

		// add some namespaces to the service
		List newNamespaces = new ArrayList(Arrays.asList(namespaces.getNamespace()));
		// caGrid metadata namespace
		NamespaceType cagridMdNamespace = CommonTools.createNamespaceType(schemaDir + File.separator
			+ MetadataConstants.CAGRID_METADATA_SCHEMA, new File(schemaDir));
		cagridMdNamespace.setGenerateStubs(Boolean.FALSE);
		newNamespaces.add(cagridMdNamespace);

		// add those new namespaces to the list of namespace types
		NamespaceType[] nsArray = new NamespaceType[newNamespaces.size()];
		newNamespaces.toArray(nsArray);
		namespaces.setNamespace(nsArray);
		description.setNamespaces(namespaces);
	}


	private void addServiceMetadata(ServiceDescription desc) {
		ResourcePropertyType serviceMetadata = new ResourcePropertyType();
		serviceMetadata.setPopulateFromFile(true);
		serviceMetadata.setRegister(true);
		serviceMetadata.setQName(MetadataConstants.SERVICE_METADATA_QNAME);
		ServiceMetadata smd = new ServiceMetadata();
		ServiceMetadataServiceDescription des = new ServiceMetadataServiceDescription();
		Service service = new Service();
		des.setService(service);
		smd.setServiceDescription(des);
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


	private String getServiceSchemaDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "schema"
			+ File.separator + props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	}


	private String getServiceLibDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
	}


	private void copySchema(String schemaName, String outputDir) throws Exception {
		File schemaFile = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator
			+ MetadataConstants.EXTENSION_NAME + File.separator + "schema" + File.separator + schemaName);
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
				return (name.endsWith(".jar") && (name.startsWith(METADATA_JAR_PREFIX)));
			}
		});

		if (libs != null) {
			File[] copiedLibs = new File[libs.length];
			for (int i = 0; i < libs.length; i++) {
				File outFile = new File(toDir + File.separator + libs[i].getName());
				copiedLibs[i] = outFile;
				Utils.copyFile(libs[i], outFile);
			}
			modifyClasspathFile(copiedLibs, props);
		}

	}


	private void modifyClasspathFile(File[] libs, Properties props) throws Exception {
		File classpathFile = new File(props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
			+ File.separator + ".classpath");
		ExtensionUtilities.syncEclipseClasspath(classpathFile, libs);
	}
}