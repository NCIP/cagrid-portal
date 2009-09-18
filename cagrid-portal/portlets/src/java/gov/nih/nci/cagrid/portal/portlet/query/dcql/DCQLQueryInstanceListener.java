package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface DCQLQueryInstanceListener {

    void onSheduled(DCQLQueryInstance instance);

    void onRunning(DCQLQueryInstance instance);

    void onComplete(DCQLQueryInstance instance, String results);

    void onCancelled(DCQLQueryInstance instance, boolean cancelled);

    void onError(DCQLQueryInstance instance, Exception error);

    void onTimeout(DCQLQueryInstance instance, boolean cancelled);
}
