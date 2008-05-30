package gov.nih.nci.cagrid.introduce.portal.init;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.IntroduceGDEExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.cagrid.grape.ApplicationInitializer;
import org.cagrid.grape.model.Application;
import org.cagrid.grape.model.Menu;
import org.cagrid.grape.model.Menus;


public class IntroducePortalInitializer implements ApplicationInitializer {
    private static final int HELP_MENU = 4;

    private static final int CONFIG_MENU = 3;

    private static final Logger logger = Logger.getLogger(IntroducePortalInitializer.class);
    
    public void intialize(Application app) throws Exception {
        PropertyConfigurator.configure("." + File.separator + "conf" + File.separator
            + "log4j.properties");

        logger.info("\n\nStarting up Introduce ... \n\n");
        ExtensionsLoader.getInstance();
        addGDEExtensions(app);
        setConfigurationOptions();
    }


    private boolean hasKey(Enumeration keys, String key) {
        while (keys.hasMoreElements()) {
            String testKey = (String) keys.nextElement();
            if (testKey.equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    
    private void addGDEExtensions(Application app){
        List gdeExtensions = ExtensionsLoader.getInstance().getIntroduceGDEExtensions();
        for (Iterator iterator = gdeExtensions.iterator(); iterator.hasNext();) {
            IntroduceGDEExtensionDescriptionType gdeExtension = (IntroduceGDEExtensionDescriptionType) iterator.next();
            Menus extensionMenus = gdeExtension.getMenus();
            for (int menuI = 0; menuI < extensionMenus.getMenu().length; menuI++) {
                Menu extensionMenu = extensionMenus.getMenu()[menuI];
                Menu existingMenu = null;
                int menuIndex = 0;
                for(; menuIndex < app.getMenus().getMenu().length; menuIndex++){
                    if(app.getMenus().getMenu(menuIndex).getTitle().equals(extensionMenu.getTitle())){
                        existingMenu = app.getMenus().getMenu(menuIndex);
                        break;
                    }
                }
                //TODO: currently only working on toplevel menus and not submenus
                if(existingMenu!=null){
                    //menu already exists
                    int existingMenuSize = 0;
                    if(existingMenu.getComponents()!=null && existingMenu.getComponents().getComponent()!=null){
                        existingMenuSize = existingMenu.getComponents().getComponent().length;
                        org.cagrid.grape.model.Component[] newComponents = new org.cagrid.grape.model.Component[existingMenu.getComponents().getComponent().length + extensionMenu.getComponents().getComponent().length];
                        System.arraycopy(existingMenu.getComponents().getComponent(), 0, newComponents, 0, existingMenuSize);
                        System.arraycopy(extensionMenu.getComponents().getComponent(), 0, newComponents, existingMenuSize, extensionMenu.getComponents().getComponent().length);
                        existingMenu.getComponents().setComponent(newComponents);
                    } else {
                        existingMenu.setComponents(extensionMenu.getComponents());
                    }
                    
                } else {
                    //need to add a new menu
                    Menu[] newMenus = new Menu [app.getMenus().getMenu().length + 1];
                    System.arraycopy(app.getMenus().getMenu(), 0, newMenus, 0, app.getMenus().getMenu().length);
                    newMenus[app.getMenus().getMenu().length+1] = extensionMenu;
                    app.getMenus().setMenu(newMenus);
                }
            }
            
            
        }
    }


    private void setConfigurationOptions() {
        try {
            if (!hasKey(ResourceManager.getConfigurationPropertyKeys(),
                IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY)) {
                ResourceManager.setConfigurationProperty(IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY,
                    NamespaceTypeDiscoveryComponent.ERROR_POLICY);
            }
            ResourceManager.setConfigurationProperty(IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY
                + ".options", NamespaceTypeDiscoveryComponent.ERROR_POLICY + "," + NamespaceTypeDiscoveryComponent.REPLACE_POLICY
                + "," + NamespaceTypeDiscoveryComponent.IGNORE_POLICY);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
