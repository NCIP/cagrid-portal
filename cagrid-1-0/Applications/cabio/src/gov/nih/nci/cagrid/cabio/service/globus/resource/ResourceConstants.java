package gov.nih.nci.cagrid.cabio.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cabio.cagrid.nci.nih.gov/CaBIOSvc";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "CaBIOSvcKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "CaBIOSvcResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName DOMAINMODEL_MD_RP = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel");
	
}
