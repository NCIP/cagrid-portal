package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("dataset")

public class DataSetCatalogEntry extends CatalogEntry {

    public ProvenanceGraph provenance;

    public List<Term> typesOfCancer = new ArrayList<Term>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="provenance_id")
    public ProvenanceGraph getProvenance() {
        return provenance;
    }

    public void setProvenance(ProvenanceGraph provenance) {
        this.provenance = provenance;
    }

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "cat_entry_tocterms", 
			joinColumns = @JoinColumn(name = "entry_id"), 
			inverseJoinColumns = @JoinColumn(name = "term_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"entry_id", "term_id" })
	)
    public List<Term> getTypesOfCancer() {
        return typesOfCancer;
    }

    public void setTypesOfCancer(List<Term> typesOfCancer) {
        this.typesOfCancer = typesOfCancer;
    }
}