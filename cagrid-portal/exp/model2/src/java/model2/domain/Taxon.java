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
import javax.persistence.ManyToMany;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("Taxon")
public class Taxon extends HierarchyNode {

	private List<Protein> proteins = new ArrayList<Protein>();
	/**
	 * 
	 */
	public Taxon() {

	}
	
	@ManyToMany(mappedBy = "taxons")
	public List<Protein> getProteins() {
		return proteins;
	}
	public void setProteins(List<Protein> proteins) {
		this.proteins = proteins;
	}

}
