package gov.nih.nci.cagrid.gums;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidLoginFault;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.idp.bean.User;
import gov.nih.nci.cagrid.gums.idp.bean.UserFilter;

public interface IdPAdministration {
	public User[] findUsers(UserFilter filter)
			throws GUMSFault, GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault;

	public void updateUser(User u)
			throws GUMSFault,GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault,
			NoSuchUserFault, InvalidUserPropertyFault;
	
	public void removeUser(String userId)
	throws GUMSFault,GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault;

}
