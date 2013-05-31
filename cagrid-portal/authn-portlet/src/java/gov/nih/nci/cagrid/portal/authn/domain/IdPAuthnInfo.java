/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.authn.domain;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 *
 */
public class IdPAuthnInfo {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String saml;
    private String gridIdentity;

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


    public String getGridIdentity() {
        return gridIdentity;
    }

    public void setGridIdentity(String gridIdentity) {
        this.gridIdentity = gridIdentity;
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
