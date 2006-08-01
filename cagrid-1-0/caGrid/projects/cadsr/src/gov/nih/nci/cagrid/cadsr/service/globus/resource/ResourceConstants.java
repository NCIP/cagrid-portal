package gov.nih.nci.cagrid.cadsr.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cadsr.cagrid.nci.nih.gov";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "CaDSRServiceKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "CaDSRServiceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName SERVICEMETADATA_MD_RP = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
}
