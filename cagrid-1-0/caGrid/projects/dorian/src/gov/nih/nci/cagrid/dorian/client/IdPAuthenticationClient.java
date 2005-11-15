package gov.nih.nci.cagrid.gums.client;



import gov.nih.nci.cagrid.gums.IdPAuthentication;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidLoginFault;
import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.security.commstyle.AnonymousSecureConversationWithEncryption;

import org.globus.wsrf.utils.FaultHelper;
import org.opensaml.SAMLAssertion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uk.org.ogsadai.common.XMLUtilities;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdPAuthenticationClient extends GUMSBaseClient implements
		IdPAuthentication {

	private BasicAuthCredential cred;
	
	public IdPAuthenticationClient(String serviceURI, BasicAuthCredential cred) {
		super(serviceURI);
		this.cred = cred;
	}
	
	

	public SAMLAssertion authenticate() throws GUMSFault,GUMSInternalFault, InvalidLoginFault {
		// TODO Auto-generated method stub
		GUMSPortType port = null;
		try {
			port = this.getPort(new AnonymousSecureConversationWithEncryption());
		}catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
		try {
			String xml = port.idpAuthenticate(cred).getXml();
			Document doc =XMLUtilities.xmlStringToDOM(xml,false);
			return new SAMLAssertion(doc.getDocumentElement());
		}catch(GUMSInternalFault gie){
			throw gie;
		}catch (InvalidLoginFault ilf){
			throw ilf;
		}catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
	}
	
	public static void main(String[] args){
		try{
			BasicAuthCredential cred = new BasicAuthCredential("gomets123","langella");
			IdPAuthenticationClient client = new IdPAuthenticationClient("http://localhost:8080/wsrf/services/cagrid/gums",cred);
			SAMLAssertion saml = client.authenticate();
			System.out.println("SAML:");
			System.out.println(XMLUtilities.xmlDOMToString((Element) saml
					.toDOM()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
