package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AutomaticRegistrationPolicy implements IdPRegistrationPolicy{

	public String getDescription() {
		return "This policy automatically approves user when they register.";
	}

	public String getName() {
		return "Automatic Registration";
	}	

	public ApplicationReview register(Application a) throws GUMSInternalFault, InvalidUserPropertyFault {
		ApplicationReview ar = new ApplicationReview();
		ar.setStatus(UserManager.ACTIVE);
		ar.setRole(UserManager.NON_ADMINISTRATOR);
		ar
				.setMessage("Your account was approved, your current account status is "
						+ UserManager.ACTIVE + " you mail use "
						+ a.getEmail() + " to access your account");
		return ar;
	}

}
