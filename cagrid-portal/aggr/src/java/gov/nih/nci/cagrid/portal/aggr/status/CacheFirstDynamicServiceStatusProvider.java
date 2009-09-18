package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * This will first use the local cahce to return URLs
 * If not in cache it will delegate the call to another ServiceUrlProvider
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CacheFirstDynamicServiceStatusProvider implements ServiceUrlProvider {


    private Hashtable<String, Set> cache = new Hashtable<String, Set>();

    private String[] indexServiceUrls;
    private long timeout;
    private boolean requireMetadataCompliance;
    private ServiceUrlProvider dynamicServiceStatusProvider;

    protected static final Log logger = LogFactory.getLog(CacheFirstDynamicServiceStatusProvider.class);


    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceUrlProvider#getUrls(java.lang.String)
    */
    public Set<String> getUrls(String indexServiceUrl) {

        if (cache.containsKey(indexServiceUrl) && cache.get(indexServiceUrl).size() > 0) {
            logger.debug("Will use cache to return URL's");

            Set<String> _cachedUrls = cache.get(indexServiceUrl);
            Set _urls = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            synchronized (_cachedUrls) {
                _urls.addAll(_cachedUrls);
            }
            return _urls;
        } else {
            logger.warn("Cache not initialized for this Index Will use dynamic lookup.");
            return dynamicServiceStatusProvider.getUrls(indexServiceUrl);
        }
    }

    public void monitorIndex() {
        logger.debug("Will start retreivel of services from Index");
        for (String indexServiceUrl : cache.keySet()) {
            logger.debug("will update cache for Index: " + indexServiceUrl);

            Set<String> eprs = dynamicServiceStatusProvider.getUrls(indexServiceUrl);
            Set<String> _urls = cache.get(indexServiceUrl);
            synchronized (_urls) {
                if (eprs != null && eprs.size() > 0) {
                    _urls.clear();
                    _urls.addAll(eprs);
                    logger.info("Added " + _urls.size() + " services to cache");
                }
            }
        }
    }

    public String[] getIndexServiceUrls() {
        return indexServiceUrls;
    }

    public void setIndexServiceUrls(String[] indexServiceUrls) {
        for (String indexServiceUrl : indexServiceUrls) {
            if (!cache.containsKey(indexServiceUrl)) {
                cache.put(indexServiceUrl, Collections.synchronizedSet(new HashSet<String>()));
            }
        }
        logger.info("Initialized index cache with " + cache.size() + " indexes");
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isRequireMetadataCompliance() {
        return requireMetadataCompliance;
    }

    public void setRequireMetadataCompliance(boolean requireMetadataCompliance) {
        this.requireMetadataCompliance = requireMetadataCompliance;
    }

    public ServiceUrlProvider getDynamicServiceStatusProvider() {
        return dynamicServiceStatusProvider;
    }

    public void setDynamicServiceStatusProvider(ServiceUrlProvider dynamicServiceStatusProvider) {
        this.dynamicServiceStatusProvider = dynamicServiceStatusProvider;
    }
}
