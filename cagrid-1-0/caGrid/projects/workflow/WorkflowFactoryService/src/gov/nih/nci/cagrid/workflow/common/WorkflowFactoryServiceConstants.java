package gov.nih.nci.cagrid.workflow.common;

import javax.xml.namespace.QName;


public interface WorkflowFactoryServiceConstants {
	public static final String SERVICE_NS = "http://workflow.cagrid.nci.nih.gov/WorkflowFactoryService";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "WorkflowFactoryServiceKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "WorkflowFactoryServiceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
