/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "c_hier")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_c_hier") })
public class ConceptHierarchy extends AbstractDomainObject {

	private String name;
	private String uri;
	private List<ConceptHierarchyNode> nodes = new ArrayList<ConceptHierarchyNode>();
	
	
	/**
	 * 
	 */
	public ConceptHierarchy() {

	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUri() {
		return uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
	}


	@OneToMany(cascade = CascadeType.ALL, mappedBy = "hierarchy")
	public List<ConceptHierarchyNode> getNodes() {
		return nodes;
	}


	public void setNodes(List<ConceptHierarchyNode> nodes) {
		this.nodes = nodes;
	}

}
