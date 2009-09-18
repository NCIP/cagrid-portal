package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cat_ratings")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_ratings")
    })
public class Rating extends AbstractDomainObject implements MutableTemporal, Commentable {

    public int rating;

    public Date createdAt;

    public Date updatedAt;


    public CatalogEntry ratingOf;
    public PersonCatalogEntry ratingContributor;
    private List<Comment> comments = new ArrayList<Comment>();

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @ManyToOne
    @JoinColumn(name="catalog_id")
    public CatalogEntry getRatingOf() {
        return ratingOf;
    }

    public void setRatingOf(CatalogEntry ratingOf) {
        this.ratingOf = ratingOf;
    }


    @ManyToOne
    @JoinColumn(name="person_catalog_id")
    public PersonCatalogEntry getRatingContributor() {
        return ratingContributor;
    }

    public void setRatingContributor(PersonCatalogEntry ratingContributor) {
        this.ratingContributor = ratingContributor;
    }


    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}