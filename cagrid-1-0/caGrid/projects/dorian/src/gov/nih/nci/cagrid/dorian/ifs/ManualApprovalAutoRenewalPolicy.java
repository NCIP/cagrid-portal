package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.UserPolicyFault;

import java.security.cert.X509Certificate;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class ManualApprovalAutoRenewalPolicy extends UserPolicy {
	public void applyPolicy(IFSUser user) throws GUMSInternalFault,
			UserPolicyFault {
		UserManager um = getUserManager();
		IFSConfiguration conf = getConfiguration();
		
		try {
			// Next we check if the user's credentials have expired
			X509Certificate cert = CertUtil.loadCertificateFromString(user
					.getCertificate().getCertificateAsString());
			if (CertUtil.isExpired(cert)) {
				um.renewUserCredentials(user);
			} else if (conf.getMaxProxyLifetime().after(cert.getNotAfter())) {
				um.renewUserCredentials(user);
			}
		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error renewing the credentials of the user "
					+ user.getGridId());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}
}
