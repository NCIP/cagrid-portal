package org.cagrid.gaards.dorian.client;

import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.axis.utils.ClassUtils;
import org.cagrid.gaards.dorian.common.DorianI;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.cagrid.gaards.dorian.stubs.DorianPortType;
import org.cagrid.gaards.dorian.stubs.service.DorianServiceAddressingLocator;
import org.globus.gsi.GlobusCredential;

/**
 * This class is autogenerated, DO NOT EDIT.
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class DorianClient extends ServiceSecurityClient implements DorianI {
    protected DorianPortType portType;
    private Object portTypeMutex;

    public DorianClient(String url) throws MalformedURIException, RemoteException {
        this(url, null);
    }

    public DorianClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
        super(url, proxy);
        initialize();
    }

    public DorianClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
        this(epr, null);
    }

    public DorianClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException,
        RemoteException {
        super(epr, proxy);
        initialize();
    }

    private void initialize() throws RemoteException {
        this.portTypeMutex = new Object();
        this.portType = createPortType();
    }

    private DorianPortType createPortType() throws RemoteException {

        DorianServiceAddressingLocator locator = new DorianServiceAddressingLocator();
        // attempt to load our context sensitive wsdd file
        InputStream resourceAsStream = ClassUtils.getResourceAsStream(getClass(), "client-config.wsdd");
        if (resourceAsStream != null) {
            // we found it, so tell axis to configure an engine to use it
            EngineConfiguration engineConfig = new FileProvider(resourceAsStream);
            // set the engine of the locator
            locator.setEngine(new AxisClient(engineConfig));
        }
        DorianPortType port = null;
        try {
            port = locator.getDorianPortTypePort(getEndpointReference());
        } catch (Exception e) {
            throw new RemoteException("Unable to locate portType:" + e.getMessage(), e);
        }

        return port;
    }

    public static void usage() {
        System.out.println(DorianClient.class.getName() + " -url <service url>");
    }

    public static void main(String[] args) {
        System.out.println("Running the Grid Service Client");
        try {

            GridUserClient client = new GridUserClient("https://localhost:8443/wsrf/services/cagrid/Dorian");
            List<TrustedIdentityProvider> idps =client.getTrustedIdentityProviders();
            for(int i=0; i<idps.size(); i++){
                System.out.println(idps.get(i).getDisplayName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getServiceSecurityMetadata");
    gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest params = new gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest();
    gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse boxedResult = portType.getServiceSecurityMetadata(params);
    return boxedResult.getServiceSecurityMetadata();
    }
  }

  public java.lang.String registerWithIdP(org.cagrid.gaards.dorian.idp.Application application) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"registerWithIdP");
    org.cagrid.gaards.dorian.stubs.RegisterWithIdPRequest params = new org.cagrid.gaards.dorian.stubs.RegisterWithIdPRequest();
    org.cagrid.gaards.dorian.stubs.RegisterWithIdPRequestApplication applicationContainer = new org.cagrid.gaards.dorian.stubs.RegisterWithIdPRequestApplication();
    applicationContainer.setApplication(application);
    params.setApplication(applicationContainer);
    org.cagrid.gaards.dorian.stubs.RegisterWithIdPResponse boxedResult = portType.registerWithIdP(params);
    return boxedResult.getResponse();
    }
  }

  public org.cagrid.gaards.dorian.idp.IdPUser[] findIdPUsers(org.cagrid.gaards.dorian.idp.IdPUserFilter filter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"findIdPUsers");
    org.cagrid.gaards.dorian.stubs.FindIdPUsersRequest params = new org.cagrid.gaards.dorian.stubs.FindIdPUsersRequest();
    org.cagrid.gaards.dorian.stubs.FindIdPUsersRequestFilter filterContainer = new org.cagrid.gaards.dorian.stubs.FindIdPUsersRequestFilter();
    filterContainer.setIdPUserFilter(filter);
    params.setFilter(filterContainer);
    org.cagrid.gaards.dorian.stubs.FindIdPUsersResponse boxedResult = portType.findIdPUsers(params);
    return boxedResult.getIdPUser();
    }
  }

  public void updateIdPUser(org.cagrid.gaards.dorian.idp.IdPUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.NoSuchUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"updateIdPUser");
    org.cagrid.gaards.dorian.stubs.UpdateIdPUserRequest params = new org.cagrid.gaards.dorian.stubs.UpdateIdPUserRequest();
    org.cagrid.gaards.dorian.stubs.UpdateIdPUserRequestUser userContainer = new org.cagrid.gaards.dorian.stubs.UpdateIdPUserRequestUser();
    userContainer.setIdPUser(user);
    params.setUser(userContainer);
    org.cagrid.gaards.dorian.stubs.UpdateIdPUserResponse boxedResult = portType.updateIdPUser(params);
    }
  }

  public void removeIdPUser(java.lang.String userId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"removeIdPUser");
    org.cagrid.gaards.dorian.stubs.RemoveIdPUserRequest params = new org.cagrid.gaards.dorian.stubs.RemoveIdPUserRequest();
    params.setUserId(userId);
    org.cagrid.gaards.dorian.stubs.RemoveIdPUserResponse boxedResult = portType.removeIdPUser(params);
    }
  }

  public org.cagrid.gaards.dorian.X509Certificate[] createProxy(org.cagrid.gaards.dorian.SAMLAssertion saml,org.cagrid.gaards.dorian.federation.PublicKey publicKey,org.cagrid.gaards.dorian.federation.ProxyLifetime lifetime,org.cagrid.gaards.dorian.federation.DelegationPathLength delegation) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault, org.cagrid.gaards.dorian.stubs.types.InvalidProxyFault, org.cagrid.gaards.dorian.stubs.types.UserPolicyFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"createProxy");
    org.cagrid.gaards.dorian.stubs.CreateProxyRequest params = new org.cagrid.gaards.dorian.stubs.CreateProxyRequest();
    org.cagrid.gaards.dorian.stubs.CreateProxyRequestSaml samlContainer = new org.cagrid.gaards.dorian.stubs.CreateProxyRequestSaml();
    samlContainer.setSAMLAssertion(saml);
    params.setSaml(samlContainer);
    org.cagrid.gaards.dorian.stubs.CreateProxyRequestPublicKey publicKeyContainer = new org.cagrid.gaards.dorian.stubs.CreateProxyRequestPublicKey();
    publicKeyContainer.setPublicKey(publicKey);
    params.setPublicKey(publicKeyContainer);
    org.cagrid.gaards.dorian.stubs.CreateProxyRequestLifetime lifetimeContainer = new org.cagrid.gaards.dorian.stubs.CreateProxyRequestLifetime();
    lifetimeContainer.setProxyLifetime(lifetime);
    params.setLifetime(lifetimeContainer);
    org.cagrid.gaards.dorian.stubs.CreateProxyRequestDelegation delegationContainer = new org.cagrid.gaards.dorian.stubs.CreateProxyRequestDelegation();
    delegationContainer.setDelegationPathLength(delegation);
    params.setDelegation(delegationContainer);
    org.cagrid.gaards.dorian.stubs.CreateProxyResponse boxedResult = portType.createProxy(params);
    return boxedResult.getX509Certificate();
    }
  }

  public org.cagrid.gaards.dorian.X509Certificate getCACertificate() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getCACertificate");
    org.cagrid.gaards.dorian.stubs.GetCACertificateRequest params = new org.cagrid.gaards.dorian.stubs.GetCACertificateRequest();
    org.cagrid.gaards.dorian.stubs.GetCACertificateResponse boxedResult = portType.getCACertificate(params);
    return boxedResult.getX509Certificate();
    }
  }

  public org.cagrid.gaards.dorian.federation.TrustedIdP[] getTrustedIdPs() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getTrustedIdPs");
    org.cagrid.gaards.dorian.stubs.GetTrustedIdPsRequest params = new org.cagrid.gaards.dorian.stubs.GetTrustedIdPsRequest();
    org.cagrid.gaards.dorian.stubs.GetTrustedIdPsResponse boxedResult = portType.getTrustedIdPs(params);
    return boxedResult.getTrustedIdP();
    }
  }

  public org.cagrid.gaards.dorian.federation.TrustedIdP addTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP idp) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"addTrustedIdP");
    org.cagrid.gaards.dorian.stubs.AddTrustedIdPRequest params = new org.cagrid.gaards.dorian.stubs.AddTrustedIdPRequest();
    org.cagrid.gaards.dorian.stubs.AddTrustedIdPRequestIdp idpContainer = new org.cagrid.gaards.dorian.stubs.AddTrustedIdPRequestIdp();
    idpContainer.setTrustedIdP(idp);
    params.setIdp(idpContainer);
    org.cagrid.gaards.dorian.stubs.AddTrustedIdPResponse boxedResult = portType.addTrustedIdP(params);
    return boxedResult.getTrustedIdP();
    }
  }

  public void updateTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP trustedIdP) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"updateTrustedIdP");
    org.cagrid.gaards.dorian.stubs.UpdateTrustedIdPRequest params = new org.cagrid.gaards.dorian.stubs.UpdateTrustedIdPRequest();
    org.cagrid.gaards.dorian.stubs.UpdateTrustedIdPRequestTrustedIdP trustedIdPContainer = new org.cagrid.gaards.dorian.stubs.UpdateTrustedIdPRequestTrustedIdP();
    trustedIdPContainer.setTrustedIdP(trustedIdP);
    params.setTrustedIdP(trustedIdPContainer);
    org.cagrid.gaards.dorian.stubs.UpdateTrustedIdPResponse boxedResult = portType.updateTrustedIdP(params);
    }
  }

  public void removeTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP trustedIdP) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"removeTrustedIdP");
    org.cagrid.gaards.dorian.stubs.RemoveTrustedIdPRequest params = new org.cagrid.gaards.dorian.stubs.RemoveTrustedIdPRequest();
    org.cagrid.gaards.dorian.stubs.RemoveTrustedIdPRequestTrustedIdP trustedIdPContainer = new org.cagrid.gaards.dorian.stubs.RemoveTrustedIdPRequestTrustedIdP();
    trustedIdPContainer.setTrustedIdP(trustedIdP);
    params.setTrustedIdP(trustedIdPContainer);
    org.cagrid.gaards.dorian.stubs.RemoveTrustedIdPResponse boxedResult = portType.removeTrustedIdP(params);
    }
  }

  public org.cagrid.gaards.dorian.federation.GridUser[] findGridUsers(org.cagrid.gaards.dorian.federation.GridUserFilter filter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"findGridUsers");
    org.cagrid.gaards.dorian.stubs.FindGridUsersRequest params = new org.cagrid.gaards.dorian.stubs.FindGridUsersRequest();
    org.cagrid.gaards.dorian.stubs.FindGridUsersRequestFilter filterContainer = new org.cagrid.gaards.dorian.stubs.FindGridUsersRequestFilter();
    filterContainer.setGridUserFilter(filter);
    params.setFilter(filterContainer);
    org.cagrid.gaards.dorian.stubs.FindGridUsersResponse boxedResult = portType.findGridUsers(params);
    return boxedResult.getGridUser();
    }
  }

  public void updateGridUser(org.cagrid.gaards.dorian.federation.GridUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"updateGridUser");
    org.cagrid.gaards.dorian.stubs.UpdateGridUserRequest params = new org.cagrid.gaards.dorian.stubs.UpdateGridUserRequest();
    org.cagrid.gaards.dorian.stubs.UpdateGridUserRequestUser userContainer = new org.cagrid.gaards.dorian.stubs.UpdateGridUserRequestUser();
    userContainer.setGridUser(user);
    params.setUser(userContainer);
    org.cagrid.gaards.dorian.stubs.UpdateGridUserResponse boxedResult = portType.updateGridUser(params);
    }
  }

  public void removeGridUser(org.cagrid.gaards.dorian.federation.GridUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"removeGridUser");
    org.cagrid.gaards.dorian.stubs.RemoveGridUserRequest params = new org.cagrid.gaards.dorian.stubs.RemoveGridUserRequest();
    org.cagrid.gaards.dorian.stubs.RemoveGridUserRequestUser userContainer = new org.cagrid.gaards.dorian.stubs.RemoveGridUserRequestUser();
    userContainer.setGridUser(user);
    params.setUser(userContainer);
    org.cagrid.gaards.dorian.stubs.RemoveGridUserResponse boxedResult = portType.removeGridUser(params);
    }
  }

  public org.cagrid.gaards.dorian.federation.GridUser renewGridUserCredentials(org.cagrid.gaards.dorian.federation.GridUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"renewGridUserCredentials");
    org.cagrid.gaards.dorian.stubs.RenewGridUserCredentialsRequest params = new org.cagrid.gaards.dorian.stubs.RenewGridUserCredentialsRequest();
    org.cagrid.gaards.dorian.stubs.RenewGridUserCredentialsRequestUser userContainer = new org.cagrid.gaards.dorian.stubs.RenewGridUserCredentialsRequestUser();
    userContainer.setGridUser(user);
    params.setUser(userContainer);
    org.cagrid.gaards.dorian.stubs.RenewGridUserCredentialsResponse boxedResult = portType.renewGridUserCredentials(params);
    return boxedResult.getGridUser();
    }
  }

  public org.cagrid.gaards.dorian.federation.GridUserPolicy[] getGridUserPolicies() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getGridUserPolicies");
    org.cagrid.gaards.dorian.stubs.GetGridUserPoliciesRequest params = new org.cagrid.gaards.dorian.stubs.GetGridUserPoliciesRequest();
    org.cagrid.gaards.dorian.stubs.GetGridUserPoliciesResponse boxedResult = portType.getGridUserPolicies(params);
    return boxedResult.getGridUserPolicy();
    }
  }

  public gov.nih.nci.cagrid.authentication.bean.SAMLAssertion authenticate(gov.nih.nci.cagrid.authentication.bean.Credential credential) throws RemoteException, gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault, gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault, gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"authenticate");
    gov.nih.nci.cagrid.authentication.AuthenticateRequest params = new gov.nih.nci.cagrid.authentication.AuthenticateRequest();
    gov.nih.nci.cagrid.authentication.AuthenticateRequestCredential credentialContainer = new gov.nih.nci.cagrid.authentication.AuthenticateRequestCredential();
    credentialContainer.setCredential(credential);
    params.setCredential(credentialContainer);
    gov.nih.nci.cagrid.authentication.AuthenticateResponse boxedResult = portType.authenticate(params);
    return boxedResult.getSAMLAssertion();
    }
  }

  public gov.nih.nci.cagrid.opensaml.SAMLAssertion authenticateUser(org.cagrid.gaards.authentication.Credential credential) throws RemoteException, org.cagrid.gaards.authentication.faults.AuthenticationProviderFault, org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault, org.cagrid.gaards.authentication.faults.InsufficientAttributeFault, org.cagrid.gaards.authentication.faults.InvalidCredentialFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"authenticateUser");
    org.cagrid.gaards.authentication.AuthenticateUserRequest params = new org.cagrid.gaards.authentication.AuthenticateUserRequest();
    org.cagrid.gaards.authentication.AuthenticateUserRequestCredential credentialContainer = new org.cagrid.gaards.authentication.AuthenticateUserRequestCredential();
    credentialContainer.setCredential(credential);
    params.setCredential(credentialContainer);
    org.cagrid.gaards.authentication.AuthenticateUserResponse boxedResult = portType.authenticateUser(params);
    return boxedResult.getAssertion();
    }
  }

  public void addAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"addAdmin");
    org.cagrid.gaards.dorian.stubs.AddAdminRequest params = new org.cagrid.gaards.dorian.stubs.AddAdminRequest();
    params.setGridIdentity(gridIdentity);
    org.cagrid.gaards.dorian.stubs.AddAdminResponse boxedResult = portType.addAdmin(params);
    }
  }

  public void removeAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"removeAdmin");
    org.cagrid.gaards.dorian.stubs.RemoveAdminRequest params = new org.cagrid.gaards.dorian.stubs.RemoveAdminRequest();
    params.setGridIdentity(gridIdentity);
    org.cagrid.gaards.dorian.stubs.RemoveAdminResponse boxedResult = portType.removeAdmin(params);
    }
  }

  public java.lang.String[] getAdmins() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getAdmins");
    org.cagrid.gaards.dorian.stubs.GetAdminsRequest params = new org.cagrid.gaards.dorian.stubs.GetAdminsRequest();
    org.cagrid.gaards.dorian.stubs.GetAdminsResponse boxedResult = portType.getAdmins(params);
    return boxedResult.getResponse();
    }
  }

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord requestHostCertificate(org.cagrid.gaards.dorian.federation.HostCertificateRequest req) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateRequestFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"requestHostCertificate");
    org.cagrid.gaards.dorian.stubs.RequestHostCertificateRequest params = new org.cagrid.gaards.dorian.stubs.RequestHostCertificateRequest();
    org.cagrid.gaards.dorian.stubs.RequestHostCertificateRequestReq reqContainer = new org.cagrid.gaards.dorian.stubs.RequestHostCertificateRequestReq();
    reqContainer.setHostCertificateRequest(req);
    params.setReq(reqContainer);
    org.cagrid.gaards.dorian.stubs.RequestHostCertificateResponse boxedResult = portType.requestHostCertificate(params);
    return boxedResult.getHostCertificateRecord();
    }
  }

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord[] getOwnedHostCertificates() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getOwnedHostCertificates");
    org.cagrid.gaards.dorian.stubs.GetOwnedHostCertificatesRequest params = new org.cagrid.gaards.dorian.stubs.GetOwnedHostCertificatesRequest();
    org.cagrid.gaards.dorian.stubs.GetOwnedHostCertificatesResponse boxedResult = portType.getOwnedHostCertificates(params);
    return boxedResult.getHostCertificateRecord();
    }
  }

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord approveHostCertificate(java.math.BigInteger recordId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"approveHostCertificate");
    org.cagrid.gaards.dorian.stubs.ApproveHostCertificateRequest params = new org.cagrid.gaards.dorian.stubs.ApproveHostCertificateRequest();
    params.setRecordId(recordId);
    org.cagrid.gaards.dorian.stubs.ApproveHostCertificateResponse boxedResult = portType.approveHostCertificate(params);
    return boxedResult.getHostCertificateRecord();
    }
  }

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord[] findHostCertificates(org.cagrid.gaards.dorian.federation.HostCertificateFilter hostCertificateFilter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"findHostCertificates");
    org.cagrid.gaards.dorian.stubs.FindHostCertificatesRequest params = new org.cagrid.gaards.dorian.stubs.FindHostCertificatesRequest();
    org.cagrid.gaards.dorian.stubs.FindHostCertificatesRequestHostCertificateFilter hostCertificateFilterContainer = new org.cagrid.gaards.dorian.stubs.FindHostCertificatesRequestHostCertificateFilter();
    hostCertificateFilterContainer.setHostCertificateFilter(hostCertificateFilter);
    params.setHostCertificateFilter(hostCertificateFilterContainer);
    org.cagrid.gaards.dorian.stubs.FindHostCertificatesResponse boxedResult = portType.findHostCertificates(params);
    return boxedResult.getHostCertificateRecord();
    }
  }

  public void updateHostCertificateRecord(org.cagrid.gaards.dorian.federation.HostCertificateUpdate hostCertificateUpdate) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"updateHostCertificateRecord");
    org.cagrid.gaards.dorian.stubs.UpdateHostCertificateRecordRequest params = new org.cagrid.gaards.dorian.stubs.UpdateHostCertificateRecordRequest();
    org.cagrid.gaards.dorian.stubs.UpdateHostCertificateRecordRequestHostCertificateUpdate hostCertificateUpdateContainer = new org.cagrid.gaards.dorian.stubs.UpdateHostCertificateRecordRequestHostCertificateUpdate();
    hostCertificateUpdateContainer.setHostCertificateUpdate(hostCertificateUpdate);
    params.setHostCertificateUpdate(hostCertificateUpdateContainer);
    org.cagrid.gaards.dorian.stubs.UpdateHostCertificateRecordResponse boxedResult = portType.updateHostCertificateRecord(params);
    }
  }

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord renewHostCertificate(java.math.BigInteger recordId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"renewHostCertificate");
    org.cagrid.gaards.dorian.stubs.RenewHostCertificateRequest params = new org.cagrid.gaards.dorian.stubs.RenewHostCertificateRequest();
    params.setRecordId(recordId);
    org.cagrid.gaards.dorian.stubs.RenewHostCertificateResponse boxedResult = portType.renewHostCertificate(params);
    return boxedResult.getHostCertificateRecord();
    }
  }

  public void changeIdPUserPassword(org.cagrid.gaards.dorian.idp.BasicAuthCredential credential,java.lang.String newPassword) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"changeIdPUserPassword");
    org.cagrid.gaards.dorian.stubs.ChangeIdPUserPasswordRequest params = new org.cagrid.gaards.dorian.stubs.ChangeIdPUserPasswordRequest();
    org.cagrid.gaards.dorian.stubs.ChangeIdPUserPasswordRequestCredential credentialContainer = new org.cagrid.gaards.dorian.stubs.ChangeIdPUserPasswordRequestCredential();
    credentialContainer.setBasicAuthCredential(credential);
    params.setCredential(credentialContainer);
    params.setNewPassword(newPassword);
    org.cagrid.gaards.dorian.stubs.ChangeIdPUserPasswordResponse boxedResult = portType.changeIdPUserPassword(params);
    }
  }

  public boolean doesLocalUserExist(java.lang.String userId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"doesLocalUserExist");
    org.cagrid.gaards.dorian.stubs.DoesLocalUserExistRequest params = new org.cagrid.gaards.dorian.stubs.DoesLocalUserExistRequest();
    params.setUserId(userId);
    org.cagrid.gaards.dorian.stubs.DoesLocalUserExistResponse boxedResult = portType.doesLocalUserExist(params);
    return boxedResult.isResponse();
    }
  }

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getMultipleResourceProperties");
    return portType.getMultipleResourceProperties(params);
    }
  }

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getResourceProperty");
    return portType.getResourceProperty(params);
    }
  }

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"queryResourceProperties");
    return portType.queryResourceProperties(params);
    }
  }

  public void changeLocalUserPassword(org.cagrid.gaards.authentication.BasicAuthentication credential,java.lang.String newPassword) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"changeLocalUserPassword");
    org.cagrid.gaards.dorian.stubs.ChangeLocalUserPasswordRequest params = new org.cagrid.gaards.dorian.stubs.ChangeLocalUserPasswordRequest();
    org.cagrid.gaards.dorian.stubs.ChangeLocalUserPasswordRequestCredential credentialContainer = new org.cagrid.gaards.dorian.stubs.ChangeLocalUserPasswordRequestCredential();
    credentialContainer.setBasicAuthentication(credential);
    params.setCredential(credentialContainer);
    params.setNewPassword(newPassword);
    org.cagrid.gaards.dorian.stubs.ChangeLocalUserPasswordResponse boxedResult = portType.changeLocalUserPassword(params);
    }
  }

}
