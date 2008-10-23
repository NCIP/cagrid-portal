package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageReceiver;

import javax.portlet.RenderRequest;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewDiagnosticsController extends AbstractViewObjectController {

    private InterPortletMessageReceiver interPortletMessageReceiver;

    protected Object getObject(RenderRequest request) {
        DiagnosticsBean _bean = new DiagnosticsBean();

        if (getInterPortletMessageReceiver().handles(request)) {
            String url = (String) getInterPortletMessageReceiver().receive(request);
            _bean.setUrl(url);
        }
        return _bean;
    }


    public InterPortletMessageReceiver getInterPortletMessageReceiver() {
        return interPortletMessageReceiver;
    }

    public void setInterPortletMessageReceiver(InterPortletMessageReceiver interPortletMessageReceiver) {
        this.interPortletMessageReceiver = interPortletMessageReceiver;
    }
}
