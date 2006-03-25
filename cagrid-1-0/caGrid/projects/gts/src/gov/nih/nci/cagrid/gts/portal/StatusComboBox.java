package gov.nih.nci.cagrid.gts.portal;

import gov.nih.nci.cagrid.gts.bean.Status;

import javax.swing.JComboBox;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class StatusComboBox extends JComboBox {


	public StatusComboBox() {
	    this.addItem(Status.Trusted);
	    this.addItem(Status.Pending);
	    this.addItem(Status.Suspended);
		this.setEditable(false);
	}

	public Status getStatus() {
		return (Status) getSelectedItem();
	}

}
