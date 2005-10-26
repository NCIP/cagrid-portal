package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.gums.idp.bean.UserStatus;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserStatusComboBox extends AxisTypeComboBox {

	public UserStatusComboBox() {
		this(false);
	}

	public UserStatusComboBox(boolean anyState) {
		super(UserStatus.class, anyState);
	}

	public UserStatus getSelectedUserStatus() {
		return (UserStatus) getSelectedObject();
	}

}
