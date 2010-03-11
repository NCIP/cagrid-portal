/**
 * 
 */
package model1.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "annots")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_annots") })
public class Annotation extends AbstractDomainObject {

	private String value;
	private Source source;
	private Gene gene;
	
	/**
	 * 
	 */
	public Annotation() {

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name = "source_id")
	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	@ManyToOne
	@JoinColumn(name = "gene_id")
	public Gene getGene() {
		return gene;
	}

	public void setGene(Gene gene) {
		this.gene = gene;
	}

	

}
