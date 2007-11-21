/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface MetadataHashProvider {

	String getHash(String serviceUrl);
	
}
