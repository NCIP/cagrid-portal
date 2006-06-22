package gov.nih.nci.cagrid.introduce.portal.modification.services.methods;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.projectmobius.portal.PortalResourceManager;


public class MethodPopUpMenu extends JPopupMenu {

	private JMenuItem removeMethodMenuItem = null;
	MethodTypeTreeNode node;
	private JMenuItem modifyMethodMenuItem = null;


	/**
	 * This method initializes
	 */
	public MethodPopUpMenu(MethodTypeTreeNode node) {
		super();
		this.node = node;
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.add(getRemoveMethodMenuItem());
		this.add(getModifyMethodMenuItem());
	}


	/**
	 * This method initializes removeMethodMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRemoveMethodMenuItem() {
		if (removeMethodMenuItem == null) {
			removeMethodMenuItem = new JMenuItem();
			removeMethodMenuItem.setText("Remove Method");
			removeMethodMenuItem.setIcon(PortalLookAndFeel.getAddIcon());
			removeMethodMenuItem.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					((MethodsTypeTreeNode) node.getParent()).removeMethod(node);
				}
			});
		}
		return removeMethodMenuItem;
	}


	/**
	 * This method initializes modifyMethodMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getModifyMethodMenuItem() {
		if (modifyMethodMenuItem == null) {
			modifyMethodMenuItem = new JMenuItem();
			modifyMethodMenuItem.setIcon(IntroduceLookAndFeel.getModifyIcon());
			modifyMethodMenuItem.setText("Modify Method");
			modifyMethodMenuItem.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
						new MethodViewer(node.getMethod(), node.getInfo()));

				}
			});
		}
		return modifyMethodMenuItem;
	}

}
