package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.Comment;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;

import java.util.Date;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommentsManagerFacade extends CatalogEntryManagerFacade {

    public List<Comment> getAll() {
        return loadCurrentCE().getComments();
    }

    public String addComment(String comment) {
        Comment cmt = new Comment();
        PortalUser portalUser = getUserModel().getPortalUser();

        if (portalUser == null)
            throw new CaGridPortletApplicationException("Null user trying to add comment");
        cmt.setAuthor(portalUser);
        cmt.setCreatedAt(new Date());
        cmt.setCommentText(comment);
        CatalogEntry ce = getUserModel().getCurrentCatalogEntry();
        ce.getComments().add(cmt);
        getCatalogEntryDao().save(ce);

        return "Added";

    }
}
