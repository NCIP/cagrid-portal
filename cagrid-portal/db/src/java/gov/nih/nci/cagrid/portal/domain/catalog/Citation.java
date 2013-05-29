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

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cat_citations")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_citations")
    })
public class Citation extends AbstractDomainObject implements Temporal {

    /**
     * citation not nullable
     */
    public String citation;

    public String pubMedID;

    public Date createdAt;

    public List<CatalogEntry> citationOf = new ArrayList<CatalogEntry>();

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getPubMedID() {
        return pubMedID;
    }

    public void setPubMedID(String pubMedID) {
        this.pubMedID = pubMedID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToMany(mappedBy="citations")
    public List<CatalogEntry> getCitationOf() {
        return citationOf;
    }

    public void setCitationOf(List<CatalogEntry> citationOf) {
        this.citationOf = citationOf;
    }
}