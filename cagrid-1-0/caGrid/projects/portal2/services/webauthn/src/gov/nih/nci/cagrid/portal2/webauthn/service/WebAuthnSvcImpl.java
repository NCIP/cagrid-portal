package gov.nih.nci.cagrid.portal2.webauthn.service;

import gov.nih.nci.cagrid.portal2.webauthn.common.WebAuthnSvcI;

import java.rmi.RemoteException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.1
 * 
 */
public class WebAuthnSvcImpl extends WebAuthnSvcImplBase {

	private WebAuthnSvcI svc;
	
	public WebAuthnSvcImpl() throws RemoteException {
		super();
		try{
			String[] contexts = ServiceConfiguration.getConfiguration().getApplicationContexts().split(",");
			ApplicationContext ctx = new ClassPathXmlApplicationContext(contexts);
			this.svc = (WebAuthnSvcI)ctx.getBean("webAuthnSvc");
		}catch(Exception ex){
			throw new RuntimeException("Error initializing: " + ex.getMessage(), ex);
		}
	}
	
  public gov.nih.nci.cagrid.portal2.webauthn.types.UserInfoType getUserInfo(java.lang.String loginKey) throws RemoteException, gov.nih.nci.cagrid.portal2.webauthn.types.faults.InvalidKeyFault {
	  return this.svc.getUserInfo(loginKey);
  }

  public java.lang.String createLoginKeyForLocalUser(gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.Credential credential) throws RemoteException, gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.InvalidCredentialFault, gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.InsufficientAttributeFault, gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.AuthenticationProviderFault, gov.nih.nci.cagrid.portal2.webauthn.types.faults.WebAuthnSvcFault {
    return this.svc.createLoginKeyForLocalUser(credential);
  }

  public java.lang.String createLoginKeyForGridUser(java.lang.String idpUrl,gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.Credential credential) throws RemoteException, gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.InvalidCredentialFault, gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.InsufficientAttributeFault, gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.AuthenticationProviderFault, gov.nih.nci.cagrid.portal2.webauthn.types.faults.WebAuthnSvcFault {
	  return this.svc.createLoginKeyForGridUser(idpUrl, credential);
  }

}

