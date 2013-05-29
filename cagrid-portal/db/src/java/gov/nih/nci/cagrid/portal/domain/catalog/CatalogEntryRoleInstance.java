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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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

    @OneToOne(cascade = CascadeType.ALL)
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