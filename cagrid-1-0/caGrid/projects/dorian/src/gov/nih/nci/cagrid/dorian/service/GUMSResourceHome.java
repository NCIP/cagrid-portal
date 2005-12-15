package gov.nih.nci.cagrid.gums.service;

import org.globus.wsrf.Resource;
import org.globus.wsrf.impl.SingletonResourceHome;
/**
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMSResourceHome extends SingletonResourceHome {

	public Resource findSingleton() {
		try {
			// Create a resource and initialize it.
			GUMSResource mathResource = new GUMSResource();
			mathResource.initialize();
			return mathResource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}