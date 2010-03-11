package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("criterion")

public class CriterionDescriptor extends InputDescriptor {

    public SharedQueryCatalogEntry query;
    public UMLAttributeCatalogEntry umlAttribute;

    @ManyToOne
       @JoinColumn(name="sharedQueryCatalog_fk")
    public SharedQueryCatalogEntry getQuery() {
        return query;
    }

    public void setQuery(SharedQueryCatalogEntry query) {
        this.query = query;
    }

    @ManyToOne
    @JoinColumn(name="umlattribute_fk")
    public UMLAttributeCatalogEntry getUmlAttribute() {
        return umlAttribute;
    }

    public void setUmlAttribute(UMLAttributeCatalogEntry umlAttribute) {
        this.umlAttribute = umlAttribute;
    }
}