package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.beans.IntroduceService;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.util.Properties;

public class ServiceInformation {
	
	private IntroduceService introService;
	
	private Properties serviceProperties;
	
	public ServiceInformation(IntroduceService service, Properties properties) {
		this.introService = service;
		this.serviceProperties = properties;
	}
	
	public ServiceMetadataListType getMetadata() {
		return introService.getServiceMetadataList();
	}

	public void setMetadata(ServiceMetadataListType metadata) {
		introService.setServiceMetadataList(metadata);
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
