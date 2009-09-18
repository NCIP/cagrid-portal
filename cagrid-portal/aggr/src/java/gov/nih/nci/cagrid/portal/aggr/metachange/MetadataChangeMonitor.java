/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.aggr.AbstractMonitor;
import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Transactional
public class MetadataChangeMonitor extends AbstractMonitor {

    private static final Log logger = LogFactory
            .getLog(MetadataChangeMonitor.class);

    private ServiceUrlProvider cachedServiceUrlProvider;

    private MetadataHashProvider cachedMetadataHashProvider;

    private MetadataHashProvider dynamicMetadataHashProvider;

    /**
     *
     */
    public MetadataChangeMonitor() {

    }

    public void checkForMetadataChanges() {
        for (String indexServiceUrl : getIndexServiceUrls()) {

            Set<String> serviceUrls = getCachedServiceUrlProvider().getUrls(
                    indexServiceUrl);
            logger.debug("Got " + serviceUrls.size()
                    + " cached service URLs for index service "
                    + indexServiceUrl);
            for (String serviceUrl : serviceUrls) {

                String cachedHash = null;
                try {
                    cachedHash = getCachedMetadataHashProvider()
                            .getHash(serviceUrl);
                } catch (Exception ex) {
                    // This might happen if the cached service has just been deleted
                    logger
                            .warn("Could not retrieve cached metadata hash for service "
                                    + serviceUrl + ": " + ex.getMessage());
                    continue;
                }


                String dynamicHash = null;
                try {
                    dynamicHash = getDynamicMetadataHashProvider()
                            .getHash(serviceUrl);
                } catch (Exception ex) {
                    // This might happen if the dynamic service is down
                    logger.warn("Could not retreive dynamic metadata hash for service " + serviceUrl + ": " + ex.getMessage());
                    continue;
                }

                if (cachedHash == null) {
                    String msg = "cached hash must not be null";
                    logger.error(msg);
                    throw new IllegalArgumentException(msg);
                }
                if (dynamicHash == null) {
                    String msg = "dynamic hash must not be null";
                    logger.error(msg);
                    throw new IllegalArgumentException(msg);
                }

                if (!cachedHash.equals(dynamicHash)) {

                    logger.debug("Publishing ServiceMetadataChangeEvent for "
                            + serviceUrl);
                    MetadataChangeEvent event = new MetadataChangeEvent(
                            this);
                    event.setServiceUrl(serviceUrl);
                    getApplicationContext().publishEvent(event);

                } else {
                    logger.debug(serviceUrl + " is up to date");
                }

            }
        }
    }

    public MetadataHashProvider getCachedMetadataHashProvider() {
        return cachedMetadataHashProvider;
    }

    public void setCachedMetadataHashProvider(
            MetadataHashProvider cachedServiceMetadataHashProvider) {
        this.cachedMetadataHashProvider = cachedServiceMetadataHashProvider;
    }

    public ServiceUrlProvider getCachedServiceUrlProvider() {
        return cachedServiceUrlProvider;
    }

    public void setCachedServiceUrlProvider(
            ServiceUrlProvider cachedServiceUrlProvider) {
        this.cachedServiceUrlProvider = cachedServiceUrlProvider;
    }

    public MetadataHashProvider getDynamicMetadataHashProvider() {
        return dynamicMetadataHashProvider;
    }

    public void setDynamicMetadataHashProvider(
            MetadataHashProvider dynamicServiceMetadataHashProvider) {
        this.dynamicMetadataHashProvider = dynamicServiceMetadataHashProvider;
    }

}
