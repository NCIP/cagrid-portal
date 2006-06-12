package gov.nih.nci.cagrid.introduce.portal.modification.services;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import org.projectmobius.portal.PortalResourceManager;

public class ServicesPopUpMenu extends JPopupMenu {

	private JMenuItem addResourceMenuItem = null;
	private ServicesTypeTreeNode node;
	/**
	 * This method initializes 
	 * 
	 */
	public ServicesPopUpMenu(ServicesTypeTreeNode node) {
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
			addResourceMenuItem.setText("Add Service Context");
			addResourceMenuItem.setIcon(IntroduceLookAndFeel.getCreateServiceIcon());
			addResourceMenuItem.addMouseListener(new MouseAdapter() {
			
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					ServiceType service = new ServiceType();
					service.setMethods(new MethodsType());
					service.setResourcePropertiesList(new ResourcePropertiesListType());
					service.setResourceFrameworkType(IntroduceConstants.INTRODUCE_BASE_RESOURCE);
					node.addService(service);
					PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
						new ModifyService(new SpecificServiceInformation(node.getInfo(),service)));
					
				}
			
			});
		}
		return addResourceMenuItem;
	}

}
