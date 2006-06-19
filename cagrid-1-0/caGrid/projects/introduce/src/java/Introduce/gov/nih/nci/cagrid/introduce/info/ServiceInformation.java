package gov.nih.nci.cagrid.introduce.info;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;

import java.io.File;
import java.util.Properties;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ServiceInformation {

	private ServiceDescription introService;

	private Properties introduceServiceProperties;

	private File baseDirectory;


	public ServiceInformation(ServiceDescription service, Properties properties, File baseDirectory) {
		this.introService = service;
		this.introduceServiceProperties = properties;
		this.baseDirectory = baseDirectory;
	}

	public Properties getIntroduceServiceProperties() {
		return introduceServiceProperties;
	}


	public void setServiceProperties(Properties serviceProperties) {
		this.introduceServiceProperties = serviceProperties;
	}


	public ServicesType getServices() {
		return introService.getServices();
	}


	public void setServices(ServicesType services) {
		this.introService.setServices(services);
	}


	public NamespacesType getNamespaces() {
		return introService.getNamespaces();
	}


	public void setNamespaces(NamespacesType namespaces) {
		this.introService.setNamespaces(namespaces);
	}


	public ExtensionsType getExtensions() {
		return introService.getExtensions();
	}


	public void setExtensions(ExtensionsType extensions) {
		this.introService.setExtensions(extensions);
	}
	
	public ServiceProperties getServiceProperties() {
		return this.introService.getServiceProperties();
	}


	public void setServiceProperties(ServiceProperties serviceProperties) {
		this.introService.setServiceProperties(serviceProperties);
	}
	

	public File getBaseDirectory() {
		return baseDirectory;
	}
	

	public ServiceDescription getServiceDescriptor() {
		return introService;
	}



}
