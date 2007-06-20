package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class ResourcePropertiesPopUpMenu extends JPopupMenu {

    private ResourcePropertiesTypeTreeNode node;
    private JMenuItem modifyResourcePropetiesMenuItem = null;


    /**
     * This method initializes
     */
    public ResourcePropertiesPopUpMenu(ResourcePropertiesTypeTreeNode node) {
        super();
        this.node = node;
        initialize();
    }


    /**
     * This method initializes this
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
        if (this.modifyResourcePropetiesMenuItem == null) {
            this.modifyResourcePropetiesMenuItem = new JMenuItem();
            this.modifyResourcePropetiesMenuItem.setText("Modify Properties");
            this.modifyResourcePropetiesMenuItem.setIcon(IntroduceLookAndFeel.getModifyIcon());
            this.modifyResourcePropetiesMenuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    ResourcePropertiesPopUpMenu.modifyResourceProperties(ResourcePropertiesPopUpMenu.this.node);
                }

            });
        }
        return this.modifyResourcePropetiesMenuItem;
    }


    public static void modifyResourceProperties(ResourcePropertiesTypeTreeNode node) {
        ModifyResourcePropertiesComponent comp = new ModifyResourcePropertiesComponent(node.getService(), node
            .getInfo().getNamespaces(), new File(node.getInfo().getBaseDirectory().getAbsolutePath() + File.separator
            + "etc"), new File(node.getInfo().getBaseDirectory().getAbsolutePath() + File.separator + "schema"
            + File.separator + node.getInfo().getServices().getService(0).getName()), true);
        comp.setSize(600, 300);
        PortalUtils.centerComponent(comp);
        comp.setVisible(true);
    }

}
