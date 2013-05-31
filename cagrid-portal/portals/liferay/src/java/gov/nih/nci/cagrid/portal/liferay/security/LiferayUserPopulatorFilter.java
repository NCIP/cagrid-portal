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
package gov.nih.nci.cagrid.portal.liferay.security;

import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.service.RoleServiceUtil;
import gov.nih.nci.cagrid.portal.domain.LiferayUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LiferayUserPopulatorFilter implements Filter {

    private static final Log logger = LogFactory.getLog(LiferayUserPopulatorFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        String remoteUser = httpReq.getRemoteUser();

        //will only be true if user is logged into liferay
        if (remoteUser != null) {
            try {
                //only do it if object not in session
                if (isAdmin(Long.parseLong(remoteUser))) {
                    httpReq.getSession().setAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE, LiferayUser.HTTP_SESSION_ATTR_VALUE_ROLE_ADMIN);
                } else {
                    httpReq.getSession().setAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE, LiferayUser.HTTP_SESSION_ATTR_VALUE_ROLE_USER);
                }
            } catch (Exception e) {
                logger.warn(e);
            }
        }
        filterChain.doFilter(req, res);
    }

    private boolean isAdmin(long userId) throws Exception {
        List<Role> roles = RoleServiceUtil.getUserRoles(userId);
        for (Role role : roles) {
            if (RoleConstants.ADMINISTRATOR.equals(role.getName())
                    || RoleConstants.POWER_USER.equals(role.getName()))
                return true;
        }
        return false;
    }


    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
