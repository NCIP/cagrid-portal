/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.security;

import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.security.CDSCredentialRetriever;

import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.portlet.context.PortletWebRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com>Manav Kher</a>
 */
public class PortalUserInterceptor2 implements WebRequestInterceptor {

    private static final Log logger = LogFactory
            .getLog(PortalUserInterceptor2.class);
    private UserModel userModel;

    private String portalUserSessionAttributeName;
    private String portalUserIdSessionAttributeName;
    private PortalUserDao portalUserDao;
    private CDSCredentialRetriever cdsCredentialRetriever;

    /**
     *
     */
    public PortalUserInterceptor2() {
        // TODO Auto-generated constructor stub
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.springframework.web.context.request.WebRequestInterceptor#afterCompletion
      * (org.springframework.web.context.request.WebRequest, java.lang.Exception)
      */
    public void afterCompletion(WebRequest arg0, Exception arg1)
            throws Exception {
        // TODO Auto-generated method stub

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.springframework.web.context.request.WebRequestInterceptor#postHandle
      * (org.springframework.web.context.request.WebRequest,
      * org.springframework.ui.ModelMap)
      */
    public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {
        // TODO Auto-generated method stub

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.springframework.web.context.request.WebRequestInterceptor#preHandle
      * (org.springframework.web.context.request.WebRequest)
      */
    public void preHandle(WebRequest webRequest) throws Exception {

        PortletWebRequest portletWebRequest = (PortletWebRequest) webRequest;


        PortletSession session = portletWebRequest.getRequest()
                .getPortletSession();
        String portalUserId = (String) session.getAttribute(
                getPortalUserIdSessionAttributeName(),
                PortletSession.APPLICATION_SCOPE);

        if (portalUserId != null) {
            logger.debug("portalUserId = " + portalUserId);

            // See if the same user is in the session
            PortalUser portalUser = (PortalUser) session.getAttribute(
                    getPortalUserSessionAttributeName(),
                    PortletSession.APPLICATION_SCOPE);
            if (portalUser != null) {
                logger.debug("Portal user found in session under: "
                        + getPortalUserSessionAttributeName());
                if (!portalUser.getPortalId().equals(portalUserId)) {
                    throw new Exception(
                            "A portal user is in the session with a different ID.");
                }
            } else {
                logger
                        .debug("No portal user in session. Fetching from database and putting in session and models.");
                portalUser = getPortalUserDao().getByPortalId(portalUserId);

                if (portalUser == null) {
                    logger.warn("No user found for portal ID: " + portalUserId);
                }
                /** lets check if logged in with webSSO **/
                else {
                    try {
                        if (portalUser.getGridCredential() == null
                                && portalUser.getDelegatedEPR() != null) {
                            logger
                                    .info("User logged in with webSSO. Will try to get delegated credential");
                            portalUser
                                    .setGridCredential(cdsCredentialRetriever
                                            .getCredential(portalUser
                                            .getDelegatedEPR()));
                        }
                    } catch (AuthnServiceException e) {
                        throw new AuthnServiceException(
                                "Could not retreive credentials from CDS service. You will not be able to invoke secure services",
                                e);
                    }
                }
                /** set in session **/
                getUserModel().setPortalUser(portalUser);
                logger.debug("Putting portal user in session under: "
                        + getPortalUserSessionAttributeName());
                session.setAttribute(getPortalUserSessionAttributeName(),
                        portalUser, PortletSession.APPLICATION_SCOPE);
            }

        }
    }

    public CDSCredentialRetriever getCdsCredentialRetriever() {
        return cdsCredentialRetriever;
    }

    public void setCdsCredentialRetriever(
            CDSCredentialRetriever cdsCredentialRetriever) {
        this.cdsCredentialRetriever = cdsCredentialRetriever;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getPortalUserSessionAttributeName() {
        return portalUserSessionAttributeName;
    }

    public void setPortalUserSessionAttributeName(
            String portalUserSessionAttributeName) {
        this.portalUserSessionAttributeName = portalUserSessionAttributeName;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    public String getPortalUserIdSessionAttributeName() {
        return portalUserIdSessionAttributeName;
    }

    public void setPortalUserIdSessionAttributeName(
            String portalUserIdSessionAttributeName) {
        this.portalUserIdSessionAttributeName = portalUserIdSessionAttributeName;
    }

}
