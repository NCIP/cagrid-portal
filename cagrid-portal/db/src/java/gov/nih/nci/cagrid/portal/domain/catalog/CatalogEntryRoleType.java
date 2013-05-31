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
@Table(name = "cat_role_type")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_role_type")
    })
public class CatalogEntryRoleType extends AbstractDomainObject implements MutableTemporal, Commentable {

    public String name;

    public String description;

    public Date createdAt;
    public Date updatedAt;

    public int minCardinality;

    public int maxCardinality;

    public List<CatalogEntryRoleInstance> instances = new ArrayList<CatalogEntryRoleInstance>();

    public CatalogEntryRelationshipType relationshipType;
    private List<Comment> comments = new ArrayList<Comment>();
    
    private String type;

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

    @ManyToOne( cascade = {CascadeType.ALL} )
    @JoinColumn(name="type_id")
    public CatalogEntryRelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(CatalogEntryRelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public int getMinCardinality() {
        return minCardinality;
    }

    public void setMinCardinality(int minCardinality) {
        this.minCardinality = minCardinality;
    }

    public int getMaxCardinality() {
        return maxCardinality;
    }

    public void setMaxCardinality(int maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    @OneToMany(mappedBy="type")
    public List<CatalogEntryRoleInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<CatalogEntryRoleInstance> instances) {
        this.instances = instances;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}