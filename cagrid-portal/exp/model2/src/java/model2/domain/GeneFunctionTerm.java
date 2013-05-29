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
package model2.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("GeneFunctionTerm")
public class GeneFunctionTerm extends HierarchyNode {

	private List<Gene> genes = new ArrayList<Gene>();	
	
	/**
	 * 
	 */
	public GeneFunctionTerm() {

	}
	
	@ManyToMany
	@JoinTable(
			name = "hier_nodes_genes", 
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

	
	

}
