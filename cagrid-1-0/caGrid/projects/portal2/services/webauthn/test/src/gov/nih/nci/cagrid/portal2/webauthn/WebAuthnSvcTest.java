/**
 * 
 */
package gov.nih.nci.cagrid.portal2.webauthn;

import gov.nih.nci.cagrid.portal2.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.webauthn.common.WebAuthnSvc;
import gov.nih.nci.cagrid.portal2.webauthn.types.UserInfoType;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.Credential;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal2.webauthn.types.faults.InvalidKeyFault;
import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class WebAuthnSvcTest extends TestCase {

	private ApplicationContext ctx;

	public WebAuthnSvcTest() {
		try {
			// this.ctx = new FileSystemXmlApplicationContext(new
			// String[]{"etc/webauthnsvc-context.xml"});
			this.ctx = new ClassPathXmlApplicationContext(new String[] {
					"applicationContext-db.xml", "webauthnsvc-context.xml" });
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error getting application context");

		}
	}

	public void testGetGridPortalUserInfo() {
		WebAuthnSvc svc = (WebAuthnSvc) this.ctx.getBean("webAuthnSvc");
		String idpUrl = "https://cbiovdev5035.nci.nih.gov:8443/wsrf/services/cagrid/Dorian";
		BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
		bac.setUserId("grid_mgr");
		bac.setPassword("Gr!d_mgr123");
		Credential cred = new Credential();
		cred.setBasicAuthenticationCredential(bac);
		String loginKey = null;
		try {
			loginKey = svc.createLoginKeyForGridUser(idpUrl, cred);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error getting login key: " + ex.getMessage());
		}
		assertNotNull("Login Key is null", loginKey);
		UserInfoType userInfo = null;
		try {
			userInfo = svc.getUserInfo(loginKey);
		} catch (Exception ex) {
			fail("Error getting user info: " + ex.getMessage());
		}
		assertNotNull("User Info is null", userInfo);
		try {
			svc.getUserInfo(loginKey);
			fail("Should have thrown exception when retrieving user info for second time.");
		} catch (InvalidKeyFault ex) {
			assertTrue(true);
		} catch (Exception ex) {
			fail("Should have thrown InvalidKeyFault. Got "
					+ ex.getClass().getName() + " instead");
		}
		assertNotNull("SAML is null", userInfo.getSaml());
		System.out.println(userInfo.getSaml().getXml());

	}

	public void testGetPortalUserInfo() {

		PortalUser newUser = new PortalUser();
		newUser.setUsername("testuser");
		newUser.setPassword("testuser");

		PortalUserDao portalUserDao = (PortalUserDao) this.ctx
				.getBean("portalUserDao");
		WebAuthnSvc svc = (WebAuthnSvc) this.ctx.getBean("webAuthnSvc");

		try {
			BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
			bac.setUserId(newUser.getUsername());
			bac.setPassword(newUser.getPassword());
			Credential cred = new Credential();
			cred.setBasicAuthenticationCredential(bac);
			String loginKey = null;

			try {
				loginKey = svc.createLoginKeyForLocalUser(cred);
				fail("Should have failed to create login key for non-existent local user");
			} catch (InvalidCredentialFault ex) {
				assertTrue(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Error encountered: " + ex.getMessage());
			}

			portalUserDao.save(newUser);

			try {
				loginKey = svc.createLoginKeyForLocalUser(cred);
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Error getting login key: " + ex.getMessage());
			}
			assertNotNull("Login Key is null", loginKey);
			UserInfoType userInfo = null;
			try {
				userInfo = svc.getUserInfo(loginKey);
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Error getting user info: " + ex.getMessage());
			}
			assertNotNull("User Info is null", userInfo);
			try {
				svc.getUserInfo(loginKey);
				fail("Should have thrown exception when retrieving user info for second time.");
			} catch (InvalidKeyFault ex) {
				assertTrue(true);
			} catch (Exception ex) {
				fail("Should have thrown InvalidKeyFault. Got "
						+ ex.getClass().getName() + " instead");
			}
			assertNull("SAML should be null", userInfo.getSaml());
		} finally {
			portalUserDao.delete(newUser);
		}
	}

}
