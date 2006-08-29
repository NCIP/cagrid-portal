package gov.nih.nci.cagrid.bdt.service.globus.resource;

import org.globus.transfer.AnyXmlType;
import org.globus.ws.enumeration.EnumIterator;

public interface BDTResourceI {
	
	public EnumIterator createEnumeration() throws BDTException;
	
	public AnyXmlType get() throws BDTException;
}
