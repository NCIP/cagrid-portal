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

import javax.persistence.*;

@Entity
@Table(name = "cat_fav_role")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_fav_role")
    })
public class FavoriteRole extends AbstractDomainObject {

    public String description;

    public CatalogEntry favorite;
    public FavoriteOfRole inverseRole;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name="catalog_id")
    public CatalogEntry getFavorite() {
        return favorite;
    }

    public void setFavorite(CatalogEntry favorite) {
        this.favorite = favorite;
    }

    @OneToOne
    @PrimaryKeyJoinColumn
    public FavoriteOfRole getInverseRole() {
        return inverseRole;
    }

    public void setInverseRole(FavoriteOfRole inverseRole) {
        this.inverseRole = inverseRole;
    }
}