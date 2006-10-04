package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class ServicePopUpMenu extends JPopupMenu {

	ServiceTypeTreeNode node;
	private JMenuItem modificationMenuItem = null;


	/**
	 * This method initializes
	 */
	public ServicePopUpMenu(ServiceTypeTreeNode node) {
		super();
		this.node = node;
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.add(getModificationMenuItem());
	}


	/**
	 * This method initializes modificationMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getModificationMenuItem() {
		if (modificationMenuItem == null) {
			modificationMenuItem = new JMenuItem();
			modificationMenuItem.setText("Modify Service Context");
			modificationMenuItem.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					
					ModifyService comp = new ModifyService(node, new SpecificServiceInformation(node.getInfo(),
						node.getServiceType()),false);
					comp.pack();
					PortalUtils.centerWindow(comp);
					comp.setVisible(true);
				}

			});
		}
		return modificationMenuItem;
	}
}
