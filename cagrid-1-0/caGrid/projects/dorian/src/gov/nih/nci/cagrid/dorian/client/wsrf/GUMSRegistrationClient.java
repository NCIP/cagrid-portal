package gov.nih.nci.cagrid.gums.client.wsrf;

import java.rmi.RemoteException;

import org.apache.axis.AxisFault;

import gov.nih.nci.cagrid.gums.Registration;
import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.common.GUMSException;
import gov.nih.nci.cagrid.gums.common.GUMSInternalException;
import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.gums.wsrf.RequiredUserAttributes;
import gov.nih.nci.cagrid.security.commstyle.AnonymousSecureConversationWithEncryption;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: JanusRegistrationClient.java,v 1.1 2005/09/26 20:22:20 langella
 *          Exp $
 */
public class GUMSRegistrationClient extends GUMSBaseClient implements
		Registration {

	public GUMSRegistrationClient(String serviceURI) {
		super(serviceURI);
	}

	public AttributeDescriptor[] getRequiredUserAttributes()
			throws GUMSInternalException, GUMSException {
		GUMSPortType port = null;
		try {
			port = this
					.getPort(new AnonymousSecureConversationWithEncryption());

		}catch (Exception e) {
			e.printStackTrace();
			throw new GUMSException(e.getMessage());
		}
		try {
			return port.getRequiredUserAttributes(new RequiredUserAttributes())
					.getAttributeDescriptors();
		} catch(AxisFault fault){
			fault.printStackTrace();
			throw new GUMSException(simplifyMessage(fault.getFaultString()));
		}catch (RemoteException e) {
			e.printStackTrace();
			throw new GUMSException(simplifyMessage(parseRemoteException(e)));
		}
	}

}
