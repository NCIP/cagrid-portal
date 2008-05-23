/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.domain.metadata.common.SemanticMetadata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "sem_meta_map")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_sem_meta_map") })
public class SemanticMetadataMapping extends AbstractDomainObject {

	private GridService gridService;
	private String objectPath;
	private String objectIdentifier;
	private SemanticMetadata semanticMetadata;
	private ConceptHierarchyNode concept;
	
	/**
	 * 
	 */
	public SemanticMetadataMapping() {

	}

	@ManyToOne
	@JoinColumn(name = "service_id")
	public GridService getGridService() {
		return gridService;
	}

	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}
	@Column(length = 4000)
	public String getObjectPath() {
		return objectPath;
	}

	public void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
	}

	public String getObjectIdentifier() {
		return objectIdentifier;
	}

	public void setObjectIdentifier(String objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}

	@OneToOne
	@JoinColumn(name = "sem_meta_id")
	public SemanticMetadata getSemanticMetadata() {
		return semanticMetadata;
	}

	public void setSemanticMetadata(SemanticMetadata semanticMetadata) {
		this.semanticMetadata = semanticMetadata;
	}

	@ManyToOne
	@JoinColumn(name = "c_hier_node_id")
	public ConceptHierarchyNode getConcept() {
		return concept;
	}

	public void setConcept(ConceptHierarchyNode concept) {
		this.concept = concept;
	}

}
