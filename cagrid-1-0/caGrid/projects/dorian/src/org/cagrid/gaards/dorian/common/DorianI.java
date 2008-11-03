package org.cagrid.gaards.dorian.common;

import java.rmi.RemoteException;

import org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserCertificateFault;

/**
 * Dorian Grid Service
 *
 * This class is autogenerated, DO NOT EDIT.
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public interface DorianI {

    public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException ;

  /**
   * This method allows an administrator to perform on audit on Grid/Federation related transactions.
   *
   * @param f
   * @throws DorianInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.dorian.federation.FederationAuditRecord[] performFederationAudit(org.cagrid.gaards.dorian.federation.FederationAuditFilter f) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Submits an application for an account with a the Dorian Identity Provider.
   *
   * @param application
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws InvalidUserPropertyFault
   *	Invalid application submitted.
   */
  public java.lang.String registerWithIdP(org.cagrid.gaards.dorian.idp.Application application) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault ;

  /**
   * Obtain a list of users accounts within the Dorian IdP, meeting a specified search criteria.
   *
   * @param f
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public org.cagrid.gaards.dorian.idp.LocalUser[] findLocalUsers(org.cagrid.gaards.dorian.idp.LocalUserFilter f) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Update the account information for a Dorian IdP user.
   *
   * @param user
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws NoSuchUserFault
   *	The user specified does not exist.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public void updateLocalUser(org.cagrid.gaards.dorian.idp.LocalUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.NoSuchUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Remove an IdP user account.
   *
   * @param userId
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public void removeLocalUser(java.lang.String userId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Create a proxy certificate.  (This method is DEPRECATED, please use requestUserCertificate)
   *
   * @param saml
   * @param publicKey
   * @param lifetime
   * @param delegation
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws InvalidAssertionFault
   *	Invalid SAML Assertions provided.
   * @throws InvalidProxyFault
   *	Invalid proxy error.
   * @throws UserPolicyFault
   *	The IdP's user policy reported an error.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public org.cagrid.gaards.dorian.X509Certificate[] createProxy(org.cagrid.gaards.dorian.SAMLAssertion saml,org.cagrid.gaards.dorian.federation.PublicKey publicKey,org.cagrid.gaards.dorian.federation.ProxyLifetime lifetime,org.cagrid.gaards.dorian.federation.DelegationPathLength delegation) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault, org.cagrid.gaards.dorian.stubs.types.InvalidProxyFault, org.cagrid.gaards.dorian.stubs.types.UserPolicyFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Obtains the Dorian CA certificate.
   *
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   */
  public org.cagrid.gaards.dorian.X509Certificate getCACertificate() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault ;

  /**
   * Gets a list of the IdPs trusted by Dorian.
   *
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public org.cagrid.gaards.dorian.federation.TrustedIdP[] getTrustedIdPs() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Add a Trusted Identity Provider to Dorian.
   *
   * @param idp
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws InvalidTrustedIdPFault
   *	Invalid Trusted Identity Provider specified.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public org.cagrid.gaards.dorian.federation.TrustedIdP addTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP idp) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Update the information for a Trusted IdP.
   *
   * @param trustedIdP
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws InvalidTrustedIdPFault
   *	Invalid Trusted Identity Provider specified.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public void updateTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP trustedIdP) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Remove a TrustedIdP.
   *
   * @param trustedIdP
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws InvalidTrustedIdPFault
   *	Invalid Trusted Identity Provider specified.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public void removeTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP trustedIdP) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Obtain a list of grid users accounts within the Dorian, meeting a specified search criteria.
   *
   * @param filter
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public org.cagrid.gaards.dorian.federation.GridUser[] findGridUsers(org.cagrid.gaards.dorian.federation.GridUserFilter filter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows an admin to update the account of a grid user.
   *
   * @param user
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws InvalidUserFault
   *	Invalid user specified.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public void updateGridUser(org.cagrid.gaards.dorian.federation.GridUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Removes a grid user account.
   *
   * @param user
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws InvalidUserFault
   *	Invalid user specified.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public void removeGridUser(org.cagrid.gaards.dorian.federation.GridUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Get a list of the Trusted IdP user policies supported by Dorian.
   *
   * @throws DorianInternalFault
   *	An unexpected internal Dorian error.
   * @throws PermissionDeniedFault
   *	Client does not have permission to perform the request.
   */
  public org.cagrid.gaards.dorian.federation.GridUserPolicy[] getGridUserPolicies() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * Authentication Service interface for authenticating with the Dorian IdP.
   *
   * @param credential
   * @throws InvalidCredentialFault
   *	An unexpected internal Dorian error.
   * @throws InsufficientAttributeFault
   *	The DorianIdP could obtain the attributes needed to issue a valid SAML assertion.
   * @throws AuthenticationProviderFault
   *	An error occurred in authenticating.
   */
  public gov.nih.nci.cagrid.authentication.bean.SAMLAssertion authenticate(gov.nih.nci.cagrid.authentication.bean.Credential credential) throws RemoteException, gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault, gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault, gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault ;

  /**
   * Authentication Service interface for authenticating with the Dorian IdP.
   *
   * @param credential
   * @throws AuthenticationProviderFault
   *	
   * @throws CredentialNotSupportedFault
   *	
   * @throws InsufficientAttributeFault
   *	
   * @throws InvalidCredentialFault
   *	
   */
  public gov.nih.nci.cagrid.opensaml.SAMLAssertion authenticateUser(org.cagrid.gaards.authentication.Credential credential) throws RemoteException, org.cagrid.gaards.authentication.faults.AuthenticationProviderFault, org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault, org.cagrid.gaards.authentication.faults.InsufficientAttributeFault, org.cagrid.gaards.authentication.faults.InvalidCredentialFault ;

  /**
   * This method allows an admin to grant user admin privileges to Dorian.
   *
   * @param gridIdentity
   * @throws DorianInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void addAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows and admin to revoke a user's administrative rights to Dorian.
   *
   * @param gridIdentity
   * @throws DorianInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void removeAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method returns a list for users with administrative access to Dorian.
   *
   * @throws DorianInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public java.lang.String[] getAdmins() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows a user to request a host certificate.
   *
   * @param req
   * @throws DorianInternalFault
   *	
   * @throws InvalidHostCertificateRequestFault
   *	
   * @throws InvalidHostCertificateFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.dorian.federation.HostCertificateRecord requestHostCertificate(org.cagrid.gaards.dorian.federation.HostCertificateRequest req) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateRequestFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method returns the list of host certificates that are owned by the caller.
   *
   * @throws DorianInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.dorian.federation.HostCertificateRecord[] getOwnedHostCertificates() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows an administrator to approve a host certificate request.
   *
   * @param recordId
   * @throws DorianInternalFault
   *	
   * @throws InvalidHostCertificateFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.dorian.federation.HostCertificateRecord approveHostCertificate(java.math.BigInteger recordId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows one to search for host certificates issued by the Dorian CA.
   *
   * @param hostCertificateFilter
   * @throws DorianInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.dorian.federation.HostCertificateRecord[] findHostCertificates(org.cagrid.gaards.dorian.federation.HostCertificateFilter hostCertificateFilter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows and admin to update a host certificate record.
   *
   * @param hostCertificateUpdate
   * @throws DorianInternalFault
   *	
   * @throws InvalidHostCertificateFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void updateHostCertificateRecord(org.cagrid.gaards.dorian.federation.HostCertificateUpdate hostCertificateUpdate) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows a user to renew a host certificate that was issued to them.
   *
   * @param recordId
   * @throws DorianInternalFault
   *	
   * @throws InvalidHostCertificateFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.dorian.federation.HostCertificateRecord renewHostCertificate(java.math.BigInteger recordId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows a user with a Dorian IdP account to change their password. (This method is DEPRECATED, please use changeLocalUserPassword)
   *
   * @param credential
   * @param newPassword
   * @throws DorianInternalFault
   *	
   * @throws PermissionDeniedFault
   *	
   * @throws InvalidUserPropertyFault
   *	
   */
  public void changeIdPUserPassword(org.cagrid.gaards.dorian.idp.BasicAuthCredential credential,java.lang.String newPassword) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault ;

  /**
   * This method determines whether or not a Local user exists.
   *
   * @param userId
   * @throws DorianInternalFault
   *	
   */
  public boolean doesLocalUserExist(java.lang.String userId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault ;

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException ;

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException ;

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException ;

  /**
   * This method allows a user with a Dorian IdP account to change their password.
   *
   * @param credential
   * @param newPassword
   * @throws DorianInternalFault
   *	
   * @throws InvalidUserPropertyFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void changeLocalUserPassword(org.cagrid.gaards.authentication.BasicAuthentication credential,java.lang.String newPassword) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows an administrator to update a user certificate record.
   *
   * @param update
   * @throws DorianInternalFault
   *	
   * @throws InvalidUserCertificateFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void updateUserCertificate(org.cagrid.gaards.dorian.federation.UserCertificateUpdate update) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allow a user to request a short term user certificate.
   *
   * @param saml
   * @param key
   * @param lifetime
   * @throws DorianInternalFault
   *	
   * @throws InvalidAssertionFault
   *	
   * @throws PermissionDeniedFault
   *	
   * @throws UserPolicyFault
   *	
   */
  public org.cagrid.gaards.dorian.X509Certificate requestUserCertificate(gov.nih.nci.cagrid.opensaml.SAMLAssertion saml,org.cagrid.gaards.dorian.federation.PublicKey key,org.cagrid.gaards.dorian.federation.CertificateLifetime lifetime) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault, org.cagrid.gaards.dorian.stubs.types.UserPolicyFault ;

  /**
   * This method allows an admin to search for user certificates issued by the Dorian CA.
   *
   * @param userCertificateFilter
   * @throws DorianInternalFault
   *	
   * @throws InvalidUserCertificateFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public org.cagrid.gaards.dorian.federation.UserCertificateRecord[] findUserCertificates(org.cagrid.gaards.dorian.federation.UserCertificateFilter userCertificateFilter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

  /**
   * This method allows an administrator to remove a user certificate.
   *
   * @param serialNumber
   * @throws DorianInternalFault
   *	
   * @throws InvalidUserCertificateFault
   *	
   * @throws PermissionDeniedFault
   *	
   */
  public void removeUserCertificate(java.lang.String serialNumber) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault ;

}

