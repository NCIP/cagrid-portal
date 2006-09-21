package gov.nih.nci.cagrid.fqp.results.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://fqp.cagrid.nci.nih.gov/FederatedResults";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "FederatedQueryResultsKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "FederatedQueryResultsResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
