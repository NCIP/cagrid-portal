package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CDSCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.CDSDelegateCredentialStep;
import gov.nci.nih.cagrid.tests.core.steps.CDSFindMyDelegatedCredentialsStep;
import gov.nci.nih.cagrid.tests.core.steps.CDSGetDelegatedCredentialFailStep;
import gov.nci.nih.cagrid.tests.core.steps.CDSGetDelegatedCredentialStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianAddTrustedCAStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianApproveRegistrationStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianAuthenticateStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianConfigureStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianDestroyDefaultProxyStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianSubmitRegistrationStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusInstallSecurityDescriptorStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.ProxyActiveStep;
import gov.nci.nih.cagrid.tests.core.steps.SleepStep;
import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.cds.common.ProxyLifetime;
import org.cagrid.gaards.cds.service.Errors;

import com.atomicobject.haste.framework.Story;

public class DelegateCredentialTest extends Story {
	private GlobusHelper globus;
	private File dorianServiceDir;
	private File cdsServiceDir;
	private File caFile;

	private final static int SHORT_LIFETIME_SECONDS = 30;

	public DelegateCredentialTest() {
		super();
	}

	@Override
	public String getName() {
		return "Credential Delegation Service (CDS) Story";
	}

	@Override
	protected boolean storySetUp() throws Throwable {
		return true;
	}

