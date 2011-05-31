/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.dataservice;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
	private SourceUMLAssociationEdge source;
	private TargetUMLAssociationEdge target;
	
	public boolean isBidirectional() {
		return bidirectional;
	}
	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "source_id")
	public SourceUMLAssociationEdge getSource() {
		return source;
	}
	public void setSource(SourceUMLAssociationEdge source) {
		this.source = source;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "target_id")
	public TargetUMLAssociationEdge getTarget() {
		return target;
	}
	public void setTarget(TargetUMLAssociationEdge target) {
		this.target = target;
	}
	

}
