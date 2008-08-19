package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DelegatingDynamicServiceStatusProvider implements ServiceStatusProvider {

    List<ServiceStatusProvider> providers = new ArrayList<ServiceStatusProvider>();
    private final Log logger = LogFactory.getLog(DelegatingDynamicServiceStatusProvider.class);


    public ServiceStatus getStatus(String serviceUrl) {
        //set it to inactive first
        ServiceStatus status = ServiceStatus.INACTIVE;

        for (ServiceStatusProvider provider : providers) {
            status = provider.getStatus(serviceUrl);
            logger.debug(provider.getClass().getName() + " returned " + status + " status for servie "
                    + serviceUrl);

            //only continue down the chain if service is active
            if (!status.equals(ServiceStatus.ACTIVE))
                break;
        }

        logger.debug("Returning " + status + " for service " + serviceUrl);

        return status;
    }

    public List<ServiceStatusProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<ServiceStatusProvider> providers) {
        this.providers = providers;
    }
}
