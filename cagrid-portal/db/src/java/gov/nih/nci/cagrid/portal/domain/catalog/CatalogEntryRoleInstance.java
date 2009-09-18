package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cat_role_instance")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_role_instance")
    })
public class CatalogEntryRoleInstance extends AbstractDomainObject implements Commentable, MutableTemporal {

    public String description;

    public CatalogEntry catalogEntry;
    public CatalogEntryRoleType type;
    public CatalogEntryRelationshipInstance relationship;

    public Date createdAt;
    public Date updatedAt;
    private List<Comment> comments = new ArrayList<Comment>();


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name="entry_id")
    public CatalogEntry getCatalogEntry() {
        return catalogEntry;
    }

    public void setCatalogEntry(CatalogEntry catalogEntry) {
        this.catalogEntry = catalogEntry;
    }

    @ManyToOne
    @JoinColumn(name="role_type")
    public CatalogEntryRoleType getType() {
        return type;
    }

    public void setType(CatalogEntryRoleType type) {
        this.type = type;
    }

    @OneToOne
    @JoinColumn(name="relationship_fk")
    public CatalogEntryRelationshipInstance getRelationship() {
        return relationship;
    }

    public void setRelationship(CatalogEntryRelationshipInstance relationship) {
        this.relationship = relationship;
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

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}