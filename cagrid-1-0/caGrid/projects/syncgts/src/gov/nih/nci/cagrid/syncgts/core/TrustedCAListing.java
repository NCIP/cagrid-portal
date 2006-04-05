package gov.nih.nci.cagrid.syncgts.core;

import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TrustedCAListing {

	private String service;
	private TrustedAuthority trustedAuthority;


	public TrustedCAListing(String service, TrustedAuthority ta) {
		this.service = service;
		this.trustedAuthority = ta;
	}


	public String getService() {
		return service;
	}


	public TrustedAuthority getTrustedAuthority() {
		return trustedAuthority;
	}

}
