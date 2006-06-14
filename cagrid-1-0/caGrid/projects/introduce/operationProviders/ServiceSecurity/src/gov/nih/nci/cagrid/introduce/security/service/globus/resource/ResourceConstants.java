package gov.nih.nci.cagrid.introduce.security.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity/ServiceSecurity";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "ServiceSecurityKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "ServiceSecurityResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
