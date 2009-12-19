package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.portal.PortalTestUtils;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DynamicServiceUrlProviderSystemTest extends TestCase {
    private final Log logger = LogFactory.getLog(DynamicServiceUrlProviderSystemTest.class);


    public void testRun() {
        String idxUrl = "http://index.ctsa.cagrid.org:8080/wsrf/services/DefaultIndexService";


        Long beginTime = PortalTestUtils.getTimestamp();

        DynamicServiceUrlProvider provider = new DynamicServiceUrlProvider();
        try {
            Set<String> svcs = provider.getUrls(idxUrl);
            assertNotNull(svcs);
            for (String svc : svcs) {
                assertTrue(svc.length() > 0);
                logger.debug(svc);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        System.out.println("Total Time to query index: " + (PortalTestUtils.getTimestamp() - beginTime));
    }

}
