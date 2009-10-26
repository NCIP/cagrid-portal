package gov.nih.nci.cagrid.portal.authn.web.controllers;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AuthnPortletContext extends TestPortletContextClassBase {
    public String[] getConfigLocations() {
        return new String[]{"classpath*:applicationContext-authn-beans.xml",
                "classpath*:cagrid-authn-portlet.xml"
               };
    }

}

