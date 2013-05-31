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

import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

@Entity
@DiscriminatorValue("poc")
public class PointOfContactCatalogEntry extends BasePersonCatalogEntry {
    public PointOfContact about;


    @OneToOne
    @JoinColumn(name = "poc_id")
    public PointOfContact getAbout() {
        return about;
    }

    public void setAbout(PointOfContact about) {
        this.about = about;
    }
}
