/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CreateServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.InvokeServiceStep;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.ReflectionUtils;

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

public abstract class AbstractServiceTest
	extends Story
{
	protected File testDir;
	protected File introduceDir;
	protected File tempDir;
	protected File serviceDir;
	protected GlobusHelper globus;
	protected EndpointReferenceType endpoint;
	protected int port;
	protected CreateServiceStep createServiceStep;
	protected File metadataFile;

	protected void init(String serviceName)
	{
		// test dir (home of the service test)
		testDir = new File("test" + File.separator + "resources" + File.separator + "services" + File.separator + serviceName);
		// introduce dir
		introduceDir = new File(System.getProperty("introduce.dir", 
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "introduce"
		));
		
		// create temp dir
		try {
			tempDir = FileUtils.createTempDir(ReflectionUtils.getClassShortName(getClass()), "dir");
		} catch (IOException e) {
			throw new IllegalArgumentException("could not create temp dir", e);
		}

		// set globus helper and port
		globus = new GlobusHelper(tempDir);
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));

		// set createServiceStep
		try {
			createServiceStep = new CreateServiceStep(introduceDir, testDir, tempDir);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not instantiate CreateServiceStep", e);
		}

		// set endpoint
		try {
			endpoint = new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/" + createServiceStep.getServiceName()));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("endpoint badly formed");
		}
		
		// set metadataFile
		metadataFile = new File(testDir, "etc" + File.separator + "serviceMetadata.xml");		
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
				return InvokeServiceStep.parseParamPos(f1) - InvokeServiceStep.parseParamPos(f2);
			}
		});
		dirs = dirList.toArray(new File[0]);
		
		// add steps
		for (File methodDir : dirs) {
			steps.add(new InvokeServiceStep(
				serviceDir, testDir, 
				methodDir, InvokeServiceStep.parseParamName(methodDir),
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
