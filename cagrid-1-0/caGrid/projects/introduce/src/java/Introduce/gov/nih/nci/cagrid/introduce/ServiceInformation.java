package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.util.Properties;

public class ServiceInformation {
	
	private ServiceDescription introService;
	
	private Properties serviceProperties;
	
	public ServiceInformation(ServiceDescription service, Properties properties) {
		this.introService = service;
		this.serviceProperties = properties;
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
	
	
}
