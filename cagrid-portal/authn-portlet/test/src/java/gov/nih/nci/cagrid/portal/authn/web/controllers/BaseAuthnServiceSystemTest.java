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

import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.authn.AuthnTimeoutException;
import gov.nih.nci.cagrid.portal.authn.service.BaseAuthnService;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BaseAuthnServiceSystemTest {


    @Test
    public void doSomething() throws Exception{
        BaseAuthnService service = new BaseAuthnService();
            service.authenticateToIdP("testuser", "testpassword", "https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService");

    }
}
