package org.cagrid.gaards.dorian.service.globus.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.dorian.common.DorianConstants;
import org.cagrid.gaards.dorian.federation.TrustedIdP;
import org.cagrid.gaards.dorian.federation.TrustedIdPManager;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProviders;
import org.cagrid.gaards.dorian.stubs.DorianResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;


/**
 * The implementation of this DorianResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class DorianResource extends DorianResourceBase {

    private TrustedIdPManager identityProviderManager;
    private Log log;


    public DorianResource() {
        this.log = LogFactory.getLog(this.getClass().getName());
    }


    public ResourcePropertySet getResourcePropertySet() {
        ResourcePropertySet set = super.getResourcePropertySet();
        updateTrustedIdentityProviders(set);
        return set;
    }


    public org.cagrid.gaards.dorian.federation.TrustedIdentityProviders getTrustedIdentityProviders() {
        updateTrustedIdentityProviders(super.getResourcePropertySet());
        return ((DorianResourceProperties) getResourceBean()).getTrustedIdentityProviders();
    }


    private void updateTrustedIdentityProviders(ResourcePropertySet set) {
        try {
            TrustedIdentityProviders idps = new TrustedIdentityProviders();
            TrustedIdP[] list1 = this.identityProviderManager.getTrustedIdPs();
            if(list1!=null){
              TrustedIdentityProvider[] list2 = new TrustedIdentityProvider[list1.length];
              for(int i=0; i<list1.length; i++){
                  list2[i] = new TrustedIdentityProvider();
                  list2[i].setName(list1[i].getName());
                  list2[i].setDisplayName(list1[i].getDisplayName());
                  list2[i].setAuthenticationServiceURL(list1[i].getAuthenticationServiceURL());
                  list2[i].setAuthenticationServiceIdentity(list1[i].getAuthenticationServiceIdentity());
              }
              idps.setTrustedIdentityProvider(list2);
            }
            ResourceProperty prop = set.get(DorianConstants.TRUSTEDIDENTITYPROVIDERS);
            prop.set(0, idps);
        } catch (Exception e) {
            log.error(e);
        }
    }


    public void setIdentityProviderManager(TrustedIdPManager identityProviderManager) {
        this.identityProviderManager = identityProviderManager;
    }
    
    
}
