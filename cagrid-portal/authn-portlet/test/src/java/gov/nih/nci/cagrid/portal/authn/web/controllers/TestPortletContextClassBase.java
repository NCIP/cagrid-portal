package gov.nih.nci.cagrid.portal.authn.web.controllers;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.portlet.context.ConfigurablePortletApplicationContext;

import javax.servlet.ServletContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletConfig;

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
