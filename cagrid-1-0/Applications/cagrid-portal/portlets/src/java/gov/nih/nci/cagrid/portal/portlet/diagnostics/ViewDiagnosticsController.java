package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;

import javax.portlet.RenderRequest;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewDiagnosticsController extends AbstractViewObjectController {

    DiagnosticsBean diagnosticsBean;

    protected Object getObject(RenderRequest request) {
        return diagnosticsBean;
    }

    public DiagnosticsBean getDiagnosticsBean() {
        return diagnosticsBean;
    }

    public void setDiagnosticsBean(DiagnosticsBean diagnosticsBean) {
        this.diagnosticsBean = diagnosticsBean;
    }
}
