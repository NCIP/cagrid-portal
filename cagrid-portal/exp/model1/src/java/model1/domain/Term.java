/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package model1.domain;

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
@Table(name = "terms_node")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_terms_node") })
public class Term extends AbstractDomainObject {

	private String value;
	private Term parent;
	private List<Term> children = new ArrayList<Term>();
	private List<Term> ancestors = new ArrayList<Term>();
	private List<Term> descendants = new ArrayList<Term>();
	private List<Gene> genes = new ArrayList<Gene>();
	private int level;
	
	/**
	 * 
	 */
	public Term() {

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public Term getParent() {
		return parent;
	}

	public void setParent(Term parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
	public List<Term> getChildren() {
		return children;
	}

	public void setChildren(List<Term> children) {
		this.children = children;
	}

	@ManyToMany
	@JoinTable(
			name = "terms_desc", 
			joinColumns = 
				@JoinColumn(name = "descendant_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "ancestor_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"ancestor_id", "descendant_id" }))
	@OrderBy("level desc")	
	public List<Term> getAncestors() {
		return ancestors;
	}

	
	public void setAncestors(List<Term> ancestors) {
		this.ancestors = ancestors;
	}

	@ManyToMany(
			cascade = CascadeType.ALL, mappedBy="ancestors"
    )
	public List<Term> getDescendants() {
		return descendants;
	}

	public void setDescendants(List<Term> descendants) {
		this.descendants = descendants;
	}

	@ManyToMany
	@JoinTable(
			name = "terms_genes", 
			joinColumns = 
				@JoinColumn(name = "term_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "gene_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"term_id", "gene_id" }))
	public List<Gene> getGenes() {
		return genes;
	}

	public void setGenes(List<Gene> genes) {
		this.genes = genes;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
