package org.cagrid.gaards.ui.dorian.federation;

import org.cagrid.gaards.dorian.federation.UserCertificateStatus;
import org.cagrid.gaards.ui.common.AxisTypeComboBox;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserCertificateStatusComboBox extends AxisTypeComboBox {
	
	private static final long serialVersionUID = 1L;

	public UserCertificateStatusComboBox() {
		this(false);
	}

	public UserCertificateStatusComboBox(boolean anyState) {
		super(UserCertificateStatus.class, anyState);
	}

	public UserCertificateStatus getSelectedUserStatus() {
		return (UserCertificateStatus) getSelectedObject();
	}

}
