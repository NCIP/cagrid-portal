package gov.nih.nci.cagrid.gts.client;

import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;

import java.rmi.RemoteException;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class GTSSearchClient {

	GridTrustServiceClient client;


	public GTSSearchClient(String url) {
		this.client = new GridTrustServiceClient(url);
	}


	public TrustedAuthority[] findTrustedAuthorities(TrustedAuthorityFilter f) throws RemoteException, GTSInternalFault {
		return this.client.findTrustedAuthorities(f);
	}


	public TrustLevel[] getTrustLevels() throws RemoteException, GTSInternalFault {
		return this.client.getTrustLevels();
	}


	public AuthorityGTS[] getAuthorities() throws RemoteException, GTSInternalFault {
		return this.client.getAuthorities();
	}
}
