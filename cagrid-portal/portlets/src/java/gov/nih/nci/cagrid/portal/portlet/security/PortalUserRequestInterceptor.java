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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.security;

import gov.nih.nci.cagrid.portal.domain.PortalUser;
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
public class PortalUserRequestInterceptor implements WebRequestInterceptor {

    private static final Log logger = LogFactory.getLog(PortalUserRequestInterceptor.class);

    private String portalUserSessionAttributeName;
    private String portalUserRequestAttributeName;

    /**
     *
     */
    public PortalUserRequestInterceptor() {

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.context.request.WebRequestInterceptor#afterCompletion(org.springframework.web.context.request.WebRequest,
      *      java.lang.Exception)
      */
    public void afterCompletion(WebRequest arg0, Exception arg1)
            throws Exception {

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.context.request.WebRequestInterceptor#postHandle(org.springframework.web.context.request.WebRequest,
      *      org.springframework.ui.ModelMap)
      */
    public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.context.request.WebRequestInterceptor#preHandle(org.springframework.web.context.request.WebRequest)
      */
    public void preHandle(WebRequest webRequest) throws Exception {
        PortletWebRequest portletWebRequest = (PortletWebRequest) webRequest;
        PortalUser portalUser = (PortalUser) portletWebRequest.getRequest()
                .getPortletSession().getAttribute(getPortalUserSessionAttributeName(),
                        PortletSession.APPLICATION_SCOPE);
        if (portalUser == null) {
            logger.debug("Didn't find PortalUser in session under " + getPortalUserSessionAttributeName());
        } else {
            logger.debug("Putting PortalUser in request under " + getPortalUserRequestAttributeName());
            portletWebRequest.getRequest().setAttribute(
                    getPortalUserRequestAttributeName(), portalUser);
        }
    }

    public String getPortalUserSessionAttributeName() {
        return portalUserSessionAttributeName;
    }

    public void setPortalUserSessionAttributeName(String portalUserAttributeName) {
        this.portalUserSessionAttributeName = portalUserAttributeName;
    }

    public String getPortalUserRequestAttributeName() {
        return portalUserRequestAttributeName;
    }

    public void setPortalUserRequestAttributeName(
            String portalUserRequestAttributeName) {
        this.portalUserRequestAttributeName = portalUserRequestAttributeName;
    }

}
