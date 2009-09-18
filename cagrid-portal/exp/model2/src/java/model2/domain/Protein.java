/**
 * 
 */
package model2.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "protein")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_protein") })
public class Protein extends AbstractDomainObject {
	
	private String name;
	private List<Gene> genes = new ArrayList<Gene>();
	private List<Taxon> taxons = new ArrayList<Taxon>();

	/**
	 * 
	 */
	public Protein() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany
	@JoinTable(
			name = "proteins_genes", 
			joinColumns = 
				@JoinColumn(name = "protein_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "gene_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"protein_id", "gene_id" }))
	public List<Gene> getGenes() {
		return genes;
	}

	public void setGenes(List<Gene> genes) {
		this.genes = genes;
	}

	@ManyToMany
	@JoinTable(
			name = "proteins_taxons", 
			joinColumns = 
				@JoinColumn(name = "protein_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "taxon_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"protein_id", "taxon_id" }))
	public List<Taxon> getTaxons() {
		return taxons;
	}

	public void setTaxons(List<Taxon> taxons) {
		this.taxons = taxons;
	}

}
