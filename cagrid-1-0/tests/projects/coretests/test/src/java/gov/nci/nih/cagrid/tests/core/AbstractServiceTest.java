/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.ServiceCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.ServiceInvokeStep;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.IntroduceServiceInfo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;

/**
 * This is a base class to be used for creating and invoking Introduce-built services.
 * @see <a href="http://gforge.nci.nih.gov/plugins/scmcvs/cvsweb.php/cagrid-1-0/Documentation/docs/tests/cagrid-1-0-testing.doc?cvsroot=cagrid-1-0">cagrid-1-0-testing.doc</a> 
 * @author Patrick McConnell
 */
public abstract class AbstractServiceTest
	extends Story
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

	protected void init(String serviceName)
	{
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
			serviceInfo = new IntroduceServiceInfo(new File(testDir, "introduce.xml"));
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

		// set createServiceStep
		try {
			createServiceStep = new ServiceCreateStep(introduceDir, testDir, tempDir);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not instantiate CreateServiceStep", e);
		}
		serviceDir = createServiceStep.getServiceDir();

		// set endpoint
		try {
			endpoint = new EndpointReferenceType(new Address(protocol + "://localhost:" + port + "/wsrf/services/cagrid/" + createServiceStep.getServiceName()));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("endpoint badly formed");
		}
		
		// set metadataFile
		metadataFile = new File(testDir, "etc" + File.separator + IntroduceServiceInfo.INTRODUCE_SERVICEMETADATA_FILENAME);		
	}
	
	@SuppressWarnings("unchecked")
	public void addInvokeSteps(Vector steps) 
		throws Exception
	{
		// get directories
		File methodsDir = new File(testDir, "test" + File.separator + "resources");
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
		
		// add steps
		for (File methodDir : dirs) {
			steps.add(new ServiceInvokeStep(
				serviceDir, testDir, 
				methodDir, ServiceInvokeStep.parseParamName(methodDir),
				endpoint.getAddress().toString()
			));
		}
	}

	protected boolean storySetUp() 
		throws Throwable
	{
		return true;
	}

	protected void storyTearDown() 
		throws Throwable
	{
		if (globus != null) {
			globus.stopGlobus(port);
			globus.cleanupTempGlobus();
		}
	}
	
	/**
	 * used to make sure that if we are going to use a junit testsuite to test
	 * this that the test suite will not error out looking for a single test......
	 */
	public void testDummy() throws Throwable {
	}
}
