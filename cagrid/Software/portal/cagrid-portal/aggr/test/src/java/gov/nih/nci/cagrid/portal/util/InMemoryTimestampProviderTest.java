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
