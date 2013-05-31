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

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.PortalUser;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "cat_relationship_instance")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_relationship_instance")
    })
public class CatalogEntryRelationshipInstance extends AbstractDomainObject implements Commentable {

    public Date createdAt;

    public Date updatedAt;

    public Date since;

    public Date until;

    public CatalogEntryRoleInstance roleA;
    public CatalogEntryRoleInstance roleB;
    public CatalogEntryRelationshipType type;
    private List<Comment> comments = new ArrayList<Comment>();
    
    private PortalUser author;


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

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }


    @OneToOne (cascade=CascadeType.ALL)
    @JoinColumn(name = "catalog_instance_roleA")
    public CatalogEntryRoleInstance getRoleA() {
        return roleA;
    }

    public void setRoleA(CatalogEntryRoleInstance roleA) {
        this.roleA = roleA;
    }

    @OneToOne  (cascade=CascadeType.ALL)
    @JoinColumn(name = "catalog_instance_roleB")
    public CatalogEntryRoleInstance getRoleB() {
        return roleB;
    }

    public void setRoleB(CatalogEntryRoleInstance roleB) {
        this.roleB = roleB;
    }

    @OneToOne
        @JoinColumn(name = "catalog_relationship_type_id")
    public CatalogEntryRelationshipType getType() {
        return type;
    }

    public void setType(CatalogEntryRelationshipType type) {
        this.type = type;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @ManyToOne
    @JoinColumn(name="author_id")
	public PortalUser getAuthor() {
		return author;
	}

	public void setAuthor(PortalUser author) {
		this.author = author;
	}
}
