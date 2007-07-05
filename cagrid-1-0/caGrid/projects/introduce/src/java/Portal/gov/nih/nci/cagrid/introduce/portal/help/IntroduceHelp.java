package gov.nih.nci.cagrid.introduce.portal.help;

import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JFrame;


public class IntroduceHelp extends JFrame {
	private HelpBroker fHelp;
	private CSH.DisplayHelpFromSource fDisplayHelp;
	private HelpSet helpSet;


	public IntroduceHelp() {
		initHelpSystem();
	}


	public CSH.DisplayHelpFromSource getFDisplayHelp() {
		return fDisplayHelp;
	}


	public void setFDisplayHelp(CSH.DisplayHelpFromSource displayHelp) {
		fDisplayHelp = displayHelp;
	}


	public HelpBroker getFHelp() {
		return fHelp;
	}


	public void setFHelp(HelpBroker help) {
		fHelp = help;
	}


	public HelpSet getHelpSet() {
		return helpSet;
	}


	public void setHelpSet(HelpSet helpSet) {
		this.helpSet = helpSet;
	}


	/**
	 * Initialize the JavaHelp system.
	 */
	public void initHelpSystem() {
		// optimization to avoid repeated init
		if (fHelp != null && fDisplayHelp != null)
			return;

		// (uses the classloader mechanism)
		ClassLoader loader = this.getClass().getClassLoader();
		URL helpSetURL = HelpSet.findHelpSet(loader, "introduce.hs");
		try {
			helpSet = new HelpSet(null, helpSetURL);
			fHelp = helpSet.createHelpBroker();
			fHelp.enableHelpKey(this.getRootPane(), "index", helpSet);
			fDisplayHelp = new CSH.DisplayHelpFromSource(fHelp);

		} catch (HelpSetException ex) {
			System.out.println("Cannot create help system with: " + helpSetURL + " " + ex.getMessage());
		}
	}

}
