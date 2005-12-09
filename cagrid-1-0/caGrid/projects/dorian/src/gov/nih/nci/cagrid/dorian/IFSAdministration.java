package gov.nih.nci.cagrid.gums;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface IFSAdministration {
	public TrustedIdP[] getTrustedIdPs() throws GUMSFault,
			PermissionDeniedFault, InvalidUserFault, GUMSInternalFault;

	public IFSUser[] findUsers(IFSUserFilter filter) throws GUMSFault,
			PermissionDeniedFault, InvalidUserFault, GUMSInternalFault;

	public void updateUser(IFSUser usr) throws GUMSFault,
			PermissionDeniedFault, InvalidUserFault, GUMSInternalFault;

	public void removeUser(IFSUser usr) throws GUMSFault,
			PermissionDeniedFault, InvalidUserFault, GUMSInternalFault;

}
