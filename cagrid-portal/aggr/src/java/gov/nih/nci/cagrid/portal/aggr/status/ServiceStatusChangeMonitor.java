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
/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.AbstractMonitor;
import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Transactional
public class ServiceStatusChangeMonitor extends AbstractMonitor {

    private static final Log logger = LogFactory
            .getLog(ServiceStatusChangeMonitor.class);

    private ServiceStatusProvider dynamicServiceStatusProvider;

    private ServiceStatusProvider cachedServiceStatusProvider;

    private ServiceUrlProvider cachedServiceUrlProvider;

    /**
     *
     */
    public ServiceStatusChangeMonitor() {

    }

    public void checkForServiceStatusChanges() {

        for (String indexServiceUrl : getIndexServiceUrls()) {

            Set<String> serviceUrls = getCachedServiceUrlProvider().getUrls(
                    indexServiceUrl);
            logger.debug("Got " + serviceUrls.size()
                    + " cached service URLs for index service "
                    + indexServiceUrl);
            for (String serviceUrl : serviceUrls) {

                ServiceStatus dynamicStatus = null;
                try {
                    dynamicStatus = getDynamicServiceStatusProvider()
                            .getStatus(serviceUrl);
                } catch (Exception ex) {
                    logger.warn("Could not get dynamic service status for: " + serviceUrl
                            + "  " + ex.getMessage(), ex);
                    continue;
                }
                ServiceStatus cachedStatus = null;
                try {
                    cachedStatus = getCachedServiceStatusProvider().getStatus(
                            serviceUrl);
                } catch (Exception ex) {
                    logger.warn("Could not get cached service status: "
                            + ex.getMessage(), ex);
                    continue;
                }

                if (dynamicStatus == null) {
                    String msg = "dynamic status must not be null";
                    logger.error(msg);
                    throw new IllegalArgumentException(msg);
                }
                if (cachedStatus == null) {
                    String msg = "cached status must not be null";
                    logger.error(msg);
                    throw new IllegalArgumentException(msg);
                }

                if (ServiceStatus.BANNED.equals(cachedStatus)) {
                    logger.debug(serviceUrl
                            + " is banned. No event will be published.");
                } else if (!dynamicStatus.equals(cachedStatus)) {

                    if (ServiceStatus.DORMANT.equals(cachedStatus)
                            && ServiceStatus.INACTIVE.equals(dynamicStatus)) {
                        // do nothing
                        logger
                                .debug(serviceUrl
                                        + " is dormant and service is inactive. No event will be published.");
                    } else {

                        ServiceStatusChangeEvent event = new ServiceStatusChangeEvent(
                                this);
                        event.setOldStatus(cachedStatus);
                        event.setNewStatus(dynamicStatus);
                        event.setServiceUrl(serviceUrl);
                        getApplicationContext().publishEvent(event);
                    }
                }

            }
        }

    }

    public ServiceStatusProvider getCachedServiceStatusProvider() {
        return cachedServiceStatusProvider;
    }

    public void setCachedServiceStatusProvider(
            ServiceStatusProvider cachedServiceStatusProvider) {
        this.cachedServiceStatusProvider = cachedServiceStatusProvider;
    }

    public ServiceUrlProvider getCachedServiceUrlProvider() {
        return cachedServiceUrlProvider;
    }

    public void setCachedServiceUrlProvider(
            ServiceUrlProvider cachedServiceUrlProvider) {
        this.cachedServiceUrlProvider = cachedServiceUrlProvider;
    }

    public ServiceStatusProvider getDynamicServiceStatusProvider() {
        return dynamicServiceStatusProvider;
    }

    public void setDynamicServiceStatusProvider(
            ServiceStatusProvider dynamicServiceStatusProvider) {
        this.dynamicServiceStatusProvider = dynamicServiceStatusProvider;
    }

}
