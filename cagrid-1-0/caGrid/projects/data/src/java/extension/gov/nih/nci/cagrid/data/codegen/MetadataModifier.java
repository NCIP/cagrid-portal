package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.data.common.MetadataUtilities;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.rmi.RemoteException;

import org.apache.axis.message.MessageElement;

/** 
 *  MetadataModifier
 *  Performs modification on metadata for the data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 8, 2006 
 * @version $Id$ 
 */
public class MetadataModifier {
	
	private ServiceExtensionDescriptionType extension;
	private ServiceInformation info;
	private CaDSRServiceClient cadsrClient = null;
	private Project cadsrProject = null;
	private UMLPackageMetadata cadsrPackage = null;
		
	public MetadataModifier(ServiceExtensionDescriptionType extension, ServiceInformation info) {
		this.extension = extension;
		this.info = info;
	}
	
	
	public DomainModel createDomainModel(String schemaNamespace) throws RemoteException, CodegenExtensionException {		
		// get the metadata from the caDSR
		Project proj = getCadsrProject();
		UMLPackageMetadata pack = getCadsrPackage();
		if (pack == null) {
			throw new CodegenExtensionException("Specified domain package not found in caDSR!");
		}
		
		// create the domain model skeleton
		DomainModel model = MetadataUtilities.createDomainModel(proj);
		
		// get the user's model namespace
		NamespaceType namespace = getDomainNamespace(schemaNamespace);
		if (namespace == null) {
			throw new CodegenExtensionException("Specified namespace " + namespace + " not found in the service configuration");
		}
		
		// search the namespace for schema element types
		SchemaElementType[] schemaTypes = namespace.getSchemaElement();
		if (schemaTypes != null) {
			String[] classNames = new String[schemaTypes.length];
			for (int i = 0; i < schemaTypes.length; i++) {
				classNames[i] = schemaTypes[i].getClassName();
			}
			
			// add metadata for the selected schema types
			MetadataUtilities.setExposedClasses(model, pack, classNames);
		}
		
		return model;
	}
	
	
	private NamespaceType getDomainNamespace(String schemaNamespace) {
		NamespaceType[] namespaces = info.getNamespaces().getNamespace();
		for (int i = 0; namespaces != null && i < namespaces.length; i++) {
			if (namespaces[i].getNamespace().equals(schemaNamespace)) {
				return namespaces[i];
			}
		}
		return null;
	}
	
	
	private CaDSRServiceClient getCadsrClient() {
		if (cadsrClient == null) {
			ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(extension, info);
			MessageElement cadsrElement = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.CADSR_ELEMENT_NAME);
			if (cadsrElement != null) {
				String url = cadsrElement.getAttribute(DataServiceConstants.CADSR_URL_ATTRIB);				
				cadsrClient = new CaDSRServiceClient(url);
			}
		}
		return cadsrClient;
	}
	
	
	private Project getCadsrProject() throws RemoteException {
		if (cadsrProject == null) {
			ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(extension, info);
			MessageElement cadsrElement = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.CADSR_ELEMENT_NAME);
			if (cadsrElement != null) {
				String projectName = cadsrElement.getAttribute(DataServiceConstants.CADSR_PROJECT_ATTRIB);
				Project[] projects = getCadsrClient().findAllProjects();
				for (int i = 0; projects != null && i < projects.length; i++) {
					if (projects[i].getLongName().equals(projectName)) {
						cadsrProject = projects[i];
						break;
					}
				}
			}
		}
		return cadsrProject;
	}
	
	
	private UMLPackageMetadata getCadsrPackage() throws RemoteException {
		if (cadsrPackage == null) {
			ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(extension, info);
			MessageElement cadsrElement = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.CADSR_ELEMENT_NAME);
			if (cadsrElement != null) {
				String packageName = cadsrElement.getAttribute(DataServiceConstants.CADSR_PACKAGE_ATTRIB);
				UMLPackageMetadata[] packages = getCadsrClient().findPackagesInProject(getCadsrProject());
				for (int i = 0; packages != null && i < packages.length; i++) {
					if (packages[i].getName().equals(packageName)) {
						cadsrPackage = packages[i];
						break;
					}
				}
			}
		}
		return cadsrPackage;
	}
}
