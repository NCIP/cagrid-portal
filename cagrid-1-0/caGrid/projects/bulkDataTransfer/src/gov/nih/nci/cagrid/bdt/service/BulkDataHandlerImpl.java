package gov.nih.nci.cagrid.bdt.service;

import gov.nih.nci.cagrid.bdt.service.globus.resource.BDTResource;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.apache.axis.message.MessageElement;
import org.globus.ws.enumeration.EnumIterator;
import org.globus.ws.enumeration.EnumProvider;
import org.globus.ws.enumeration.EnumResource;
import org.globus.ws.enumeration.EnumResourceHome;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.encoding.SerializationException;
import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse;
import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerationContextType;
import org.xmlsoap.schemas.ws._2004._09.enumeration.ExpirationType;


/**
 * gov.nih.nci.cagrid.bdtI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class BulkDataHandlerImpl extends BulkDataHandlerImplBase {

	public BulkDataHandlerImpl() throws RemoteException {
		super();
	}


	public org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse createEnumeration() throws RemoteException {
		BDTResource bdtResource = (BDTResource) ResourceContext.getResourceContext().getResource();
		EnumIterator iter = bdtResource.createEnumeration();
		EnumResourceHome resourceHome = null;
		try {
			resourceHome = EnumResourceHome.getEnumResourceHome();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EnumResource resource = resourceHome.createEnumeration(iter, false);
		ResourceKey key = resourceHome.getKey(resource);
		try {
			EnumerationContextType enumContext = EnumProvider.createEnumerationContextType(key);
			EnumerateResponse response = new EnumerateResponse(new MessageElement[]{}, enumContext,
				new ExpirationType());
			return response;
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	public org.globus.transfer.AnyXmlType get(org.globus.transfer.EmptyType empty) throws RemoteException {
		BDTResource bdtResource = (BDTResource) ResourceContext.getResourceContext().getResource();
		return bdtResource.get();
	}

}
