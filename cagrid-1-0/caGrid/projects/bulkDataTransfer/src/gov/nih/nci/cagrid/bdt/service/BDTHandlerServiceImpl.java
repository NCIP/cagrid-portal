package gov.nih.nci.cagrid.bdt.service;

import java.rmi.RemoteException;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

/** 
 *  gov.nih.nci.cagrid.bdtI
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class BDTHandlerServiceImpl extends BDTHandlerServiceImplBase {

	public static final String WS_ENUMERATION_NS = "";
	public static final String WS_TRANSFER_NS = "";
	
	public BDTHandlerServiceImpl() throws RemoteException {
		super();
	}
	
	public org.apache.axis.message.addressing.EndpointReferenceType getTransferProvider(gov.nih.nci.cagrid.bdt.metadata.beans.TransferProviderType transferProvider) throws RemoteException {
		if(transferProvider.getNamespace().equals(WS_ENUMERATION_NS) || transferProvider.getNamespace().equals(WS_ENUMERATION_NS)){
			EndpointReferenceType epr = new EndpointReferenceType();
			MessageContext ctx = MessageContext.getCurrentContext();
			String transportURL = (String) ctx.getProperty(MessageContext.TRANS_URL);
			try {
				epr.setAddress(new Address(transportURL));
			} catch (MalformedURIException e) {
				e.printStackTrace();
				throw new RemoteException("Cannot locate transfer provider: " + transferProvider.getNamespace());
			}
			return epr;
		} else {
			throw new RemoteException("Transfer Provider Not Supported: " + transferProvider.getNamespace());
		}
	}

}

