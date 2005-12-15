package gov.nih.nci.cagrid.dorian.client;



import gov.nih.nci.cagrid.dorian.IdPAuthentication;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.common.IOUtils;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.wsrf.DorianPortType;
import gov.nih.nci.cagrid.security.commstyle.AnonymousSecureConversationWithEncryption;

import org.opensaml.SAMLAssertion;
import org.w3c.dom.Element;

import uk.org.ogsadai.common.XMLUtilities;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdPAuthenticationClient extends DorianBaseClient implements
		IdPAuthentication {

	private BasicAuthCredential cred;
	
	public IdPAuthenticationClient(String serviceURI, BasicAuthCredential cred) {
		super(serviceURI);
		this.cred = cred;
	}
	
	

	public SAMLAssertion authenticate() throws DorianFault,DorianInternalFault, PermissionDeniedFault {
		// TODO Auto-generated method stub
		DorianPortType port = null;
		try {
			port = this.getPort(new AnonymousSecureConversationWithEncryption());
		}catch (Exception e) {
			DorianFault fault = new DorianFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
		try {
			String xml = port.authenticateWithIdP(cred).getXml();
			return IOUtils.stringToSAMLAssertion(xml);
		}catch(DorianInternalFault gie){
			throw gie;
		}catch (PermissionDeniedFault ilf){
			throw ilf;
		}catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}
}
