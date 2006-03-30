package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.creator.extension.CreationExtensionPostProcessor;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;

/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class CommonTools {

	public static Process createAndOutputProcess(String cmd) throws Exception {
		final Process p;

		p = Runtime.getRuntime().exec(cmd);
		StreamGobbler errGobbler = new StreamGobbler(p.getErrorStream(), "ERR");
		StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "OUT");
		errGobbler.start();
		outGobbler.start();

		return p;
	}


	public static String getAntCommand(String antCommand, String buildFileDir) throws Exception {
		String cmd = " " + antCommand;
		cmd = getAntCommandCall(buildFileDir) + cmd;
		return cmd;
	}


	public static String getAntAllCommand(String buildFileDir) throws Exception {
		return getAntCommand("all", buildFileDir);
	}


	public static String getAntFlattenCommand(String buildFileDir) throws Exception {
		return getAntCommand("flatten", buildFileDir);
	}


	public static String getAntDeployTomcatCommand(String buildFileDir) throws Exception {
		return getAntCommand("deployTomcat", buildFileDir);
	}


	public static String getAntDeployGlobusCommand(String buildFileDir) throws Exception {
		return getAntCommand("deployGlobus", buildFileDir);
	}

	public static String getAntSkeletonCreationCommand(String buildFileDir, String name, String dir,
		String packagename, String namespacedomain) throws Exception {
		// fix dir path if it relative......
		File dirF = new File(dir);
		if (!dirF.isAbsolute()) {
			dir = buildFileDir + File.separator + dir;
		}
		String cmd = " -Dintroduce.skeleton.destination.dir=" + dir + " -Dintroduce.skeleton.service.name=" + name
			+ " -Dintroduce.skeleton.package=" + packagename + " -Dintroduce.skeleton.package.dir="
			+ packagename.replace('.', File.separatorChar) + " -Dintroduce.skeleton.namespace.domain="
			+ namespacedomain + " createService";
		cmd = getAntCommandCall(buildFileDir) + cmd;
		return cmd;
	}


	static String getAntCommandCall(String buildFileDir) throws Exception {
		String os = System.getProperty("os.name");
		String cmd = "";
		if ((os.indexOf("Windows") >= 0) || (os.indexOf("windows") >= 0)) {
			cmd = "-classpath \"" + CommonTools.getAntLauncherJarLocation(System.getProperty("java.class.path"), true)
				+ "\" org.apache.tools.ant.launch.Launcher -lib \"" + System.getProperty("java.class.path")
				+ "\" -buildfile " + "\"" + buildFileDir + File.separator + "build.xml\"" + cmd;
			cmd = "java.exe " + cmd;
		} else if ((os.indexOf("Linux") >= 0) || (os.indexOf("linux") >= 0)) {
			// escape out the spaces.....
			buildFileDir = buildFileDir.replaceAll("\\s", "\\ ");
			cmd = "-classpath " + CommonTools.getAntLauncherJarLocation(System.getProperty("java.class.path"), false)
				+ " org.apache.tools.ant.launch.Launcher -lib " + System.getProperty("java.class.path")
				+ " -buildfile " + buildFileDir + File.separator + "build.xml" + cmd;
			cmd = "java " + cmd;
		} else {
			throw new Exception("Cannot create grid service, your operatingsystem, " + os + " is not supported.");
		}
		return cmd;
	}


	static String getAntLauncherJarLocation(String path, boolean isWindows) {
		String separator = isWindows ? ";" : ":";
		StringTokenizer pathTokenizer = new StringTokenizer(path, separator);
		while (pathTokenizer.hasMoreTokens()) {
			String pathElement = pathTokenizer.nextToken();
			if (pathElement.indexOf("ant-launcher") != -1 && pathElement.endsWith(".jar")) {
				return pathElement;
			}
		}
		return null;
	}


	public static String getPackageName(Namespace namespace) {
		try {
			// TODO: where should this mapperClassname preference be set
			String mapperClassname = "gov.nih.nci.cagrid.introduce.common.CaBIGNamespaceToPackageMapper";
			Class clazz = Class.forName(mapperClassname);
			NamespaceToPackageMapper mapper = (NamespaceToPackageMapper) clazz.newInstance();
			return mapper.getPackageName(namespace.getRaw());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean equals(ServiceSecurity ss, MethodSecurity ms) {
		if ((ss == null) && (ms == null)) {
			return true;
		} else if ((ss != null) && (ms == null)) {
			return false;
		} else if ((ss == null) && (ms != null)) {
			return false;
		} else if (!Utils.equals(ss.getSecuritySetting(), ms.getSecuritySetting())) {
			return false;
		} else if (!Utils.equals(ss.getAnonymousClients(), ms.getAnonymousClients())) {
			return false;
		} else if (!Utils.equals(ss.getClientAuthorization(), ms.getClientAuthorization())) {
			return false;
		} else if (!Utils.equals(ss.getClientCommunication(), ms.getClientCommunication())) {
			return false;
		} else if (!Utils.equals(ss.getDelegationMode(), ms.getDelegationMode())) {
			return false;
		} else if (!Utils.equals(ss.getSecureConversation(), ms.getSecureConversation())) {
			return false;
		} else if (!Utils.equals(ss.getSecureMessage(), ms.getSecureMessage())) {
			return false;
		} else if (!Utils.equals(ss.getTransportLevelSecurity(), ms.getTransportLevelSecurity())) {
			return false;
		} else {
			return true;
		}
	}

	
	public static NamespaceType createNamespaceType(String xsdFilename) throws MobiusException {
		NamespaceType namespaceType = new NamespaceType();
		namespaceType.setLocation(xsdFilename);
		Document schemaDoc = XMLUtilities.fileNameToDocument(xsdFilename);
		
		String rawNamespace = schemaDoc.getRootElement().getAttributeValue("targetNamespace");
		Namespace namespace = new Namespace(rawNamespace);
		String packageName = getPackageName(namespace);
		namespaceType.setPackageName(packageName);
		
		namespaceType.setNamespace(namespace.getRaw());
		
		List elementTypes = schemaDoc.getRootElement().getChildren("element", schemaDoc.getRootElement().getNamespace());
		SchemaElementType[] schemaTypes = new SchemaElementType[elementTypes.size()];
		for (int i = 0; i < elementTypes.size(); i++) {
			Element element = (Element) elementTypes.get(i);
			SchemaElementType type = new SchemaElementType();
			type.setType(element.getAttributeValue("name"));
			schemaTypes[i] = type;
		}
		namespaceType.setSchemaElement(schemaTypes);
		return namespaceType;
	}
	
	public static CreationExtensionPostProcessor getCreationPostProcessor(String extensionName){
		return null;
	}
	
	public static CodegenExtensionPostProcessor getCodegenPostProcessor(String extensionName){
		return null;
	}
	
	public static CodegenExtensionPreProcessor getCodegenPreProcessor(String extensionName){
		return null;
	}
}
