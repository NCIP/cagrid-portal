package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 9, 2006
 * Time: 1:45:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResearchCenterLocalTestCase extends BaseSpringDataAccessAbstractTestCase {
     GridServiceManager gridServiceManager;

    public void testRC(){
        ResearchCenter rc = new ResearchCenter();
        rc.setDisplayName("test-center");

       gridServiceManager.save(rc);

        PointOfContact poc = new PointOfContact();
        poc.setFirstName("test");
        poc.setFirstName("user");

        rc.getPocCollection().add(poc);

        gridServiceManager.save(rc);




    }


    /** For spring
     *
     * @param gridServiceManager
     */
    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}
