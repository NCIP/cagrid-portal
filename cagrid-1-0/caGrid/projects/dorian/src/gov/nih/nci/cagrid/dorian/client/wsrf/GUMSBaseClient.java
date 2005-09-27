package gov.nih.nci.cagrid.gums.client.wsrf;

import gov.nih.cagrid.security.commstyle.CommunicationStyle;
import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.gums.wsrf.service.GUMSServiceAddressingLocator;

import org.apache.axis.client.Stub;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.security.Constants;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: GUMSBaseClient.java,v 1.1 2005-09-27 18:31:18 langella Exp $
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

}
