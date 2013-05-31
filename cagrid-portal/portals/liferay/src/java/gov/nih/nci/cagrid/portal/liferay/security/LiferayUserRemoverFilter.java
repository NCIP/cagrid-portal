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

import gov.nih.nci.cagrid.portal.domain.LiferayUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * On logout this filter will remove the LiferayUser roles
 * from session
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LiferayUserRemoverFilter implements Filter {

    private static final Log logger = LogFactory.getLog(LiferayUserRemoverFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;

        if (httpReq.getSession().getAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE) != null) {
            logger.debug("User has signed out. Will remove User attributes from session");
            httpReq.getSession().removeAttribute(LiferayUser.HTTP_SESSION_ATTR_KEY_USER_ROLE);
        }
        filterChain.doFilter(req, res);
    }


    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
