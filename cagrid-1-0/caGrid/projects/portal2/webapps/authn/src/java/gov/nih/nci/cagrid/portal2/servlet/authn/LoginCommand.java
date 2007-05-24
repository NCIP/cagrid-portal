/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class LoginCommand {
	
	private static final String GRID_USER = "grid_user";
	private static final String NON_GRID_USER = "non_grid_user";
	
	
	private String userType;
	private String username;
	private String password;
	private String targetUrl;
	private String idpUrl;
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getIdpUrl() {
		return idpUrl;
	}

	public void setIdpUrl(String idpUrl) {
		this.idpUrl = idpUrl;
	}

}
