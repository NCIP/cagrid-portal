package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis.message.MessageElement;
import org.apache.log4j.Logger;
import org.jdom.Element;


/**
 * DataServiceCodegenPreProcessor 
 * Preprocessor for data service codegen operations.
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 11, 2006
 * @version $Id$
 */
public class DataServiceCodegenPreProcessor implements CodegenExtensionPreProcessor {

	private static Logger LOG = Logger.getLogger(DataServiceCodegenPreProcessor.class);


	public void preCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		try {
			modifyServiceProperties(desc, info);
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error modifying deployment properties: " 
				+ ex.getMessage(), ex);
		}
		modifyMetadata(desc, info);
	}


	private void modifyMetadata(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		// find the service's etc directory, where the domain model goes
		String domainModelFile = info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
			+ File.separator + "etc" + File.separator + "domainModel.xml";
		
		LOG.debug("Looking for user-supplied domain model xml file");
		String suppliedDomainModel = getSuppliedDomainModelFilename(desc, info);
		if (suppliedDomainModel != null) {
			LOG.debug("User-supplied domain model is " + suppliedDomainModel);
			LOG.info("Copying domain model from " + suppliedDomainModel + " to " + domainModelFile);
			try {
				Utils.copyFile(new File(suppliedDomainModel), new File(domainModelFile));
			} catch (Exception ex) {
				throw new CodegenExtensionException("Error copying domain model file: " + ex.getMessage(), ex);
			}
		} else {
			LOG.info("No domain model supplied, generating from caDSR");
			generateDomainModel(desc, info, domainModelFile);
		}
		
		// if the domain model was actually placed in the service's etc
		// directory, then add the resource property for the domain model
		if (new File(domainModelFile).exists()) {
			addDomainModelResourceProperty(info);
		}
	}
	
	
	private void generateDomainModel(ServiceExtensionDescriptionType desc, ServiceInformation info, 
		String domainModelFile) throws CodegenExtensionException {
		LOG.debug("Looking for caDSR element in extension data");
		// verify there's a caDSR element in the extension data bucket
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		if (ExtensionTools.getExtensionDataElement(
			data, DataServiceConstants.CADSR_ELEMENT_NAME) != null) {
			Element cadsrElement = AxisJdomUtils.fromMessageElement(
			ExtensionTools.getExtensionDataElement(data, DataServiceConstants.CADSR_ELEMENT_NAME));
			LOG.debug("Extracting caDSR information from element");
			String cadsrUrl = cadsrElement.getAttributeValue(DataServiceConstants.CADSR_URL_ATTRIB);
			String cadsrProject = cadsrElement.getAttributeValue(DataServiceConstants.CADSR_PROJECT_ATTRIB);
			String cadsrPackage = cadsrElement.getAttributeValue(DataServiceConstants.CADSR_PACKAGE_ATTRIB);
			// get selected classes
			String[] selectedClasses = null;
			Element classesElement = cadsrElement.getChild(DataServiceConstants.CADSR_SELECTED_CLASSES);
			if (classesElement != null 
				&& classesElement.getChildren(DataServiceConstants.CADSR_CLASS).size() != 0) {
				List children = classesElement.getChildren(DataServiceConstants.CADSR_CLASS);
				selectedClasses = new String[children.size()];
				int index = 0;
				Iterator childIter = children.iterator();
				while (childIter.hasNext()) {
					selectedClasses[index] = ((Element) childIter.next()).getText();
					index++;
				}
			}
			// get the target namespace, if specified
			String targetNamespace = null;
			MessageElement targetNsElement = ExtensionTools.getExtensionDataElement(data,
				DataServiceConstants.DATA_MODEL_ELEMENT_NAME);
			if (targetNsElement != null) {
				targetNamespace = targetNsElement.getValue();
			}
			if (targetNamespace != null) {
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
				LOG.info("caDSR URL=" + cadsrUrl);
				DomainModel model = null;
				try {
					// cadsr can now generate the data service DomainModel
					CaDSRServiceClient cadsrClient = new CaDSRServiceClient(cadsrUrl);
					// get the project
					Project proj = getProject(cadsrClient, cadsrProject);
					if (proj == null) {
						throw new CodegenExtensionException("caDSR service " + cadsrUrl 
							+ " did not find project " + cadsrProject);
					}
					if (selectedClasses != null) {
						UMLClassMetadata[] classMetadata = getUmlClassMetadata(
							cadsrClient, proj, cadsrPackage, selectedClasses);
						UMLAssociation[] associations = getUmlClassAssociations(
							cadsrClient, proj, classMetadata);
						model = cadsrClient.generateDomainModelForClasses(
							proj, classMetadata, associations);
					} else {
						model = cadsrClient.generateDomainModelForPackages(
							proj, new String[]{cadsrPackage});
					}
					System.out.println("Created data service Domain Model!");
					LOG.info("Created data service Domain Model!");
				} catch (Exception ex) {
					throw new CodegenExtensionException("Error connecting to caDSR for metadata: " 
						+ ex.getMessage(), ex);
				}

				// find the client-configuration.wsdd needed to serialize 
				// the domain model
				String configFilename = ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data"
					+ File.separator + "DomainModel-client-config.wsdd";
				LOG.debug("Serializing domain model to file " + domainModelFile);
				LOG.debug("Using config filename " + configFilename);
				try {
					FileWriter domainModelFileWriter = new FileWriter(domainModelFile);
					InputStream configInput = new FileInputStream(configFilename);
					Utils.serializeObject(model, DataServiceConstants.DOMAIN_MODEL_QNAME, domainModelFileWriter,
						configInput);
					domainModelFileWriter.flush();
					domainModelFileWriter.close();
					configInput.close();
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
		}
	}


	private void modifyServiceProperties(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws Exception {
		ServiceProperties props = info.getServiceProperties();
		if (props == null) {
			props = new ServiceProperties();
		}
		if (props.getProperty() == null) {
			props.setProperty(new ServicePropertiesProperty[]{});
		}
		String qpClassname = getQueryProcesorClass(desc, info);
		if (qpClassname != null) {
			// find the QP class
			String serviceDir = info.getIntroduceServiceProperties().getProperty(
				IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR);
			// get the eclipse classpath document
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
			// load the class from the additional libraries and current
			// classpath
			URL[] libUrls = new URL[libs.size()];
			Iterator libIter = libs.iterator();
			int i = 0;
			while (libIter.hasNext()) {
				libUrls[i] = ((File) libIter.next()).toURL();
			}
			ClassLoader qpLoader = new URLClassLoader(libUrls, getClass().getClassLoader());
			Class qpClass = qpLoader.loadClass(qpClassname);
			CQLQueryProcessor processor = (CQLQueryProcessor) qpClass.newInstance();
			// get the map of required deploy properties
			Map params = processor.getRequiredParameters();
			List qpProperties = new ArrayList();
			if (params != null) {
				// add the parameters
				Iterator paramKeyIter = params.keySet().iterator();
				while (paramKeyIter.hasNext()) {
					String key = (String) paramKeyIter.next();
					// verify the keys of the required params list are valid
					// Java identifiers
					if (!CommonTools.isValidJavaField(key)) {
						throw new CodegenExtensionException("The query processor's required parameter " + key
							+ " is not a valid Java field name.");
					}
					if (!hasProperty(props, key)) {
						ServicePropertiesProperty prop = new ServicePropertiesProperty();
						String value = (String) params.get(key);
						if (value == null) {
							value = "";
						}
						prop.setKey(key);
						prop.setValue(value);
						qpProperties.add(prop);
					}
				}
				// add the query processor class name to the properties
				for (int p = 0; p < props.getProperty().length; p++) {
					if (props.getProperty(p).getKey().equals(DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY)) {
						props.getProperty(p).setValue(qpClassname);
					}
				}
				// write all the properties back into the service properties
				// bean
				qpProperties.addAll(Arrays.asList(props.getProperty()));
				ServicePropertiesProperty[] allProperties = new ServicePropertiesProperty[qpProperties.size()];
				qpProperties.toArray(allProperties);
				props.setProperty(allProperties);
				info.setServiceProperties(props);
			}
		}
	}


	private boolean hasProperty(ServiceProperties props, String key) {
		if (props != null && props.getProperty() != null) {
			for (int i = 0; i < props.getProperty().length; i++) {
				if (props.getProperty(i).getKey().equals(key)) {
					return true;
				}
			}
		}
		return false;
	}


	private String getQueryProcesorClass(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		MessageElement qpEntry = ExtensionTools.getExtensionDataElement(data,
			DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
		if (qpEntry != null) {
			String queryProcessorClass = qpEntry.getValue();
			return queryProcessorClass;
		}
		return null;
	}
	
	
	private String getSuppliedDomainModelFilename(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		MessageElement element = ExtensionTools.getExtensionDataElement(
			data, DataServiceConstants.SUPPLIED_DOMAIN_MODEL);
		if (element != null) {
			return element.getValue();
		}
		return null;
	}
	
	
	private void addDomainModelResourceProperty(ServiceInformation info) {
		ResourcePropertyType domainMetadata = new ResourcePropertyType();
		domainMetadata.setPopulateFromFile(true); // no metadata file yet...
		domainMetadata.setRegister(true);
		domainMetadata.setQName(DataServiceConstants.DOMAIN_MODEL_QNAME);
		ResourcePropertiesListType propsList = info.getServices().getService(0).getResourcePropertiesList();
		if (propsList == null) {
			propsList = new ResourcePropertiesListType();
			info.getServices().getService(0).setResourcePropertiesList(propsList);
		}
		ResourcePropertyType[] metadataArray = propsList.getResourceProperty();
		if (metadataArray == null || metadataArray.length == 0) {
			metadataArray = new ResourcePropertyType[]{domainMetadata};
		} else {
			ResourcePropertyType[] tmpArray = new ResourcePropertyType[metadataArray.length + 1];
			System.arraycopy(metadataArray, 0, tmpArray, 0, metadataArray.length);
			tmpArray[metadataArray.length] = domainMetadata;
			metadataArray = tmpArray;
		}
		propsList.setResourceProperty(metadataArray);
	}
	
	
	private boolean domainModelResourcePropertyExists(ServiceInformation info) {
		ResourcePropertiesListType propsList = info.getServices().getService()[0].getResourcePropertiesList();
		if (propsList == null) {
			return false;
		}
		ResourcePropertyType[] props = propsList.getResourceProperty();
		if (props == null || props.length == 0) {
			return false;
		}
		for (int i = 0; i < props.length; i++) {
			if (props[i].getQName().equals(DataServiceConstants.DOMAIN_MODEL_QNAME)) {
				return true;
			}
		}
		return false;
	}
	
	
	private Project getProject(CaDSRServiceClient client, String projectName) throws RemoteException {
		// get the project
		Project proj = null;
		Project[] allProjects = client.findAllProjects();
		for (int i = 0; i < allProjects.length; i++) {
			if (allProjects[i].getLongName().equals(projectName)) {
				proj = allProjects[i];
				break;
			}
		}
		return proj;
	}
	
	
	private UMLClassMetadata[] getUmlClassMetadata(CaDSRServiceClient client, Project proj,
		String packName, String[] classNames) throws RemoteException {
		Set metadata = new HashSet();
		UMLClassMetadata[] allClasses = client.findClassesInPackage(proj, packName);
		for (int i = 0; i < allClasses.length; i++) {
			UMLClassMetadata currentClass = allClasses[i];
			for (int j = 0; j < classNames.length; j++) {
				if (classNames[j].equals(currentClass.getName())) {
					metadata.add(currentClass);
					break;
				}
			}
		}
		UMLClassMetadata[] mdArray = new UMLClassMetadata[metadata.size()];
		metadata.toArray(mdArray);
		return mdArray;
	}
	
	
	private UMLAssociation[] getUmlClassAssociations(CaDSRServiceClient client, Project proj,
		UMLClassMetadata[] classes) throws RemoteException {
		Set associations = new HashSet();
		for (int i = 0; i < classes.length; i++) {
			UMLAssociation[] classAssociations = client.findAssociationsForClass(proj, classes[i]);
			Collections.addAll(associations, classAssociations);
		}
		UMLAssociation[] assocArray = new UMLAssociation[associations.size()];
		associations.toArray(assocArray);
		return assocArray;
	}
}
