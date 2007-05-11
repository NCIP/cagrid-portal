package gov.nih.nci.cagrid.wsenum.common;

import javax.xml.namespace.QName;

/** 
 *  WsEnumConstants
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 15, 2006 
 * @version $Id$ 
 */
public class WsEnumConstants {
	// Namespace URI for WS-Enumeration
	public static final String WS_ENUMERATION_URI = "http://schemas.xmlsoap.org/ws/2004/09/enumeration";

	// package name for generated beans of WS-Enumeration
	public static final String ENUMERATION_PACKAGE_NAME = "org.xmlsoap.schemas.ws._2004._09.enumeration";

	// package name for beans from addressing namespace used by WS-Enumeration
	public static final String ADDRESSING_PACKAGE_NAME = "org.globus.addressing";
	
	// schema names
	public static final String ENUMERATION_WSDL_NAME = "enumeration.wsdl";
	public static final String ENUMERATION_XSD_NAME = "enumeration.xsd";
	public static final String ADDRESSING_XSD_NAME = "addressing.xsd";
	
	// WSDL port type
	public static final String PORT_TYPE_NAME = "DataSource";
	
	// type for enumerate response
	public static final String ENUMERATE_RESPONSE_TYPE = "EnumerateResponse";
	
	// QName for enumerate response message
	public static final QName ENUMERATE_RESPONSE_MESSAGE_QNAME = new QName(WS_ENUMERATION_URI, "EnumerateResponseMessage");
    
    // caGrid enumeration service context
    public static final String CAGRID_ENUMERATION_SERVICE_NAME = "CaGridEnumeration";
    public static final String CAGRID_ENUMERATION_SERVICE_PACKAGE = "gov.nih.nci.cagrid.enumeration";
    public static final String CAGRID_ENUMERATION_SERVICE_NAMESPACE = 
        "http://" + CAGRID_ENUMERATION_SERVICE_PACKAGE + 
        "/" + CAGRID_ENUMERATION_SERVICE_NAME;
}
