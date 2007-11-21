/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.dataservice;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("SourceUMLAssociationEdge")
public class SourceUMLAssociationEdge extends UMLAssociationEdge {
	
	private UMLAssociation association;

	/**
	 * 
	 */
	public SourceUMLAssociationEdge() {

	}
	
	@OneToOne(mappedBy = "source")
	public UMLAssociation getAssociation() {
		return association;
	}
	public void setAssociation(UMLAssociation association) {
		this.association = association;
	}	

}
