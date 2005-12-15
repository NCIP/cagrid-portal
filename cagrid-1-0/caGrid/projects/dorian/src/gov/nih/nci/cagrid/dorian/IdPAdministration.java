package gov.nih.nci.cagrid.dorian;

import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.idp.bean.NoSuchUserFault;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface IdPAdministration {
	public IdPUser[] findUsers(IdPUserFilter filter)
			throws DorianFault, DorianInternalFault, PermissionDeniedFault;

	public void updateUser(IdPUser u)
			throws DorianFault,DorianInternalFault, PermissionDeniedFault,
			NoSuchUserFault, InvalidUserPropertyFault;
	
	public void removeUser(String userId)
	throws DorianFault,DorianInternalFault, PermissionDeniedFault;

}
