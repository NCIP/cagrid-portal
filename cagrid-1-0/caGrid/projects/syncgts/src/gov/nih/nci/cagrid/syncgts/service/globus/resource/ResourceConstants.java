package gov.nih.nci.cagrid.syncgts.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/SyncGTS/SyncGTS";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "SyncGTSKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "SyncGTSResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
