package gov.nih.nci.cagrid.portal.liferay.security;

import com.liferay.portal.model.impl.RoleImpl;
import com.liferay.portal.model.Role;
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
        String remoteUser = (String) httpReq.getSession().getAttribute("j_username");

        //will only be true if user is logged into liferay
        if (remoteUser != null) {
            try {
                //only do it if object not in session
                if (httpReq.getSession().getAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE) == null) {
                    logger.debug("Adding liferay user groups to Request");
                    if (isAdmin(Long.parseLong(remoteUser))) {
                        logger.debug("Found admin user rule. Setting it in session");
                        httpReq.getSession().setAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE, LiferayUser.HTTP_SESSION_ATTR_VALUE_ROLE_ADMIN);
                    } else {
                        httpReq.getSession().setAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE, LiferayUser.HTTP_SESSION_ATTR_VALUE_ROLE_USER);
                    }
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
            if (role.getName().equals(RoleImpl.ADMINISTRATOR))
                return true;
        }
        return false;
    }


    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
