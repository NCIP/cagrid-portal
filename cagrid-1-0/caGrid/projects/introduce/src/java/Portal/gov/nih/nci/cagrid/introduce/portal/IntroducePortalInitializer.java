package gov.nih.nci.cagrid.introduce.portal;
import gov.nih.nci.cagrid.introduce.portal.help.IntroduceHelp;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.projectmobius.portal.GridPortalInitializer;
import org.projectmobius.portal.PortalResourceManager;

public class IntroducePortalInitializer implements GridPortalInitializer {
	private static final int HELP_MENU = 4;

	public void intialize() throws Exception {
		IntroduceHelp help = new IntroduceHelp();
		JMenu helpMenu = PortalResourceManager.getInstance().getGridPortal().getJMenuBar().getMenu(HELP_MENU);
		JMenuItem helpMenuItem = new JMenuItem("Introduce Help");
		helpMenuItem.addActionListener(help.getFDisplayHelp());
		helpMenu.add(helpMenuItem);
	}

}
