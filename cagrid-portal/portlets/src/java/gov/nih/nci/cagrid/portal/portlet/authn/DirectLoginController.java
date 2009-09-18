/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal.domain.AuthnTicket;
import gov.nih.nci.cagrid.portal.security.AuthnService;
import gov.nih.nci.cagrid.portal.security.EncryptionService;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractCommandController;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class DirectLoginController extends AbstractCommandController {

    private AuthnService authnService;
    private EncryptionService encryptionService;
    private String redirectUrlPreferenceName;
    private String ticketParameterName;
    private String errorsAttributeName;
    private String ifsUrl;
    private String errorOperationName;
    private String viewOperationName;
    private String portalUserAttributeName;

    /**
     *
     */
    public DirectLoginController() {

    }

    /**
     * @param commandClass
     */
    public DirectLoginController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public DirectLoginController(Class commandClass, String commandName) {
        super(commandClass, commandName);

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest,
      *      javax.portlet.ActionResponse, java.lang.Object,
      *      org.springframework.validation.BindException)
      */
    @Override
    protected void handleAction(ActionRequest request, ActionResponse response,
                                Object obj, BindException errors) throws Exception {

        if (errors.hasErrors()) {
            request.setAttribute(getErrorsAttributeName(), errors);
            response.setRenderParameter("operation", getErrorOperationName());
            return;
        }

        DirectLoginCommand command = (DirectLoginCommand) obj;
        AuthnTicket ticket = null;
        try {
            ticket = getAuthnService().authenticate(command.getUsername(),
                    command.getPassword(), command.getIdpUrl(), getIfsUrl());
        } catch (InvalidCredentialFault ex) {
            errors.reject("authn.badCredentials",
                    "Invalid username and/or password.");
            request.setAttribute(getErrorsAttributeName(), errors);
            response.setRenderParameter("operation", getErrorOperationName());
            return;
        }

        String ticketEncrypted = getEncryptionService().encrypt(
                ticket.getTicket());
        String redirectUrl = request.getPreferences().getValue(
                getRedirectUrlPreferenceName(), null);
        if (redirectUrl == null) {
            throw new Exception("No redirectUrl found under preference '"
                    + getRedirectUrlPreferenceName());
        }

        StringBuilder portalAuthnUrl = new StringBuilder(command.getPortalAuthnUrl());
        portalAuthnUrl.append("&");
        portalAuthnUrl.append(getTicketParameterName());
        portalAuthnUrl.append("=");
        portalAuthnUrl.append(URLEncoder.encode(ticketEncrypted, "UTF8"));

        logger.debug("portalAuthnUrl = " + portalAuthnUrl);
        redirectUrl += URLEncoder.encode(portalAuthnUrl.toString(), "UTF8");
        logger.debug("redirectUrl = " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest,
      *      javax.portlet.RenderResponse, java.lang.Object,
      *      org.springframework.validation.BindException)
      */
    @Override
    protected ModelAndView handleRender(RenderRequest arg0,
                                        RenderResponse arg1, Object arg2, BindException arg3)
            throws Exception {
        throw new IllegalStateException(getClass().getName()
                + " does not handle render requests.");
    }

    public AuthnService getAuthnService() {
        return authnService;
    }

    public void setAuthnService(AuthnService authnService) {
        this.authnService = authnService;
    }

    public EncryptionService getEncryptionService() {
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public String getRedirectUrlPreferenceName() {
        return redirectUrlPreferenceName;
    }

    public void setRedirectUrlPreferenceName(String redirectUrlPreferenceName) {
        this.redirectUrlPreferenceName = redirectUrlPreferenceName;
    }

    public String getTicketParameterName() {
        return ticketParameterName;
    }

    public void setTicketParameterName(String ticketParameterName) {
        this.ticketParameterName = ticketParameterName;
    }

    public String getErrorsAttributeName() {
        return errorsAttributeName;
    }

    public void setErrorsAttributeName(String errorsAttributeName) {
        this.errorsAttributeName = errorsAttributeName;
    }

    public String getIfsUrl() {
        return ifsUrl;
    }

    public void setIfsUrl(String ifsUrl) {
        this.ifsUrl = ifsUrl;
    }

    public String getErrorOperationName() {
        return errorOperationName;
    }

    public void setErrorOperationName(String errorOperationName) {
        this.errorOperationName = errorOperationName;
    }

    public String getViewOperationName() {
        return viewOperationName;
    }

    public void setViewOperationName(String viewOperationName) {
        this.viewOperationName = viewOperationName;
    }

    public String getPortalUserAttributeName() {
        return portalUserAttributeName;
    }

    public void setPortalUserAttributeName(String portalUserAttributeName) {
        this.portalUserAttributeName = portalUserAttributeName;
    }

}
