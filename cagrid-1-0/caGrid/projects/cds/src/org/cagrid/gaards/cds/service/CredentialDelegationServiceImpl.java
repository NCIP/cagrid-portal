package org.cagrid.gaards.cds.service;

import java.rmi.RemoteException;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.1
 */
public class CredentialDelegationServiceImpl extends CredentialDelegationServiceImplBase {

	public CredentialDelegationServiceImpl() throws RemoteException {
		super();
	}

  public void delegateCredential(org.cagrid.gaards.cds.common.DelegationPolicy policy) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault {
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println(policy.getClass().getName());
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("*****************************************");
	}

}
