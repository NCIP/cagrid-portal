/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr;

import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface ServiceUrlProvider {
	
	/**
	 * The returned Set may be empty but should never be null.
	 * 
	 * @param indexServiceUrl 
	 * @return set of service URL String objects
	 */
	Set<String> getUrls(String indexServiceUrl);
}
