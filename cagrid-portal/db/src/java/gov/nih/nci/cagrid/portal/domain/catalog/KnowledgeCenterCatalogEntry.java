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
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("kc")

public class KnowledgeCenterCatalogEntry extends CommunityCatalogEntry {

    public List<InstitutionCatalogEntry> knowledgeCenterInstitutions = new ArrayList<InstitutionCatalogEntry>();

    @ManyToMany
    public List<InstitutionCatalogEntry> getKnowledgeCenterInstitutions() {
        return knowledgeCenterInstitutions;
    }

    public void setKnowledgeCenterInstitutions(List<InstitutionCatalogEntry> knowledgeCenterInstitutions) {
        this.knowledgeCenterInstitutions = knowledgeCenterInstitutions;
    }
}