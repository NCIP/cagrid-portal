/*
 * Created on Apr 22, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.util.EnvUtils;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.StdIOThread;
import gov.nih.nci.cagrid.tests.client.IntroduceEchoClient;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.oasis.wsrf.lifetime.Destroy;

import com.counter.CounterPortType;
import com.counter.CreateCounter;
import com.counter.CreateCounterResponse;
import com.counter.service.CounterServiceAddressingLocator;

public class GlobusHelper
{
	private boolean useCounterCheck = true;
	private File tmpDir;
	private boolean secure;
	private File tmpGlobusLocation;
	private Process p;
	private File securityDescriptor = new File("security-descriptor.xml");
	private Exception isGlobusRunningException;
	
	public GlobusHelper() 
	{
		this(false, null);
	}
	
	public GlobusHelper(File tmpDir) 
	{
		this(false, tmpDir);
	}
	
	public GlobusHelper(boolean secure) 
	{
		this(secure, null);
	}
	
	public GlobusHelper(boolean secure, File tmpDir) 
	{
		super();
		
		this.secure = secure;
		this.tmpDir = tmpDir;
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
		this.tmpGlobusLocation = FileUtils.createTempDir("Globus", "dir", tmpDir);
		
		// copy globus to tmp location
		FileUtils.copyRecursive(new File(globusLocation), tmpGlobusLocation, null);
	}
	
	public void deployService(File serviceDir) 
		throws IOException, InterruptedException
	{
		deployService(serviceDir, "deployGlobus");
	}
	
	public void deployService(File serviceDir, String target) 
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
				"cmd", "/c", ant + ".bat", target,
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
		this.p = runGlobusCommand("org.globus.wsrf.container.ServiceContainer", new Integer(port));
		
		// make sure it is running
		isGlobusRunningException = null;
		sleep(2000);
		for (int i = 0; i < 10; i++) {
			if (isGlobusRunning(port)) return;
			sleep(500);
		}
		isGlobusRunningException.printStackTrace();
		throw new IOException("could not start Globus");
	}
	
	private Process runGlobusCommand(String clName, Integer port) 
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
		
		// build command
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add(java.toString());
		cmd.add("-Dlog4j.configuration=container-log4j.properties");
		cmd.add("-DGLOBUS_LOCATION=" + tmpGlobusLocation);
		cmd.add("-Djava.endorsed.dirs=" + tmpGlobusLocation + File.separator + "endorsed");
		cmd.add("-classpath");
		cmd.add(classpath);
		cmd.add("org.globus.bootstrap.Bootstrap");
		cmd.add(clName);
		if (port != null) {
			cmd.add("-p");
			cmd.add(String.valueOf(port));
		}
		if (secure && securityDescriptor != null) {
			cmd.add("-containerDesc");
			cmd.add(securityDescriptor.toString());
		}
		if (! secure) cmd.add("-nosec");

		// build environment
		String[] envp = new String[] {
			//"ANT_HOME", System.getenv("ANT_HOME"),
			//"JAVA_HOME", System.getProperty("java.home"),
			"GLOBUS_LOCATION=" + tmpGlobusLocation,
		};
		envp = EnvUtils.overrideEnv(envp);
		
		// start globus
		Process p = Runtime.getRuntime().exec(cmd.toArray(new String[0]), envp, tmpGlobusLocation);
		new StdIOThread(p.getInputStream()).start();
		new StdIOThread(p.getErrorStream()).start();
		return p;
	}
	
	public boolean isGlobusRunning(int port)
	{
		if (useCounterCheck) return isGlobusRunningCounter(port);
		else return isGlobusRunningEcho(port);
	}
	
	public boolean isGlobusRunningCounter(int port)
	{
		try {
			org.globus.axis.util.Util.registerTransport();
			CounterServiceAddressingLocator locator = new CounterServiceAddressingLocator();
			EngineConfiguration engineConfig = new FileProvider(
				System.getenv("GLOBUS_LOCATION") + File.separator + "client-config.wsdd"
			);
			locator.setEngine(new AxisClient(engineConfig));
			
			String url = "http://localhost:" + port + "/wsrf/services/CounterService";
			if (secure) url = "https://localhost:" + port + "/wsrf/services/CounterService";
			CounterPortType counter = locator.getCounterPortTypePort(new EndpointReferenceType(new Address(
				url
			)));
			CreateCounterResponse response = counter.createCounter(new CreateCounter());	
			EndpointReferenceType endpoint = response.getEndpointReference();
			//endpoint.getProperties().get_any()[0].getValue();
			counter = locator.getCounterPortTypePort(endpoint); 			
			counter.add(0);
			counter.destroy(new Destroy());
			return true;
		} catch (IOException e) {
			this.isGlobusRunningException = e;
			return false;
		} catch (ServiceException e) {
			this.isGlobusRunningException = e;
			return false;
		}
	}
	
	public boolean isGlobusRunningEcho(int port)
	{
		String url = "http://localhost:" + port + "/wsrf/services/cagrid/IntroduceEcho";
		if (secure) url = "https://localhost:" + port + "/wsrf/services/cagrid/IntroduceEcho";
		
		try {
			IntroduceEchoClient client = new IntroduceEchoClient(url);
			if (! "are you alive?".equals(client.echo("are you alive?"))) return false;
			return true;
		} catch (MalformedURIException e) {
			this.isGlobusRunningException = e;
			return false;
		} catch (RemoteException e) {
			this.isGlobusRunningException = e;
			return false;
		}
	}
	
	public void stopGlobus(int port) 
		throws IOException
	{
		if (p == null) return;
		
		runGlobusCommand("org.globus.wsrf.container.ShutdownClient", port);
		sleep(2000);

		p.destroy();
		p = null;
	}
	
	public void cleanupTempGlobus()
	{
		if (tmpGlobusLocation != null) FileUtils.deleteRecursive(tmpGlobusLocation);
	}
	
	public File getTempGlobusLocation()
	{
		return tmpGlobusLocation;
	}

	public File getSecurityDescriptor()
	{
		return securityDescriptor;
	}

	public void setSecurityDescriptor(File securityDescriptor)
	{
		this.securityDescriptor = securityDescriptor;
	}
	
	private static void sleep(long ms)
	{
		Object sleep = new Object();
		try { synchronized (sleep) { sleep.wait(ms); } }
		catch(Exception e) { e.printStackTrace(); }
	}

	public boolean isUseCounterCheck()
	{
		return useCounterCheck;
	}

	public void setUseCounterCheck(boolean useCounterCheck)
	{
		this.useCounterCheck = useCounterCheck;
	}
}
