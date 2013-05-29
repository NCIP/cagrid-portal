/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.util;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EmailValidatorTest {

    @Test
    public void validate() {
        assertFalse(EmailValidator.validateEmail("junk"));
        assertTrue(EmailValidator.validateEmail("manav.kher@semanticbits.com"));
        assertTrue(EmailValidator.validateEmail("man@some.org"));
        assertFalse(EmailValidator.validateEmail("man@org"));
    }
}
