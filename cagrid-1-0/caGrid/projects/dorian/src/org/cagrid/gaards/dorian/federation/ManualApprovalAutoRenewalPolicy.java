package org.cagrid.gaards.dorian.federation;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.security.cert.X509Certificate;

import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.UserPolicyFault;
import org.cagrid.gaards.pki.CertUtil;


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
			} else if (FederationUtils.getMaxProxyLifetime(conf).after(cert.getNotAfter())) {
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
