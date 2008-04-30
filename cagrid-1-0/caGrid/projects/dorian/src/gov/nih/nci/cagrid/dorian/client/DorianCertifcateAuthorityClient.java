package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;

import java.rmi.RemoteException;
import java.security.cert.X509Certificate;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.pki.CertUtil;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DorianCertifcateAuthorityClient {

	private DorianClient client;


	public DorianCertifcateAuthorityClient(String serviceURI) throws MalformedURIException, RemoteException {
		client = new DorianClient(serviceURI);
	}


	public X509Certificate getCACertificate() throws DorianFault, DorianInternalFault {
		try {
			return CertUtil.loadCertificate(client.getCACertificate().getCertificateAsString());
		} catch (DorianInternalFault gie) {
			throw gie;
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
