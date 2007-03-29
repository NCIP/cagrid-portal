package gov.nih.nci.cagrid.gridgrouper.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/GridGrouper";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "GridGrouperKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "GridGrouperResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName SERVICEMETADATA_MD_RP = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
}
