package org.cagrid.gaards.ui.dorian;

import org.cagrid.grape.configuration.ServiceDescriptor;


public class ServiceHandle {

    private ServiceDescriptor des;


    public ServiceHandle(ServiceDescriptor des) {
        this.des = des;
    }


    public String toString() {
        return des.getDisplayName();
    }


    public ServiceDescriptor getServiceDescriptor() {
        return des;
    }

}
