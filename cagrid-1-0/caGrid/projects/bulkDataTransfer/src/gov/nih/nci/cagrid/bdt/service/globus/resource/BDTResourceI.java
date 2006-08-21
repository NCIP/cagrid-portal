package gov.nih.nci.cagrid.bdt.service.globus.resource;

import java.rmi.RemoteException;

import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerationContextType;

import transfer.AnyXmlType;

public interface BDTResourceI {
	
	public EnumerationContextType createEnumeration() throws RemoteException;
	
	public AnyXmlType get() throws RemoteException ;

}
