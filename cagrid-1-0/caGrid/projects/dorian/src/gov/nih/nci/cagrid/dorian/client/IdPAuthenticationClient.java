package gov.nih.nci.cagrid.dorian.client;



import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.security.commstyle.CommunicationStyle;
import gov.nih.nci.cagrid.dorian.IdPAuthentication;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.common.SAMLUtils;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.wsrf.DorianPortType;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

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
	private CommunicationStyle style;
	
	public IdPAuthenticationClient(String serviceURI,CommunicationStyle style, BasicAuthCredential cred) {
		super(serviceURI);
		this.cred = cred;
		this.style = style;
	}
	
	

	public SAMLAssertion authenticate() throws DorianFault,DorianInternalFault, PermissionDeniedFault {
		DorianPortType port = null;
		try {
			port = this.getPort(style);
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
			//System.out.println(XMLUtilities.formatXML(xml));
			return SAMLUtils.stringToSAMLAssertion(xml);
		}catch(DorianInternalFault gie){
			throw gie;
		}catch (PermissionDeniedFault ilf){
			throw ilf;
		}catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}
}
