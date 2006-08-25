/*
 * Created on Jun 10, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class AntUtils
{
	public static String getAntCommand()
	{
		// ant home
		String ant = System.getenv("ANT_HOME");
		if (ant == null) throw new IllegalArgumentException("ANT_HOME not set");
		
		// ant home/bin
		if (! ant.endsWith(File.separator)) ant += File.separator;
		ant += "bin" + File.separator;
		
		// ant home/bin/ant
		if (OSUtils.isWindows()) {
			ant += "ant.bat";
		} else {
			ant += "ant";
		}
		
		if (! new File(ant).exists()) throw new IllegalArgumentException(ant + " does not exist");
		return ant;
	}
	
	public static void runAnt(
		File dir, String buildFile, String target, Properties sysProps, String[] envp
	)
		throws IOException, InterruptedException 
	{
		// build command
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add(getAntCommand());
		
		// add system properties
		if (sysProps != null) {
			Enumeration keys = sysProps.keys();
			while (keys.hasMoreElements()) {
				String name = (String) keys.nextElement();
				String value = (String) sysProps.getProperty(name);
				cmd.add("\"-D" + name + "=" + value + "\"");
			}
		}
		
		// add build file
		if (buildFile != null) {
			cmd.add("-f");
			cmd.add(buildFile);
		}
		
		// add target
		if (target != null) cmd.add(target);
		
		// run ant
		Process p = Runtime.getRuntime().exec(
			cmd.toArray(new String[0]), envp, dir
		);
		// track stdout and stderr
		StringBuffer stdout = new StringBuffer();
		StringBuffer stderr = new StringBuffer();
		new IOThread(p.getInputStream(), System.out, stdout).start();
		new IOThread(p.getErrorStream(), System.err, stderr).start();
		
		// wait and return
		int result = p.waitFor();
		if (
			stdout.indexOf("BUILD FAILED") != -1 || 
			stderr.indexOf("BUILD FAILED") != -1 ||
			stdout.indexOf("Build failed") != -1 || 
			stderr.indexOf("Build failed") != -1
		) {
			System.err.println(stderr);
			System.out.println(stdout);
			throw new IOException("ant command '" + target + "' failed");
		}
	}
}
