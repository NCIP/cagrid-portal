package gov.nih.nci.cagrid.gums.client.wsrf;

import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.gums.wsrf.service.GUMSServiceAddressingLocator;
import gov.nih.nci.cagrid.security.commstyle.CommunicationStyle;

import org.apache.axis.client.Stub;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.security.Constants;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMSBaseClient {
	protected String serviceURI;

	public GUMSBaseClient(String serviceURI) {
		this.serviceURI = serviceURI;
	}

	protected GUMSPortType getPort(CommunicationStyle style) throws Exception{
		GUMSServiceAddressingLocator locator = new GUMSServiceAddressingLocator();
		EndpointReferenceType endpoint = new EndpointReferenceType();
		endpoint.setAddress(new Address(serviceURI));
		GUMSPortType port = locator.getGUMSPortTypePort(endpoint);
        style.configure((Stub)port);	
        ((Stub)port)._setProperty(Constants.AUTHORIZATION, NoAuthorization.getInstance());
		return port;

	}
	
	public static String parseRemoteException(Exception e){
		String err = e.getMessage();
		String ex = "java.rmi.RemoteException:";
		int index = err.indexOf(ex);
		if (index >= 0) {
			err = err.substring(index + ex.length());
		}	
		return err;
	}
	
	public static String simplifyMessage(String m){
		if((m == null) || (m.equalsIgnoreCase("null"))){ 
		  m = "Unknown Error";
		}else if (m.indexOf("Connection refused") >= 0) {
		
			m = "Could not connect to the service, the service may not exist or may be down.";
		} else if (m.indexOf("Unknown CA") >= 0) {
			m = "Could establish a connection with the service, Unknown CA.";
		}	
		return m;	
	}


}
