package gov.nih.nci.cagrid.enumeration.service.globus;

import gov.nih.nci.cagrid.enumeration.service.CaGridEnumerationImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the FederatedQueryProcessorImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class CaGridEnumerationProviderImpl{
	
	CaGridEnumerationImpl impl;
	
	public CaGridEnumerationProviderImpl() throws RemoteException {
		impl = new CaGridEnumerationImpl();
	}
	

}
