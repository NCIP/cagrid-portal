package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DynamicServiceStatusProviderSystemTest extends TestCase {
    String indexSvcUrl = "http://some.index.svc";
    final String svcUrl1 = "http://service1";
    final String svcUrl2 = "http://service2";
    final String svcUrl3 = "http://service3";
    final String validGridSvc = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/CaDSRService";


    public void testgetStatus() {
        IdxDependantDynamicServiceStatusProvider provider = new IdxDependantDynamicServiceStatusProvider();
        provider.setIndexServiceUrls(new String[]{
                indexSvcUrl
        }
        );


        ServiceUrlProvider dynProv = new ServiceUrlProvider() {
            public Set<String> getUrls(String indexServiceUrl) {
                Set<String> urls = new HashSet<String>();
                urls.add(svcUrl1);
                urls.add(svcUrl2);
                urls.add(svcUrl3);
                return urls;
            }
        };

        provider.setDynamicServiceUrlProvider(dynProv);
        assertEquals(ServiceStatus.INACTIVE, provider.getStatus(svcUrl1));
        assertEquals(ServiceStatus.ACTIVE, provider.getStatus(validGridSvc));

        provider.setStrictIndexVerification(true);
        assertEquals(ServiceStatus.INACTIVE, provider.getStatus(validGridSvc));

        ServiceUrlProvider dynProv2 = new ServiceUrlProvider() {
            public Set<String> getUrls(String indexServiceUrl) {
                Set<String> urls = new HashSet<String>();
                urls.add(validGridSvc);
                return urls;
            }
        };

        provider.setDynamicServiceUrlProvider(dynProv2);
        assertEquals(ServiceStatus.ACTIVE, provider.getStatus(validGridSvc));


    }
}
