package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.util.Properties;

public class ServiceInformation {

	private ServiceMetadataListType metadata;
	
	private MethodsType methods;
	
	private Properties serviceProperties;
	
	public ServiceInformation(MethodsType methods , ServiceMetadataListType metadata, Properties properties) {
		this.methods = methods;
		this.metadata = metadata;
		this.serviceProperties = properties;
	}
	
	public ServiceMetadataListType getMetadata() {
		return metadata;
	}

	public void setMetadata(ServiceMetadataListType metadata) {
		this.metadata = metadata;
	}

	public Properties getServiceProperties() {
		return serviceProperties;
	}

	public void setServiceProperties(Properties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	public MethodsType getMethods() {
		return methods;
	}

	public void setMethods(MethodsType methods) {
		this.methods = methods;
	}
	
}
