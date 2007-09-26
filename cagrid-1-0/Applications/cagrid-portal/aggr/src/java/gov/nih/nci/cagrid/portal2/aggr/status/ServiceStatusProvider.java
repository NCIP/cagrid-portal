/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.status;

import gov.nih.nci.cagrid.portal2.domain.ServiceStatus;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface ServiceStatusProvider {
	ServiceStatus getStatus(String serviceUrl);
}
