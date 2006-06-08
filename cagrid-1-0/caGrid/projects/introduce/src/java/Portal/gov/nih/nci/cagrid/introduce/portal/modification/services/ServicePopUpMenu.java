package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ModifyResourcePropertiesComponent;

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
			removeMethodMenuItem.setIcon(IntroduceLookAndFeel.getRemoveIcon());
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
					PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
						new ModifyService(node.getServiceType()));
				}
			
			});
		}
		return modificationMenuItem;
	}
}
