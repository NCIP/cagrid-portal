/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.status;

import gov.nih.nci.cagrid.portal2.domain.StatusChange;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface ServiceStatusPolicy {

	boolean shouldBanService(List<StatusChange> statusHistory);
	
}
