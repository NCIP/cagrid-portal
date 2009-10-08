/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.security;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.portlet.query.QueryModel;
import gov.nih.nci.cagrid.portal.security.AuthnServiceException;
import gov.nih.nci.cagrid.portal.security.CDSCredentialRetriever;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.portlet.context.PortletWebRequest;

import javax.portlet.PortletSession;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class PortalUserInterceptor implements WebRequestInterceptor {

    private static final Log logger = LogFactory
            .getLog(PortalUserInterceptor.class);

    private PortalUserDao portalUserDao;
    private String portalUserAttributeName;
    private QueryModel queryModel;
    private String userIdAttributeName;
    private String proxyAttributeName;
    private CDSCredentialRetriever cdsCredentialRetriever;

    /**
     *
     */
    public PortalUserInterceptor() {

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.context.request.WebRequestInterceptor#afterCompletion(org.springframework.web.context.request.WebRequest,
      *      java.lang.Exception)
      */
    public void afterCompletion(WebRequest request, Exception ex)
            throws Exception {

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.context.request.WebRequestInterceptor#postHandle(org.springframework.web.context.request.WebRequest,
      *      org.springframework.ui.ModelMap)
      */
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.context.request.WebRequestInterceptor#preHandle(org.springframework.web.context.request.WebRequest)
      */
    public void preHandle(WebRequest request) throws Exception {

        PortletWebRequest req = (PortletWebRequest) request;

        PortalUser portalUser = null;
        Integer userId = (Integer) req.getRequest().getAttribute(getUserIdAttributeName());
        if (userId == null) {
            logger.debug("Didn't find portal user ID under " + getUserIdAttributeName());
        } else {
            portalUser = getPortalUserDao().getById(userId);
            logger.debug("Putting portal user " + portalUser.getId()
                    + " in session");
            req.getRequest().getPortletSession().setAttribute(
                    getPortalUserAttributeName(), portalUser,
                    PortletSession.APPLICATION_SCOPE);
            getQueryModel().setPortalUser(portalUser);
            try {
                if (portalUser.getGridCredential() == null && portalUser.getDelegatedEPR() != null) {
                    logger.info("User logged in with webSSO. Will try to get delegated credential");
                    portalUser.setGridCredential(cdsCredentialRetriever.getCredential(portalUser.getDelegatedEPR()));
                }
            } catch (AuthnServiceException e) {
                throw new AuthnServiceException("Could not retreive credentials from CDS service. You will not be able to invoke secure services");
            }

        }
    }

    public String getPortalUserAttributeName() {
        return portalUserAttributeName;
    }

    public void setPortalUserAttributeName(String portletUserAttributeName) {
        this.portalUserAttributeName = portletUserAttributeName;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    public QueryModel getQueryModel() {
        return queryModel;
    }

    public void setQueryModel(QueryModel queryModel) {
        this.queryModel = queryModel;
    }

    public String getUserIdAttributeName() {
        return userIdAttributeName;
    }

    public void setUserIdAttributeName(String userIdAttributeName) {
        this.userIdAttributeName = userIdAttributeName;
    }

    public String getProxyAttributeName() {
        return proxyAttributeName;
    }

    public void setProxyAttributeName(String proxyAttributeName) {
        this.proxyAttributeName = proxyAttributeName;
    }

    public CDSCredentialRetriever getCdsCredentialRetriever() {
        return cdsCredentialRetriever;
    }

    public void setCdsCredentialRetriever(CDSCredentialRetriever cdsCredentialRetriever) {
        this.cdsCredentialRetriever = cdsCredentialRetriever;
    }
}
