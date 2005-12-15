package gov.nih.nci.cagrid.dorian;

import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface IFSAdministration {
	public TrustedIdP[] getTrustedIdPs() throws DorianFault, PermissionDeniedFault, InvalidUserFault, DorianInternalFault;


	public IFSUser[] findUsers(IFSUserFilter filter) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		DorianInternalFault;


	public void updateUser(IFSUser usr) throws DorianFault, PermissionDeniedFault, InvalidUserFault, DorianInternalFault;


	public void removeUser(IFSUser usr) throws DorianFault, PermissionDeniedFault, InvalidUserFault, DorianInternalFault;


	public IFSUser renewUserCredentials(IFSUser usr) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		DorianInternalFault;


	public IFSUserPolicy[] getUserPolicies() throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		DorianInternalFault;


	public TrustedIdP addTrustedIdP(TrustedIdP idp) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		InvalidTrustedIdPFault, DorianInternalFault;


	public void updateTrustedIdP(TrustedIdP idp) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		InvalidTrustedIdPFault, DorianInternalFault;


	public void removeTrustedIdP(TrustedIdP idp) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		InvalidTrustedIdPFault, DorianInternalFault;

}
