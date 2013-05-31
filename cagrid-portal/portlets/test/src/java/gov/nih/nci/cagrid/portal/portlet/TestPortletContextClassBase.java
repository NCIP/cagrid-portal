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

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.portlet.context.ConfigurablePortletApplicationContext;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class TestPortletContextClassBase extends ClassPathXmlApplicationContext
        implements ConfigurablePortletApplicationContext {

    private ServletContext servletContext;
    private PortletContext portletContext;
    private PortletConfig portletConfig;
    private String namespace;

    public abstract String[] getConfigLocations();

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    public PortletConfig getPortletConfig() {
        return portletConfig;
    }

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
