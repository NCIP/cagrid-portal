package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Comment;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommentDao extends AbstractDao<Comment> {

    public CommentDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return Comment.class;
    }
}