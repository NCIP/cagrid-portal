package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cat_comments")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_comments")
    })

public class Comment extends AbstractDomainObject implements Temporal {

    public String commentText;

    public Date createdAt;

    public PortalUser author;

    public PersonCatalogEntry commentor;

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "author_id")
    public PortalUser getAuthor() {
        return author;
    }

    public void setAuthor(PortalUser author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "commentor_id")
    public PersonCatalogEntry getCommentor() {
        return commentor;
    }

    public void setCommentor(PersonCatalogEntry commentor) {
        this.commentor = commentor;
    }

    
}