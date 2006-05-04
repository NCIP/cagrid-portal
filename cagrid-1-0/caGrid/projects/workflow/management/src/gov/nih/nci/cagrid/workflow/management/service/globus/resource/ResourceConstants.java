package gov.nih.nci.cagrid.workflow.management.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://workflow.cagrid.nci.nih.gov//WorkFlowManagementService";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "WorkFlowManagementServiceKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "WorkFlowManagementServiceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
