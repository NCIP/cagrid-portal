/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "idp_authn")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_idp_authn")})
public class IdPAuthentication extends AbstractDomainObject {

	private String identity;
	private String gridCredential;
	private String username;
	private String password;
	private IdentityProvider identityProvider;
	private PortalUser portalUser;
	private boolean defaultFlag;
	
	/**
	 * 
	 */
	public IdPAuthentication() {

	}

	@Lob
	public String getGridCredential() {
		return gridCredential;
	}

	public void setGridCredential(String gridCredential) {
		this.gridCredential = gridCredential;
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

	@ManyToOne
	@JoinColumn(name="idp_id")
	public IdentityProvider getIdentityProvider() {
		return identityProvider;
	}

	public void setIdentityProvider(IdentityProvider identityProvider) {
		this.identityProvider = identityProvider;
	}

	@ManyToOne
	@JoinColumn(name="portal_user_id")
	public PortalUser getPortalUser() {
		return portalUser;
	}

	public void setPortalUser(PortalUser portalUser) {
		this.portalUser = portalUser;
	}

	@Column(name = "default_flag")
	public boolean isDefault() {
		return defaultFlag;
	}

	public void setDefault(boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	

}
