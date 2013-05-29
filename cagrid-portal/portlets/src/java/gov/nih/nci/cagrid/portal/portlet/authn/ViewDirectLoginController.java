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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.service.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.*;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewDirectLoginController extends AbstractController {

    private String portalUserAttributeName;
    private String viewName;
    private String errorsAttributeName;
    private String commandName;
    private String authnErrorMessageAttributeName;
    private String redirectUrlPreference;


    /**
     *
     */
    public ViewDirectLoginController() {

    }

    @Override
    protected void handleActionRequestInternal(ActionRequest request, ActionResponse response) throws Exception {
        if (request.getParameterMap().containsKey(getRedirectUrlPreference()))
            response.setRenderParameter(getRedirectUrlPreference(), request.getParameter(getRedirectUrlPreference()));
    }

    protected ModelAndView handleRenderRequestInternal(RenderRequest request,
                                                       RenderResponse response) throws Exception {
        ModelAndView mav = null;
        BindException errors = null;
        if (getErrorsAttributeName() != null) {
            errors = (BindException) request.getAttribute(getErrorsAttributeName());
        }
        if (errors != null) {
            mav = new ModelAndView(getViewName(), errors.getModel());
        } else {
            mav = new ModelAndView(getViewName());
        }

        logger.debug("Looking for PortalUser under '" + getPortalUserAttributeName() + "'");

        PortalUser user = (PortalUser) request.getPortletSession()
                .getAttribute(getPortalUserAttributeName(),
                        PortletSession.APPLICATION_SCOPE);
        if (user != null) {
            mav.addObject("portalUser", user);
        } else {
            if (request.getParameterMap().containsKey(getRedirectUrlPreference())) {
                mav.addObject(getRedirectUrlPreference(), request.getParameter(getRedirectUrlPreference()));
            }
            mav.addObject("registerUrl", request.getPreferences().getValue("registerUrl", ""));
            mav.addObject(getCommandName(), new DirectLoginCommand());

        }
        String authnErrorMsg = (String) request.getAttribute(getAuthnErrorMessageAttributeName());
        if (authnErrorMsg != null) {
            mav.addObject("authnErrorMessage", authnErrorMsg);
        }

        return mav;
    }

    public String getRedirectUrlPreference() {
        return redirectUrlPreference;
    }

    @Required
    public void setRedirectUrlPreference(String redirectUrlPreference) {
        this.redirectUrlPreference = redirectUrlPreference;
    }

    @Required
    public String getPortalUserAttributeName() {
        return portalUserAttributeName;
    }

    public void setPortalUserAttributeName(String portalUserAttributeName) {
        this.portalUserAttributeName = portalUserAttributeName;
    }

    @Required
    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Required
    public String getErrorsAttributeName() {
        return errorsAttributeName;
    }

    public void setErrorsAttributeName(String errorsAttributeName) {
        this.errorsAttributeName = errorsAttributeName;
    }

    @Required
    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    @Required
    public String getAuthnErrorMessageAttributeName() {
        return authnErrorMessageAttributeName;
    }

    public void setAuthnErrorMessageAttributeName(
            String authnErrorMessageAttributeName) {
        this.authnErrorMessageAttributeName = authnErrorMessageAttributeName;
    }


}
