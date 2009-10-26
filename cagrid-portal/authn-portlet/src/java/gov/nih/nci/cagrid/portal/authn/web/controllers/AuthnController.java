package gov.nih.nci.cagrid.portal.authn.web.controllers;

import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.authn.AuthnTimeoutException;
import gov.nih.nci.cagrid.portal.authn.EncryptionService;
import gov.nih.nci.cagrid.portal.authn.domain.DirectLoginCommand;
import gov.nih.nci.cagrid.portal.authn.domain.IdPAuthnInfo;
import gov.nih.nci.cagrid.portal.authn.domain.IdPBean;
import gov.nih.nci.cagrid.portal.authn.service.AuthnService;
import gov.nih.nci.cagrid.portal.authn.service.IdPService;
import org.globus.gsi.GlobusCredential;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;

import javax.portlet.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.portal.util.PortalUtil;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AuthnController extends SimpleFormController {


    private AuthnService authnService;
    private EncryptionService encryptionService;
    private String redirectUrlPreferenceName;
    private String sharedSessionAttribute;
    private IdPService idPService;
    private String ifsUrl;
    private String portalUserIdSessionAttributeName;
    private String errorsObjectName;


    public AuthnController() {
        setRedirectAction(true);
    }

    protected Map referenceData(PortletRequest request) throws Exception {
        Map<String, List<IdPBean>> model = new HashMap<String, List<IdPBean>>();
        model.put("listIdPsFromDorian", getIdPService().listIdPsFromDorian(getIfsUrl()));
        return model;
    }

    @Override
    public ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        PortletSession session = request.getPortletSession();
        IdPAuthnInfo portalAuthn = (IdPAuthnInfo) session.getAttribute(
                getPortalUserIdSessionAttributeName(),
                PortletSession.APPLICATION_SCOPE);

        if (portalAuthn != null) {
            Map model = new HashMap();
            model.put("portalUser", portalAuthn);
            return new ModelAndView(getSuccessView(), model);
        }
        return super.handleRenderRequestInternal(request, response);
    }

    protected void onSubmitAction(ActionRequest request, ActionResponse response, Object obj, BindException errors) throws Exception {

        DirectLoginCommand command = (DirectLoginCommand) obj;

        GlobusCredential cred = null;

        String idpUrl = command.getIdpUrl().substring(1);
        IdPAuthnInfo authnInfo = null;
        try {
            authnInfo = getAuthnService().authenticateToIdP(command.getUsername(), command.getPassword(), idpUrl);
        } catch (AuthnServiceException e) {
            logger.warn("Authentication failed", e);
            errors.reject("authn.exception", "Could not authenticate with the Authentication Service");
        } catch (InvalidCredentialFault invalidCredentialFault) {
            errors.reject("authn.invalid.credentials", "Username/Password incorrect.");
        } catch (AuthnTimeoutException e) {
            logger.warn("Authentication service timed out", e);
            errors.reject("authn.timeout.exception", "Authentication service timed out. Please try again.");
        }

        if (errors.hasErrors()) {
            logger.debug("Has errors, setting error attribute and returning.");
            request.setAttribute(getErrorsObjectName(), errors);
            return;
        }

        cred = getAuthnService().authenticateToIFS(getIfsUrl(), authnInfo.getSaml());
        authnInfo.setGridIdentity(cred.getIdentity());

        logger.debug("Login successful.");
        String redirectUrl = request.getPreferences().getValue(
                getRedirectUrlPreferenceName(), null);
        if (redirectUrl == null) {
            throw new Exception("No redirectUrl found under preference '"
                    + getRedirectUrlPreferenceName());
        }

        logger.debug("Adding authentication info to session");
        PortalUtil.getHttpServletRequest(request).getSession().setAttribute(getSharedSessionAttribute(), authnInfo);

        StringBuilder portalAuthnUrl = new StringBuilder(command.getPortalAuthnUrl());
        logger.debug("portalAuthnUrl = " + portalAuthnUrl);
        redirectUrl += URLEncoder.encode(portalAuthnUrl.toString(), "UTF8");
        logger.debug("redirectUrl = " + redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    public String getErrorsObjectName() {
        return errorsObjectName;
    }

    public void setErrorsObjectName(String errorsObjectName) {
        this.errorsObjectName = errorsObjectName;
    }

    public String getIfsUrl() {
        return ifsUrl;
    }

    public void setIfsUrl(String ifsUrl) {
        this.ifsUrl = ifsUrl;
    }

    public String getPortalUserIdSessionAttributeName() {
        return portalUserIdSessionAttributeName;
    }

    public void setPortalUserIdSessionAttributeName(String portalUserIdSessionAttributeName) {
        this.portalUserIdSessionAttributeName = portalUserIdSessionAttributeName;
    }

    public String getSharedSessionAttribute() {
        return sharedSessionAttribute;
    }

    public void setSharedSessionAttribute(String sharedSessionAttribute) {
        this.sharedSessionAttribute = sharedSessionAttribute;
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


    public IdPService getIdPService() {
        return idPService;
    }

    public void setIdPService(IdPService idPService) {
        this.idPService = idPService;
    }
}
