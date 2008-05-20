/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "c_hier_node")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_c_hier_node") })
public class ConceptHierarchyNode extends AbstractDomainObject {

	private String code;
	private String name;
	private String description;
	private ConceptHierarchy hierarchy;
	private ConceptHierarchyNode parent;
	private List<ConceptHierarchyNode> children = new ArrayList<ConceptHierarchyNode>();
	private List<ConceptHierarchyNode> ancestors = new ArrayList<ConceptHierarchyNode>();
	private List<ConceptHierarchyNode> descendants = new ArrayList<ConceptHierarchyNode>();
	private int level;
	private List<GridService> services = new ArrayList<GridService>();
	
	/**
	 * 
	 */
	public ConceptHierarchyNode() {

	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public ConceptHierarchyNode getParent() {
		return parent;
	}

	public void setParent(ConceptHierarchyNode parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
	public List<ConceptHierarchyNode> getChildren() {
		return children;
	}

	public void setChildren(List<ConceptHierarchyNode> children) {
		this.children = children;
	}
	
	
	@ManyToMany
	@JoinTable(
			name = "c_hier_desc", 
			joinColumns = 
				@JoinColumn(name = "descendant_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "ancestor_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"ancestor_id", "descendant_id" }))
	@OrderBy("level desc")					
	public List<ConceptHierarchyNode> getAncestors() {
		return ancestors;
	}

	
	public void setAncestors(List<ConceptHierarchyNode> ancestors) {
		this.ancestors = ancestors;
	}

	@ManyToMany(
			cascade = CascadeType.ALL, mappedBy="ancestors"
    )
	public List<ConceptHierarchyNode> getDescendants() {
		return descendants;
	}

	public void setDescendants(List<ConceptHierarchyNode> descendants) {
		this.descendants = descendants;
	}

	@ManyToOne
	@JoinColumn(name = "hierarchy_id")
	public ConceptHierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(ConceptHierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@ManyToMany(
			mappedBy="concepts"
    )
	public List<GridService> getServices() {
		return services;
	}

	public void setServices(List<GridService> services) {
		this.services = services;
	}

}
