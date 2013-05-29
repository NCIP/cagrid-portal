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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Entity
@Table(name = "gene")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_gene") })
public class Gene extends AbstractDomainObject {

	private String name;
	private String symbol;
	private String clusterId;
	private Chromosome chromosome;
	private List<GeneFunctionTerm> terms = new ArrayList<GeneFunctionTerm>();
	private List<Protein> proteins = new ArrayList<Protein>();

	/**
	 * 
	 */
	public Gene() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	@ManyToMany(mappedBy = "genes")
	public List<GeneFunctionTerm> getTerms() {
		return terms;
	}

	public void setTerms(List<GeneFunctionTerm> terms) {
		this.terms = terms;
	}

	@ManyToOne
	@JoinColumn(name = "chromosome_id")
	public Chromosome getChromosome() {
		return chromosome;
	}

	public void setChromosome(Chromosome chromosome) {
		this.chromosome = chromosome;
	}

	@ManyToMany(mappedBy = "genes")
	public List<Protein> getProteins() {
		return proteins;
	}

	public void setProteins(List<Protein> proteins) {
		this.proteins = proteins;
	}

}
