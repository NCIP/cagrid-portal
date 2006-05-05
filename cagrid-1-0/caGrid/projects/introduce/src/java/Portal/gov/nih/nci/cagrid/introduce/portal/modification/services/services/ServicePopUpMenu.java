package gov.nih.nci.cagrid.introduce.portal.modification.services.services;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ServicePopUpMenu extends JPopupMenu {

	private JMenuItem removeMethodMenuItem = null;
	ServiceTypeTreeNode node;
	/**
	 * This method initializes 
	 * 
	 */
	public ServicePopUpMenu(ServiceTypeTreeNode node) {
		super();
		this.node = node;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.add(getRemoveMethodMenuItem());
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
					((ServicesTypeTreeNode)node.getParent()).removeResource(node);
				}
			});
		}
		return removeMethodMenuItem;
	}

}
