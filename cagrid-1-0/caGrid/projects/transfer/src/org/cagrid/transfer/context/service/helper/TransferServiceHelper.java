package org.cagrid.transfer.context.service.helper;

import java.io.File;
import java.io.InputStream;
import java.rmi.RemoteException;


public class TransferServiceHelper {

    public static org.cagrid.transfer.context.stubs.types.TransferServiceContextReference createTransferContext(
        File file) throws RemoteException {
        org.apache.axis.message.addressing.EndpointReferenceType epr = new org.apache.axis.message.addressing.EndpointReferenceType();
        org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResourceHome home = null;
        org.globus.wsrf.ResourceKey resourceKey = null;
        org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();

        String servicePath = ctx.getTargetService();
        String homeName = "java:comp/env/services/cagrid/TransferServiceContext/home";

        try {
            javax.naming.Context initialContext = new javax.naming.InitialContext();
            home = (org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResourceHome) initialContext
                .lookup(homeName);
            resourceKey = home.createResource();

            // Grab the newly created resource
            org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource thisResource = (org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource) home
                .find(resourceKey);

            thisResource.setSecurityDescriptor(gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils
                .createCreatorOnlyResourceSecurityDescriptor());
            thisResource.stage(file);

            String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
            transportURL += "TransferServiceContext";
            epr = org.globus.wsrf.utils.AddressingUtils.createEndpointReference(transportURL, resourceKey);
        } catch (Exception e) {
            throw new RemoteException("Error looking up TransferServiceContext home:" + e.getMessage(), e);
        }

        // return the typed EPR
        org.cagrid.transfer.context.stubs.types.TransferServiceContextReference ref = new org.cagrid.transfer.context.stubs.types.TransferServiceContextReference();
        ref.setEndpointReference(epr);

        return ref;
    }


    public static org.cagrid.transfer.context.stubs.types.TransferServiceContextReference createTransferContext(
        byte[] data) throws RemoteException {
        org.apache.axis.message.addressing.EndpointReferenceType epr = new org.apache.axis.message.addressing.EndpointReferenceType();
        org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResourceHome home = null;
        org.globus.wsrf.ResourceKey resourceKey = null;
        org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
        String servicePath = ctx.getTargetService();
        String homeName = "java:comp/env/services/cagrid/TransferServiceContext/home";

        try {
            javax.naming.Context initialContext = new javax.naming.InitialContext();
            home = (org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResourceHome) initialContext
                .lookup(homeName);
            resourceKey = home.createResource();

            // Grab the newly created resource
            org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource thisResource = (org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource) home
                .find(resourceKey);

            thisResource.setSecurityDescriptor(gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils
                .createCreatorOnlyResourceSecurityDescriptor());
            thisResource.stage(data);

            String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
            transportURL += "TransferServiceContext";
            epr = org.globus.wsrf.utils.AddressingUtils.createEndpointReference(transportURL, resourceKey);
        } catch (Exception e) {
            throw new RemoteException("Error looking up TransferServiceContext home:" + e.getMessage(), e);
        }

        // return the typed EPR
        org.cagrid.transfer.context.stubs.types.TransferServiceContextReference ref = new org.cagrid.transfer.context.stubs.types.TransferServiceContextReference();
        ref.setEndpointReference(epr);

        return ref;
    }


    public static org.cagrid.transfer.context.stubs.types.TransferServiceContextReference createTransferContext(
        InputStream is) throws RemoteException {
        org.apache.axis.message.addressing.EndpointReferenceType epr = new org.apache.axis.message.addressing.EndpointReferenceType();
        org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResourceHome home = null;
        org.globus.wsrf.ResourceKey resourceKey = null;
        org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
        String servicePath = ctx.getTargetService();
        String homeName = "java:comp/env/services/cagrid/TransferServiceContext/home";

        try {
            javax.naming.Context initialContext = new javax.naming.InitialContext();
            home = (org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResourceHome) initialContext
                .lookup(homeName);
            resourceKey = home.createResource();

            // Grab the newly created resource
            org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource thisResource = (org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource) home
                .find(resourceKey);

            thisResource.setSecurityDescriptor(gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils
                .createCreatorOnlyResourceSecurityDescriptor());
            thisResource.stage(is);

            String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
            transportURL += "TransferServiceContext";
            epr = org.globus.wsrf.utils.AddressingUtils.createEndpointReference(transportURL, resourceKey);
        } catch (Exception e) {
            throw new RemoteException("Error looking up TransferServiceContext home:" + e.getMessage(), e);
        }

        // return the typed EPR
        org.cagrid.transfer.context.stubs.types.TransferServiceContextReference ref = new org.cagrid.transfer.context.stubs.types.TransferServiceContextReference();
        ref.setEndpointReference(epr);

        return ref;
    }
}
