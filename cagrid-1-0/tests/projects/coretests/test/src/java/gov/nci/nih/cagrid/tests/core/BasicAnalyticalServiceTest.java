/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CheckServiceMetadataStep;
import gov.nci.nih.cagrid.tests.core.steps.CleanupTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.DeployGlobusServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.InvokeServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.StartGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.StopGlobusStep;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.ReflectionUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;

public class BasicAnalyticalServiceTest
	extends Story
{
	private File testDir;
	private File introduceDir;
	private File tempDir;
	private File serviceDir;
	private GlobusHelper globus;
	private EndpointReferenceType endpoint;
	private int port;
	
	public BasicAnalyticalServiceTest()
	{
		super();
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
	
	@SuppressWarnings("unchecked")
	protected Vector steps() 
	{
		// service creation
		testDir = new File("test" + File.separator + "resources" + File.separator + "services" + File.separator + "BasicAnalyticalService");
		introduceDir = new File(System.getProperty("introduce.dir", 
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "introduce"
		));
		try {
			tempDir = FileUtils.createTempDir(ReflectionUtils.getClassShortName(getClass()), "dir");
		} catch (IOException e) {
			throw new IllegalArgumentException("could not create temp dir", e);
		}

		globus = new GlobusHelper(tempDir);
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));

		CreateServiceStep createServiceStep = null;
		try {
			createServiceStep = new CreateServiceStep(introduceDir, testDir, tempDir);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not instantiate CreateServiceStep", e);
		}

		try {
			endpoint = new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/" + createServiceStep.getServiceName()));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("endpoint badly formed");
		}
		File metadataFile = new File(testDir, "etc" + File.separator + "serviceMetadata.xml");

		Vector steps = new Vector();
		steps.add(createServiceStep);
		steps.add(new CreateTempGlobusStep(globus));
		steps.add(new DeployGlobusServiceStep(globus, createServiceStep.getServiceDir()));
		steps.add(new StartGlobusStep(globus, port));
		try {
			addInvokeSteps(steps);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not add invoke steps", e);
		}
		steps.add(new CheckServiceMetadataStep(endpoint, metadataFile));
		steps.add(new StopGlobusStep(globus, port));
		steps.add(new CleanupTempGlobusStep(globus));
		return steps;
	}

	@SuppressWarnings("unchecked")
	private void addInvokeSteps(Vector steps) 
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
				endpoint.toString()
			));
		}
	}

	public String getDescription()
	{
		return "GlobusHelperTest";
	}
	
	/**
	 * used to make sure that if we are going to use a junit testsuite to test
	 * this that the test suite will not error out looking for a single test......
	 */
	public void testDummy() throws Throwable {
	}

	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(BasicAnalyticalServiceTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
