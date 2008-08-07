package org.cagrid.gaards.ui.dorian;

import gov.nih.nci.cagrid.common.Runner;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.configuration.ServiceConfiguration;
import org.cagrid.grape.configuration.ServiceDescriptor;
import org.cagrid.grape.configuration.Services;


public class AuthenticationLookupThread extends Runner {

    private Logger log;
    private DorianHandle handle;
    private List<AuthenticationServiceHandle> authenticationServices;


    public AuthenticationLookupThread(DorianHandle handle) {
        this.log = Logger.getLogger(getClass());
        this.handle = handle;
    }


    public void execute() {
        this.authenticationServices = new ArrayList<AuthenticationServiceHandle>();
        try {
            GridUserClient client = this.handle.getUserClient();
            List<TrustedIdentityProvider> idps = client.getTrustedIdentityProviders();
            if (idps != null) {
                for (int i = 0; i < idps.size(); i++) {
                    ServiceDescriptor des = new ServiceDescriptor();
                    des.setDisplayName(idps.get(i).getDisplayName());
                    des.setServiceURL(idps.get(i).getAuthenticationServiceURL());
                    des.setServiceIdentity(idps.get(i).getAuthenticationServiceIdentity());
                    AuthenticationServiceHandle as = new AuthenticationServiceHandle(des);
                    authenticationServices.add(as);
                }
            } else {
                ServiceConfiguration conf = (ServiceConfiguration) GridApplication.getContext()
                    .getConfigurationManager().getConfigurationObject(DorianUIConstants.AUTHENTICATION_SERVICE_CONF);
                Services s = conf.getServices();
                if (s != null) {
                    ServiceDescriptor[] list = s.getServiceDescriptor();
                    if (list != null) {
                        for (int i = 0; i < list.length; i++) {
                            authenticationServices.add(new AuthenticationServiceHandle(list[i]));
                        }
                    }
                }
            }

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        this.handle.setAuthenticationServices(this.authenticationServices);
    }


    public List<AuthenticationServiceHandle> getAuthenticationServices() {
        return authenticationServices;
    }
}
