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
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cat_hyperlinks")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_hyperlinks")
    })
public class Hyperlink extends AbstractDomainObject implements MutableTemporal, Commentable {

    public String description;

    public URL url;

    public Date createdAt;

    public Date updatedAt;

    public CatalogEntry hyperlinkOf;
    private List<Comment> comments = new ArrayList<Comment>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
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
    public CatalogEntry getHyperlinkOf() {
        return hyperlinkOf;
    }

    public void setHyperlinkOf(CatalogEntry hyperlinkOf) {
        this.hyperlinkOf = hyperlinkOf;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}