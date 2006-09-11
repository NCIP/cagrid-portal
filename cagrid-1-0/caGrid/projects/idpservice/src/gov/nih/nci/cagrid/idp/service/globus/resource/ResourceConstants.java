package gov.nih.nci.cagrid.idp.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/IDPService/IDPService";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "IDPServiceKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "IDPServiceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
