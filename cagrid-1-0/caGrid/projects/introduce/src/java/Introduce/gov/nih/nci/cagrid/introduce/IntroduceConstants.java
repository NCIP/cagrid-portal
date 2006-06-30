package gov.nih.nci.cagrid.introduce;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * Constants used in introduce
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin </A>
 */
public class IntroduceConstants {
	
	public static final String SERVICE_SECURITY_METADATA_METHOD = "getServiceSecurityMetadata";

	// service skeleton properties
	public static final String INTRODUCE_PROPERTIES_FILE = "introduce.properties";

	public static final String INTRODUCE_SKELETON_DESTINATION_DIR = "introduce.skeleton.destination.dir";

	public static final String INTRODUCE_SKELETON_SERVICE_NAME = "introduce.skeleton.service.name";

	public static final String INTRODUCE_SKELETON_PACKAGE_DIR = "introduce.skeleton.package.dir";

	public static final String INTRODUCE_SKELETON_EXTENSIONS = "introduce.skeleton.extensions";

	public static final String NAMESPACE2PACKAGE_MAPPINGS_FILE = "namespace2package.mappings";

	public static final String INTRODUCE_SKELETON_TIMESTAMP = "introduce.skeleton.timestamp";

	public static final String INTRODUCE_SKELETON_SERVICES_LIST = "introduce.skeleton.services.list";

	public static final String INTRODUCE_SKELETON_NAMESPACE_DOMAIN = "introduce.skeleton.namespace.domain";

	public static final String INTRODUCE_SKELETON_PACKAGE = "introduce.skeleton.package";

	public static final String INTRODUCE_SERVICE_PROPERTIES = "service.properties";

	public static final String INTRODUCE_NS_EXCLUDES = "introduce.ns.excludes";

	public static final QName INTRODUCE_SKELETON_QNAME = new QName(
			"gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton");

	// resource types
	public static final String INTRODUCE_SINGLETON_RESOURCE = "singleton";

	public static final String INTRODUCE_MAIN_RESOURCE = "main";

	public static final String INTRODUCE_BASE_RESOURCE = "base";

	// deployment properties
	public static final String DEPLOY_PROPERTIES_FILE = "deploy.properties";

	// introduce specific constants
	public static final String INTRODUCE_VERSION = "1.0b";

	public static final String INTRODUCE_XML_FILE = "introduce.xml";

	// w3c namespaces
	public static final String W3CNAMESPACE = "http://www.w3.org/2001/XMLSchema";

	public static final String W3CNAMESPACE_PREFIX = "xs";

	public static final String WSADDRESING_NAMESPACE = "http://schemas.xmlsoap.org/ws/2004/03/addressing";
	public static final String WSADDRESING_EPR_TYPE = "EndpointReference";
	public static final String WSADDRESSING_EPR_CLASSNAME = "org.apache.axis.message.addressing.EndpointReferenceType";

	public static final String WSADDRESING_LOCATION = ".." + File.separator + "ws" + File.separator + "addressing" + File.separator + "WS-Addressing.xsd";

	public static final List GLOBUS_NAMESPACES = new ArrayList(
			Arrays
					.asList(new String[] {
							WSADDRESING_NAMESPACE,
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd",
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.wsdl",
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd",
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.wsdl",
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd",
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ServiceGroup-1.2-draft-01.xsd",
							"http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ServiceGroup-1.2-draft-01.wsdl",
							"http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd",
							"http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.wsdl",
							"http://schemas.xmlsoap.org/ws/2004/04/trust",
							"http://schemas.xmlsoap.org/ws/2002/12/policy",
							"http://schemas.xmlsoap.org/ws/2002/07/utility",
							"http://schemas.xmlsoap.org/ws/2004/04/sc",
							"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
							"http://www.w3.org/2000/09/xmldsig#",
							"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" }));

	private IntroduceConstants() {
		// prevents instantiation
	}
}
