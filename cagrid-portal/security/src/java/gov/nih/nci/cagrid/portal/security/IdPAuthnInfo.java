/**
 * 
 */
package gov.nih.nci.cagrid.portal.security;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class IdPAuthnInfo {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String saml;

	public IdPAuthnInfo() {

	}

	public IdPAuthnInfo(String username, String email, String firstName,
			String lastName, String saml) {
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.saml = saml;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSaml() {
		return saml;
	}

	public void setSaml(String saml) {
		this.saml = saml;
	}

}
