package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class ResourcePropertiesPopUpMenu extends JPopupMenu {

	private ResourcePropertiesTypeTreeNode node;
	private JMenuItem modifyResourcePropetiesMenuItem = null;


	/**
	 * This method initializes
	 * 
	 */
	public ResourcePropertiesPopUpMenu(ResourcePropertiesTypeTreeNode node) {
		super();
		this.node = node;
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.add(getModifyResourcePropetiesMenuItem());

	}


	/**
	 * This method initializes modifyResourcePropetiesMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getModifyResourcePropetiesMenuItem() {
		if (modifyResourcePropetiesMenuItem == null) {
			modifyResourcePropetiesMenuItem = new JMenuItem();
			modifyResourcePropetiesMenuItem.setText("Modify Properties");
			modifyResourcePropetiesMenuItem.setIcon(IntroduceLookAndFeel.getModifyIcon());
			modifyResourcePropetiesMenuItem.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					ModifyResourcePropertiesComponent comp = new ModifyResourcePropertiesComponent(node
						.getResourceProperties(), node.getInfo().getNamespaces(), true);
					PortalUtils.centerWindow(comp);
					comp.setVisible(true);
					node.reInitialize(node.getResourceProperties());
				}

			});
		}
		return modifyResourcePropetiesMenuItem;
	}

}
