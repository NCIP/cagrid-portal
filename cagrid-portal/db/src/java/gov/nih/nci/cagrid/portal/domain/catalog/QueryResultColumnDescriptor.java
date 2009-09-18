package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("query_result")
public class QueryResultColumnDescriptor extends OutputColumnDescriptor {

    public SharedQueryCatalogEntry query;
    public UMLAttributeCatalogEntry umlAttribute;

    @ManyToOne
    @JoinColumn(name="query_id")
    public SharedQueryCatalogEntry getQuery() {
        return query;
    }

    public void setQuery(SharedQueryCatalogEntry query) {
        this.query = query;
    }

    @ManyToOne
    @JoinColumn(name="umlAttribute_id")
    public UMLAttributeCatalogEntry getUmlAttribute() {
        return umlAttribute;
    }

    public void setUmlAttribute(UMLAttributeCatalogEntry umlAttribute) {
        this.umlAttribute = umlAttribute;
    }
}