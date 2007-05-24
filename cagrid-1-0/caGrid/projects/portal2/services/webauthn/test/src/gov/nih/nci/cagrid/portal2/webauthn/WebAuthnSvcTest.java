/**
 * 
 */
package gov.nih.nci.cagrid.portal2.webauthn;

import gov.nih.nci.cagrid.portal2.webauthn.common.WebAuthnSvc;
import gov.nih.nci.cagrid.portal2.webauthn.types.UserInfoType;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.Credential;
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
	
	public WebAuthnSvcTest(){
		try{
//			this.ctx = new FileSystemXmlApplicationContext(new String[]{"etc/webauthnsvc-context.xml"});
			this.ctx = new ClassPathXmlApplicationContext(new String[]{"applicationContext-db.xml", "webauthnsvc-context.xml"});
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("Error getting application context");
			
		}
	}
	
	public void testGetUserInfo(){
		WebAuthnSvc svc = (WebAuthnSvc) this.ctx.getBean("webAuthnSvc");
		String idpUrl = "https://cbiovdev5034.nci.nih.gov:8443/wsrf/services/cagrid/Dorian";
		BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
		bac.setUserId("manager");
		bac.setPassword("manager");
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
		try{
			userInfo = svc.getUserInfo(loginKey);
		}catch(Exception ex){
			fail("Error getting user info: " + ex.getMessage());
		}
		assertNotNull("User Info is null", userInfo);
		try{
			svc.getUserInfo(loginKey);
			fail("Should have thrown exception when retrieving user info for second time.");
		}catch(InvalidKeyFault ex){
			assertTrue(true);
		}catch(Exception ex){
			fail("Should have thrown InvalidKeyFault. Got " + ex.getClass().getName() + " instead");
		}
		System.out.println(userInfo.getSaml().getXml());
	}

}
