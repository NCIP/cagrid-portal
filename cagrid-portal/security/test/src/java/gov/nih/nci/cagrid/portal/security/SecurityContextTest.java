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
package gov.nih.nci.cagrid.portal.security;

import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Test the loading of the spring context file(s)
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SecurityContextTest extends AbstractDependencyInjectionSpringContextTests {


    @Test
    public void testLoad() {
        assertNotNull(getApplicationContext());
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-security.xml",
        };
    }
}