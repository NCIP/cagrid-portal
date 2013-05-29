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
package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("umlAttribute")
public class UMLAttributeCatalogEntry extends CatalogEntry {

    public List<CriterionDescriptor> savedQueryCriteria = new ArrayList<CriterionDescriptor>();
    public List<QueryResultColumnDescriptor> savedQueryColumns = new ArrayList<QueryResultColumnDescriptor>();

    @OneToMany(mappedBy="umlAttribute")
    public List<CriterionDescriptor> getSavedQueryCriteria() {
        return savedQueryCriteria;
    }

    public void setSavedQueryCriteria(List<CriterionDescriptor> savedQueryCriteria) {
        this.savedQueryCriteria = savedQueryCriteria;
    }

    @OneToMany(mappedBy = "umlAttribute")
    public List<QueryResultColumnDescriptor> getSavedQueryColumns() {
        return savedQueryColumns;
    }

    public void setSavedQueryColumns(List<QueryResultColumnDescriptor> savedQueryColumns) {
        this.savedQueryColumns = savedQueryColumns;
    }
}