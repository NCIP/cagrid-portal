package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.codegen.SchemaInformation;

import java.io.File;
import java.util.Properties;

import javax.xml.namespace.QName;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ServiceInformation {

	private ServiceDescription introService;

	private Properties serviceProperties;

	private File baseDirectory;


	public ServiceInformation(ServiceDescription service, Properties properties, File baseDirectory) {
		this.introService = service;
		this.serviceProperties = properties;
		this.baseDirectory = baseDirectory;
	}


	public MetadataListType getMetadata() {
		return introService.getMetadataList();
	}


	public void setMetadata(MetadataListType metadata) {
		introService.setMetadataList(metadata);
	}


	public Properties getServiceProperties() {
		return serviceProperties;
	}


	public void setServiceProperties(Properties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}


	public MethodsType getMethods() {
		return introService.getMethods();
	}


	public void setMethods(MethodsType methods) {
		this.introService.setMethods(methods);
	}

	public NamespacesType getNamespaces() {
		return introService.getNamespaces();
	}


	public void setNamespaces(NamespacesType namespaces) {
		this.introService.setNamespaces(namespaces);
	}

	public ServiceSecurity getServiceSecurity() {
		return this.introService.getServiceSecurity();
	}


	public File getBaseDirectory() {
		return baseDirectory;
	}


	public SchemaInformation getSchemaInformation(QName qname) {
		if (introService.getNamespaces() != null && introService.getNamespaces().getNamespace() != null) {
			NamespaceType[] namespaces = introService.getNamespaces().getNamespace();
			for (int i = 0; i < namespaces.length; i++) {
				NamespaceType namespace = namespaces[i];
				if (namespace.getNamespace().equals(qname.getNamespaceURI())) {
					if(namespace.getSchemaElement() != null){
						for(int j=0; j < namespace.getSchemaElement().length; j ++){
							SchemaElementType type = namespace.getSchemaElement(j);
							if(type.getType().equals(qname.getLocalPart())){
								SchemaInformation info = new SchemaInformation(namespace,type);
								return info;
							}
						}
					}
				}
			}
		}
		return null;
	}


	public ServiceDescription getServiceDescriptor() {
		return introService;
	}

}
