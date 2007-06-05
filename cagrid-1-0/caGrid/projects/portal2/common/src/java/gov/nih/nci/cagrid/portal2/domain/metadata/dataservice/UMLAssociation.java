/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain.metadata.dataservice;

import gov.nih.nci.cagrid.portal2.domain.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "uml_assocs")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_uml_assocs")
    }
)
public class UMLAssociation extends AbstractDomainObject {
	
	private boolean bidirectional;
	private UMLAssociationEdge source;
	private UMLAssociationEdge target;
	
	public boolean isBidirectional() {
		return bidirectional;
	}
	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}
	
	@OneToOne(mappedBy = "association")
	public UMLAssociationEdge getSource() {
		return source;
	}
	public void setSource(UMLAssociationEdge source) {
		this.source = source;
	}
	
	@OneToOne(mappedBy = "association")
	public UMLAssociationEdge getTarget() {
		return target;
	}
	public void setTarget(UMLAssociationEdge target) {
		this.target = target;
	}
	

}
