package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 30, 2006
 * Time: 8:21:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResearchCenterLocalTestCase extends BaseSpringDataAccessAbstractTestCase {

    private ResearchCenterManager rcManager;

    public void testSearch() {
        List searchResult = rcManager.keywordSearch("osu");
        assertNotNull(searchResult);
        assertTrue(searchResult.size() > 0);

        //make sure searches are case insensitive
        List searchResultUpper = rcManager.keywordSearch("OSU");
        assertNotNull(searchResult);
        assertTrue(searchResult.size() > 0);


        assertTrue(searchResultUpper.size() == searchResult.size());
    }

    public void setRcManager(ResearchCenterManager rcManager) {
        this.rcManager = rcManager;
    }
}
