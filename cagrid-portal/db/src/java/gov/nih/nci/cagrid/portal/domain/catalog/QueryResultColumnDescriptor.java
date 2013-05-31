/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
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