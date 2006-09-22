/*
 * Created on Jul 24, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.DorianAddTrustedCAStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianApproveRegistrationStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianAuthenticateStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianConfigureStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianDestroyDefaultProxyStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianSubmitRegistrationStep;
import gov.nci.nih.cagrid.tests.core.steps.GTSSyncOnceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.steps.GrouperAddAdminStep;
import gov.nci.nih.cagrid.tests.core.steps.GrouperAddMemberStep;
import gov.nci.nih.cagrid.tests.core.steps.GrouperCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GrouperCreateDbStep;
import gov.nci.nih.cagrid.tests.core.steps.GrouperCreateGroupStep;
import gov.nci.nih.cagrid.tests.core.steps.GrouperCreateStemStep;
import gov.nci.nih.cagrid.tests.core.steps.GrouperInitStep;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

/**
 * This is an integration test that tests some of the major functionality of Dorian.
 * It syncs GTS and deploys the dorian service and an echo service to globus.  It
 * authenticates the dorian user and then insures some authentication failures.  It then
 * adds the dorian CA to the globus trusted CAs.  An application for a user account is submitted,
 * approved, and the new user is authenticated. 
 * @testType integration
 * @steps GlobusCreateStep, GTSSyncOnceStep, GlobusDeployServiceStep, DorianConfigureStep, GlobusStartStep
 * @steps DorianAuthenticateStep, DorianDestroyDefaultProxyStep, DorianAuthenticateFailStep
 * @steps DorianAddTrustedCAStep, DorianSubmitRegistrationStep, DorianApproveRegistrationStep
 * @steps GlobusStopStep, GlobusCleanupStep
 * @steps DorianCleanupStep
 * @author Patrick McConnell
 */
public class GridGrouperTest
	extends Story
{
	private GlobusHelper grouperGlobus;
	private GlobusHelper dorianGlobus;
	private File grouperDir;
	private File dorianDir;
	private int grouperPort;
	private int dorianPort;
	private File caFile;
	private String grouperAdminName;
	private String grouperAdmin;
	
	public GridGrouperTest()
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
		
		if (dorianGlobus != null) {
			dorianGlobus.stopGlobus(dorianPort);
			dorianGlobus.cleanupTempGlobus();
		}
		if (grouperGlobus != null) {
			grouperGlobus.stopGlobus(grouperPort);
			grouperGlobus.cleanupTempGlobus();
		}
		new DorianDestroyDefaultProxyStep().runStep();
		new DorianCleanupStep().runStep();
		new GrouperCleanupStep(grouperDir).runStep();
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps()		
	{
		dorianGlobus = new GlobusHelper(true);
		dorianGlobus.setUseCounterCheck(false);
		dorianPort = Integer.parseInt(System.getProperty("test.globus.secure.port", "8443"));
		dorianDir = new File(System.getProperty("dorian.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "dorian"
		));
		caFile = new File(
			System.getProperty("user.home"), 
			".globus" + File.separator + "certificates" + File.separator + "DorianTest_ca.1"
		);

		grouperGlobus = new GlobusHelper(true);
		grouperGlobus.setUseCounterCheck(false);
		grouperPort = Integer.parseInt(System.getProperty("test.globus.secure.port2", "9443"));
		grouperDir = new File(System.getProperty("grouper.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "gridgrouper"
		));
		grouperAdminName = System.getProperty("grouper.adminId", "grouper");
		grouperAdmin = System.getProperty("grouper.adminId", "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=" + grouperAdminName);

		Vector steps = new Vector();
		
		// initialize dorian
		steps.add(new GlobusCreateStep(dorianGlobus));
		steps.add(new GTSSyncOnceStep(dorianGlobus));
		steps.add(new GlobusDeployServiceStep(dorianGlobus, dorianDir));
		steps.add(new DorianConfigureStep(dorianGlobus));
		steps.add(new GlobusDeployServiceStep(dorianGlobus, new File("..", "echo")));
		steps.add(new GlobusStartStep(dorianGlobus, dorianPort));
		
		// initialize grouper
		steps.add(new GrouperCreateDbStep());
		steps.add(new GrouperInitStep(grouperDir));
		steps.add(new GrouperAddAdminStep(grouperDir, grouperAdmin));
		steps.add(new GlobusCreateStep(grouperGlobus));
		steps.add(new GTSSyncOnceStep(grouperGlobus));
		steps.add(new GlobusDeployServiceStep(grouperGlobus, grouperDir));
		steps.add(new GlobusDeployServiceStep(grouperGlobus, new File("..", "echo")));
		steps.add(new GlobusStartStep(grouperGlobus, grouperPort));
		
		// test successful authenticate
		steps.add(new DorianAuthenticateStep("dorian", "password", dorianPort));
		steps.add(new DorianDestroyDefaultProxyStep());

		// add trusted ca
		steps.add(new DorianAuthenticateStep("dorian", "password", dorianPort));
		steps.add(new DorianAddTrustedCAStep(caFile, dorianPort));
		steps.add(new DorianDestroyDefaultProxyStep());
		
		// add grouper admin user
		File applicationFile = new File("test", "resources" + File.separator + "userApplications").listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && file.getName().endsWith(".xml");
			}
		})[0];
		try {
			Application application = (Application) Utils.deserializeDocument(applicationFile.toString(), Application.class);
			application.setUserId(grouperAdmin);
			// submit registration
			steps.add(new DorianSubmitRegistrationStep(application, dorianPort));
			
			// approve registration
			DorianAuthenticateStep auth = new DorianAuthenticateStep("dorian", "password", dorianPort);
			steps.add(auth);
			steps.add(new DorianApproveRegistrationStep(application, dorianPort, auth));
			steps.add(new DorianDestroyDefaultProxyStep());
			
			// check that we can authenticate
			steps.add(new DorianAuthenticateStep(application.getUserId(), application.getPassword(), dorianPort));
		} catch (Exception e) {
			throw new RuntimeException("unable add new user steps", e); 
		}
		
		// test grouper
		steps.add(new GrouperCreateStemStep("test:my:stem"));
		steps.add(new GrouperCreateGroupStep("test:mygroup"));
		steps.add(new GrouperCreateGroupStep("test:my:stem:anothergroup"));
		steps.add(new GrouperAddMemberStep("test:mygroup", "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=subject1"));
		steps.add(new GrouperAddMemberStep("test:my:stem:anothergroup", "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=subject2"));
		
		// cleanup dorian
		steps.add(new GlobusStopStep(dorianGlobus, dorianPort));
		steps.add(new GlobusCleanupStep(dorianGlobus));
		steps.add(new DorianCleanupStep());
		steps.add(new DorianDestroyDefaultProxyStep());
		
		// cleanup grouper
		steps.add(new GlobusStopStep(grouperGlobus, grouperPort));
		steps.add(new GlobusCleanupStep(grouperGlobus));
		steps.add(new GrouperCleanupStep(grouperDir));
		
		return steps;
	}
	
	public String getDescription()
	{
		return "GridGrouperTest";
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
		TestResult result = runner.doRun(new TestSuite(GridGrouperTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
