package gov.nih.nci.cagrid.gts.portal;

import java.util.List;

import javax.swing.JComboBox;

import org.projectmobius.portal.PortalResourceManager;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GTSServiceListComboBox extends JComboBox {

	private static String lastSelectedService;

	public GTSServiceListComboBox() {
		GTSPortalConf conf = (GTSPortalConf) PortalResourceManager
				.getInstance().getResource(GTSPortalConf.RESOURCE);
		List services = conf.getGTSServiceList();
		for (int i = 0; i < services.size(); i++) {
			this.addItem(services.get(i));
		}
		if (lastSelectedService == null) {
			lastSelectedService = getSelectedService();
		} else {
			this.setSelectedItem(lastSelectedService);
		}
		this.setEditable(true);

		this.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				lastSelectedService = getSelectedService();
			}
		});
	}

	public String getSelectedService() {
		return (String) getSelectedItem();
	}

}
