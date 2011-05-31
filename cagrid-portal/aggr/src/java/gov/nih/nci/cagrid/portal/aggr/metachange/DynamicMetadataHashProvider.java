/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.MetadataUtils;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class DynamicMetadataHashProvider implements MetadataHashProvider {

    private long timeout;
    private MetadataUtils metadataUtils;

    /**
     *
     */
    public DynamicMetadataHashProvider() {

    }

    /* (non-Javadoc)
      * @see gov.nih.nci.cagrid.portal.aggr.metachange.ServiceMetadataHashProvider#getHash(java.lang.String)
      */
    public String getHash(String serviceUrl) {
        String hash = null;
        try {
            Metadata meta = metadataUtils.getMetadata(serviceUrl, getTimeout());
            hash = PortalUtils.createHashFromMetadata(meta);
        } catch (Exception ex) {
            throw new RuntimeException("Error getting dynamic metadata hash: " + ex.getMessage(), ex);
        }
        return hash;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public MetadataUtils getMetadataUtils() {
        return metadataUtils;
    }

    @Required
    public void setMetadataUtils(MetadataUtils metadataUtils) {
        this.metadataUtils = metadataUtils;
    }
}
