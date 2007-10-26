package org.cagrid.gaards.cds.delegated.service;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceContext;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.1
 * 
 */
public class DelegatedCredentialImpl extends DelegatedCredentialImplBase {

	public DelegatedCredentialImpl() throws RemoteException {
		super();
	}

  public org.cagrid.gaards.cds.common.DelegationIdentifier getDelegatedCredential() throws RemoteException {
		DelegatedCredentialResource resource = (DelegatedCredentialResource) ResourceContext
				.getResourceContext().getResource();
		return resource.getDelegatedCredential();
	}

}
