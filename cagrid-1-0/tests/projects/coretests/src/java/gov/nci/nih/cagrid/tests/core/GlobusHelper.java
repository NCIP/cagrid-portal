/*
 * Created on Apr 22, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import gov.nci.nih.cagrid.tests.core.util.EnvUtils;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.StdIOThread;

public class GlobusHelper
{
	private File tmpGlobusLocation;
	private Process p;
	
	public GlobusHelper() 
	{
		super();
	}
	
	public void createTempGlobus() 
		throws IOException
	{
		// get globus location
		String globusLocation = System.getenv("GLOBUS_LOCATION");
		if (globusLocation == null || globusLocation.equals("")) {
			throw new IllegalArgumentException("GLOBUS_LOCATION not set");
		}
		
		// create tmp globus location
		File tmpGlobusLocation = File.createTempFile("Globus", "dir");
		tmpGlobusLocation.delete();
		tmpGlobusLocation.mkdir();
		this.tmpGlobusLocation = tmpGlobusLocation;
		// copy globus to tmp location
		FileUtils.copyRecursive(new File(globusLocation), tmpGlobusLocation, null);
	}
	
	public void deployService(File serviceDir) 
		throws IOException, InterruptedException
	{
		String antHome = System.getenv("ANT_HOME");
		if (antHome == null || antHome.equals("")) {
			throw new IllegalArgumentException("ANT_HOME not set");
		}
		File ant = new File(antHome, "bin" + File.separator + "ant");
		
		String[] cmd = new String[] { ant.toString(), "deployGlobus" };
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			cmd = new String[] {
				"cmd", "/c", ant + ".bat", "deployGlobus",
			};
		}
		String[] envp = new String[] {
			"GLOBUS_LOCATION=" + tmpGlobusLocation.toString(),
		};
		envp = EnvUtils.overrideEnv(envp);

		Process p = Runtime.getRuntime().exec(cmd, envp, serviceDir);
		new StdIOThread(p.getInputStream()).start();
		new StdIOThread(p.getErrorStream()).start();
		p.waitFor();
		
		if (p.exitValue() != 0) {
			throw new IOException("deployService ant command failed: " + p.exitValue());
		}
	}
	
	public void startGlobus(int port) 
		throws IOException
	{
		this.p = runGlobusCommand("org.globus.wsrf.container.ServiceContainer");
		
		// make sure it is running
		sleep(2000);
		for (int i = 0; i < 10; i++) {
			if (isGlobusRunning(port)) return;
			sleep(500);
		}		
		throw new IOException("could not start Globus");
	}
	
	private Process runGlobusCommand(String clName) 
		throws IOException
	{
		// create globus startup params
		//%_RUNJAVA% -Dlog4j.configuration=container-log4j.properties %LOCAL_OPTS% %GLOBUS_OPTIONS% -classpath %LOCALCLASSPATH% org.globus.bootstrap.Bootstrap org.globus.wsrf.container.ServiceContainer %CMD_LINE_ARGS%
		//C:\Globus4.0.1\bin>"C:\jdk1.5.0_03\bin\java" -Dlog4j.configuration=container-log
		//4j.properties -DGLOBUS_LOCATION="C:\Globus4.0.1" -Djava.endorsed.dirs="C:\Globus
		//	4.0.1\endorsed"  -classpath "C:\Globus4.0.1\lib\bootstrap.jar";"C:\Globus4.0.1\l
		//	ib\cog-url.jar";"C:\Globus4.0.1\lib\axis-url.jar" org.globus.bootstrap.Bootstrap
		//	 org.globus.wsrf.container.ServiceContainer  -nosec
		File java = new File(System.getProperty("java.home"), "bin" + File.separator + "java");
		File lib = new File(tmpGlobusLocation, "lib"); 
		String classpath = lib + File.separator + "bootstrap.jar";
		classpath += File.pathSeparator + lib + File.separator + "cog-url.jar";
		classpath += File.pathSeparator + lib + File.separator + "axis-url.jar";
		String[] cmd = new String[] {
			java.toString(),
			"-Dlog4j.configuration=container-log4j.properties",
			"-DGLOBUS_LOCATION=" + tmpGlobusLocation,
			"-Djava.endorsed.dirs=" + tmpGlobusLocation + File.separator + "endorsed",
			"-classpath", classpath,
			"org.globus.bootstrap.Bootstrap",
			clName,
			//"-p", String.valueOf(port),
			"-nosec",
			//"-p", String.valueOf(port),
		};
		String[] envp = new String[] {
			//"ANT_HOME", System.getenv("ANT_HOME"),
			//"JAVA_HOME", System.getProperty("java.home"),
			"GLOBUS_LOCATION=" + tmpGlobusLocation,
		};
		envp = EnvUtils.overrideEnv(envp);
		
		// start globus
		Process p = Runtime.getRuntime().exec(cmd, envp, tmpGlobusLocation);
		new StdIOThread(p.getInputStream()).start();
		new StdIOThread(p.getErrorStream()).start();
		return p;
	}
	
	public boolean isGlobusRunning(int port)
	{
		try {
			Socket socket = new Socket("127.0.0.1", port);
			socket.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void stopGlobus(int port) 
		throws IOException
	{
		if (p == null) return;
		
		runGlobusCommand("org.globus.wsrf.container.ShutdownClient");
		sleep(2000);

		p.destroy();
		p = null;
	}
	
	public void cleanupTempGlobus()
	{
		FileUtils.deleteRecursive(tmpGlobusLocation);
	}
	
	private static void sleep(long ms)
	{
		Object sleep = new Object();
		try { synchronized (sleep) { sleep.wait(ms); } }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static void main(String[] args)
		throws Exception
	{
		GlobusHelper globus = new GlobusHelper();
		globus.deployService(new File("."));
		globus.startGlobus(8080);
		globus.stopGlobus(8080);
		System.out.println("Done");
	}
}
