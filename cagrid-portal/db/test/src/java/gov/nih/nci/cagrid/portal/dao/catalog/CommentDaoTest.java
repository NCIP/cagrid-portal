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