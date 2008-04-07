package org.cagrid.gme.common;

import javax.xml.namespace.QName;


public interface GlobalModelExchangeConstants {
	public static final String SERVICE_NS = "http://gme.cagrid.org/GlobalModelExchange";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "GlobalModelExchangeKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "GlobalModelExchangeResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName SERVICEMETADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
}
