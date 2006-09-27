package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 1:57:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResearchCenterDAOLocalTest extends BaseSpringDataAccessAbstractTestCase {

    private GridServiceBaseDAO gridServiceBaseDAO;


    public void testRCDAO() {
        ResearchCenter rc = new ResearchCenter();

        PointOfContact poc = new PointOfContact();
        poc.setFirstName("Test");

        rc.addPOC(poc);

        gridServiceBaseDAO.saveOrUpdate(rc);


    }

    public void setGridServiceBaseDAO(GridServiceBaseDAO gridServiceBaseDAO) {
        this.gridServiceBaseDAO = gridServiceBaseDAO;
    }
}
