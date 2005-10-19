package gov.nih.nci.cagrid.gums.client.wsrf;

import gov.nih.nci.cagrid.gums.Registration;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSException;
import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidApplicationFault;
import gov.nih.nci.cagrid.gums.ifs.bean.UserApplication;
import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.gums.wsrf.RequiredUserAttributes;
import gov.nih.nci.cagrid.security.commstyle.AnonymousSecureConversationWithEncryption;

import java.rmi.RemoteException;

import org.apache.axis.AxisFault;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMSRegistrationClient extends GUMSBaseClient implements
		Registration {

	public GUMSRegistrationClient(String serviceURI) {
		super(serviceURI);
	}

	public AttributeDescriptor[] getRequiredUserAttributes()
			throws GUMSInternalFault, GUMSException {
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
		} catch(GUMSInternalFault gie){
			throw gie;
		}catch(AxisFault fault){
			fault.printStackTrace();
			throw new GUMSException(simplifyMessage(fault.getFaultString()));
		}catch (RemoteException e) {
			e.printStackTrace();
			throw new GUMSException(simplifyMessage(e.getMessage()));
		}
	}

	public String registerUser(UserApplication application) throws InvalidApplicationFault, GUMSInternalFault,GUMSException {
		GUMSPortType port = null;
		try {
			port = this
					.getPort(new AnonymousSecureConversationWithEncryption());

		}catch (Exception e) {
			e.printStackTrace();
			throw new GUMSException(e.getMessage());
		}
		try {
			return port.registerUser(application);
		} catch(GUMSInternalFault gie){
			throw gie;
		}catch(AxisFault fault){
			fault.printStackTrace();
			throw new GUMSException(simplifyMessage(fault.getFaultString()));
		}catch (RemoteException e) {
			e.printStackTrace();
			throw new GUMSException(simplifyMessage(e.getMessage()));
		}
	}
	
	

}
