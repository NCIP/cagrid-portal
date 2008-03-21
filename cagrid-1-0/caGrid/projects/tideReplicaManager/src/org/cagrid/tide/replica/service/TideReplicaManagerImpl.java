package org.cagrid.tide.replica.service;

import java.io.File;
import java.rmi.RemoteException;

import org.cagrid.tide.descriptor.TideReplicaDescriptor;
import org.cagrid.tide.descriptor.TideReplicasDescriptor;
import org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource;
import org.cagrid.transfer.context.service.helper.DataStagedCallback;
import org.cagrid.transfer.context.service.helper.TransferServiceHelper;
import org.cagrid.transfer.descriptor.DataDescriptor;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class TideReplicaManagerImpl extends TideReplicaManagerImplBase {

    public TideReplicaManagerImpl() throws RemoteException {
        super();
    }

  public org.cagrid.tide.replica.context.stubs.types.TideReplicaManagerContextReference createTideReplicaManagerContext(org.cagrid.tide.descriptor.TideDescriptor tideDescriptor) throws RemoteException {
        org.apache.axis.message.addressing.EndpointReferenceType epr = new org.apache.axis.message.addressing.EndpointReferenceType();
        org.cagrid.tide.replica.context.service.globus.resource.TideReplicaManagerContextResourceHome home = null;
        org.globus.wsrf.ResourceKey resourceKey = null;
        org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
        String servicePath = ctx.getTargetService();
        String homeName = org.globus.wsrf.Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/"
            + "tideReplicaManagerContextHome";

        try {
            javax.naming.Context initialContext = new javax.naming.InitialContext();
            home = (org.cagrid.tide.replica.context.service.globus.resource.TideReplicaManagerContextResourceHome) initialContext
                .lookup(homeName);
            resourceKey = home.createResource(tideDescriptor.getId());

            // Grab the newly created resource
            org.cagrid.tide.replica.context.service.globus.resource.TideReplicaManagerContextResource thisResource = (org.cagrid.tide.replica.context.service.globus.resource.TideReplicaManagerContextResource) home
                .find(resourceKey);

            // This is where the creator of this resource type can set whatever
            // needs
            // to be set on the resource so that it can function appropriatly
            // for instance
            // if you want the resouce to only have the query string then there
            // is where you would
            // give it the query string.
            TideReplicasDescriptor replicas = new TideReplicasDescriptor();
            replicas.setTideDescriptor(tideDescriptor);
            thisResource.setTideReplicasDescriptor(replicas);

            // sample of setting creator only security. This will only allow the
            // caller that created
            // this resource to be able to use it.
            // thisResource.setSecurityDescriptor(gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils.createCreatorOnlyResourceSecurityDescriptor());

            String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
            transportURL += "TideReplicaManagerContext";
            epr = org.globus.wsrf.utils.AddressingUtils.createEndpointReference(transportURL, resourceKey);
        } catch (Exception e) {
            throw new RemoteException("Error looking up TideReplicaManagerContext home:" + e.getMessage(), e);
        }

        // return the typed EPR
        org.cagrid.tide.replica.context.stubs.types.TideReplicaManagerContextReference ref = new org.cagrid.tide.replica.context.stubs.types.TideReplicaManagerContextReference();
        ref.setEndpointReference(epr);

        return ref;
    }

  public org.cagrid.tide.replica.context.stubs.types.TideReplicaManagerContextReference getTideReplicaManagerContext(java.lang.String tideID) throws RemoteException {
        org.cagrid.tide.replica.context.service.globus.resource.TideReplicaManagerContextResourceHome home = null;
        org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
        String servicePath = ctx.getTargetService();
        String homeName = org.globus.wsrf.Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/"
            + "tideReplicaManagerContextHome";

        try {
            javax.naming.Context initialContext = new javax.naming.InitialContext();
            home = (org.cagrid.tide.replica.context.service.globus.resource.TideReplicaManagerContextResourceHome) initialContext
                .lookup(homeName);

            return home.getResourceReference(tideID);

        } catch (Exception e) {
            throw new RemoteException("Error looking up TideReplicaManagerContext home:" + e.getMessage(), e);
        }
    }

}
