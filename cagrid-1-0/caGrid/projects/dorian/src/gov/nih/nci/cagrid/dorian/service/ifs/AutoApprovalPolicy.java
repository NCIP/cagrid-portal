package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class AutoApprovalPolicy extends UserPolicy {
	public void applyPolicy(IFSUser user) throws DorianInternalFault,
			UserPolicyFault {
		UserManager um = getUserManager();
		// First we approve if the user has not been approved.
		if (user.getUserStatus().equals(IFSUserStatus.Pending)) {
			user.setUserStatus(IFSUserStatus.Active);
			try {
				um.updateUser(user);
			} catch (Exception e) {
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("Error updating the status of the user "
						+ user.getGridId());
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		}
	}
}
