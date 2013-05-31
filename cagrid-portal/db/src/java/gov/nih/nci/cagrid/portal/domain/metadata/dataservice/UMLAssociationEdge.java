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
package gov.nih.nci.cagrid.portal.domain.metadata.dataservice;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForceDiscriminator;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "edge_type", discriminatorType = DiscriminatorType.STRING)
@ForceDiscriminator
public abstract class UMLAssociationEdge extends AbstractDomainObject {

	private int minCardinality;
	private int maxCardinality;
	private String role;
	private UMLClass type;

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
