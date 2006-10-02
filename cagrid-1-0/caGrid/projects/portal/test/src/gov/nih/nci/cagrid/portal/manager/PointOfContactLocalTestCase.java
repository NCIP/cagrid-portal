package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 30, 2006
 * Time: 8:51:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointOfContactLocalTestCase extends BaseSpringDataAccessAbstractTestCase {


    private PointOfContactManager pocManager;

    public void testKeywordSearch() {
        List result = pocManager.keywordSearch("Oster");
        assertNotNull(result);


    }

    public void setPocManager(PointOfContactManager pocManager) {
        this.pocManager = pocManager;
    }
}
