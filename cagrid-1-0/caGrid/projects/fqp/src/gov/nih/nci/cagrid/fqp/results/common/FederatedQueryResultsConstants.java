package gov.nih.nci.cagrid.fqp.results.common;

import javax.xml.namespace.QName;


public interface FederatedQueryResultsConstants {
	public static final String SERVICE_NS = "http://fqp.cagrid.nci.nih.gov/FederatedResults";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "FederatedQueryResultsKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "FederatedQueryResultsResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName CURRENTTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "CurrentTime");
	public static final QName TERMINATIONTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime");
	
}
