package gov.nih.nci.cagrid.dorian.ui.idp;

import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class StateListComboBox extends AxisTypeComboBox {

	public StateListComboBox() {
		this(false);
	}

	public StateListComboBox(boolean anyState) {
		super(StateCode.class, anyState);
	}

	public StateCode getSelectedState() {
		return (StateCode) getSelectedObject();
	}

}
