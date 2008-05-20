/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.QueryInstanceExecutor;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public interface CQLQueryInstanceExecutor extends QueryInstanceExecutor<CQLQueryInstance> {

    void setCqlQueryInstanceListener(CQLQueryInstanceListener listener);

    CQLQueryInstanceListener getCqlQueryInstanceListener();

}
