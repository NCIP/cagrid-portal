package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.projectmobius.portal.PortalResourceManager;


public class ServicePopUpMenu extends JPopupMenu {

	private JMenuItem removeMethodMenuItem = null;
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
		this.add(getRemoveMethodMenuItem());
		this.add(getModificationMenuItem());
	}


	/**
	 * This method initializes removeMethodMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRemoveMethodMenuItem() {
		if (removeMethodMenuItem == null) {
			removeMethodMenuItem = new JMenuItem();
			removeMethodMenuItem.setText("Remove Service Context");
			removeMethodMenuItem.setIcon(PortalLookAndFeel.getRemoveIcon());
			removeMethodMenuItem.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					((ServicesTypeTreeNode) node.getParent()).removeResource(node);
				}
			});
		}
		return removeMethodMenuItem;
	}


	/**
	 * This method initializes modificationMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getModificationMenuItem() {
		if (modificationMenuItem == null) {
			modificationMenuItem = new JMenuItem();
			modificationMenuItem.setText("Modify Service");
			modificationMenuItem.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					ModifyService comp = new ModifyService(node, new SpecificServiceInformation(node.getInfo(),
						node.getServiceType()));
					// PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
					// new ModifyService(newNode, new
					// SpecificServiceInformation(node.getInfo(),service)));
					comp.setSize(new Dimension(500,200));
					PortalUtils.centerWindow(comp);
					comp.setVisible(true);
				}

			});
		}
		return modificationMenuItem;
	}
}
