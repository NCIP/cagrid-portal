package gov.nih.nci.cagrid.fqp.common;

import javax.xml.namespace.QName;


public interface FederatedQueryProcessorConstants {
	public static final String SERVICE_NS = "http://fqp.cagrid.nci.nih.gov/FederatedQueryProcessor";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "FederatedQueryProcessorKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "FederatedQueryProcessorResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName SERVICEMETADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
}
