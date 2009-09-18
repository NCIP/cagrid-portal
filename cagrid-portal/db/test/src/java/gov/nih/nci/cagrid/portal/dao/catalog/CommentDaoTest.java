package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.Comment;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommentDaoTest extends DaoTestBase<CommentDao> {

    @Test
    public void save(){
        Comment comment = new Comment();

        getDao().save(comment);

    }
}