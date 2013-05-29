/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "enumer")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_enumer") })
public class Enumeration extends AbstractDomainObject {

	private String permissibleValue;
	private List<SemanticMetadata> semanticMetadata = new ArrayList<SemanticMetadata>();
	private String valueMeaning;
	private ValueDomain valueDomain;


	public String getPermissibleValue() {
		return permissibleValue;
	}
	public void setPermissibleValue(String permissibleValue) {
		this.permissibleValue = permissibleValue;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "enumer_sem_meta", 
			joinColumns = @JoinColumn(name = "enumer_id"), 
			inverseJoinColumns = @JoinColumn(name = "sem_meta_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"enumer_id", "sem_meta_id" })
	)
	public List<SemanticMetadata> getSemanticMetadata() {
		return semanticMetadata;
	}
	public void setSemanticMetadata(List<SemanticMetadata> semanticMetadata) {
		this.semanticMetadata = semanticMetadata;
	}
	
	@ManyToOne
	public ValueDomain getValueDomain() {
		return valueDomain;
	}
	public void setValueDomain(ValueDomain valueDomain) {
		this.valueDomain = valueDomain;
	}
	
	public String getValueMeaning() {
		return valueMeaning;
	}
	public void setValueMeaning(String valueMeaning) {
		this.valueMeaning = valueMeaning;
	}
	
}
