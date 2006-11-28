package gov.nih.nci.cagrid.cabio.service.globus;


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
public class CaBIOSvcAuthorization{
	
	
	public CaBIOSvcAuthorization() {
	}
	
	public static String getCallerIdentity() {
		String caller = org.globus.wsrf.security.SecurityManager.getManager().getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			return null;
		} else {
			return caller;
		}
	}
	
	
	
}
