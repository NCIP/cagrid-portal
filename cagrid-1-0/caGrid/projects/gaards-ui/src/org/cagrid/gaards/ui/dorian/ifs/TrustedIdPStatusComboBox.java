package org.cagrid.gaards.ui.dorian.ifs;

import org.cagrid.gaards.ui.dorian.idp.AxisTypeComboBox;

import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TrustedIdPStatusComboBox extends AxisTypeComboBox {

	public TrustedIdPStatusComboBox() {
		this(false);
	}

	public TrustedIdPStatusComboBox(boolean anyState) {
		super(TrustedIdPStatus.class, anyState);
	}

	public TrustedIdPStatus getSelectedStatus() {
		return (TrustedIdPStatus) getSelectedObject();
	}

}
