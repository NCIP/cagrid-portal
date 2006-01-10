package gov.nih.nci.cagrid.introduce.codegen.metadata;

import java.util.Properties;

import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;

public class TemplateObjectContainer {

	private ServiceMetadataListType metadata;
	
	private Properties serviceProperties;
	
	public TemplateObjectContainer(ServiceMetadataListType metadata, Properties properties) {
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
	
}
