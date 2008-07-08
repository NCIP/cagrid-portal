package gov.nih.nci.cagrid.workflow.context.common;

import javax.xml.namespace.QName;


public interface WorkflowServiceImplConstants {
	public static final String SERVICE_NS = "http://workflow.cagrid.nci.nih.gov/WorkflowServiceImpl";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "WorkflowServiceImplKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "WorkflowServiceImplResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName CURRENTTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "CurrentTime");
	public static final QName TERMINATIONTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime");
	public static final QName WORKFLOWSTATUSELEMENT = new QName("http://types.workflow.cagrid.nci.nih.gov/WorkflowManagementFactoryService", "WorkflowStatusElement");
	
}
