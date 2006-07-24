/*
 * Created on Jul 24, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CleanupTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.DeployGlobusServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.DestroyDefaultProxyStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianAddTrustedCAStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianApproveRegistrationStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianAuthenticateFailStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianAuthenticateStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianSubmitRegistrationStep;
import gov.nci.nih.cagrid.tests.core.steps.StartGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.StopGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.SyncGtsOnceStep;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

public class DorianTest
	extends Story
{
	private GlobusHelper globus;
	private File serviceDir;
	private int port;
	private File caFile;
	
	public DorianTest()
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
		caFile.delete();
		
		if (globus != null) {
			globus.stopGlobus(port);
			globus.cleanupTempGlobus();
		}
		new DestroyDefaultProxyStep().runStep();
		new DorianCleanupStep().runStep();
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps()		
	{
		globus = new GlobusHelper(true);
		globus.setUseCounterCheck(false);
		port = Integer.parseInt(System.getProperty("test.globus.secure.port", "8443"));
		serviceDir = new File(System.getProperty("cadsr.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "dorian"
		));
		caFile = new File(
			System.getProperty("user.home"), 
			".globus" + File.separator + "certificates" + File.separator + "DorianTest_ca.1"
		);
		
		Vector steps = new Vector();
		
		// initialize
		steps.add(new CreateTempGlobusStep(globus));
		steps.add(new SyncGtsOnceStep(globus));
		steps.add(new DeployGlobusServiceStep(globus, serviceDir));
		steps.add(new DeployGlobusServiceStep(globus, new File("..", "echo")));
		steps.add(new StartGlobusStep(globus, port));
		
		// successful authenticate
		steps.add(new DorianAuthenticateStep("dorian", "password", port));
		steps.add(new DestroyDefaultProxyStep());
		
		// failed authenticate
		steps.add(new DorianAuthenticateFailStep("junk", "junk", port));
		steps.add(new DorianAuthenticateFailStep("dorian", "junk", port));
		steps.add(new DorianAuthenticateFailStep("junk", "password", port));

		// add trusted ca
		steps.add(new DorianAuthenticateStep("dorian", "password", port));
		steps.add(new DorianAddTrustedCAStep(caFile, port));
		steps.add(new DestroyDefaultProxyStep());
		
		// new users
		File[] files = new File("test", "resources" + File.separator + "userApplications").listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && file.getName().endsWith(".xml");
			}
		});
		for (File file : files) {
			try {
				Application application = (Application) Utils.deserializeDocument(file.toString(), Application.class);
				// submit registration
				steps.add(new DorianSubmitRegistrationStep(application, port));
				
				// approve registration
				DorianAuthenticateStep auth = new DorianAuthenticateStep("dorian", "password", port);
				steps.add(auth);
				steps.add(new DorianApproveRegistrationStep(application, port, auth));
				steps.add(new DestroyDefaultProxyStep());

				// check that we can authenticate
				steps.add(new DorianAuthenticateStep(application.getUserId(), application.getPassword(), port));
				steps.add(new DorianAuthenticateFailStep(application.getUserId(), application.getPassword().toUpperCase(), port));
				steps.add(new DestroyDefaultProxyStep());				
			} catch (Exception e) {
				throw new RuntimeException("unable add new user steps", e); 
			}
		}
		
		// cleanup
		steps.add(new StopGlobusStep(globus, port));
		steps.add(new CleanupTempGlobusStep(globus));
		
		return steps;
	}
	
	public String getDescription()
	{
		return "CaDSRServiceTest";
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
		TestResult result = runner.doRun(new TestSuite(DorianTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
