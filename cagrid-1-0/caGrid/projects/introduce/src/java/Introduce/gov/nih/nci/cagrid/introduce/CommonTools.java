package gov.nih.nci.cagrid.introduce;

import java.io.File;
import java.util.StringTokenizer;

public class CommonTools {
	
	public static String getAntSkeletonCreationCommand(String name, String dir, String packagename, String namespacedomain) throws Exception {
		String os = System.getProperty("os.name");
		String cmd = " -Dintroduce.skeleton.destination.dir=" + dir + " -Dintroduce.skeleton.service.name="
			+ name + " -Dintroduce.skeleton.package=" + packagename
			+ " -Dintroduce.skeleton.package.dir=" + packagename.replace('.', File.separatorChar)
			+ " -Dintroduce.skeleton.namespace.domain=" + namespacedomain + " createService";
		cmd = "java.exe -classpath " + CommonTools.getAntLauncherJarLocation(System.getProperty("java.class.path"),true) + " org.apache.tools.ant.launch.Launcher -buildfile=build.xml " + cmd;
//		if ((os.indexOf("Windows") >= 0) || (os.indexOf("windows") >= 0)) {
//			String path = (new File("")).getAbsolutePath();
//			return "rundll32 SHELL32.DLL,ShellExec_RunDLL cmd /K cd " + path + "&ant" + cmd;
//		} else if ((os.indexOf("Linux") >= 0) || (os.indexOf("linux") >= 0)) {
//			return "xterm -hold -geometry 50x10 -e ant" + cmd;
//		} else {
//			throw new Exception("Cannot create grid service, your operating system, " + os + " is not supported.");
//		}
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
	
}
