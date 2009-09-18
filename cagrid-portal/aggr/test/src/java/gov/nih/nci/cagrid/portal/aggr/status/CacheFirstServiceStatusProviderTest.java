package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.regsvc.DynamicServiceUrlProvider;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CacheFirstServiceStatusProviderTest extends TestCase {
    String _dummyIdx = "http://dummy.idx";
    Set<String> _dynamidIdxUrls = new HashSet<String>() {
        {
            add("http://gridservice1.org");
        }
    };


    public void testCache() {

        DynamicServiceUrlProvider mockDynamicUrlProvider = mock(DynamicServiceUrlProvider.class);
        stub(mockDynamicUrlProvider.getUrls(_dummyIdx)).toReturn(_dynamidIdxUrls);

        CacheFirstDynamicServiceStatusProvider _provider = new CacheFirstDynamicServiceStatusProvider();

        _provider.setDynamicServiceStatusProvider(mockDynamicUrlProvider);
        _provider.getUrls(_dummyIdx);

        // verify that cache is not being used
        verify(mockDynamicUrlProvider, times(1)).getUrls(_dummyIdx);

        _provider.setIndexServiceUrls(new String[]{_dummyIdx});
        assertNotNull(_provider.getUrls(_dummyIdx));

    }

    public void testWorking() throws Exception {
        CacheFirstDynamicServiceStatusProvider cachedProvider = new CacheFirstDynamicServiceStatusProvider();

        DynamicServiceUrlProvider mockDynamicUrlProvider = mock(DynamicServiceUrlProvider.class);
        stub(mockDynamicUrlProvider.getUrls(_dummyIdx)).toReturn(_dynamidIdxUrls);
        cachedProvider.setDynamicServiceStatusProvider(mockDynamicUrlProvider);

        cachedProvider.setIndexServiceUrls(new String[]{_dummyIdx});
        cachedProvider.monitorIndex();
        assertEquals("Cache not in sync with index", cachedProvider.getUrls(_dummyIdx), _dynamidIdxUrls);
        verify(mockDynamicUrlProvider, times(1)).getUrls(_dummyIdx);

        _dynamidIdxUrls.add("http://gridservice2.org");
        stub(mockDynamicUrlProvider.getUrls(_dummyIdx)).toReturn(_dynamidIdxUrls);
        cachedProvider.monitorIndex();
        assertEquals("Cache not in sync with index", cachedProvider.getUrls(_dummyIdx), _dynamidIdxUrls);
        verify(mockDynamicUrlProvider, times(2)).getUrls(_dummyIdx);

        _dynamidIdxUrls.remove("http://gridservice2.org");

        stub(mockDynamicUrlProvider.getUrls(_dummyIdx)).toReturn(_dynamidIdxUrls);
        cachedProvider.monitorIndex();
        assertEquals("Cache not in sync with index", cachedProvider.getUrls(_dummyIdx), _dynamidIdxUrls);
        verify(mockDynamicUrlProvider, times(3)).getUrls(_dummyIdx);

        //verify cache is being used
        verifyNoMoreInteractions(mockDynamicUrlProvider);

    }
}
