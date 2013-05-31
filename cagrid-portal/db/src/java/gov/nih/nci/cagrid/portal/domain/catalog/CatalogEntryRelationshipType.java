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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cat_relationship_type", 
		uniqueConstraints = 
			@UniqueConstraint(columnNames = 
				{"name"}))
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_relationship_type")
    })
public class CatalogEntryRelationshipType extends AbstractDomainObject implements Temporal,Commentable {

    public Date createdAt;

    public String name;

    public String description;

    public boolean temporal;

    public CatalogEntryRoleType roleTypeB;
    public CatalogEntryRoleType roleTypeA;
    private List<Comment> comments = new ArrayList<Comment>();
    
    private CatalogEntryRelationshipType parent;
    private List<CatalogEntryRelationshipType> children = new ArrayList<CatalogEntryRelationshipType>();

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTemporal() {
        return temporal;
    }

    public void setTemporal(boolean temporal) {
        this.temporal = temporal;
    }

    @OneToOne
     @JoinColumn(name = "catalog_role_typeB")
    public CatalogEntryRoleType getRoleTypeB() {
        return roleTypeB;
    }

    public void setRoleTypeB(CatalogEntryRoleType roleTypeB) {
        this.roleTypeB = roleTypeB;
    }

    @OneToOne
     @JoinColumn(name = "catalog_role_typeA")
    public CatalogEntryRoleType getRoleTypeA() {
        return roleTypeA;
    }

    public void setRoleTypeA(CatalogEntryRoleType roleTypeA) {
        this.roleTypeA = roleTypeA;
    }


    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @ManyToOne
    @JoinColumn(name="parent_id")
	public CatalogEntryRelationshipType getParent() {
		return parent;
	}

	public void setParent(CatalogEntryRelationshipType parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy="parent")
	public List<CatalogEntryRelationshipType> getChildren() {
		return children;
	}

	public void setChildren(List<CatalogEntryRelationshipType> children) {
		this.children = children;
	}
}