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
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

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
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "val_dom")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_val_dom") })
public class ValueDomain extends AbstractDomainObject {

	private List<Enumeration> enumerationCollection = new ArrayList<Enumeration>();
	private String longName;
	private List<SemanticMetadata> semanticMetadata = new ArrayList<SemanticMetadata>();
	private String unitOfMeasure;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "valueDomain")
	public List<Enumeration> getEnumerationCollection() {
		return enumerationCollection;
	}
	public void setEnumerationCollection(List<Enumeration> enumerationCollection) {
		this.enumerationCollection = enumerationCollection;
	}
	

	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "val_dom_sem_meta", 
			joinColumns = @JoinColumn(name = "val_dom_id"), 
			inverseJoinColumns = @JoinColumn(name = "sem_meta_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"val_dom_id", "sem_meta_id" })
	)
	public List<SemanticMetadata> getSemanticMetadata() {
		return semanticMetadata;
	}
	public void setSemanticMetadata(List<SemanticMetadata> semanticMetadata) {
		this.semanticMetadata = semanticMetadata;
	}
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	
}
