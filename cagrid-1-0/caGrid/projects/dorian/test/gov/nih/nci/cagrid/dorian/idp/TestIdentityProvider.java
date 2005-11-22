package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.CountryCode;
import gov.nih.nci.cagrid.gums.idp.bean.StateCode;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.gums.test.TestResourceManager;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdentityProvider extends TestCase {

	private IdPConfiguration conf;
	private Database db;
	private CertificateAuthority ca;
	
	public static String IDP_CONFIG = "resources" + File.separator
	+ "general-test" + File.separator + "idp-config.xml";

	private int count = 0;
	

	public void testAutomaticRegistration() {
		try {
			
			AssertionCredentialsManager am = new AssertionCredentialsManager(conf,ca,db);
			IdentityProvider idp = new IdentityProvider(conf,db,am);
			conf.setRegistrationPolicy(
					new AutomaticRegistrationPolicy());
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass()
					.getName());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testManualRegistration() {
		try {
			AssertionCredentialsManager am = new AssertionCredentialsManager(conf,ca,db);
			IdentityProvider idp = new IdentityProvider(conf,db,am);
			conf.setRegistrationPolicy(
					new ManualRegistrationPolicy());
			assertEquals(ManualRegistrationPolicy.class.getName(), conf
					.getRegistrationPolicy().getClass()
					.getName());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Active);
			idp.updateUser(cred, users[0]);
			users = idp.findUsers(cred, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testMultipleUsers() {
		try {

			AssertionCredentialsManager am = new AssertionCredentialsManager(conf,ca,db);
			IdentityProvider idp = new IdentityProvider(conf,db,am);
			conf.setRegistrationPolicy(
					new ManualRegistrationPolicy());
			assertEquals(ManualRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass()
					.getName());
			BasicAuthCredential cred = getAdminCreds();
			for (int i = 0; i < 10; i++) {
				Application a = createApplication();
				idp.register(a);
				
				IdPUserFilter uf = new IdPUserFilter();
				uf.setUserId(a.getUserId());
				IdPUser[] users = idp.findUsers(cred, uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Pending, users[0].getStatus());
				assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
				users[0].setStatus(IdPUserStatus.Active);
				idp.updateUser(cred, users[0]);
				users = idp.findUsers(cred, uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Active, users[0].getStatus());
				uf.setUserId("user");
				users = idp.findUsers(cred, uf);
				assertEquals(i + 1, users.length);
			}
			
			IdPUserFilter uf = new IdPUserFilter();
			IdPUser[] users = idp.findUsers(cred, uf);
			assertEquals(11, users.length);
			for (int i = 0; i < 10; i++) {
				IdPUserFilter f = new IdPUserFilter();
				f.setUserId(users[i].getUserId());
				IdPUser[] us = idp.findUsers(cred,f);
				assertEquals(1, us.length);
				us[0].setFirstName("NEW NAME");
				idp.updateUser(cred,us[0]);
				IdPUser[] us2 = idp.findUsers(cred,f);
				assertEquals(1, us2.length);
				assertEquals(us[0], us2[0]);
				idp.removeUser(cred,users[i].getUserId());
				us = idp.findUsers(cred,f);
				assertEquals(0, us.length);
			}
			users = idp.findUsers(cred, uf);
			assertEquals(1, users.length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}

	private BasicAuthCredential getAdminCreds() {
		BasicAuthCredential cred = new BasicAuthCredential();
		cred.setUserId(IdentityProvider.ADMIN_USER_ID);
		cred.setPassword(IdentityProvider.ADMIN_PASSWORD);
		return cred;
	}

	private Application createApplication() {
		Application u = new Application();
		u.setUserId(count + "user");
		u.setEmail(count + "user@mail.com");
		u.setPassword(count + "password");
		u.setFirstName(count + "first");
		u.setLastName(count + "last");
		u.setAddress(count + "address");
		u.setAddress2(count + "address2");
		u.setCity("Columbus");
		u.setState(StateCode.OH);
		u.setCountry(CountryCode.US);
		u.setZipcode("43210");
		u.setPhoneNumber("614-555-5555");
		u.setOrganization(count + "organization");
		count = count + 1;
		return u;
	}



	protected void setUp() throws Exception {
		super.setUp();
		try {
			count = 0;
		    db = TestUtils.getDB();
		    assertEquals(0,db.getUsedConnectionCount());
		    ca = TestUtils.getCA();
		    TestResourceManager trm = new TestResourceManager(IDP_CONFIG);
		    this.conf = (IdPConfiguration)trm.getResource(IdPConfiguration.RESOURCE);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0,db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	
}
