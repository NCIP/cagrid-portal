package gov.nih.nci.cagrid.introduce.portal.init;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.help.IntroduceHelp;
import gov.nih.nci.cagrid.introduce.portal.preferences.PreferencesDialog;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.projectmobius.portal.GridPortalInitializer;
import org.projectmobius.portal.PortalResourceManager;

public class IntroducePortalInitializer implements GridPortalInitializer {
	private static final int HELP_MENU = 4;

	private static final int CONFIG_MENU = 3;

	public void intialize() throws Exception {
		ExtensionsLoader.getInstance();

		IntroduceHelp help = new IntroduceHelp();
		JMenu helpMenu = PortalResourceManager.getInstance().getGridPortal()
				.getJMenuBar().getMenu(HELP_MENU);
		JMenuItem helpMenuItem = new JMenuItem("Introduce Help",
				IntroduceLookAndFeel.getHelpIcon());
		helpMenuItem.setMnemonic(KeyEvent.VK_F1);
		helpMenuItem.addActionListener(help.getFDisplayHelp());
		helpMenu.insert(helpMenuItem, 0);

		JMenu configMenu = PortalResourceManager.getInstance().getGridPortal()
				.getJMenuBar().getMenu(CONFIG_MENU);
		JMenuItem configMenuItem = new JMenuItem("Preferences");
		final PreferencesDialog preferences = new PreferencesDialog(PortalResourceManager.getInstance().getGridPortal());
		configMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// user want to configure preferences....
				preferences.setVisible(true);
			}

		});
		configMenu.insert(configMenuItem, 0);
		
	}

}
