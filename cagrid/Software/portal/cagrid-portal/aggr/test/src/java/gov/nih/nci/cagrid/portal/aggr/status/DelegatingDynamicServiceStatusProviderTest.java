package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DelegatingDynamicServiceStatusProviderTest extends AbstractServiceSatusTest {


    public void testDelegation() {

        final ServiceStatusProvider _provider2 = new ServiceStatusProvider() {
            public ServiceStatus getStatus(String serviceUrl) {
                if (serviceUrl.equals(validSvcNotInIndex) || serviceUrl.equals(svcInIndex1))
                    return ServiceStatus.ACTIVE;

                return ServiceStatus.INACTIVE;
            }
        };

        DelegatingDynamicServiceStatusProvider provider = new DelegatingDynamicServiceStatusProvider();

        List providers = new ArrayList<ServiceStatusProvider>() {
            {
                add(idxProvider);
                add(_provider2);
            }
        };

        provider.setProviders(providers);

        // svcURl1 is in the index
        assertEquals("Service is in the index, but dynamic status is Inactive",
                ServiceStatus.INACTIVE, provider.getStatus(svcInIndex2));

        assertEquals("Service is not in the index. Should be inactive",
                ServiceStatus.INACTIVE, provider.getStatus(validSvcNotInIndex));

        assertEquals("Service is in the index. Should be active.",
                ServiceStatus.ACTIVE, provider.getStatus(svcInIndex1));


    }
}
