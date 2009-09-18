/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CQLQueryInstanceListener {
	void onSheduled(CQLQueryInstance instance);
	void onRunning(CQLQueryInstance instance);
	void onComplete(CQLQueryInstance instance, String results);
	void onCancelled(CQLQueryInstance instance, boolean cancelled);
	void onError(CQLQueryInstance instance, Exception error);
	void onTimeout(CQLQueryInstance instance, boolean cancelled);
}
