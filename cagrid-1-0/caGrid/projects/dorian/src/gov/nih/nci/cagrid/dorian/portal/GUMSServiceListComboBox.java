package gov.nih.nci.cagrid.gums.portal;

import java.util.List;

import javax.swing.JComboBox;

import org.projectmobius.portal.PortalResourceManager;

public class GUMSServiceListComboBox extends JComboBox {

	private static String lastSelectedService;

	public GUMSServiceListComboBox() {
		GumsPortalConf conf = (GumsPortalConf) PortalResourceManager
				.getInstance().getResource(GumsPortalConf.RESOURCE);
		List services = conf.getGumsServiceList();
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
