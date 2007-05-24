/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Entity
@DiscriminatorValue("PortalUser")
public class PortalUser extends Person {

	private String username;

	private String password;

	private List<Role> roles = new ArrayList<Role>();

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

	@ManyToMany
	@JoinTable(
			name = "portaluser_roles", 
			joinColumns = 
				@JoinColumn(name = "person_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "role_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"person_id", "role_id" }))
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
