package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ResourcePropertyPopUpMenu extends JPopupMenu {

	private JMenuItem removeResourcePropertyMenuItem = null;
	private ResourcePropertyTypeTreeNode node;
	/**
	 * This method initializes 
	 * 
	 */
	public ResourcePropertyPopUpMenu(ResourcePropertyTypeTreeNode node) {
		super();
		this.node = node;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.add(getRemoveResourcePropertyMenuItem());
			
	}

	/**
	 * This method initializes removeResourcePropertyMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getRemoveResourcePropertyMenuItem() {
		if (removeResourcePropertyMenuItem == null) {
			removeResourcePropertyMenuItem = new JMenuItem();
			removeResourcePropertyMenuItem.setIcon(IntroduceLookAndFeel.getRemoveIcon());
			removeResourcePropertyMenuItem.setText("Remove Resource Property");
			removeResourcePropertyMenuItem.addMouseListener(new MouseAdapter() {		
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					((ResourcePropertiesTypeTreeNode)node.getParent()).removeResourceProperty(node);
				}
			});
		}
		return removeResourcePropertyMenuItem;
	}

}
