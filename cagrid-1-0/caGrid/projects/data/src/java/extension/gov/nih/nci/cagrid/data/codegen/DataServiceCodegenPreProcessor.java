package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis.message.MessageElement;

/** 
 *  DataServiceCodegenPreProcessor
 *  Preprocessor for data service codegen operations.
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 11, 2006 
 * @version $Id$ 
 */
public class DataServiceCodegenPreProcessor implements CodegenExtensionPreProcessor {

	public void preCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		modifyMetadata(desc, info);
	}
	
	
	private void modifyMetadata(ServiceExtensionDescriptionType desc, ServiceInformation info) throws CodegenExtensionException {
		// verify there's a caDSR element in the extension data bucket		
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		MessageElement cadsrElement = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.CADSR_ELEMENT_NAME);
		if (cadsrElement != null) {
			String cadsrUrl = cadsrElement.getAttribute(DataServiceConstants.CADSR_URL_ATTRIB);
			String cadsrProject = cadsrElement.getAttribute(DataServiceConstants.CADSR_PROJECT_ATTRIB);
			String cadsrPackage = cadsrElement.getAttribute(DataServiceConstants.CADSR_PACKAGE_ATTRIB);
			// get the target namespace, if specified
			String targetNamespace = null;
			MessageElement targetNsElement = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.DATA_MODEL_ELEMENT_NAME);
			if (targetNsElement != null) {
				targetNamespace = targetNsElement.getValue();
			}
			if (targetNamespace != null) {
				// get the data service's description
				ServiceType dataService = null;
				String serviceName = info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
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
				
				// get the namespace type specified, then list selected schema element types
				String[] typeNames = null;
				NamespaceType[] namespaces = info.getNamespaces().getNamespace();
				for (int i = 0; i < namespaces.length; i++) {
					if (namespaces[i].getNamespace().equals(targetNamespace)) {
						SchemaElementType[] types = namespaces[i].getSchemaElement();
						typeNames = new String[types.length];
						for (int j = 0; j < types.length; j++) {
							typeNames[j] = types[j].getType();
						}
						break;
					}
				}
				
				// build the domain model
				DomainModel model = null;
				try {
					/*
					MetadataBuilder builder = new MetadataBuilder(cadsrUrl, cadsrProject, cadsrPackage, typeNames);
					model = builder.getDomainModel();
					*/
					// cadsr can now generate the data service DomainModel
					CaDSRServiceClient cadsrClient = new CaDSRServiceClient(cadsrUrl);
					// get the project
					Project proj = null;
					Project[] allProjects = cadsrClient.findAllProjects();
					for (int i = 0; i < allProjects.length; i++) {
						if (allProjects[i].getLongName().equals(cadsrProject)) {
							proj = allProjects[i];
							break;
						}
					}
					if (proj == null) {
						throw new CodegenExtensionException("caDSR service " + cadsrUrl + " did not find project " + cadsrProject);
					}
					model = cadsrClient.generateDomainModelForPackages(proj, new String[] {cadsrPackage});
					System.out.println("Created data service metadata!");
				} catch (RemoteException ex) {
					throw new CodegenExtensionException("Error connecting to caDSR for metadata: " + ex.getMessage(), ex);
				}
				
				// find the service's etc directory and serialize the domain model to it
				String domainModelFile = info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
					+ File.separator + "etc" + File.separator + "domainModel.xml";
				try {					
					Utils.serializeDocument(domainModelFile, model, DataServiceConstants.DOMAIN_MODEL_QNAME);
				} catch (Exception ex) {
					throw new CodegenExtensionException("Error serializing the domain model to disk: " + ex.getMessage(), ex);
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
					propertyArray = new ResourcePropertyType[] {domainModelResourceProperty};
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
}
