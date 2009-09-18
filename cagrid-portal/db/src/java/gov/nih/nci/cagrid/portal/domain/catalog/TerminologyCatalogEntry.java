package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("terminology")

public class TerminologyCatalogEntry extends DataSetCatalogEntry {

    public Terminology about;

    @ManyToOne
    @JoinColumn(name="terminology_id")
    public Terminology getAbout() {
        return about;
    }

    public void setAbout(Terminology about) {
        this.about = about;
    }
}