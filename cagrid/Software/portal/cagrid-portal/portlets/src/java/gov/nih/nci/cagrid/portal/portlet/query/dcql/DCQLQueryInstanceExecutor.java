package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.QueryInstanceExecutor;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface DCQLQueryInstanceExecutor extends QueryInstanceExecutor<DCQLQueryInstance> {

    void setDcqlQueryInstanceListener(DCQLQueryInstanceListener listener);

    DCQLQueryInstanceListener getDcqlQueryInstanceListener();


}
