/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

import gov.nih.nci.cagrid.portal2.domain.GridPortalUser;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.webauthn.client.WebAuthnSvcClient;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.Credential;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class AuthnUtils {
	public static String generateLoginKey(WebAuthnSvcClient webAuthnSvcClient,
			PortalUser user) {
		String loginKey = null;
		try {
			Credential credential = new Credential();
			BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
			bac.setUserId(user.getUsername());
			bac.setPassword(user.getPassword());
			credential.setBasicAuthenticationCredential(bac);
			if (user instanceof GridPortalUser) {
				String idpUrl = ((GridPortalUser)user).getIdpUrl();
				loginKey = webAuthnSvcClient.createLoginKeyForGridUser(idpUrl, credential);
			} else {
				loginKey = webAuthnSvcClient.createLoginKeyForLocalUser(credential);
			}
		} catch (Exception ex) {
			// TODO: better error handling
			throw new RuntimeException("Error getting login key: "
					+ ex.getMessage(), ex);
		}
		return loginKey;
	}
}
