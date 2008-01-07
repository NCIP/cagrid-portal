package org.cagrid.gaards.ui.cds;

import org.cagrid.gaards.cds.common.DelegatedCredentialEvent;
import org.cagrid.gaards.ui.common.AxisTypeComboBox;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DelegatedCredentialEventComboBox extends AxisTypeComboBox {

	public DelegatedCredentialEventComboBox() {
		super(DelegatedCredentialEvent.class, true);
	}

	public DelegatedCredentialEvent getEvent() {
		return (DelegatedCredentialEvent) getSelectedObject();
	}

}
