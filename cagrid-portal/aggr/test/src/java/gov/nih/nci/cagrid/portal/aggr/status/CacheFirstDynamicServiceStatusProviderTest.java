package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CacheFirstDynamicServiceStatusProviderTest {
    String _dummyIdx = "http://dummy.idx";
    Set<String> _dynamidIdxUrls = new HashSet<String>() {
        {
            add("http://gridservice1.org");
            add("http://gridservice2.ORG");
        }
    };

    @Test
    public void caseSentitive() {
        CacheFirstDynamicServiceStatusProvider provider = new CacheFirstDynamicServiceStatusProvider();
        provider.setIndexServiceUrls(new String[]{_dummyIdx});


        ServiceUrlProvider dynamicServiceStatusProvider = mock(ServiceUrlProvider.class);
        when(dynamicServiceStatusProvider.getUrls(_dummyIdx)).thenReturn(_dynamidIdxUrls);
        provider.setDynamicServiceStatusProvider(dynamicServiceStatusProvider);

        provider.monitorIndex();

        assertTrue(provider.getUrls(_dummyIdx).contains("http://gridservice1.org"));
        assertTrue("Should not be case sensitive", provider.getUrls(_dummyIdx).contains("http://gridservice2.org"));

    }
}
