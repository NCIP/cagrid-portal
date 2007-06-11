package gov.nih.nci.cagrid.dorian.service.globus;


import java.rmi.RemoteException;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;

import org.globus.wsrf.impl.security.authorization.exceptions.AuthorizationException;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.impl.security.authorization.exceptions.InvalidPolicyException;
import org.globus.wsrf.security.authorization.PDP;
import org.globus.wsrf.security.authorization.PDPConfig;
import org.w3c.dom.Node;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This is a PDP for use with the globus authorization callout.
 * This class will have a authorize<methodName> method for each method on this grid service.
 * The method is responsibe for making any authorization callouts required to satisfy the 
 * authorization requirements placed on each method call.  Each method will either return
 * apon a successful authorization or will throw an exception apon a failed authorization.
 * 
 * @created by Introduce Toolkit version 1.1
 * 
 */
public class DorianAuthorization implements PDP {

	public static final String SERVICE_NAMESPACE = "http://cagrid.nci.nih.gov/Dorian";
	
	
	public DorianAuthorization() {
	}
	
	protected String getServiceNamespace(){
		return SERVICE_NAMESPACE;
	}
	
	public static String getCallerIdentity() {
		String caller = org.globus.wsrf.security.SecurityManager.getManager().getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			return null;
		} else {
			return caller;
		}
	}
					
	public static void authorizeGetServiceSecurityMetadata() throws RemoteException {
		
		
	}
					
	public static void authorizeRegisterWithIdP() throws RemoteException {
		
		
	}
					
	public static void authorizeFindIdPUsers() throws RemoteException {
		
		
	}
					
	public static void authorizeUpdateIdPUser() throws RemoteException {
		
		
	}
					
	public static void authorizeRemoveIdPUser() throws RemoteException {
		
		
	}
					
	public static void authorizeAuthenticateWithIdP() throws RemoteException {
		
		
	}
					
	public static void authorizeCreateProxy() throws RemoteException {
		
		
	}
					
	public static void authorizeGetCACertificate() throws RemoteException {
		
		
	}
					
	public static void authorizeGetTrustedIdPs() throws RemoteException {
		
		
	}
					
	public static void authorizeAddTrustedIdP() throws RemoteException {
		
		
	}
					
	public static void authorizeUpdateTrustedIdP() throws RemoteException {
		
		
	}
					
	public static void authorizeRemoveTrustedIdP() throws RemoteException {
		
		
	}
					
	public static void authorizeFindIFSUsers() throws RemoteException {
		
		
	}
					
	public static void authorizeUpdateIFSUser() throws RemoteException {
		
		
	}
					
	public static void authorizeRemoveIFSUser() throws RemoteException {
		
		
	}
					
	public static void authorizeRenewIFSUserCredentials() throws RemoteException {
		
		
	}
					
	public static void authorizeGetIFSUserPolicies() throws RemoteException {
		
		
	}
					
	public static void authorizeAuthenticate() throws RemoteException {
		
		
	}
					
	public static void authorizeAddAdmin() throws RemoteException {
		
		
	}
					
	public static void authorizeRemoveAdmin() throws RemoteException {
		
		
	}
					
	public static void authorizeGetAdmins() throws RemoteException {
		
		
	}
					
	public static void authorizeRequestHostCertificate() throws RemoteException {
		
		
	}
					
	public static void authorizeGetOwnedHostCertificates() throws RemoteException {
		
		
	}
					
	public static void authorizeApproveHostCertificate() throws RemoteException {
		
		
	}
					
	public static void authorizeFindHostCertificates() throws RemoteException {
		
		
	}
					
	public static void authorizeUpdateHostCertificateRecord() throws RemoteException {
		
		
	}
					
	public static void authorizeRenewHostCertificate() throws RemoteException {
		
		
	}
	
	
	public boolean isPermitted(Subject peerSubject, MessageContext context, QName operation)
		throws AuthorizationException {
		
		if(!operation.getNamespaceURI().equals(getServiceNamespace())){
		  return false;
		}
		if(operation.getLocalPart().equals("getServiceSecurityMetadata")){
			try{
				authorizeGetServiceSecurityMetadata();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("registerWithIdP")){
			try{
				authorizeRegisterWithIdP();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("findIdPUsers")){
			try{
				authorizeFindIdPUsers();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("updateIdPUser")){
			try{
				authorizeUpdateIdPUser();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("removeIdPUser")){
			try{
				authorizeRemoveIdPUser();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("authenticateWithIdP")){
			try{
				authorizeAuthenticateWithIdP();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("createProxy")){
			try{
				authorizeCreateProxy();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("getCACertificate")){
			try{
				authorizeGetCACertificate();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("getTrustedIdPs")){
			try{
				authorizeGetTrustedIdPs();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("addTrustedIdP")){
			try{
				authorizeAddTrustedIdP();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("updateTrustedIdP")){
			try{
				authorizeUpdateTrustedIdP();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("removeTrustedIdP")){
			try{
				authorizeRemoveTrustedIdP();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("findIFSUsers")){
			try{
				authorizeFindIFSUsers();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("updateIFSUser")){
			try{
				authorizeUpdateIFSUser();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("removeIFSUser")){
			try{
				authorizeRemoveIFSUser();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("renewIFSUserCredentials")){
			try{
				authorizeRenewIFSUserCredentials();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("getIFSUserPolicies")){
			try{
				authorizeGetIFSUserPolicies();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("authenticate")){
			try{
				authorizeAuthenticate();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("addAdmin")){
			try{
				authorizeAddAdmin();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("removeAdmin")){
			try{
				authorizeRemoveAdmin();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("getAdmins")){
			try{
				authorizeGetAdmins();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("requestHostCertificate")){
			try{
				authorizeRequestHostCertificate();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("getOwnedHostCertificates")){
			try{
				authorizeGetOwnedHostCertificates();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("approveHostCertificate")){
			try{
				authorizeApproveHostCertificate();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("findHostCertificates")){
			try{
				authorizeFindHostCertificates();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("updateHostCertificateRecord")){
			try{
				authorizeUpdateHostCertificateRecord();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} else if(operation.getLocalPart().equals("renewHostCertificate")){
			try{
				authorizeRenewHostCertificate();
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		} 		
		return false;
	}
	

	public Node getPolicy(Node query) throws InvalidPolicyException {
		return null;
	}


	public String[] getPolicyNames() {
		return null;
	}


	public Node setPolicy(Node policy) throws InvalidPolicyException {
		return null;
	}


	public void close() throws CloseException {


	}


	public void initialize(PDPConfig config, String name, String id) throws InitializeException {

	}
	
	
}
