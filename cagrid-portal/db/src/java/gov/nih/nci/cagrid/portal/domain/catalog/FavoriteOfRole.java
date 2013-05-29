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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cat_fav_of_role")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_fav_of_role")
    })
public class FavoriteOfRole extends AbstractDomainObject {

    public String description;

    public PersonCatalogEntry favoriteOf;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name="person_catalog_id")
    public PersonCatalogEntry getFavoriteOf() {
        return favoriteOf;
    }

    public void setFavoriteOf(PersonCatalogEntry favoriteOf) {
        this.favoriteOf = favoriteOf;
    }
}