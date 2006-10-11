/*
 * Created on Oct 10, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.steps.ServiceCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.ServiceInvokeStep;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

public class ServiceHelper
{
	protected String serviceName;
	protected File testDir;
	protected File introduceDir;
	protected File tempDir;
	protected File serviceDir;
	protected IntroduceServiceInfo serviceInfo;
	protected GlobusHelper globus;
	protected EndpointReferenceType endpoint;
	protected int port;
	protected ServiceCreateStep createServiceStep;
	protected File metadataFile;
	protected File gmeServiceDir;
	protected File cadsrServiceDir;

	public ServiceHelper(String serviceName)
	{
		this(serviceName, null);
	}

	public ServiceHelper(String serviceName, File serviceDir)
	{
		super();
		
		// service name
		this.serviceName = serviceName;
		
		// test dir (home of the service test)
		testDir = new File("test" + File.separator + "resources" + File.separator + "services" + File.separator + serviceName);
		// introduce dir
		introduceDir = new File(System.getProperty("introduce.dir", 
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "introduce"
		));
		
		// create temp dir
		try {
			String tempRoot = System.getProperty("temp.dir");
			tempDir = FileUtils.createTempDir(
				//ReflectionUtils.getClassShortName(getClass()), "dir", tempRoot == null ? null : new File(tempRoot) 
				"Service", "dir", tempRoot == null ? null : new File(tempRoot)
			);
		} catch (IOException e) {
			throw new IllegalArgumentException("could not create temp dir", e);
		}

		// parse introduce service info
		try {
			this.serviceDir = serviceDir;
			if (this.serviceDir == null) this.serviceDir =  testDir;
			serviceInfo = new IntroduceServiceInfo(new File(this.serviceDir, "introduce.xml"));
		} catch (Exception e) {
			throw new IllegalArgumentException("could not parse introduce.xml", e);
		}
		
		// set globus helper and port
		String protocol = "http";
		globus = new GlobusHelper(serviceInfo.isTransportSecurity(), tempDir);
		if (serviceInfo.isTransportSecurity()) {
			port = Integer.parseInt(System.getProperty("test.globus.secure.port", "8443"));
			protocol = "https";
			globus.setUseCounterCheck(false);
		} else {
			port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));
		}

		// set endpoint
		try {
			endpoint = new EndpointReferenceType(new Address(protocol + "://localhost:" + port + "/wsrf/services/cagrid/" + serviceInfo.getServiceName()));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("endpoint badly formed");
		}
		
		// set metadataFile
		metadataFile = new File(testDir, "etc" + File.separator + IntroduceServiceInfo.INTRODUCE_SERVICEMETADATA_FILENAME);
		
		// set gme and cadsr service dirs
		gmeServiceDir = new File(System.getProperty("gme.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "gme"
		));		
		cadsrServiceDir = new File(System.getProperty("cadsr.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "cadsr"
		));		
	}
	
	public ArrayList<ServiceInvokeStep> getInvokeSteps() 
		throws Exception
	{
		File methodsDir = new File(testDir, "test" + File.separator + "resources");
		return getInvokeSteps(serviceDir, testDir, methodsDir, endpoint);
	}
	
	public static ArrayList<ServiceInvokeStep> getInvokeSteps(File serviceDir, File testDir, File methodsDir, EndpointReferenceType endpoint) 
		throws Exception
	{
		ArrayList<ServiceInvokeStep> steps = new ArrayList<ServiceInvokeStep>();

		File[] dirs = getInvokeDirs(methodsDir); 
		
		// add steps
		for (File methodDir : dirs) {
			steps.add(new ServiceInvokeStep(
				serviceDir, testDir, 
				methodDir, ServiceInvokeStep.parseParamName(methodDir),
				endpoint.getAddress().toString()
			));
		}
		
		return steps;
	}
	
	public static File[] getInvokeDirs(File methodsDir)
	{
		// get directories
		File[] dirs = methodsDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory() & file.getName().matches("\\d+_\\w+");
			}
		});
		
		// sort directories
		ArrayList<File> dirList = new ArrayList<File>(dirs.length);
		for (File dir : dirs) dirList.add(dir);
		Collections.sort(dirList, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return ServiceInvokeStep.parseParamPos(f1) - ServiceInvokeStep.parseParamPos(f2);
			}
		});
		dirs = dirList.toArray(new File[0]);
		
		return dirs;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public File getCadsrServiceDir()
	{
		return cadsrServiceDir;
	}

	public ServiceCreateStep getCreateServiceStep()
	{
		// set createServiceStep
		try {
			createServiceStep = new ServiceCreateStep(introduceDir, testDir, tempDir);
		} catch (Exception e) {
			throw new RuntimeException("could not instantiate CreateServiceStep", e);
		}
		serviceDir = createServiceStep.getServiceDir();
		return createServiceStep;
	}

	public EndpointReferenceType getEndpoint()
	{
		return endpoint;
	}

	public GlobusHelper getGlobus()
	{
		return globus;
	}

	public File getGmeServiceDir()
	{
		return gmeServiceDir;
	}

	public File getIntroduceDir()
	{
		return introduceDir;
	}

	public File getMetadataFile()
	{
		return metadataFile;
	}

	public int getPort()
	{
		return port;
	}

	public File getServiceDir()
	{
		return serviceDir;
	}

	public IntroduceServiceInfo getServiceInfo()
	{
		return serviceInfo;
	}

	public File getTempDir()
	{
		return tempDir;
	}

	public File getTestDir()
	{
		return testDir;
	}
}
