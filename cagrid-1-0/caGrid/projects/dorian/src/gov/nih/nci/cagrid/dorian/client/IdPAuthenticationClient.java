package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.common.SAMLUtils;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdPAuthenticationClient{

	private BasicAuthCredential cred;


	private DorianClient client;


	public IdPAuthenticationClient(String serviceURI, BasicAuthCredential cred) throws MalformedURIException, RemoteException {
		client = new DorianClient(serviceURI);
		this.cred = cred;
	}


	public SAMLAssertion authenticate() throws DorianFault, DorianInternalFault, PermissionDeniedFault {
		try {
			String xml = client.authenticateWithIdP(cred).getXml();
			// System.out.println(XMLUtilities.formatXML(xml));
			return SAMLUtils.stringToSAMLAssertion(xml);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault ilf) {
			throw ilf;
		} catch (Exception e) {
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
