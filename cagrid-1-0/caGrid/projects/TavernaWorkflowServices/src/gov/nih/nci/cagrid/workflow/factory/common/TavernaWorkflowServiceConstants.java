package gov.nih.nci.cagrid.workflow.factory.common;

import javax.xml.namespace.QName;


public interface TavernaWorkflowServiceConstants {
	public static final String SERVICE_NS = "http://factory.workflow.cagrid.nci.nih.gov/TavernaWorkflowService";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "TavernaWorkflowServiceKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "TavernaWorkflowServiceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
