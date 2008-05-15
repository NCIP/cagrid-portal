package org.cagrid.gaards.authentication.common;

import javax.xml.namespace.QName;


public interface AuthenticationServiceConstants {
	public static final String SERVICE_NS = "http://authentication.gaards.cagrid.org/AuthenticationService";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "AuthenticationServiceKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "AuthenticationServiceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName SERVICEMETADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	public static final QName AUTHENTICATIONPROFILES = new QName("http://gaards.cagrid.org/authentication", "AuthenticationProfiles");
	
}
