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
public class ManualRegistrationPolicy implements IdPRegistrationPolicy {

	public String getDescription() {
		return "This policy requires registering users, to be manually approved my an administrator";
	}

	public String getName() {
		return "Manual Registration";
	}

	public ApplicationReview register(Application a) throws DorianInternalFault,
			InvalidUserPropertyFault {
		ApplicationReview ar = new ApplicationReview();
		ar.setStatus(IdPUserStatus.Pending);
		ar.setRole(IdPUserRole.Non_Administrator);
		ar
				.setMessage("Your application will be reviewed by an administrator and you will be contacted at "
						+ a.getEmail() + " upon a decision.");
		return ar;
	}

}
