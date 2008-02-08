package org.cagrid.gaards.cds.common;

import java.rmi.RemoteException;

import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;

/**
 * The Credential Delegation Service (CDS) is a WSRF-compliant Grid service that enables users/services (delegator) to delegate their Grid credentials to other users/services (delegatee) such that the delegatee(s) may act on the delegator's behalf.
 *
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.1
 * 
 */
public interface CredentialDelegationServiceI {

  /**
   * Allows a party to initate the delegation of their credential such that other parties may access their credential to act on their behalf.
   *
   * @param req
   * @throws CDSInternalFault
   *	
   * @throws InvalidPolicyFault
   *	
   * @throws DelegationFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.cds.common.DelegationSigningRequest initiateDelegation(org.cagrid.gaards.cds.common.DelegationRequest req) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault, org.cagrid.gaards.cds.stubs.types.DelegationFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * Allows the party whom initiated the delegation to approve the delegation.
   *
   * @param delegationSigningResponse
   * @throws CDSInternalFault
   *	
   * @throws DelegationFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference approveDelegation(org.cagrid.gaards.cds.common.DelegationSigningResponse delegationSigningResponse) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.DelegationFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows an entity to find delegation records meeting a specified search criteria.
   *
   * @param filter
   * @throws CDSInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.cds.common.DelegationRecord[] findDelegatedCredentials(org.cagrid.gaards.cds.common.DelegationRecordFilter filter) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows one to update the status of a Delegated Credential.
   *
   * @param id
   * @param status
   * @throws CDSInternalFault
   *	
   * @throws DelegationFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void updateDelegatedCredentialStatus(org.cagrid.gaards.cds.common.DelegationIdentifier id,org.cagrid.gaards.cds.common.DelegationStatus status) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.DelegationFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * This method will find the credentials that have been delegated to the client by other entities.
   *
   * @param filter
   * @throws CDSInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.cds.common.DelegationDescriptor[] findCredentialsDelegatedToClient(org.cagrid.gaards.cds.common.ClientDelegationFilter filter) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows a party to search the audit logs for delegated credentials.
   *
   * @param f
   * @throws CDSInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   * @throws DelegationFault
   *	
   */
  public org.cagrid.gaards.cds.common.DelegatedCredentialAuditRecord[] searchDelegatedCredentialAuditLog(org.cagrid.gaards.cds.common.DelegatedCredentialAuditFilter f) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault, org.cagrid.gaards.cds.stubs.types.DelegationFault ;

  /**
   * This method allows and administrator to delete a delegated credential.
   *
   * @param id
   * @throws CDSInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void deleteDelegatedCredential(org.cagrid.gaards.cds.common.DelegationIdentifier id) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows one to add and administrator.
   *
   * @param gridIdentity
   * @throws CDSInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void addAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows one to remove an administrator.
   *
   * @param gridIdentity
   * @throws CDSInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void removeAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

  /**
   * This method obtains the list of administrators for the CDS.
   *
   * @throws CDSInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public java.lang.String[] getAdmins() throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault ;

}

