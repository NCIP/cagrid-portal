package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.PointOfContact;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 29, 2006
 * Time: 1:06:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointOfContactDAOLocalTest extends BaseSpringDataAccessAbstractTestCase {

    private PointOfContactDAO pocDAO;

    public void testPOCDAO() {
        PointOfContact poc = new PointOfContact();
        poc.setFirstName("Test");
        try {
            pocDAO.saveOrUpdate(poc);
        } catch (DataAccessException e) {
            fail("Should fail");
            assert(e instanceof DataAccessException);
        }
    }

    public void testRetreival() {
        List uniquePeople = pocDAO.getUniquePeople();
        List allPeople = pocDAO.loadAll(PointOfContact.class);

        assert(uniquePeople.size() > 1);
        assert(allPeople.size() > 1);
        //all centers have to be more than unique centers
        assertTrue(allPeople.size() > uniquePeople.size());
    }


    public void setPocDAO(PointOfContactDAO pocDAO) {
        this.pocDAO = pocDAO;
    }

}
