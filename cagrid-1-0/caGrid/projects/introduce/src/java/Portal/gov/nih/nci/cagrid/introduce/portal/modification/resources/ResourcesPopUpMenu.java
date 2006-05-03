package gov.nih.nci.cagrid.introduce.portal.modification.resources;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

public class ResourcesPopUpMenu extends JPopupMenu {

	private JMenuItem addResourceMenuItem = null;
	private ResourcesTypeTreeNode node;
	/**
	 * This method initializes 
	 * 
	 */
	public ResourcesPopUpMenu(ResourcesTypeTreeNode node) {
		super();
		this.node = node;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.add(getAddResourceMenuItem());
			
	}

	/**
	 * This method initializes addResourceMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getAddResourceMenuItem() {
		if (addResourceMenuItem == null) {
			addResourceMenuItem = new JMenuItem();
			addResourceMenuItem.setText("Add Resource");
			addResourceMenuItem.setIcon(IntroduceLookAndFeel.getAddIcon());
			addResourceMenuItem.addMouseListener(new MouseAdapter() {
			
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					ServiceType service = new ServiceType();
					service.setName("newService");
					service.setMethods(new MethodsType());
					service.setResourcePropertiesList(new ResourcePropertiesListType());
					service.setResourceFrameworkType(IntroduceConstants.INTRODUCE_BASE_RESOURCE);
					node.addService(service);
					
				}
			
			});
		}
		return addResourceMenuItem;
	}

}
