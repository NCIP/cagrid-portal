package gov.nih.nci.cagrid.gts.portal;

import gov.nih.nci.cagrid.gts.bean.TrustLevel;

import javax.swing.JComboBox;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TrustLevelComboBox extends JComboBox {

	public TrustLevelComboBox() {
		this.addItem(TrustLevel.One);
		this.addItem(TrustLevel.Two);
		this.addItem(TrustLevel.Three);
		this.addItem(TrustLevel.Four);
		this.addItem(TrustLevel.Five);
		this.setEditable(false);
	}

	public TrustLevel getTrustLevel() {
		return (TrustLevel) getSelectedItem();
	}

}
