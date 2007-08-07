package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class ServicesPopUpMenu extends JPopupMenu {

    private JMenuItem addResourceMenuItem = null;
    private ServicesTypeTreeNode node;


    /**
     * This method initializes
     */
    public ServicesPopUpMenu(ServicesTypeTreeNode node) {
        super();
        this.node = node;
        initialize();
    }


    /**
     * This method initializes this
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
        if (this.addResourceMenuItem == null) {
            this.addResourceMenuItem = new JMenuItem();
            this.addResourceMenuItem.setText("Add Service Context");
            this.addResourceMenuItem.setIcon(IntroduceLookAndFeel.getCreateServiceIcon());
            this.addResourceMenuItem.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    ServicesPopUpMenu.addService(ServicesPopUpMenu.this.node);
                }

            });
        }
        return this.addResourceMenuItem;
    }


    public static void addService(ServicesTypeTreeNode node) {
        ServiceType service = new ServiceType();
        service.setMethods(new MethodsType());
        service.setResourcePropertiesList(new ResourcePropertiesListType());
        service.setResourceFrameworkType(IntroduceConstants.INTRODUCE_LIFETIME_RESOURCE);
        // service.setServiceSecurity(new ServiceSecurity());
        service.setMethods(new MethodsType());
        ServiceTypeTreeNode newNode = node.addService(service);
        ModifyService comp = new ModifyService(newNode, new SpecificServiceInformation(node.getInfo(), service), true);
        // PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
        // new ModifyService(newNode, new
        // SpecificServiceInformation(node.getInfo(),service)));
        comp.pack();
        comp.setVisible(true);
    }

}
