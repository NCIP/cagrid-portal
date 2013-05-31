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
package gov.nih.nci.cagrid.portal.authn.web.controllers;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.portlet.PortletMode;
import javax.portlet.PortletSession;

import static junit.framework.Assert.fail;
import gov.nih.nci.cagrid.portal.authn.domain.IdPAuthnInfo;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AuthnControllerTest extends SpringPortletContextTestBase<AuthnPortletContext> {

    public AuthnControllerTest() {
        super(AuthnPortletContext.class);
    }

    /**
     * Make sure we get a view back
     *
     * @throws Exception
     */
    @Test
    public void testView()  {

        try {
            request.setPortletMode(PortletMode.VIEW);
            request.addParameter("operation","login");
            request.getPreferences().setValue("loginRedirectUrl","http://");
            doRender(request, response);
            assertNotNull(response.getIncludedUrl());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testSuccess(){
        try {
            request.getPortletSession().setAttribute("CAGRIDPORTAL_ATTS_liferayUserId",new IdPAuthnInfo(), PortletSession.APPLICATION_SCOPE);
            request.addParameter("operation","login");
            doRender(request, response);
            // ToDo, make sure success view is returned           
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
