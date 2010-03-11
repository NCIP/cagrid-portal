/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.portal.aggr.AbstractMonitor;
import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.aggr.TrackableMonitor;
import gov.nih.nci.cagrid.portal.util.TimestampProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
@Transactional
public class RegisteredServiceMonitor extends AbstractMonitor implements TrackableMonitor {

    private static final Log logger = LogFactory
            .getLog(RegisteredServiceMonitor.class);

    /*
      * Pulls service URLs from an IndexService
      */
    private ServiceUrlProvider dynamicServiceUrlProvider;

    /*
      * Pulls service URLs from a cache (e.g. a database)
      */
    private ServiceUrlProvider cachedServiceUrlProvider;

    private TimestampProvider timestampProvider;

    public void checkForNewServices() {

        logger.debug("Checking for new services...");

        Collection<RegisteredServiceEvent> events = new ArrayList<RegisteredServiceEvent>();
        for (String indexSvcUrl : getIndexServiceUrls()) {
            logger.debug("On Index Service: " + indexSvcUrl);
            Set<String> cachedSvcUrls = null;
            try {
                cachedSvcUrls = getCachedServiceUrlProvider().getUrls(
                        indexSvcUrl);
            } catch (Exception ex) {
                logger.warn("Error retrieving cached service urls: "
                        + ex.getMessage(), ex);
                continue;
            }

            Set<String> dynamicSvcUrls = null;
            try {
                dynamicSvcUrls = getDynamicServiceUrlProvider().getUrls(
                        indexSvcUrl);
            } catch (Exception ex) {
                logger.warn("Error retrieving dynamic service urls: "
                        + ex.getMessage(), ex);
            }

            if (cachedSvcUrls == null) {
                throw new IllegalArgumentException(
                        "cached service urls must not be null");
            }

            if (dynamicSvcUrls == null) {
                throw new IllegalArgumentException(
                        "dynamic service urls must not be null");
            }

            logger.debug("Found " + cachedSvcUrls.size()
                    + " cached services and " + dynamicSvcUrls.size()
                    + " dynamic services.");

            for (String svcUrl : dynamicSvcUrls) {
                if (!cachedSvcUrls.contains(svcUrl)) {
                    RegisteredServiceEvent event = new RegisteredServiceEvent(
                            this);
                    event.setIndexServiceUrl(indexSvcUrl);
                    event.setServiceUrl(svcUrl);
                    events.add(event);
                }
            }
        }

        logger.debug("Events to publish: " + events.size());
        for (RegisteredServiceEvent event : events) {
            getApplicationContext().publishEvent(event);
        }
        // timestamp
        timestampProvider.createTimestamp();
    }

    public Date getLastExecutedOn() throws RuntimeException {
        return timestampProvider.getTimestamp();
    }

    public ServiceUrlProvider getCachedServiceUrlProvider() {
        return cachedServiceUrlProvider;
    }

    public void setCachedServiceUrlProvider(
            ServiceUrlProvider cachedServiceUrlProvider) {
        this.cachedServiceUrlProvider = cachedServiceUrlProvider;
    }

    public ServiceUrlProvider getDynamicServiceUrlProvider() {
        return dynamicServiceUrlProvider;
    }

    public void setDynamicServiceUrlProvider(
            ServiceUrlProvider dynamicServiceUrlProvider) {
        this.dynamicServiceUrlProvider = dynamicServiceUrlProvider;
    }

    public TimestampProvider getTimestampProvider() {
        return timestampProvider;
    }

    public void setTimestampProvider(TimestampProvider timestampProvider) {
        this.timestampProvider = timestampProvider;
    }
}
