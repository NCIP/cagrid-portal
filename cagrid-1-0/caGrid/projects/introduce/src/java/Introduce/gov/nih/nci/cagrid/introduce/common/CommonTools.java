package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;
import gov.nih.nci.cagrid.introduce.codegen.utils.TemplateUtils;
import gov.nih.nci.cagrid.introduce.info.SchemaInformation;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.portal.PortalResourceManager;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.parser.XSOMParser;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class CommonTools {

	public static final String ALLOWED_JAVA_CLASS_REGEX = "[A-Z]++[A-Za-z0-9\\_\\$]*";

	public static final String ALLOWED_JAVA_FIELD_REGEX = "[a-z\\_]++[A-Za-z0-9\\_\\$]*";


	public static Process createAndOutputProcess(String cmd) throws Exception {
		final Process p;

		p = Runtime.getRuntime().exec(cmd);
		StreamGobbler errGobbler = new StreamGobbler(p.getErrorStream(), "ERR");
		StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "OUT");
		errGobbler.start();
		outGobbler.start();

		return p;
	}


	public static List getProvidedNamespaces(File startDir) {
		List globusNamespaces = new ArrayList();
		File schemasDir = new File(startDir.getAbsolutePath() + File.separator + "share" + File.separator + "schema");

		CommonTools.getTargetNamespaces(globusNamespaces, schemasDir);
		return globusNamespaces;
	}


	public static File findSchema(String schemaNamespace, File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File curFile = files[i];
			if (curFile.isDirectory()) {
				File found = findSchema(schemaNamespace, curFile);
				if (found != null) {
					return found;
				}
			} else {
				if (curFile.getAbsolutePath().endsWith(".xsd") || curFile.getAbsolutePath().endsWith(".XSD")) {
					try {
						if (getTargetNamespace(curFile).equals(schemaNamespace)) {
							return curFile;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		return null;
	}


	public static void getTargetNamespaces(List namespaces, File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File curFile = files[i];
			if (curFile.isDirectory()) {
				getTargetNamespaces(namespaces, curFile);
			} else {
				if (curFile.getAbsolutePath().endsWith(".xsd") || curFile.getAbsolutePath().endsWith(".XSD")) {
					try {
						namespaces.add(getTargetNamespace(curFile));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}


	public static String getTargetNamespace(File file) throws Exception {
		Document doc = XMLUtilities.fileNameToDocument(file.getAbsolutePath());
		return doc.getRootElement().getAttributeValue("targetNamespace");

	}


	public static boolean isValidPackageName(String packageName) {
		if (packageName.length() > 0) {
			StringTokenizer strtok = new StringTokenizer(packageName, ".", false);
			while (strtok.hasMoreElements()) {
				String packageItem = strtok.nextToken();
				if (!packageItem.matches(ALLOWED_JAVA_FIELD_REGEX)) {
					return false;
				}
			}
		}
		return true;
	}


	public static boolean isValidServiceName(String serviceName) {
		if (serviceName == null || serviceName.trim().equals("")) {
			return false;
		}

		if (serviceName.substring(0, 1).toLowerCase().equals(serviceName.substring(0, 1))) {
			return false;
		}
		if (!serviceName.matches(ALLOWED_JAVA_CLASS_REGEX)) {
			return false;
		}
		return true;

	}


	public static boolean isValidJavaField(String serviceName) {
		if (serviceName.length() > 0) {
			if (!serviceName.matches(ALLOWED_JAVA_FIELD_REGEX)) {
				return false;
			}
		}
		return true;
	}


	public static String getAntCommand(String antCommand, String buildFileDir) throws Exception {
		String cmd = " " + antCommand;
		cmd = getAntCommandCall(buildFileDir) + cmd;
		return cmd;
	}


	public static String getAntAllCommand(String buildFileDir) throws Exception {
		return getAntCommand("all", buildFileDir);
	}


	public static String getAntMergeCommand(String buildFileDir) throws Exception {
		return getAntCommand("merge", buildFileDir);
	}


	public static String getAntDeployTomcatCommand(String buildFileDir) throws Exception {
		String dir = buildFileDir;
		File dirF = new File(dir);
		if (!dirF.isAbsolute()) {
			dir = buildFileDir + File.separator + dir;
		}
		dir = fixPathforOS(dir);
		String cmd = " -Dservice.properties.file=" + dir + File.separator
			+ IntroduceConstants.INTRODUCE_SERVICE_PROPERTIES;
		cmd = getAntCommand("deployTomcat", buildFileDir) + " " + cmd;
		return cmd;
	}


	private static String fixPathforOS(String path) {
		String os = System.getProperty("os.name");
		if ((os.indexOf("Windows") >= 0) || (os.indexOf("windows") >= 0)) {
			path = "\"" + path + "\"";
		} else {
			path = path.replaceAll(" ", "\\ ");
		}
		return path;
	}


	public static String getAntDeployGlobusCommand(String buildFileDir) throws Exception {
		String dir = buildFileDir;
		File dirF = new File(dir);
		if (!dirF.isAbsolute()) {
			dir = buildFileDir + File.separator + dir;
		}
		dir = fixPathforOS(dir);

		String cmd = " -Dservice.properties.file=" + buildFileDir + File.separator
			+ IntroduceConstants.INTRODUCE_SERVICE_PROPERTIES;
		cmd = getAntCommand("deployGlobus", buildFileDir) + " " + cmd;
		return cmd;
	}


	public static String getAntSkeletonCreationCommand(String buildFileDir, String name, String dir,
		String packagename, String namespacedomain, String extensions) throws Exception {
		// fix dir path if it relative......
		System.out.println("CREATION: builddir: " + buildFileDir);
		System.out.println("CREATION: destdir: " + dir);
		File dirF = new File(dir);
		if (!dirF.isAbsolute()) {
			dir = buildFileDir + File.separator + dir;
		}
		dir = fixPathforOS(dir);
		String cmd = " -Dintroduce.skeleton.destination.dir=" + dir + " -Dintroduce.skeleton.service.name=" + name
			+ " -Dintroduce.skeleton.package=" + packagename + " -Dintroduce.skeleton.package.dir="
			+ packagename.replace('.', File.separatorChar) + " -Dintroduce.skeleton.namespace.domain="
			+ namespacedomain + " -Dintroduce.skeleton.extensions=" + extensions + " createService";
		cmd = getAntCommandCall(buildFileDir) + cmd;
		System.out.println("CREATION: cmd: " + cmd);
		return cmd;
	}


	public static String getAntSkeletonPostCreationCommand(String buildFileDir, String name, String dir,
		String packagename, String namespacedomain, String extensions) throws Exception {
		// fix dir path if it relative......
		System.out.println("CREATION: builddir: " + buildFileDir);
		System.out.println("CREATION: destdir: " + dir);
		File dirF = new File(dir);
		if (!dirF.isAbsolute()) {
			dir = buildFileDir + File.separator + dir;
		}
		dir = fixPathforOS(dir);
		String cmd = " -Dintroduce.skeleton.destination.dir=" + dir + " -Dintroduce.skeleton.service.name=" + name
			+ " -Dintroduce.skeleton.package=" + packagename + " -Dintroduce.skeleton.package.dir="
			+ packagename.replace('.', File.separatorChar) + " -Dintroduce.skeleton.namespace.domain="
			+ namespacedomain + " -Dintroduce.skeleton.extensions=" + extensions + " postCreateService";
		cmd = getAntCommandCall(buildFileDir) + cmd;
		System.out.println("CREATION: cmd: " + cmd);
		return cmd;
	}


	static String getAntCommandCall(String buildFileDir) throws Exception {
		String os = System.getProperty("os.name");
		String cmd = "";
		if ((os.indexOf("Windows") >= 0) || (os.indexOf("windows") >= 0)) {
			cmd = "-classpath \"" + CommonTools.getAntLauncherJarLocation(System.getProperty("java.class.path"), true)
				+ "\" org.apache.tools.ant.launch.Launcher -buildfile " + "\"" + buildFileDir + File.separator
				+ "build.xml\"" + cmd;
			cmd = "java.exe " + cmd;
		} else {
			// escape out the spaces.....
			buildFileDir = buildFileDir.replaceAll("\\s", "\\ ");
			cmd = "-classpath " + CommonTools.getAntLauncherJarLocation(System.getProperty("java.class.path"), false)
				+ " org.apache.tools.ant.launch.Launcher -buildfile " + buildFileDir + File.separator + "build.xml"
				+ cmd;
			cmd = "java " + cmd;
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


	public static String getPackageName(Namespace namespace, NamespacesType namespaceTypes) {

		// first check to see if this namespace is already in use....
		NamespaceType nsType = CommonTools.getNamespaceType(namespaceTypes, namespace.getRaw());
		if (nsType != null) {
			return nsType.getPackageName();
		} else {
			return getPackageName(namespace);
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


	/**
	 * This method will create a namespaceType fully populated with the schema
	 * elements. It will default the location to the relative path from the file
	 * name i.e. "./filename" Be sure to change the location if this file is not
	 * going to be in the toplevel schema/Servicename directory of your service.
	 * 
	 * @param relativeFromDirectory
	 * @param xsdFilenameFromRelativeDirectory
	 * @return
	 * @throws MobiusException
	 */
	public static NamespaceType createNamespaceType(String xsdFilename) throws MobiusException {
		NamespaceType namespaceType = new NamespaceType();
		namespaceType.setLocation("./" + new File(xsdFilename).getName());
		Document schemaDoc = XMLUtilities.fileNameToDocument(xsdFilename);

		String rawNamespace = schemaDoc.getRootElement().getAttributeValue("targetNamespace");
		Namespace namespace = new Namespace(rawNamespace);
		String packageName = getPackageName(namespace);
		namespaceType.setPackageName(packageName);

		namespaceType.setNamespace(namespace.getRaw());

		List elementTypes = schemaDoc.getRootElement()
			.getChildren("element", schemaDoc.getRootElement().getNamespace());
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


	public static ServiceType getService(ServicesType services, String name) {
		if (services != null && services.getService() != null) {
			for (int i = 0; i < services.getService().length; i++) {
				if (services.getService(i).getName().equals(name)) {
					return services.getService(i);
				}
			}
		}

		return null;
	}


	public static String methodTypeToString(MethodType method) {
		// assume its void to start with
		String output = "void";

		MethodTypeOutput outputType = method.getOutput();
		if (outputType != null) {
			// use classname if set, else use schema type
			if (outputType.getQName() != null && outputType.getQName().getLocalPart() != null
				&& !outputType.getQName().getLocalPart().trim().equals("")) {
				String name = org.apache.axis.wsdl.toJava.Utils
					.xmlNameToJavaClass(outputType.getQName().getLocalPart());
				if (name.indexOf("_") == 0) {
					name = name.substring(1);
				}
				output = name;
			}

			// add array notation if its an array
			if (outputType.isIsArray()) {
				output += "[]";
			}
		}

		String input = "";
		MethodTypeInputs inputs = method.getInputs();
		if (inputs != null) {
			MethodTypeInputsInput[] inputarr = inputs.getInput();
			if (inputarr != null) {
				for (int i = 0; i < inputarr.length; i++) {
					MethodTypeInputsInput inputType = inputarr[i];
					// use classname if set, else use schema type
					if (inputType.getQName() != null && inputType.getQName().getLocalPart() != null
						&& !inputType.getQName().getLocalPart().trim().equals("")) {
						if (!input.equals("")) {
							input += ", ";
						}
						String name = org.apache.axis.wsdl.toJava.Utils.xmlNameToJavaClass(inputType.getQName()
							.getLocalPart());
						if (name.indexOf("_") == 0) {
							name = name.substring(1);
						}
						input += name;
					} else {
						// why would this be the case?
						continue;
					}

					// add array notation if its an array
					if (inputType.isIsArray()) {
						input += "[]";
					}

					input += " " + inputType.getName();
				}
			}
		}

		output += "  " + method.getName() + "(" + input + ")";

		return output;
	}


	public static void importMethod(MethodTypeImportInformation importInformation, File fromDir, File toDir,
		String fromService, String toService, String methodName, boolean copyFiles) throws Exception {
		ServiceDescription fromintroService = (ServiceDescription) Utils.deserializeDocument(fromDir.getAbsolutePath()
			+ File.separator + "introduce.xml", ServiceDescription.class);

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(toDir.getAbsolutePath()
			+ File.separator + "introduce.xml", ServiceDescription.class);

		if (copyFiles) {
			File fromwsdl = new File(fromDir.getAbsolutePath() + File.separator + "schema" + File.separator
				+ fromService);
			File towsdl = new File(toDir.getAbsolutePath() + File.separator + "schema" + File.separator + toService);
			Utils.copyDirectory(fromwsdl, towsdl);

			File fromLibDir = new File(fromDir.getAbsolutePath() + File.separator + "lib");
			File toLibDir = new File(toDir.getAbsolutePath() + File.separator + "lib");

			Utils.copyDirectory(fromLibDir, toLibDir);
		}

		// copy over the namespaces from the imported service
		// make sure to warn on duplicates and remome them
		NamespacesType fromNamespaces = fromintroService.getNamespaces();
		int fromNamespacesLength = 0;
		if (fromNamespaces != null && fromNamespaces.getNamespace() != null) {
			fromNamespacesLength = fromNamespaces.getNamespace().length;
		}
		NamespacesType toNamespaces = introService.getNamespaces();
		int toNamespacesLength = 0;
		if (toNamespaces != null && toNamespaces.getNamespace() != null) {
			toNamespacesLength = toNamespaces.getNamespace().length;
		}

		List namespaces = new ArrayList();
		List usedNamespaces = new ArrayList();
		for (int i = 0; i < toNamespacesLength; i++) {
			if (!usedNamespaces.contains(toNamespaces.getNamespace(i).getNamespace())) {
				usedNamespaces.add(toNamespaces.getNamespace(i).getNamespace());
				namespaces.add(toNamespaces.getNamespace(i));
			}
		}
		for (int i = 0; i < fromNamespacesLength; i++) {
			if (!usedNamespaces.contains(fromNamespaces.getNamespace(i).getNamespace())) {
				usedNamespaces.add(fromNamespaces.getNamespace(i).getNamespace());
				namespaces.add(fromNamespaces.getNamespace(i));
			} else {
				System.err.println("WARNING: During Import: Namespace was already being used in the original service: "
					+ fromNamespaces.getNamespace(i).getNamespace());
			}
		}
		NamespaceType[] newNamespacesArr = new NamespaceType[namespaces.size()];
		namespaces.toArray(newNamespacesArr);
		NamespacesType newNamespaces = new NamespacesType();
		newNamespaces.setNamespace(newNamespacesArr);
		introService.setNamespaces(newNamespaces);

		// find the method and add it methods....
		MethodsType fromMethods = CommonTools.getService(fromintroService.getServices(), fromService).getMethods();
		MethodType foundMethod = null;
		if (fromMethods != null && fromMethods.getMethod() != null) {
			boolean found = false;
			for (int i = 0; i < fromMethods.getMethod().length; i++) {
				foundMethod = fromMethods.getMethod(i);
				if (foundMethod.getName().equals(methodName)) {
					found = true;
					break;
				}
			}
			if (found != true) {
				throw new Exception("Method " + methodName + " was not found in imported service");
			}

		} else {
			throw new Exception("Imported service was supposed to have methods.....");
		}

		MethodsType methodsType = CommonTools.getService(introService.getServices(), toService).getMethods();

		foundMethod.setIsImported(true);
		foundMethod.setImportInformation(importInformation);

		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		MethodType[] newMethods;
		int newLength = 0;
		if (methodsType != null && methodsType.getMethod() != null) {
			newLength = methodsType.getMethod().length + 1;
			newMethods = new MethodType[newLength];
			System.arraycopy(methodsType.getMethod(), 0, newMethods, 0, methodsType.getMethod().length);
		} else {
			newLength = 1;
			newMethods = new MethodType[newLength];
		}
		MethodsType newmethodsType = new MethodsType();
		newMethods[newLength - 1] = foundMethod;
		newmethodsType.setMethod(newMethods);
		CommonTools.getService(introService.getServices(), toService).setMethods(newmethodsType);

		Utils.serializeDocument(toDir.getAbsolutePath() + File.separator + IntroduceConstants.INTRODUCE_XML_FILE,
			introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);

	}


	public static String getPackageDir(ServiceType service) {
		return service.getPackageName().replace('.', File.separatorChar);
	}


	public static NamespaceType getNamespaceType(NamespacesType namespacesType, String namespaceURI) {
		if (namespacesType != null && namespacesType.getNamespace() != null) {
			NamespaceType[] namespaces = namespacesType.getNamespace();
			for (int i = 0; i < namespaces.length; i++) {
				NamespaceType namespace = namespaces[i];
				if (namespace.getNamespace().equals(namespaceURI)) {
					return namespace;
				}
			}
		}
		return null;
	}


	public static SchemaInformation getSchemaInformation(NamespacesType namespacesType, QName qname) {
		if (namespacesType != null && namespacesType.getNamespace() != null) {
			NamespaceType[] namespaces = namespacesType.getNamespace();
			for (int i = 0; i < namespaces.length; i++) {
				NamespaceType namespace = namespaces[i];
				if (namespace.getNamespace().equals(qname.getNamespaceURI())) {
					if (namespace.getSchemaElement() != null) {
						for (int j = 0; j < namespace.getSchemaElement().length; j++) {
							SchemaElementType type = namespace.getSchemaElement(j);
							if (type.getType().equals(qname.getLocalPart())) {
								SchemaInformation info = new SchemaInformation(namespace, type);
								return info;
							}
						}
					}
				}
			}
		}
		return null;
	}


	public static void addMethod(ServiceType service, MethodType method) {
		MethodType[] methodsArray = null;
		int length = 0;
		if (service.getMethods() != null && service.getMethods().getMethod() != null) {
			length = service.getMethods().getMethod().length + 1;
		} else {
			length = 1;
		}
		methodsArray = new MethodType[length];
		if (length > 1) {
			System.arraycopy(service.getMethods().getMethod(), 0, methodsArray, 0, length - 1);
		}
		methodsArray[length - 1] = method;
		MethodsType methods = null;
		if (service.getMethods() == null) {
			methods = new MethodsType();
			service.setMethods(methods);
		} else {
			methods = service.getMethods();
		}
		methods.setMethod(methodsArray);
	}


	public static void removeMethod(MethodsType methodsType, MethodType method) {
		MethodType[] newMethods = new MethodType[methodsType.getMethod().length - 1];
		int newMethodsI = 0;
		for (int i = 0; i < methodsType.getMethod().length; i++) {
			MethodType tmethod = methodsType.getMethod(i);
			if (!(tmethod.equals(method))) {
				newMethods[newMethodsI] = tmethod;
				newMethodsI++;
			}
		}
		methodsType.setMethod(newMethods);
	}


	public static void addNamespace(ServiceDescription serviceD, NamespaceType nsType) {
		NamespaceType[] namespacesArray = null;
		int length = 0;
		if (serviceD.getNamespaces() != null && serviceD.getNamespaces().getNamespace() != null) {
			length = serviceD.getNamespaces().getNamespace().length + 1;
		} else {
			length = 1;
		}
		namespacesArray = new NamespaceType[length];
		if (length > 1) {
			System.arraycopy(serviceD.getNamespaces().getNamespace(), 0, namespacesArray, 0, length - 1);
		}
		namespacesArray[length - 1] = nsType;
		NamespacesType namespaces = null;
		if (serviceD.getNamespaces() == null) {
			namespaces = new NamespacesType();
			serviceD.setNamespaces(namespaces);
		} else {
			namespaces = serviceD.getNamespaces();
		}
		namespaces.setNamespace(namespacesArray);
	}


	/**
	 * Define a unique name for use as a variable for the metadata at the
	 * specified index given the scope of the ServiceMetadataListType.
	 * 
	 * @param metadataList
	 *            the list of metadata
	 * @param index
	 *            the index into the metadata list of the targeted metadata item
	 * @return the variable name to use
	 */
	public static String getResourcePropertyVariableName(ResourcePropertiesListType metadataList, int index) {
		String baseName = metadataList.getResourceProperty(index).getQName().getLocalPart();

		int previousNumber = 0;
		for (int i = 0; (i < index && i < metadataList.getResourceProperty().length); i++) {
			ResourcePropertyType metadata = metadataList.getResourceProperty()[i];
			if (metadata.getQName().getLocalPart().equalsIgnoreCase(baseName)) {
				// the qname local parts are the same for multiple qnames
				// resolve the issue by appending a number
				previousNumber++;
			}
		}

		// return the orginal name, if it is unique, otherwise append a number
		return TemplateUtils.lowerCaseFirstCharacter(baseName
			+ ((previousNumber > 0) ? String.valueOf(previousNumber) : ""));
	}


	/**
	 * Sets a service property on the service information. If no service
	 * properties are found, a new array of properties is created and
	 * initialized with a single property containing the key and value
	 * specified. If the property is found to exist in the service, it's value
	 * is changed to the one specified.
	 * 
	 * @param info
	 *            The service information to set a property on
	 * @param key
	 *            The key of the service property to set
	 * @param value
	 *            The value to associate with the property key
	 */
	public static void setServiceProperty(ServiceInformation info, String key, String value, boolean isFromETC) {
		ServiceProperties props = info.getServiceProperties();
		if (props == null) {
			props = new ServiceProperties();
			info.setServiceProperties(props);
		}
		ServicePropertiesProperty[] allProperties = props.getProperty();
		if (allProperties == null) {
			allProperties = new ServicePropertiesProperty[]{new ServicePropertiesProperty(new Boolean(isFromETC), key,
				value)};
		} else {
			boolean found = false;
			for (int i = 0; i < allProperties.length; i++) {
				if (allProperties[i].getKey().equals(key)) {
					allProperties[i].setValue(value);
					allProperties[i].setIsFromETC(new Boolean(isFromETC));
					found = true;
					break;
				}
			}
			if (!found) {
				ServicePropertiesProperty[] tmpProperties = new ServicePropertiesProperty[allProperties.length + 1];
				System.arraycopy(allProperties, 0, tmpProperties, 0, allProperties.length);
				tmpProperties[tmpProperties.length - 1] = new ServicePropertiesProperty(new Boolean(isFromETC), key,
					value);
				allProperties = tmpProperties;
			}
		}
		props.setProperty(allProperties);
	}


	/**
	 * Determines if a service information object contains the specified service
	 * property
	 * 
	 * @param info
	 *            The service information
	 * @param key
	 *            The property to check for
	 * @return True if a property with the key name is found, false otherwise
	 */
	public static boolean servicePropertyExists(ServiceInformation info, String key) {
		if (info.getServiceProperties() != null && info.getServiceProperties().getProperty() != null) {
			ServicePropertiesProperty[] props = info.getServiceProperties().getProperty();
			for (int i = 0; i < props.length; i++) {
				if (props[i].getKey().equals(key)) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Gets the value of a service property from service information
	 * 
	 * @param info
	 *            The service information to pull a property value from
	 * @param key
	 *            The key of the property value to find
	 * @return The value of the property
	 * @throws Exception
	 *             If no property with the specified key is found
	 */
	public static String getServicePropertyValue(ServiceInformation info, String key) throws Exception {
		if (info.getServiceProperties() != null && info.getServiceProperties().getProperty() != null) {
			ServicePropertiesProperty[] props = info.getServiceProperties().getProperty();
			for (int i = 0; i < props.length; i++) {
				if (props[i].getKey().equals(key)) {
					return props[i].getValue();
				}
			}
		}
		throw new Exception("No such property: " + key);
	}


	/**
	 * Removes a service property from service information
	 * 
	 * @param info
	 *            The service information to remove a property from
	 * @param key
	 *            The key name of the property to remove
	 * @return True if the property existed and was removed, false otherwise
	 */
	public static boolean removeServiceProperty(ServiceInformation info, String key) {
		ServicePropertiesProperty[] newProperties = new ServicePropertiesProperty[info.getServiceProperties()
			.getProperty().length];
		int newIndex = 0;
		boolean removed = false;
		for (int i = 0; i < info.getServiceProperties().getProperty().length; i++) {
			ServicePropertiesProperty current = info.getServiceProperties().getProperty(i);
			if (!current.getKey().equals(key)) {
				newProperties[newIndex] = current;
				newIndex++;
			} else {
				removed = true;
			}
		}
		return removed;
	}


	/**
	 * Determines if schema element types from a namespace type are referenced
	 * in other parts of the service (ie Methods, Exceptions)
	 * 
	 * @param nsType
	 * @param info
	 * @return
	 */
	public static boolean isNamespaceTypeInUse(NamespaceType nsType, ServiceDescription desc) {
		String namespace = nsType.getNamespace();
		ServiceType[] services = desc.getServices().getService();
		for (int s = 0; s < services.length; s++) {
			// resource properties
			ResourcePropertiesListType propsList = services[s].getResourcePropertiesList();
			if (propsList != null) {
				for (int p = 0; propsList.getResourceProperty() != null && p < propsList.getResourceProperty().length; p++) {
					ResourcePropertyType prop = propsList.getResourceProperty(p);
					if (prop.getQName().getNamespaceURI().equals(namespace)) {
						return true;
					}
				}
			}
			// methods
			MethodsType methods = services[s].getMethods();
			if (methods != null) {
				for (int m = 0; methods.getMethod() != null && m < methods.getMethod().length; m++) {
					MethodType method = methods.getMethod(m);
					// inputs
					MethodTypeInputs inputs = method.getInputs();
					if (inputs != null) {
						for (int i = 0; inputs.getInput() != null && i < inputs.getInput().length; i++) {
							MethodTypeInputsInput input = inputs.getInput(i);
							if (input.getQName().getNamespaceURI().equals(namespace)) {
								return true;
							}
						}
					}
					// output
					MethodTypeOutput output = method.getOutput();
					if (output != null) {
						if (output.getQName().getNamespaceURI().equals(namespace)) {
							return true;
						}
					}
					// exceptions
					// TODO: This is disabled until exceptions can be typed from
					// schema
					/*
					 * MethodTypeExceptions exceptions = method.getExceptions();
					 * if (exceptions != null) { for (int e = 0;
					 * exceptions.getException() != null && e <
					 * exceptions.getException().length; e++) {
					 * MethodTypeExceptionsException exception =
					 * exceptions.getException(e); // TODO: verify exception
					 * QName against nsuri } }
					 */
				}
			}
		}
		return false;
	}


	/**
	 * Determines if all schema element types used in the service are still
	 * available in the service's namespace types.
	 * 
	 * @param desc
	 * @return
	 */
	public static boolean usedTypesAvailable(ServiceDescription desc) {
		return getUnavailableUsedTypes(desc).size() == 0;
	}


	public static Set getUnavailableUsedTypes(ServiceDescription desc) {
		// build up a set of used types
		Set usedTypes = new HashSet();
		ServiceType[] services = desc.getServices().getService();
		for (int s = 0; s < services.length; s++) {
			// resource properties
			ResourcePropertiesListType propsList = services[s].getResourcePropertiesList();
			if (propsList != null) {
				for (int p = 0; propsList.getResourceProperty() != null && p < propsList.getResourceProperty().length; p++) {
					ResourcePropertyType prop = propsList.getResourceProperty(p);
					usedTypes.add(prop.getQName());
				}
			}
			// methods
			MethodsType methods = services[s].getMethods();
			if (methods != null) {
				for (int m = 0; methods.getMethod() != null && m < methods.getMethod().length; m++) {
					MethodType method = methods.getMethod(m);
					// inputs
					MethodTypeInputs inputs = method.getInputs();
					if (inputs != null) {
						for (int i = 0; inputs.getInput() != null && i < inputs.getInput().length; i++) {
							MethodTypeInputsInput input = inputs.getInput(i);
							usedTypes.add(input.getQName());
						}
					}
					// output
					MethodTypeOutput output = method.getOutput();
					if (output != null && (!output.getQName().getLocalPart().equals("void"))) {
						usedTypes.add(output.getQName());
					}
					// exceptions
					// TODO: This is disabled until exceptions can be typed from
					// schema
					/*
					 * MethodTypeExceptions exceptions = method.getExceptions();
					 * if (exceptions != null) { for (int e = 0;
					 * exceptions.getException() != null && e <
					 * exceptions.getException().length; e++) {
					 * MethodTypeExceptionsException exception =
					 * exceptions.getException(e); // TODO: verify exception
					 * QName against nsuri } }
					 */
				}
			}
		}

		// walk through namespace types removing QNames from used types
		NamespacesType namespaces = desc.getNamespaces();
		if (namespaces != null) {
			for (int n = 0; namespaces.getNamespace() != null && usedTypes.size() != 0
				&& n < namespaces.getNamespace().length; n++) {
				NamespaceType nsType = namespaces.getNamespace(n);
				for (int t = 0; nsType.getSchemaElement() != null && t < nsType.getSchemaElement().length
					&& usedTypes.size() != 0; t++) {
					SchemaElementType type = nsType.getSchemaElement(t);
					usedTypes.remove(new QName(nsType.getNamespace(), type.getType()));
				}
			}
		}
		return usedTypes;
	}

	public static boolean validateIsFaultType(NamespaceType namespace, SchemaElementType type, File baseSchemaDir) {
		XSOMParser parser = new XSOMParser();
		try {
			parser.parse(new File(baseSchemaDir.getAbsolutePath() + File.separator + namespace.getLocation()));
			IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
				IntroducePortalConf.RESOURCE);
			parser.parse(new File(
					conf.getGlobusLocation() + File.separator + "share" + File.separator + "schema" + File.separator + "wsrf" + File.separator + "faults" + File.separator + "WS-BaseFaults.xsd"));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			XSSchemaSet sset = parser.getResult();
			XSComplexType bfct = sset.getComplexType(IntroduceConstants.BASEFAULTS_NAMESPACE, "BaseFaultType");
			XSElementDecl ct = sset.getElementDecl(namespace.getNamespace(), type.getType());
			if (ct.getType().isDerivedFrom(bfct)) {
				return true;
			} else {
				return false;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return false;

	}

}
