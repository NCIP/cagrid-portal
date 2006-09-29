package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 1:57:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResearchCenterDAOLocalTest extends BaseSpringDataAccessAbstractTestCase {

    private ResearchCenterDAO rcDAO;
    private JdbcDAO jdbcDAO;


    public void testRCDAO() {
        ResearchCenter rc = new ResearchCenter();

        PointOfContact poc = new PointOfContact();
        poc.setFirstName("Test");

        rc.addPOC(poc);

        try {
            rcDAO.saveOrUpdate(rc);
            fail("Should fail");
        } catch (DataAccessException e) {
            assert(e instanceof DataAccessException);
        }


    }

    public void testRetreival() {
        List uniqueCenters = rcDAO.getUniqueCenters();
        List allCenters = rcDAO.loadAll(ResearchCenter.class);

        assert(uniqueCenters.size() > 1);
        assert(allCenters.size() > 1);
        //all centers have to be more than unique centers
        assertTrue(allCenters.size() > uniqueCenters.size());
    }

    public void testGeoCode() {
        assertNotNull(jdbcDAO.getGeoCodes("20852"));

    }

    public void setJdbcDAO(JdbcDAO jdbcDAO) {
        this.jdbcDAO = jdbcDAO;
    }

    public void setRcDAO(ResearchCenterDAO rcDAO) {
        this.rcDAO = rcDAO;
    }
}
