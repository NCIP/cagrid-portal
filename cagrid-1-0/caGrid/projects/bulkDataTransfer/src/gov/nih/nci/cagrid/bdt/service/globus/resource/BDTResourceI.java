package gov.nih.nci.cagrid.bdt.service.globus.resource;

import java.rmi.RemoteException;

import org.globus.transfer.AnyXmlType;
import org.globus.ws.enumeration.EnumIterator;

public interface BDTResourceI {
	
	public EnumIterator createEnumeration() throws BDTException;
	
	public AnyXmlType get() throws BDTException;
	
	public org.apache.axis.types.URI[] getGridFTPURLs() throws BDTException ;
}
