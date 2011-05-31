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
