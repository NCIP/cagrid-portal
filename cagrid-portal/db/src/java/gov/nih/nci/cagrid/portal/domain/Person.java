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
package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Entity
@Table(name = "persons")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_persons") })
public class Person extends AbstractDomainObject {

	private String firstName;

	private String lastName;

	private String emailAddress;

	private String phoneNumber;

	private List<Address> addresses = new ArrayList<Address>();
	
	private List<PointOfContact> pointOfContacts = new ArrayList<PointOfContact>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "person_addresses", 
			joinColumns = @JoinColumn(name = "person_id"), 
			inverseJoinColumns = @JoinColumn(name = "address_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"person_id", "address_id" })
	)
	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}


	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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


	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@OneToMany(mappedBy = "person")
	public List<PointOfContact> getPointOfContacts() {
		return pointOfContacts;
	}

	public void setPointOfContacts(List<PointOfContact> pointOfContacts) {
		this.pointOfContacts = pointOfContacts;
	}

}
