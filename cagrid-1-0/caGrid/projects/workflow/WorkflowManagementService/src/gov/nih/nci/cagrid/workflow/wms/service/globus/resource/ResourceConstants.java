package gov.nih.nci.cagrid.workflow.wms.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://workflow.cagrid.nci.nih.gov/wms/WorkflowManagementService";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "WorkflowManagementServiceKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "WorkflowManagementServiceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
