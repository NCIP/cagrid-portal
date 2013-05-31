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
package model2.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForceDiscriminator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "hier_node")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_hier_node") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "hier_type", discriminatorType = DiscriminatorType.STRING)
@ForceDiscriminator
public abstract class HierarchyNode extends AbstractDomainObject {

	private String value;
	private HierarchyNode parent;
	private List<HierarchyNode> children = new ArrayList<HierarchyNode>();
	private List<HierarchyNode> ancestors = new ArrayList<HierarchyNode>();
	private List<HierarchyNode> descendants = new ArrayList<HierarchyNode>();

	private int level;
	
	/**
	 * 
	 */
	public HierarchyNode() {

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public HierarchyNode getParent() {
		return parent;
	}

	public void setParent(HierarchyNode parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
	public List<HierarchyNode> getChildren() {
		return children;
	}

	public void setChildren(List<HierarchyNode> children) {
		this.children = children;
	}

	@ManyToMany
	@JoinTable(
			name = "hier_desc", 
			joinColumns = 
				@JoinColumn(name = "descendant_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "ancestor_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"ancestor_id", "descendant_id" }))
	@OrderBy("level desc")	
	public List<HierarchyNode> getAncestors() {
		return ancestors;
	}

	
	public void setAncestors(List<HierarchyNode> ancestors) {
		this.ancestors = ancestors;
	}

	@ManyToMany(
			cascade = CascadeType.ALL, mappedBy="ancestors"
    )
	public List<HierarchyNode> getDescendants() {
		return descendants;
	}

	public void setDescendants(List<HierarchyNode> descendants) {
		this.descendants = descendants;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
