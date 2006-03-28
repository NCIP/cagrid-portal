package gov.nih.nci.cagrid.gts.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/GridTrustService";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "GridTrustServiceKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "GridTrustServiceResourceProperties");

	// Service level metadata (exposed as resouce properties)

}
