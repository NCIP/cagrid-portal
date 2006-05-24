package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.common.Utils;
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
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class CommonTools {
	
	public static final String ALLOWED_SERVICE_NAME_REGEX = "[A-Z]++[A-Za-z0-9\\_\\$]*";

	public static Process createAndOutputProcess(String cmd) throws Exception {
		final Process p;

		p = Runtime.getRuntime().exec(cmd);
		StreamGobbler errGobbler = new StreamGobbler(p.getErrorStream(), "ERR");
		StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "OUT");
		errGobbler.start();
		outGobbler.start();

		return p;
	}
	
	public static boolean isValidServiceName(String serviceName){
		if (serviceName.length() > 0) {
			if (serviceName.substring(0, 1).toLowerCase()
					.equals(serviceName.substring(0, 1))) {
				return false;
			}
			if (!serviceName
					.matches(ALLOWED_SERVICE_NAME_REGEX)) {
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


	public static String getAntCompileCommand(String buildFileDir) throws Exception {
		return getAntCommand("compile", buildFileDir);
	}
	
	public static String getAntAllCommand(String buildFileDir) throws Exception {
		return getAntCommand("all", buildFileDir);
	}

	public static String getAntFlattenCommand(String buildFileDir) throws Exception {
		return getAntCommand("flatten", buildFileDir);
	}


	public static String getAntDeployTomcatCommand(String buildFileDir) throws Exception {
		String cmd = " -Dservice.properties.file=" + buildFileDir + File.separator + "service.properties.tmp";
		cmd = getAntCommand("deployTomcat", buildFileDir) + " " + cmd;
		return cmd;
	}


	public static String getAntDeployGlobusCommand(String buildFileDir) throws Exception {
		return getAntCommand("deployGlobus", buildFileDir);
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
		String os = System.getProperty("os.name");
		if ((os.indexOf("Windows") >= 0) || (os.indexOf("windows") >= 0)) {
			dir = "\"" + dir + "\"";
		} else {
			dir = dir.replaceAll(" ", "\\ ");
		}
		String cmd = " -Dintroduce.skeleton.destination.dir=" + dir + " -Dintroduce.skeleton.service.name=" + name
			+ " -Dintroduce.skeleton.package=" + packagename + " -Dintroduce.skeleton.package.dir="
			+ packagename.replace('.', File.separatorChar) + " -Dintroduce.skeleton.namespace.domain="
			+ namespacedomain + " -Dintroduce.skeleton.extensions=" + extensions + " createService";
		cmd = getAntCommandCall(buildFileDir) + cmd;
		System.out.println("CREATION: cmd: " + cmd );
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
			String os = System.getProperty("os.name");
			if ((os.indexOf("Windows") >= 0) || (os.indexOf("windows") >= 0)) {
				dir = "\"" + dir + "\"";
			} else {
				dir = dir.replaceAll(" ", "\\ ");
			}
			String cmd = " -Dintroduce.skeleton.destination.dir=" + dir + " -Dintroduce.skeleton.service.name=" + name
				+ " -Dintroduce.skeleton.package=" + packagename + " -Dintroduce.skeleton.package.dir="
				+ packagename.replace('.', File.separatorChar) + " -Dintroduce.skeleton.namespace.domain="
				+ namespacedomain + " -Dintroduce.skeleton.extensions=" + extensions + " postCreateService";
			cmd = getAntCommandCall(buildFileDir) + cmd;
			System.out.println("CREATION: cmd: " + cmd );
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
	
	public static String methodTypeToString(MethodType method){
		// assume its void to start with
		String output = "void";

		MethodTypeOutput outputType = method.getOutput();
		if (outputType != null) {
			// use classname if set, else use schema type
			if (outputType.getQName() != null && outputType.getQName().getLocalPart() != null
				&& !outputType.getQName().getLocalPart().trim().equals("")) {
				output = org.apache.axis.wsdl.toJava.Utils.xmlNameToJavaClass(outputType.getQName().getLocalPart());
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
						input += org.apache.axis.wsdl.toJava.Utils.xmlNameToJavaClass(inputType.getQName().getLocalPart());
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
	
	
	public static void importMethod(MethodTypeImportInformation importInformation, File fromDir, File toDir, String fromService, String toService, String methodName,boolean copyFiles) throws Exception {
		ServiceDescription fromintroService = (ServiceDescription) Utils.deserializeDocument(fromDir.getAbsolutePath() + File.separator + "introduce.xml", ServiceDescription.class);

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(toDir.getAbsolutePath() + File.separator + "introduce.xml", ServiceDescription.class);
		
		if(copyFiles){

			String fromwsdl =fromDir.getAbsolutePath()+ File.separator + "schema" + File.separator
				+ fromService;
			String towsdl = toDir.getAbsolutePath() + File.separator + "schema" + File.separator
			+ toService;
			
			File[] wsdls = new File(fromwsdl).listFiles();
			for(int i = 0; i < wsdls.length; i++){
				Utils.copyFile(wsdls[i].getAbsoluteFile(),new File(towsdl + File.separator + wsdls[i].getName()));
			}
			
			
			String fromLibDir = fromDir.getAbsolutePath() + File.separator + "lib";
			String toLibDir = toDir.getAbsolutePath() + File.separator + "lib";
		
			File[] libs = new File(fromLibDir).listFiles();
			for(int i = 0; i < libs.length; i++){
				Utils.copyFile(libs[i].getAbsoluteFile(),new File(toLibDir + File.separator + libs[i].getName()));
			}
		}
		
		//copy over the namespaces from the imported service
		NamespacesType fromNamespaces = fromintroService.getNamespaces();
		int fromNamespacesLength = 0;
		if(fromNamespaces!=null && fromNamespaces.getNamespace()!=null){
			fromNamespacesLength = fromNamespaces.getNamespace().length;
		}
		NamespacesType toNamespaces = introService.getNamespaces();
		int toNamespacesLength = 0;
		if(toNamespaces!=null && toNamespaces.getNamespace()!=null){
			toNamespacesLength = toNamespaces.getNamespace().length;
		}
		NamespacesType newNamespaces = new NamespacesType();
		NamespaceType[] newNamespacesArr = new NamespaceType[fromNamespacesLength + toNamespacesLength];
		int location = 0;
		for(int i = 0; i < fromNamespacesLength; i ++){
			newNamespacesArr[location++] = fromNamespaces.getNamespace(i);
		}
		for(int i = 0; i < toNamespacesLength; i ++){
			newNamespacesArr[location++] = toNamespaces.getNamespace(i);
		}
		newNamespaces.setNamespace(newNamespacesArr);
		introService.setNamespaces(newNamespaces);
		
		
		//find the method and add it methods....
		MethodsType fromMethods = CommonTools.getService(fromintroService.getServices(),fromService).getMethods();
		MethodType foundMethod = null;
		if(fromMethods!=null && fromMethods.getMethod()!=null){
			boolean found = false;
			for(int i = 0; i < fromMethods.getMethod().length; i ++){
				foundMethod = fromMethods.getMethod(i);
				if(foundMethod.getName().equals(methodName)){
					found = true;
					break;
				}
			}
			if(found!=true){
				throw new Exception("Method " + methodName + " was not found in imported service");
			}
			
			
		} else {
			throw new Exception("Imported service was supposed to have methods.....");
		}
		
		MethodsType methodsType = CommonTools.getService(introService.getServices(),toService).getMethods();

		
		foundMethod.setIsImported(true);
		foundMethod.setImportInformation(importInformation);
		
		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		MethodType[] newMethods;
		int newLength = 0;
		if (methodsType!=null && methodsType.getMethod() != null) {
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
		CommonTools.getService(introService.getServices(),toService).setMethods(newmethodsType);

		Utils.serializeDocument(toDir.getAbsolutePath() + File.separator + "introduce.xml",
			introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));

	}
	
}
