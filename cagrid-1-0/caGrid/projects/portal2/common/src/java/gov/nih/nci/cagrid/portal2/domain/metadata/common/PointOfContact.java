/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain.metadata.common;

import gov.nih.nci.cagrid.portal2.domain.Person;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("PointOfContact")
public class PointOfContact extends Person {
	
	private String affiliation;
	private String role;
	
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
