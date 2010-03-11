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