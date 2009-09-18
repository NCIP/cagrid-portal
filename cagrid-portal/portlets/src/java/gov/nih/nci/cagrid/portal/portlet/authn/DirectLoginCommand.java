/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DirectLoginCommand {
	
	private String username;
	private String password;
	private String idpUrl;
	private String portalAuthnUrl;

	/**
	 * 
	 */
	public DirectLoginCommand() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdpUrl() {
		return idpUrl;
	}

	public void setIdpUrl(String idpUrl) {
		this.idpUrl = idpUrl;
	}

	public String getPortalAuthnUrl() {
		return portalAuthnUrl;
	}

	public void setPortalAuthnUrl(String portalAuthnUrl) {
		this.portalAuthnUrl = portalAuthnUrl;
	}

}
