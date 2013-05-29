/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
