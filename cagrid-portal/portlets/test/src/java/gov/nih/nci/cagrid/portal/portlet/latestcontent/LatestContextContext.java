package gov.nih.nci.cagrid.portal.portlet.latestcontent;

import gov.nih.nci.cagrid.portal.portlet.TestPortletContextClassBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LatestContextContext extends TestPortletContextClassBase {
    public String[] getConfigLocations() {
        return new String[]{"classpath*:latestContent-portlet.xml", "classpath*:common.xml",
                "classpath*:applicationContext-db.xml", "classpath*:applicationContext-service.xml",
                "classpath*:applicationContext-security.xml"};
    }

}


