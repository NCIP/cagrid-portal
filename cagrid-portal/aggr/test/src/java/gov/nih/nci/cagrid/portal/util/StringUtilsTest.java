/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StringUtilsTest {

    @Test
    public void testParsePostalCode() {
        assertEquals("Non-US zip code not parsed as expect.", StringUtils.parsePostalCode("xyz"), "xyz");
        assertEquals(StringUtils.parsePostalCode("20850."), "20850");
        assertEquals(StringUtils.parsePostalCode(null), null);
    }


}
