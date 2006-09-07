package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 1:57:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResearchCenterDAOLocalTest extends BaseSpringDataAccessAbstractTestCase {

    private ResearchCenterDAO rcDAO;

    public void testGeoCoder() {
        GeoCodeValues values = rcDAO.getGeoCodes("20852");
        assertNotNull(values);

    }

    public void setRcDAO(ResearchCenterDAO rcDAO) {
        this.rcDAO = rcDAO;


    }
}
