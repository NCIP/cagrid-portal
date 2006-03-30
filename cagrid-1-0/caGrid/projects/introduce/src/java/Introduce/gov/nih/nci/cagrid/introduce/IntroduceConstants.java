package gov.nih.nci.cagrid.introduce;

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
	public static final String NAMESPACE2PACKAGE_MAPPINGS_FILE = "namespace2package.mappings";
	public static final String INTRODUCE_SKELETON_TIMESTAMP = "introduce.skeleton.timestamp";
	public static final String INTRODUCE_SKELETON_NAMESPACE_DOMAIN = "introduce.skeleton.namespace.domain";
	public static final String INTRODUCE_SKELETON_PACKAGE = "introduce.skeleton.package";
	public static final String INTRODUCE_NS_EXCLUDES = "introduce.ns.excludes";
	
	// deployment properties
	public static final String DEPLOY_PROPERTIES_FILE = "deploy.properties";
	
	// introduce specific constants
	public static final String INTRODUCE_VERSION = "0.95";
	public static final String INTRODUCE_XML_FILE = "introduce.xml";
	
	// w3c namespaces
	public static final String W3CNAMESPACE = "http://www.w3.org/2001/XMLSchema";
	public static final String W3CNAMESPACE_PREFIX = "xs";
	
	private IntroduceConstants() {
		// prevents instantiation
	}
}
