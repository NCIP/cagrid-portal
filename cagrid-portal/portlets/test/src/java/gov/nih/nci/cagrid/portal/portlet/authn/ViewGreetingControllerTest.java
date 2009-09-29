    package gov.nih.nci.cagrid.portal.portlet.authn;

import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewGreetingControllerTest extends PortalPortletIntegrationTestBase {

    public void testCreate(){
        ViewGreetingController controller = (ViewGreetingController)getApplicationContext().getBean("viewGreetingController");
        assertNotNull(controller);
    }
}
