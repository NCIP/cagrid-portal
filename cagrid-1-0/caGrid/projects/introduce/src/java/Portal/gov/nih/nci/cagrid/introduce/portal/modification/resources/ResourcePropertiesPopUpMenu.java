package gov.nih.nci.cagrid.introduce.portal.modification.resources;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

public class ResourcePropertiesPopUpMenu extends JPopupMenu {

	private JMenuItem addResourcePropertyMenuItem = null;
	private ResourcePropertiesTypeTreeNode node;
	
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
        this.add(getAddResourcePropertyMenuItem());
			
	}

	/**
	 * This method initializes addResourcePropertyMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getAddResourcePropertyMenuItem() {
		if (addResourcePropertyMenuItem == null) {
			addResourcePropertyMenuItem = new JMenuItem();
			addResourcePropertyMenuItem.setText("Add Resource Property");
			addResourcePropertyMenuItem.setIcon(IntroduceLookAndFeel.getAddIcon());
			addResourcePropertyMenuItem.addMouseListener(new MouseAdapter() {
			
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					ResourcePropertyType resource = new ResourcePropertyType();
					node.add(resource);
				}
			
			});
		}
		return addResourcePropertyMenuItem;
	}

}
