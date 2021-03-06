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