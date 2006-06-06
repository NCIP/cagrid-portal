package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.common.security.commstyle.CommunicationStyle;
import gov.nih.nci.cagrid.dorian.stubs.DorianPortType;
import gov.nih.nci.cagrid.dorian.stubs.service.DorianServiceAddressingLocator;

import org.apache.axis.client.Stub;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.axis.util.Util;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.security.Constants;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DorianBaseClient {
	protected String serviceURI;

	public DorianBaseClient(String serviceURI) {
		Util.registerTransport();
		this.serviceURI = serviceURI;
	}


	protected DorianPortType getPort(CommunicationStyle style) throws Exception {
		DorianServiceAddressingLocator locator = new DorianServiceAddressingLocator();
		EndpointReferenceType endpoint = new EndpointReferenceType();
		endpoint.setAddress(new Address(serviceURI));
		DorianPortType port = locator.getDorianPortTypePort(endpoint);
		style.configure((Stub) port);
		((Stub) port)._setProperty(Constants.AUTHORIZATION, NoAuthorization.getInstance());
		return port;

	}


	public static String parseRemoteException(Exception e) {
		String err = e.getMessage();
		String ex = "java.rmi.RemoteException:";
		int index = err.indexOf(ex);
		if (index >= 0) {
			err = err.substring(index + ex.length());
		}
		return err;
	}
}
