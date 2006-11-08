package gov.nih.nci.cagrid.cadsr.service.globus;


import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class will have a authorize<methodName> method for each method on this grid service.
 * The method is responsibe for making any authorization callouts required to satisfy the 
 * authorization requirements placed on each method call.  Each method will either simple return
 * apon a successful authorization or will throw an exception apon a failed authorization.
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class CaDSRServiceAuthorization{
	
	
	public CaDSRServiceAuthorization() {
	}
	
	public static String getCallerIdentity() {
		String caller = org.globus.wsrf.security.SecurityManager.getManager().getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			return null;
		} else {
			return caller;
		}
	}
	
					
	public static void authorizeFindAllProjects() throws RemoteException {
		
		
	}
					
	public static void authorizeFindProjects() throws RemoteException {
		
		
	}
					
	public static void authorizeFindPackagesInProject() throws RemoteException {
		
		
	}
					
	public static void authorizeFindClassesInProject() throws RemoteException {
		
		
	}
					
	public static void authorizeFindClassesInPackage() throws RemoteException {
		
		
	}
					
	public static void authorizeGenerateDomainModelForProject() throws RemoteException {
		
		
	}
					
	public static void authorizeGenerateDomainModelForPackages() throws RemoteException {
		
		
	}
					
	public static void authorizeGenerateDomainModelForClassesWithExcludes() throws RemoteException {
		
		
	}
					
	public static void authorizeFindAttributesInClass() throws RemoteException {
		
		
	}
					
	public static void authorizeFindSemanticMetadataForClass() throws RemoteException {
		
		
	}
					
	public static void authorizeFindValueDomainForAttribute() throws RemoteException {
		
		
	}
					
	public static void authorizeFindAssociationsForClass() throws RemoteException {
		
		
	}
					
	public static void authorizeFindAssociationsInPackage() throws RemoteException {
		
		
	}
					
	public static void authorizeFindAssociationsInProject() throws RemoteException {
		
		
	}
					
	public static void authorizeGenerateDomainModelForClasses() throws RemoteException {
		
		
	}
					
	public static void authorizeAnnotateServiceMetadata() throws RemoteException {
		
		
	}
					
	public static void authorizeFindContextForProject() throws RemoteException {
		
		
	}
	
	
}
