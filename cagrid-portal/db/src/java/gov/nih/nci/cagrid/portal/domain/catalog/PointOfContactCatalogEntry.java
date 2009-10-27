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
public class PointOfContactCatalogEntry extends CatalogEntry {
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
