/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DynamicMetadataHashProvider implements MetadataHashProvider {
	
	private long timeout;

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
		try{
			Metadata meta = PortalUtils.getMetadata(serviceUrl, getTimeout());
			hash = PortalUtils.createHashFromMetadata(meta);
		}catch(Exception ex){
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

}
