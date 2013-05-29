/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
    private String wikiURL;

    protected Object getObject(RenderRequest request) {
        DiagnosticsBean _bean = new DiagnosticsBean();
        _bean.setWikiURL(wikiURL);

        if (getInterPortletMessageReceiver().handles(request)) {
            String url = (String) getInterPortletMessageReceiver().receive(request);
            _bean.setUrl(url);
            if (url != null)
                _bean.setIpcRequest(true);
        }
        return _bean;
    }


    public InterPortletMessageReceiver getInterPortletMessageReceiver() {
        return interPortletMessageReceiver;
    }

    public void setInterPortletMessageReceiver(InterPortletMessageReceiver interPortletMessageReceiver) {
        this.interPortletMessageReceiver = interPortletMessageReceiver;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public void setWikiURL(String wikiURL) {
        this.wikiURL = wikiURL;
    }
}
