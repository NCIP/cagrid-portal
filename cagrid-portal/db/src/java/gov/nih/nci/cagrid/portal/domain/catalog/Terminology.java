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

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Entity
@Table(name = "cat_terminology")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_terminology")
    })
public class Terminology extends AbstractDomainObject {

    public String identifier;

    public String label;

    public URL serviceUrl;


    public List<Term> terms = new ArrayList<Term>();

    public Vector sharedQueryCriteria;

    public List<TerminologyCatalogEntry> catalogEntries = new ArrayList<TerminologyCatalogEntry>();

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public URL getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(URL serviceUrl) {
        this.serviceUrl = serviceUrl;
    }


    public Vector getSharedQueryCriteria() {
        return sharedQueryCriteria;
    }

    public void setSharedQueryCriteria(Vector sharedQueryCriteria) {
        this.sharedQueryCriteria = sharedQueryCriteria;
    }

    @OneToMany(mappedBy = "terminology")
    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    @OneToMany(mappedBy = "about")
    public List<TerminologyCatalogEntry> getCatalogEntries() {
        return catalogEntries;
    }

    public void setCatalogEntries(List<TerminologyCatalogEntry> catalogEntries) {
        this.catalogEntries = catalogEntries;
    }
}