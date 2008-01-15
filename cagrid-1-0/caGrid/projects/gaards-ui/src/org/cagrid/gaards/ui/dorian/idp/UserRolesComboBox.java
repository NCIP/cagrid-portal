package org.cagrid.gaards.ui.dorian.idp;

import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;

import org.cagrid.gaards.ui.common.AxisTypeComboBox;


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
		super(IdPUserRole.class, anyState);
	}

	public IdPUserRole getSelectedUserRole() {
		return (IdPUserRole) getSelectedObject();
	}

}
