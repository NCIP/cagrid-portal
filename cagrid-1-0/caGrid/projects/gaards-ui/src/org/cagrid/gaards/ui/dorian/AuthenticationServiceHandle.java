package org.cagrid.gaards.ui.dorian;

import gov.nih.nci.cagrid.common.Utils;

import org.cagrid.gaards.authentication.client.AuthenticationClient;
import org.cagrid.grape.configuration.ServiceDescriptor;
import org.globus.wsrf.impl.security.authorization.IdentityAuthorization;


public class AuthenticationServiceHandle extends ServiceHandle {

    public AuthenticationServiceHandle(ServiceDescriptor des) {
        super(des);
    }

    public AuthenticationClient getAuthenticationClient() throws Exception {
        AuthenticationClient client = new AuthenticationClient(getServiceDescriptor().getServiceURL());
        if(Utils.clean(getServiceDescriptor().getServiceIdentity())!=null){
            IdentityAuthorization auth = new IdentityAuthorization(getServiceDescriptor().getServiceIdentity());
            client.setAuthorization(auth);
        }
        return client;
    }

}
