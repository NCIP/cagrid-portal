package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.UserPolicyFault;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class UserPolicy {

	private IFSConfiguration configuration;

	private UserManager userManager;

	public void configure(IFSConfiguration conf, UserManager um) {
		this.configuration = conf;
		this.userManager = um;
	}

	public abstract void applyPolicy(IFSUser user) throws DorianInternalFault,
			UserPolicyFault;

	public IFSConfiguration getConfiguration() {
		return configuration;
	}

	public UserManager getUserManager() {
		return userManager;
	}	
}
