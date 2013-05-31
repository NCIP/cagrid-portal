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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalBuildStampFilter implements Filter {

    public static final String PORTAL_BUILD_STAMP_KEY ="cagrid.portal.build.stamp";

    private static final Log logger = LogFactory.getLog(PortalBuildStampFilter.class);


    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        if(httpReq.getSession().getAttribute(PORTAL_BUILD_STAMP_KEY)==null){
            //try and load properties
            String _value =getBuildStampValue();
            if(_value!=null){
                logger.debug("Setting build stamp in request to " + _value);
                httpReq.getSession().setAttribute(PORTAL_BUILD_STAMP_KEY,_value);
            }
        }
        else{
            logger.debug("Build timestamp already set");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getBuildStampValue(){
        try {
            java.util.Properties props = new java.util.Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("cagridportal.properties");
            props.load(is);
            return (String)props.get(PORTAL_BUILD_STAMP_KEY);
        } catch (IOException e) {
            logger.debug("Build stamp not set. Will return null");
        }

        return null;

    }
}
