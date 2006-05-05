package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.methods.MethodViewer;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import org.projectmobius.portal.PortalResourceManager;

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
					PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
						new ModifyResourcePropertiesComponent(node.getResourceProperties(),node.getInfo().getNamespaces(),true));
				}
			
			});
		}
		return modifyResourcePropetiesMenuItem;
	}

}
