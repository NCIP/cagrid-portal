package gov.nih.nci.cagrid.introduce;

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
	public static final QName INTRODUCE_SKELETON_QNAME = new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton");
	
	public static final String INTRODUCE_SINGLETON_RESOURCE = "singleton";
	public static final String INTRODUCE_MAIN_RESOURCE = "main";
	public static final String INTRODUCE_BASE_RESOURCE = "base";
	
	// deployment properties
	public static final String DEPLOY_PROPERTIES_FILE = "deploy.properties";
	
	// introduce specific constants
	public static final String INTRODUCE_VERSION = "0.96";
	public static final String INTRODUCE_XML_FILE = "introduce.xml";
	
	// w3c namespaces
	public static final String W3CNAMESPACE = "http://www.w3.org/2001/XMLSchema";
	public static final String W3CNAMESPACE_PREFIX = "xs";
	
	private IntroduceConstants() {
		// prevents instantiation
	}
}
