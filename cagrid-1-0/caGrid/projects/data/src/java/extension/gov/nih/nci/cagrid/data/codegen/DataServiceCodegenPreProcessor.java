package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.axis.message.MessageElement;
import org.apache.log4j.Logger;
import org.jdom.Element;


/**
 * DataServiceCodegenPreProcessor Preprocessor for data service codegen
 * operations.
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
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
			throw new CodegenExtensionException("Error modifying deployment properties: " + ex.getMessage(), ex);
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
		if (!domainModelResourcePropertyExists(info)) {
			addDomainModelResourceProperty(info);
		}
	}


	private void generateDomainModel(ServiceExtensionDescriptionType desc, ServiceInformation info,
		String domainModelFile) throws CodegenExtensionException {
		LOG.debug("Looking for caDSR element in extension data");
		// verify there's a caDSR element in the extension data bucket
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		if (ExtensionTools.getExtensionDataElement(data, DataServiceConstants.CADSR_ELEMENT_NAME) != null) {
			Element cadsrElement = AxisJdomUtils.fromMessageElement(ExtensionTools.getExtensionDataElement(data,
				DataServiceConstants.CADSR_ELEMENT_NAME));
			LOG.debug("Extracting caDSR information from element");
			// init the cadsr service client
			String cadsrUrl = cadsrElement.getAttributeValue(DataServiceConstants.CADSR_URL_ATTRIB);
			LOG.info("Initializing caDSR client (URL=" + cadsrUrl + ")");
			CaDSRServiceClient cadsrClient = null;
			try {
				cadsrClient = new CaDSRServiceClient(cadsrUrl);
			} catch (Exception ex) {
				throw new CodegenExtensionException("Error initializing caDSR client: " + ex.getMessage(), ex);
			}
			
			// create the prototype project
			String cadsrProjectName = cadsrElement.getAttributeValue(DataServiceConstants.CADSR_PROJECT_NAME_ATTRIB);
			String cadsrProjectVersion = cadsrElement
				.getAttributeValue(DataServiceConstants.CADSR_PROJECT_VERSION_ATTRIB);
			Project proj = new Project();
			proj.setShortName(cadsrProjectName);
			proj.setVersion(cadsrProjectVersion);
			
			// sets for holding all selected classes and associations
			Set allClasses = new HashSet();
			Set allAssociations = new HashSet();
			
			// walk through the selected packages
			Iterator packElementIter = cadsrElement.getChildren(DataServiceConstants.CADSR_PACKAGE_MAPPING).iterator();
			while (packElementIter.hasNext()) {
				Element packageElement = (Element) packElementIter.next();
				String packName = packageElement.getAttributeValue(DataServiceConstants.CADSR_PACKAGE_NAME);
				// get selected classes from the package 
				List selectedClassList = packageElement.getChildren(DataServiceConstants.CADSR_PACKAGE_SELECTED_CLASS);
				String[] packageClassNames = new String[selectedClassList.size()];
				int index = 0;
				Iterator selectedClassIter = selectedClassList.iterator();
				while (selectedClassIter.hasNext()) {
					Element selectedClassElement = (Element) selectedClassIter.next();
					packageClassNames[index] = selectedClassElement.getText();
					index++;
				}
				// get the UMLClassMetadata and UMLAssociations from caDSR
				try {
					UMLClassMetadata[] classMetadata = getUmlClassMetadata(cadsrClient, proj, packName,
						packageClassNames);
					UMLAssociation[] associations = getUmlClassAssociations(cadsrClient, proj, classMetadata);
					// add them to the globally selected sets
					Collections.addAll(allClasses, classMetadata);
					Collections.addAll(allAssociations, associations);
				} catch (RemoteException ex) {
					throw new CodegenExtensionException("Error getting class or association metadata: " + ex.getMessage(), ex);
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
			UMLClassMetadata[] classMetadata = new UMLClassMetadata[allClasses.size()];
			allClasses.toArray(classMetadata);
			UMLAssociation[] associations = new UMLAssociation[allAssociations.size()];
			allAssociations.toArray(associations);
			LOG.info("Contacting caDSR to build domain model.  This might take a while...");
			DomainModel model = null;
			try {
				if (classMetadata.length != 0) {
					model = cadsrClient.generateDomainModelForClasses(proj, classMetadata, associations);
					if (model == null) {
						throw new CodegenExtensionException("caDSR returned a null domain model.");
					}
				}
				System.out.println("Created data service Domain Model!");
				LOG.info("Created data service Domain Model!");
			} catch (Exception ex) {
				throw new CodegenExtensionException(
					"Error connecting to caDSR for metadata: " + ex.getMessage(), ex);
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
				Utils.serializeObject(model, DataServiceConstants.DOMAIN_MODEL_QNAME, 
					domainModelFileWriter, configInput);
				domainModelFileWriter.flush();
				domainModelFileWriter.close();
				configInput.close();
				LOG.debug("Serialized domain model");
			} catch (Exception ex) {
				throw new CodegenExtensionException("Error serializing the domain model to disk: "
					+ ex.getMessage(), ex);
			}
			
			// add the metadata to the service information as a resource
			// property
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


	private void modifyServiceProperties(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws Exception {
		ServiceProperties props = info.getServiceProperties();
		if (props == null) {
			props = new ServiceProperties();
		}
		if (props.getProperty() == null) {
			props.setProperty(new ServicePropertiesProperty[]{});
		}
		// delete any old query processor parameters from service properties
		List keptProperties = new ArrayList();
		for (int i = 0; i < props.getProperty().length; i++) {
			if (!props.getProperty(i).getKey().startsWith(DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX)) {
				keptProperties.add(props.getProperty(i));
			}
		}
		
		// verify we've got a query processor class configured
		String qpClassname = CommonTools.getServicePropertyValue(
			info, DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
		if (qpClassname != null && qpClassname.length() != 0) {
			// get the query processor parameters
			ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
			MessageElement paramMe = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.QUERY_PROCESSOR_CONFIG_ELEMENT);
			Element paramElement = AxisJdomUtils.fromMessageElement(paramMe);
			Iterator paramElemIter = paramElement.getChildren(DataServiceConstants.QUERY_PROCESSOR_PROPERTY_ELEMENT).iterator();
			while (paramElemIter.hasNext()) {
				Element paramElem = (Element) paramElemIter.next();
				String name = DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX 
					+ paramElem.getAttributeValue(DataServiceConstants.QUERY_PROCESSOR_PROPERTY_NAME);
				if (!CommonTools.isValidJavaField(name)) {
					throw new CodegenExtensionException("The query processor's required parameter " + name
						+ " is not a valid Java field name.");
				}
				String value = paramElem.getAttributeValue(DataServiceConstants.QUERY_PROCESSOR_PROPERTY_VALUE);
				// add a new property to the service props list
				keptProperties.add(new ServicePropertiesProperty(name, value));
			}
			// add the query processor class name to the properties
			for (int i = 0; i < keptProperties.size(); i++) {
				ServicePropertiesProperty prop = (ServicePropertiesProperty) keptProperties.get(i);
				if (prop.getKey().equals(DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY)) {
					prop.setValue(qpClassname);
					break;
				}
			}
		}
		// write the properties back to the service info
		ServicePropertiesProperty[] allProperties = new ServicePropertiesProperty[keptProperties.size()];
		keptProperties.toArray(allProperties);
		props.setProperty(allProperties);
		info.setServiceProperties(props);
	}
	

	private String getSuppliedDomainModelFilename(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		MessageElement element = ExtensionTools.getExtensionDataElement(data,
			DataServiceConstants.SUPPLIED_DOMAIN_MODEL);
		if (element != null) {
			return element.getValue();
		}
		return null;
	}


	private void addDomainModelResourceProperty(ServiceInformation info) {
		ResourcePropertyType domainMetadata = new ResourcePropertyType();
		domainMetadata.setPopulateFromFile(true);
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


	private void removeDomainModelResourceProperty(ServiceInformation info) {
		ResourcePropertiesListType propsList = info.getServices().getService(0).getResourcePropertiesList();
		if (propsList != null) {
			ResourcePropertyType[] metadataArray = propsList.getResourceProperty();
			Set cleaned = new HashSet();
			if (metadataArray != null) {
				for (int i = 0; i < metadataArray.length; i++) {
					if (!metadataArray[i].getQName().equals(DataServiceConstants.DOMAIN_MODEL_QNAME)) {
						cleaned.add(metadataArray[i]);
					}
				}
			}
			ResourcePropertyType[] cleanedArray = new ResourcePropertyType[cleaned.size()];
			cleaned.toArray(cleanedArray);
			propsList.setResourceProperty(cleanedArray);
		}
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


	private UMLClassMetadata[] getUmlClassMetadata(CaDSRServiceClient client, Project proj, String packName,
		String[] classNames) throws RemoteException {
		Set metadata = new HashSet();
		UMLClassMetadata[] allClasses = client.findClassesInPackage(proj, packName);
		if (allClasses != null) {
			for (int i = 0; i < allClasses.length; i++) {
				UMLClassMetadata currentClass = allClasses[i];
				for (int j = 0; j < classNames.length; j++) {
					if (classNames[j].equals(currentClass.getName())) {
						metadata.add(currentClass);
						break;
					}
				}
			}
		}
		UMLClassMetadata[] mdArray = new UMLClassMetadata[metadata.size()];
		metadata.toArray(mdArray);
		return mdArray;
	}


	private UMLAssociation[] getUmlClassAssociations(CaDSRServiceClient client, Project proj, UMLClassMetadata[] classes)
		throws RemoteException {
		Set associations = new HashSet();
		for (int i = 0; i < classes.length; i++) {
			UMLAssociation[] classAssociations = client.findAssociationsForClass(proj, classes[i]);
			if (classAssociations != null) {
				Collections.addAll(associations, classAssociations);
			}
		}
		UMLAssociation[] assocArray = new UMLAssociation[associations.size()];
		associations.toArray(assocArray);
		return assocArray;
	}
}
