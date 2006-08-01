package gov.nih.nci.cagrid.gridgrouper.stem.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/GridGrouperStem";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "GridGrouperStemKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "GridGrouperStemResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName NONE_MD_RP = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "None");
	
}
