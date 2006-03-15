package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;

import java.io.File;
import java.util.Properties;

/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
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
	
	public ServiceSecurity getServiceSecurity(){
		return this.introService.getServiceSecurity();
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}
	
	
}
