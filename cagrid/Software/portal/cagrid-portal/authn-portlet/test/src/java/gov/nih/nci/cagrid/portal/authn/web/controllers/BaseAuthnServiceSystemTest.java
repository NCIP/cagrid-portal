package gov.nih.nci.cagrid.portal.authn.web.controllers;

import org.junit.Test;
import gov.nih.nci.cagrid.portal.authn.service.BaseAuthnService;
import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.authn.AuthnTimeoutException;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BaseAuthnServiceSystemTest {


    @Test
    public void doSomething(){
      BaseAuthnService service  =new BaseAuthnService();
        try {
            service.authenticateToIdP("manav","Judwa!@#","https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService");
        } catch (AuthnServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidCredentialFault invalidCredentialFault) {
            invalidCredentialFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (AuthnTimeoutException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
