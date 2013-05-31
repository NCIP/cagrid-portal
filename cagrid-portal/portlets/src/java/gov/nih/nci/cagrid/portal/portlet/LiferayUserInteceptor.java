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
package gov.nih.nci.cagrid.portal.portlet;

import gov.nih.nci.cagrid.portal.domain.LiferayUser;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.portlet.context.PortletWebRequest;

import javax.portlet.PortletSession;

/**
 * Inteceptor will look for Liferay user groups in session
 * and create a local LiferayUser object in HTTP request
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LiferayUserInteceptor implements org.springframework.web.context.request.WebRequestInterceptor {

    private static final Log logger = LogFactory
            .getLog(LiferayUserInteceptor.class);

    private DiscoveryModel discoveryModel;
    private String liferayUserRequestAttributeName;

    public void preHandle(WebRequest webRequest) throws Exception {
        PortletWebRequest portletWebRequest = (PortletWebRequest) webRequest;

        Object obj = portletWebRequest.getRequest().getPortletSession()
                .getAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE, PortletSession.APPLICATION_SCOPE);

        if (obj != null) {
            logger.debug("Found liferay user object in session");

            LiferayUser liferayUser = new LiferayUser();

            if (obj.equals(LiferayUser.HTTP_SESSION_ATTR_VALUE_ROLE_ADMIN))
                liferayUser.setAdmin(true);
            //isAdmin is false by default

            logger.debug("Putting liferay user "
                    + " in request");
            portletWebRequest.getRequest().setAttribute(
                    getLiferayUserRequestAttributeName(), liferayUser);
            discoveryModel.setLiferayUser(liferayUser);
        } else {
            discoveryModel.setLiferayUser(null);
        }
    }

    public String getLiferayUserRequestAttributeName() {
        return liferayUserRequestAttributeName;
    }

    public void setLiferayUserRequestAttributeName(String liferayUserRequestAttributeName) {
        this.liferayUserRequestAttributeName = liferayUserRequestAttributeName;
    }

    public void postHandle(WebRequest webRequest, ModelMap modelMap) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterCompletion(WebRequest webRequest, Exception e) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
    }
}
