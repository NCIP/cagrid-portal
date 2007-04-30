package gov.nih.nci.cagrid.introduce.portal.init;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.help.IntroduceHelp;
import gov.nih.nci.cagrid.introduce.portal.preferences.PreferencesDialog;
import gov.nih.nci.cagrid.introduce.portal.updater.IntroduceUpdateWizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.projectmobius.portal.GridPortalInitializer;
import org.projectmobius.portal.PortalResourceManager;


public class IntroducePortalInitializer implements GridPortalInitializer {
	private static final Logger logger = Logger.getLogger(IntroducePortalInitializer.class);
	private static final int HELP_MENU = 4;

	private static final int CONFIG_MENU = 3;


	public void intialize() throws Exception {
		PropertyConfigurator.configure("." + File.separator + "conf" + File.separator + "introduce" + File.separator
			+ "log4j.properties");

		ExtensionsLoader.getInstance();
		prepareMenus();
		checkGlobusLocation();
	}
	
	private void prepareMenus(){
		IntroduceHelp help = new IntroduceHelp();
		JMenu helpMenu = PortalResourceManager.getInstance().getGridPortal().getJMenuBar().getMenu(HELP_MENU);
		JMenuItem helpMenuItem = new JMenuItem("Introduce Help", IntroduceLookAndFeel.getHelpIcon());
		helpMenuItem.setMnemonic(KeyEvent.VK_F1);
		helpMenuItem.addActionListener(help.getFDisplayHelp());
		helpMenu.insert(helpMenuItem, 0);
		JMenuItem updateMenuItem = new JMenuItem("Check for Updates", IntroduceLookAndFeel.getUpdateIcon());
		updateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IntroduceUpdateWizard.showUpdateWizard();
			}
		});
		helpMenu.insert(updateMenuItem, 1);

		JMenu configMenu = PortalResourceManager.getInstance().getGridPortal().getJMenuBar().getMenu(CONFIG_MENU);
		JMenuItem configMenuItem = new JMenuItem("Preferences");

		configMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreferencesDialog preferences = new PreferencesDialog(PortalResourceManager.getInstance()
					.getGridPortal());
				// user want to configure preferences....
				PortalUtils.centerWindow(preferences);
				preferences.setVisible(true);
			}
		});
		configMenu.insert(configMenuItem, 0);
	}
	

    private void checkGlobusLocation() {
        String currGlobusLocation = ResourceManager.getConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION);
        if ((currGlobusLocation == null) || (currGlobusLocation.length() == 0)) {
            try {
                String globusLocation = System.getenv("GLOBUS_LOCATION");
                ResourceManager.setConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION, globusLocation);
            } catch (Throwable ex) {
                ex.printStackTrace();
                String[] error = {"Error getting GLOBUS_LOCATION environment variable: ", ex.getMessage(),
                        "Please set GLOBUS_LOCATION in preferences!"};
                ErrorDialog.showErrorDialog("Error getting GLOBUS_LOCATION", error);
                try {
                    ResourceManager.setConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION, "");
                } catch (Exception configEx) {
                    // now what?
                    configEx.printStackTrace();
                    ErrorDialog.showErrorDialog(configEx);
                }
            }
        }
    }


}
