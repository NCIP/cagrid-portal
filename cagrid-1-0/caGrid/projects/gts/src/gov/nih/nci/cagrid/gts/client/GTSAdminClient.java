package gov.nih.nci.cagrid.gts.client;

import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;

import java.rmi.RemoteException;

import org.globus.gsi.GlobusCredential;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class GTSAdminClient {

	GridTrustServiceClient client;

	public GTSAdminClient(String url, GlobusCredential proxy) {
		this.client = new GridTrustServiceClient(url, proxy);
	}

	public TrustedAuthority addTrustedAuthority(TrustedAuthority ta)
			throws RemoteException, GTSInternalFault,
			IllegalTrustedAuthorityFault, PermissionDeniedFault {
		return client.addTrustedAuthority(ta);
	}

}
