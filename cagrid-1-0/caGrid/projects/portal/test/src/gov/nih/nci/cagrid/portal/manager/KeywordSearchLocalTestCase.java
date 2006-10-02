package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 2, 2006
 * Time: 12:34:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeywordSearchLocalTestCase extends BaseSpringDataAccessAbstractTestCase {

    private ResearchCenterManager rcManager;
    private PointOfContactManager pocManager;
    private GridServiceManager gridServiceManager;

    public void testServiceSearch() {
        try {
            List result = gridServiceManager.keywordSearch("cadsr");
            assertNotNull(result);
        } catch (PortalRuntimeException e) {
            fail(e.getMessage());
        }
    }

    public void testKeywordSearch() {
        try {
            List result = pocManager.keywordSearch("Oster");
            assertNotNull(result);
        } catch (PortalRuntimeException e) {
            fail(e.getMessage());
        }
    }

    public void testSearch() {
        try {
            List searchResult = rcManager.keywordSearch("osu");
            assertNotNull(searchResult);
            assertTrue(searchResult.size() > 0);

//make sure searches are case insensitive
            List searchResultUpper = rcManager.keywordSearch("OSU");
            assertNotNull(searchResult);
            assertTrue(searchResult.size() > 0);


            assertTrue(searchResultUpper.size() == searchResult.size());
        } catch (PortalRuntimeException e) {
            fail(e.getMessage());
        }
    }

    public void setRcManager(ResearchCenterManager rcManager) {
        this.rcManager = rcManager;
    }

    public void setPocManager(PointOfContactManager pocManager) {
        this.pocManager = pocManager;
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }

}
