package gov.nih.nci.cagrid.portal.portlet.sample;

import junit.framework.TestCase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EVSSearchHelperTest extends TestCase {

    public void testSearch() throws Exception{
        EVSSearchHelper _helper = new EVSSearchHelper();
        System.out.println(_helper.conceptCodeSearch("Gene"));
        assertNotNull(_helper.conceptCodeSearch("Gene"));

    }
}
