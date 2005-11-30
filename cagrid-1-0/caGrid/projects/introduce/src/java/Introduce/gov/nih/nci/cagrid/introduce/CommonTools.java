package gov.nih.nci.cagrid.introduce;

import java.io.File;
import java.util.StringTokenizer;

public class CommonTools {

	public static String getAntSkeletonCreationCommand(String buildFileDir,
			String name, String dir, String packagename, String namespacedomain)
			throws Exception {
		String os = System.getProperty("os.name");
		String cmd = " -Dintroduce.skeleton.destination.dir=" + dir
				+ " -Dintroduce.skeleton.service.name=" + name
				+ " -Dintroduce.skeleton.package=" + packagename
				+ " -Dintroduce.skeleton.package.dir="
				+ packagename.replace('.', File.separatorChar)
				+ " -Dintroduce.skeleton.namespace.domain=" + namespacedomain
				+ " createService";
		if ((os.indexOf("Windows") >= 0) || (os.indexOf("windows") >= 0)) {
			cmd = "-classpath "
					+ CommonTools.getAntLauncherJarLocation(System
							.getProperty("java.class.path"), true)
					+ " org.apache.tools.ant.launch.Launcher -lib "
					+ System.getProperty("java.class.path") + " -buildfile "
					+ buildFileDir + File.separator + "build.xml" + cmd;
			cmd = "java.exe " + cmd;
		} else if ((os.indexOf("Linux") >= 0) || (os.indexOf("linux") >= 0)) {
			cmd = "-classpath "
					+ CommonTools.getAntLauncherJarLocation(System
							.getProperty("java.class.path"), false)
					+ " org.apache.tools.ant.launch.Launcher -lib "
					+ System.getProperty("java.class.path") + " -buildfile "
					+ buildFileDir + File.separator + "build.xml" + cmd;
			cmd = "java " + cmd;
		} else {
			throw new Exception(
					"Cannot create grid service, your operatingsystem, " + os
							+ " is not supported.");
		}
		return cmd;
	}

	static String getAntLauncherJarLocation(String path, boolean isWindows) {
		String separator = isWindows ? ";" : ":";
		StringTokenizer pathTokenizer = new StringTokenizer(path, separator);
		while (pathTokenizer.hasMoreTokens()) {
			String pathElement = pathTokenizer.nextToken();
			if (pathElement.indexOf("ant-launcher") != -1
					&& pathElement.endsWith(".jar")) {
				return pathElement;
			}
		}
		return null;
	}

}
