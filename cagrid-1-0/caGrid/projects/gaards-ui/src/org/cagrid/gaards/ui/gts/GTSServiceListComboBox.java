package org.cagrid.gaards.ui.gts;

import java.util.List;

import javax.swing.JComboBox;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GTSServiceListComboBox extends JComboBox {
	
	private static final long serialVersionUID = 1L;

	private static String lastSelectedService;

	private static final String ANY = "Any";


	public GTSServiceListComboBox() {
		this(false);
	}


	public GTSServiceListComboBox(boolean any) {
		if (any) {
			this.addItem(ANY);
		}
		List services = GTSUIUtils.getGTSServices();
		for (int i = 0; i < services.size(); i++) {
			this.addItem(services.get(i));
		}
		if (!any) {
			if (lastSelectedService == null) {
				lastSelectedService = getSelectedService();
			} else {
				this.setSelectedItem(lastSelectedService);
			}
		}
		this.setEditable(true);

		this.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (getSelectedService() != null) {
					lastSelectedService = getSelectedService();
				}
			}
		});
	}


	public String getSelectedService() {
		String s = (String) getSelectedItem();
		if (s.equals(ANY)) {
			return null;
		} else {
			return s;
		}
	}

}
