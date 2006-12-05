/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.search;



import org.apache.axis.message.addressing.EndpointReferenceType;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface ServiceDiscoveryThread extends Runnable {
	
	void setIndexServiceURLs(String[] indexURLs);
	
	EndpointReferenceType[] getEPRs();
	
	void reset();
	
	Exception getException();
	
	boolean isFinished();

}