	@Override
	protected void storyTearDown() throws Throwable {
		this.caFile.delete();

		if (this.globus != null) {
			this.globus.stopGlobus();
			this.globus.cleanupTempGlobus();
		}
		new DorianDestroyDefaultProxyStep().runStep();
		new DorianCleanupStep().runStep();
		new CDSCleanupStep().runStep();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Vector steps() {
		this.globus = new GlobusHelper(true);

		this.dorianServiceDir = new File(System.getProperty("dorian.dir", ".."
				+ File.separator + ".." + File.separator + ".."
				+ File.separator + "caGrid" + File.separator + "projects"
				+ File.separator + "dorian"));

		this.cdsServiceDir = new File(System.getProperty("cds.dir", ".."
				+ File.separator + ".." + File.separator + ".."
				+ File.separator + "caGrid" + File.separator + "projects"
				+ File.separator + "cds"));
		this.caFile = new File(System.getProperty("user.home"), ".globus"
				+ File.separator + "certificates" + File.separator
				+ "DorianTest_ca.1");

		Vector steps = new Vector();

		String dorianURL = null;
		try {
			dorianURL = this.globus.getServiceEPR("cagrid/Dorian").getAddress()
					.toString();
		} catch (MalformedURIException e) {
			e.printStackTrace();
			fail("Unable to get the Dorian URL:" + e.getMessage());
		}

		String cdsURL = null;
		try {
			cdsURL = this.globus.getServiceEPR(
					"cagrid/CredentialDelegationService").getAddress()
					.toString();
		} catch (MalformedURIException e) {
			e.printStackTrace();
			fail("Unable to get the CDS URL:" + e.getMessage());
		}

		// initialize
		steps.add(new GlobusCreateStep(this.globus));
		steps.add(new GlobusInstallSecurityDescriptorStep(this.globus));
		steps.add(new GlobusDeployServiceStep(this.globus,
				this.dorianServiceDir));
		steps.add(new GlobusDeployServiceStep(this.globus, this.cdsServiceDir));
		steps.add(new DorianConfigureStep(this.globus));
		steps.add(new GlobusStartStep(this.globus));

		// successful authenticate
		DorianAuthenticateStep admin = new DorianAuthenticateStep("dorian",
				Constants.DORIAN_ADMIN_PASSWORD, dorianURL, 12, 2);
		steps.add(admin);
		steps.add(new DorianAddTrustedCAStep(this.caFile, dorianURL));
		steps.add(new DorianDestroyDefaultProxyStep());
		Application leonardoApp = getApplication("leonardo", "Leonardo",
				"Turtle");
		Application donatelloApp = getApplication("donatello", "Donatello",
				"Turtle");
		System.out.println(leonardoApp.getPassword());
		steps.add(new DorianSubmitRegistrationStep(leonardoApp, dorianURL));
		steps.add(new DorianSubmitRegistrationStep(donatelloApp, dorianURL));

		steps.add(new DorianApproveRegistrationStep(leonardoApp, dorianURL,
				admin));
		
		steps.add(new CDSFindMyDelegatedCredentialsStep(cdsURL,admin));

		DorianAuthenticateStep leonardo = new DorianAuthenticateStep(
				leonardoApp.getUserId(), leonardoApp.getPassword(), dorianURL);
		steps.add(leonardo);
		steps.add(new DorianDestroyDefaultProxyStep());

		List<GridCredential> allowedParties = new ArrayList<GridCredential>();
		allowedParties.add(leonardo);
		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setHours(4);
		CDSDelegateCredentialStep delegateAdmin = new CDSDelegateCredentialStep(
				cdsURL, admin, allowedParties, lifetime);
		steps.add(delegateAdmin);

		CDSGetDelegatedCredentialStep admin2 = new CDSGetDelegatedCredentialStep(
				delegateAdmin, leonardo);
		steps.add(admin2);

		steps.add(new DorianApproveRegistrationStep(donatelloApp, dorianURL,
				admin2));

		DorianAuthenticateStep donatello = new DorianAuthenticateStep(
				donatelloApp.getUserId(), donatelloApp.getPassword(), dorianURL);
		steps.add(donatello);
		steps.add(new DorianDestroyDefaultProxyStep());

		steps.add(new CDSGetDelegatedCredentialFailStep(delegateAdmin,
				donatello, Errors.PERMISSION_DENIED_TO_DELEGATED_CREDENTIAL));
		// TODO: Test Invalidating

		ProxyLifetime delegationLifetime = new ProxyLifetime();
		delegationLifetime.setSeconds(SHORT_LIFETIME_SECONDS);
		ProxyLifetime delegatedCredentialsLifetime = new ProxyLifetime();
		delegatedCredentialsLifetime.setSeconds((SHORT_LIFETIME_SECONDS / 2));

		CDSDelegateCredentialStep delegateAdminShort = new CDSDelegateCredentialStep(
				cdsURL, admin, allowedParties, delegationLifetime,
				delegatedCredentialsLifetime);
		steps.add(delegateAdminShort);

		CDSGetDelegatedCredentialStep adminShort = new CDSGetDelegatedCredentialStep(
				delegateAdminShort, leonardo);
		steps.add(adminShort);

		steps.add(new ProxyActiveStep(adminShort, true));
		steps.add(new CDSGetDelegatedCredentialFailStep(delegateAdminShort,
				donatello, Errors.PERMISSION_DENIED_TO_DELEGATED_CREDENTIAL));
		long sleepTime = ((SHORT_LIFETIME_SECONDS / 2) * 1000) + 100;
		steps.add(new SleepStep(sleepTime));
		steps.add(new ProxyActiveStep(adminShort, false));
		steps.add(new SleepStep(sleepTime));
		steps.add(new CDSGetDelegatedCredentialFailStep(delegateAdminShort,
				leonardo, "org.globus.wsrf.NoSuchResourceException"));
		return steps;
	}

	public Application getApplication(String userId, String firstName,
			String lastName) {
		Application a = new Application();
		a.setUserId(userId);
		a.setPassword(Constants.DORIAN_ADMIN_PASSWORD);
		a.setOrganization("XYZ Inc.");
		a.setFirstName(firstName);
		a.setLastName(lastName);
		a.setAddress("555 Dorian Street");
		a.setCity("Columbus");
		a.setState(StateCode.OH);
		a.setZipcode("43210");
		a.setCountry(CountryCode.US);
		a.setEmail(firstName + "." + lastName + "@xyxinc.com");
		a.setPhoneNumber("(555) 555-5555");
		return a;
	}

	@Override
	public String getDescription() {
		return "Credential Delegation Service (CDS) System Test";
	}

	/**
	 * used to make sure that if we are going to use a junit testsuite to test
	 * this that the test suite will not error out looking for a single
	 * test......
	 */
	public void testDummy() throws Throwable {
	}

	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(
				DelegateCredentialTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
