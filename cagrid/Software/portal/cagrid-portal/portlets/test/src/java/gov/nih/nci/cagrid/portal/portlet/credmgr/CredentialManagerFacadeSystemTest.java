package gov.nih.nci.cagrid.portal.portlet.credmgr;

import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CredentialManagerFacadeSystemTest extends PortalPortletIntegrationTestBase {

    public CredentialManagerFacade credentialManagerFacade;

    public void testCreds() {
        assertNotNull(credentialManagerFacade.listIdPs());
    }

    public CredentialManagerFacade getCredentialManagerFacade() {
        return credentialManagerFacade;
    }

    public void setCredentialManagerFacade(CredentialManagerFacade credentialManagerFacade) {
        this.credentialManagerFacade = credentialManagerFacade;
    }
}
