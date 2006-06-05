package gov.nih.nci.cagrid.fqp.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/cagrid/fqp/FederatedQueryPlan";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "FederatedQueryPlanKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "FederatedQueryPlanResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
