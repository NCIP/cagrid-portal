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
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.util.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cat_entry")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {
                @Parameter(name = "sequence", value = "seq_cat_entry")
        })
@DiscriminatorColumn(
        name = "catalog_type",
        discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue("CatalogEntry")
public class CatalogEntry extends AbstractDomainObject implements Commentable, MutableTemporal {

    public String name;

    public String nameAbbrv;

    public String description;

    public Date createdAt;

    public Date updatedAt;

    public CatalogEntryWorkflowStatus workflowStatus;

    public long numViews;

    public Date lastViewedAt;

    public boolean published;

    public List tags = new ArrayList<String>();

    public List<Citation> citations = new ArrayList<Citation>();

    public List<Term> terms = new ArrayList<Term>();

    public List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();

    public List<File> files = new ArrayList<File>();

    public List<Rating> ratings = new ArrayList<Rating>();

    public List<Term> areasOfFocus = new ArrayList<Term>();

    public PersonCatalogEntry contributor;

    public PortalUser author;

    public List<FavoriteOfRole> favoriteOfRole = new ArrayList<FavoriteOfRole>();

    public List<CatalogEntryRoleInstance> roles = new ArrayList<CatalogEntryRoleInstance>();

    private List<Comment> comments = new ArrayList<Comment>();

    private Image image;

    private Image thumbnail;

    private boolean featured;

    public static final int NAME_MAX_LENGTH_ALLOWED = 18;

    public boolean hidden;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getNameAbbrv() {
        return StringUtils.abbreviate(name, NAME_MAX_LENGTH_ALLOWED);
    }

    public void setNameAbbrv(String nameAbbrv) {
        this.nameAbbrv = nameAbbrv;
    }

    @Column(length = 4000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public CatalogEntryWorkflowStatus getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(CatalogEntryWorkflowStatus workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public long getNumViews() {
        return numViews;
    }

    public void setNumViews(long numViews) {
        this.numViews = numViews;
    }

    public Date getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(Date lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    //ToDo map this to something
//    public List getTags() {
//        return tags;
//    }
//
//    public void setTags(List tags) {
//        this.tags = tags;
//    }

    @ManyToMany(cascade = {CascadeType.ALL})
    public List<Citation> getCitations() {
        return citations;
    }

    public void setCitations(List<Citation> citations) {
        this.citations = citations;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cat_entry_terms",
            joinColumns = @JoinColumn(name = "entry_id"),
            inverseJoinColumns = @JoinColumn(name = "term_id"),
            uniqueConstraints = @UniqueConstraint(columnNames =
                    {"entry_id", "term_id"})
    )
    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hyperlinkOf")
    public List<Hyperlink> getHyperlinks() {
        return hyperlinks;
    }

    public void setHyperlinks(List<Hyperlink> hyperlinks) {
        this.hyperlinks = hyperlinks;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fileOf")
    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ratingOf")
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @ManyToOne
    @JoinColumn(name = "contributor_id")
    public PersonCatalogEntry getContributor() {
        return contributor;
    }

    public void setContributor(PersonCatalogEntry contributor) {
        this.contributor = contributor;
    }

    @OneToMany(mappedBy = "favoriteOf", cascade = CascadeType.ALL)
    public List<FavoriteOfRole> getFavoriteOfRole() {
        return favoriteOfRole;
    }

    public void setFavoriteOfRole(List<FavoriteOfRole> favoriteOfRole) {
        this.favoriteOfRole = favoriteOfRole;
    }

    @OneToMany(mappedBy = "catalogEntry", cascade = CascadeType.ALL)
    public List<CatalogEntryRoleInstance> getRoles() {
        return roles;
    }

    public void setRoles(List<CatalogEntryRoleInstance> roles) {
        this.roles = roles;
    }

    @ManyToOne
    public PortalUser getAuthor() {
        return author;
    }

    public void setAuthor(PortalUser author) {
        this.author = author;
    }

    @ManyToMany
    @JoinTable(
            name = "cat_entry_aofterms",
            joinColumns = @JoinColumn(name = "entry_id"),
            inverseJoinColumns = @JoinColumn(name = "term_id"),
            uniqueConstraints = @UniqueConstraint(columnNames =
                    {"entry_id", "term_id"})
    )
    public List<Term> getAreasOfFocus() {
        return areasOfFocus;
    }

    public void setAreasOfFocus(List<Term> areasOfFocus) {
        this.areasOfFocus = areasOfFocus;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Column(name = "featured")
    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    @Column(name = "hidden")
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}