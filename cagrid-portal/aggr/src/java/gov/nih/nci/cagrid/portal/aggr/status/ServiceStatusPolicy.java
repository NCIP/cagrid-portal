/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.StatusChange;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface ServiceStatusPolicy {

	boolean shouldSetServiceDormant(List<StatusChange> statusHistory);
	
}
