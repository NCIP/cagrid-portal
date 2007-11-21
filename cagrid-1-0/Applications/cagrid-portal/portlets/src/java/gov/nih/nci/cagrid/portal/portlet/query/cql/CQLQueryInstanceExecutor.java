/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CQLQueryInstanceExecutor {

	void setCqlQueryInstance(CQLQueryInstance instance);
	CQLQueryInstance getCqlQueryInstance();
	
	void setCqlQueryInstanceListener(CQLQueryInstanceListener listener);
	CQLQueryInstanceListener getCqlQueryInstanceListener();
	
	void start();
	boolean cancel();
	boolean timeout();
	
}
