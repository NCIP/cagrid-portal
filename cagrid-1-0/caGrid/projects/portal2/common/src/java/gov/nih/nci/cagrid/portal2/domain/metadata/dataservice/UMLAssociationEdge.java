/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain.metadata.dataservice;

import gov.nih.nci.cagrid.portal2.domain.AbstractDomainObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "uml_assoc_edges")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_uml_edges")
    }
)
public class UMLAssociationEdge extends AbstractDomainObject {

	private int minCardinality;
	private int maxCardinality;
	private String role;
	private UMLClass type;
	private UMLAssociation association;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "assoc_id")
	public UMLAssociation getAssociation() {
		return association;
	}
	public void setAssociation(UMLAssociation association) {
		this.association = association;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "uml_class_id")
	public UMLClass getType() {
		return type;
	}
	public void setType(UMLClass type) {
		this.type = type;
	}
	public int getMaxCardinality() {
		return maxCardinality;
	}
	public void setMaxCardinality(int maxCardinality) {
		this.maxCardinality = maxCardinality;
	}
	public int getMinCardinality() {
		return minCardinality;
	}
	public void setMinCardinality(int minCardinality) {
		this.minCardinality = minCardinality;
	}
	
}
