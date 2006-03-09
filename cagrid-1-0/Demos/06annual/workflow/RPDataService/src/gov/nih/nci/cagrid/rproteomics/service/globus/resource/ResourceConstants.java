package gov.nih.nci.cagrid.rproteomics.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://rproteomics.cagrid.nci.nih.gov/RPData";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "RPDataKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "RPDataResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
