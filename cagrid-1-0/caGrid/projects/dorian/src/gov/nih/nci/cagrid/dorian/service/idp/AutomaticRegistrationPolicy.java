package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault;


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

	public ApplicationReview register(Application a) throws DorianInternalFault, InvalidUserPropertyFault {
		ApplicationReview ar = new ApplicationReview();
		ar.setStatus(IdPUserStatus.Active);
		ar.setRole(IdPUserRole.Non_Administrator);
		ar.setMessage("Your account was approved, your current account status is " + IdPUserStatus.Active + ".");
		return ar;
	}

}
