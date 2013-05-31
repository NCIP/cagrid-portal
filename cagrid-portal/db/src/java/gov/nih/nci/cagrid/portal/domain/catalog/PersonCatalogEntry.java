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

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("person")

public class PersonCatalogEntry extends BasePersonCatalogEntry implements Contactable {


    public PortalUser about;

    public List<Rating> contributorRatings = new ArrayList<Rating>();

    public List<Comment> commentsOf = new ArrayList<Comment>();

    public List<CatalogEntry> contributions = new ArrayList<CatalogEntry>();

    public List<FavoriteRole> favoriteRole = new ArrayList<FavoriteRole>();
  


    @OneToOne
    @JoinColumn(name = "person_id")
    public PortalUser getAbout() {
        return about;
    }

    public void setAbout(PortalUser about) {
        this.about = about;
    }

    @OneToMany(mappedBy = "ratingContributor", cascade = CascadeType.ALL)
    public List<Rating> getContributorRatings() {
        return contributorRatings;
    }

    public void setContributorRatings(List<Rating> contributorRatings) {
        this.contributorRatings = contributorRatings;
    }

    // Will delete comments when user is deleted
    @OneToMany(mappedBy = "commentor", cascade = CascadeType.ALL)
    public List<Comment> getCommentsOf() {
        return commentsOf;
    }

    public void setCommentsOf(List<Comment> commentsOf) {
        this.commentsOf = commentsOf;
    }

    @OneToMany(mappedBy = "contributor")
    public List<CatalogEntry> getContributions() {
        return contributions;
    }

    public void setContributions(List<CatalogEntry> contributions) {
        this.contributions = contributions;
    }

    @OneToMany(mappedBy = "favorite")
    public List<FavoriteRole> getFavoriteRole() {
        return favoriteRole;
    }

    public void setFavoriteRole(List<FavoriteRole> favoriteRole) {
        this.favoriteRole = favoriteRole;
    }


}