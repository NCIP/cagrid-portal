package gov.nih.nci.cagrid.portal.util;

import junit.framework.TestCase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StringUtilsTest extends TestCase {

    public void testParsePostalCode(){
        assertEquals("Non-US zip code not parsed as expect.",StringUtils.parsePostalCode("xyz"),"xyz");
        assertEquals(StringUtils.parsePostalCode("20850."),"20850");
        assertEquals(StringUtils.parsePostalCode(null),null);
    }
}
