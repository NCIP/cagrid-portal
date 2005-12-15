package gov.nih.nci.cagrid.dorian.ifs.portal;

import gov.nih.nci.cagrid.dorian.idp.portal.AxisTypeComboBox;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserRole;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserRolesComboBox extends AxisTypeComboBox {

	public UserRolesComboBox() {
		this(false);
	}

	public UserRolesComboBox(boolean anyState) {
		super(IFSUserRole.class, anyState);
	}

	public IFSUserRole getSelectedUserRole() {
		return (IFSUserRole) getSelectedObject();
	}

}
