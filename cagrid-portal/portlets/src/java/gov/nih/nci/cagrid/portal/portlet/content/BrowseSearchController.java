/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.content;

import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageSender;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BrowseSearchController extends org.springframework.web.portlet.mvc.AbstractCommandController {
    private String redirectUrlPreferenceName;
    private InterPortletMessageSender interPortletMessageSender;


    protected void handleAction(ActionRequest request, ActionResponse response, Object o, BindException e) throws Exception {
        BrowseSearchCommand cmd = (BrowseSearchCommand) o;

        String redirectUrl = request.getPreferences().getValue(
                getRedirectUrlPreferenceName(), null);
        if (redirectUrl == null) {
            throw new Exception("No redirect URL preference provided.");
        }
        getInterPortletMessageSender().send(request, cmd.getSearchKeyword());
        response.sendRedirect(redirectUrl);
    }

    protected org.springframework.web.portlet.ModelAndView handleRender(RenderRequest renderRequest, RenderResponse renderResponse, Object o, BindException e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public InterPortletMessageSender getInterPortletMessageSender() {
        return interPortletMessageSender;
    }

    public void setInterPortletMessageSender(InterPortletMessageSender interPortletMessageSender) {
        this.interPortletMessageSender = interPortletMessageSender;
    }

    public String getRedirectUrlPreferenceName() {
        return redirectUrlPreferenceName;
    }

    public void setRedirectUrlPreferenceName(String redirectUrlPreferenceName) {
        this.redirectUrlPreferenceName = redirectUrlPreferenceName;
    }
}
