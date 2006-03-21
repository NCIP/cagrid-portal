package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.security.commstyle.AnonymousSecureConversationWithEncryption;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.wsrf.DorianPortType;
import gov.nih.nci.cagrid.dorian.wsrf.IFSGetCACertificate;
import gov.nih.nci.cagrid.gridca.common.CertUtil;

import java.security.cert.X509Certificate;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DorianCertifcateAuthorityClient extends DorianBaseClient {

	public DorianCertifcateAuthorityClient(String serviceURI) {
		super(serviceURI);
	}

	public X509Certificate getCACertificate() throws DorianFault,DorianInternalFault{
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
			return CertUtil.loadCertificate(port.getCACertificate(new IFSGetCACertificate()).getCertificateAsString());
		}catch(DorianInternalFault gie){
			throw gie;
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
