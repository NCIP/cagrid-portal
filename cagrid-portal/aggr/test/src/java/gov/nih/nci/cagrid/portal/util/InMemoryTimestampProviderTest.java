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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class InMemoryTimestampProviderTest {

    @Test
    public void createTimestamp() {
        InMemoryTimestampProvider provider = new InMemoryTimestampProvider();
        provider.createTimestamp();
        assertNotNull(provider.getTimestamp());
        assertEquals("Getting inconsistent results from same provider instance", provider.getTimestamp(), provider.getTimestamp());

        InMemoryTimestampProvider provider2 = new InMemoryTimestampProvider();
        provider2.createTimestamp();
        assertNotSame("Getting inconsistent results from different provider instances", provider2.getTimestamp(), provider.getTimestamp());
    }


}
