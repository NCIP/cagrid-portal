package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;

import java.security.cert.X509Certificate;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class ManualApprovalAutoRenewalPolicy extends AccountPolicy {
	public void applyPolicy(TrustedIdP idp, IFSUser user) throws DorianInternalFault, UserPolicyFault {
		UserManager um = getUserManager();
		IdentityFederationProperties conf = getConfiguration();

		try {
			// Next we check if the user's credentials have expired
			X509Certificate cert = CertUtil.loadCertificate(user.getCertificate().getCertificateAsString());
			if (CertUtil.isExpired(cert)) {
				um.renewUserCredentials(idp, user);
			} else if (IFSUtils.getMaxProxyLifetime(conf).after(cert.getNotAfter())) {
				um.renewUserCredentials(idp, user);
			}
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error renewing the credentials of the user " + user.getGridId());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}
	
	public String getDisplayName() {
		return "Manual Approval / Auto Renewal";
	}
}
