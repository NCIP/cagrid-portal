package gov.nih.nci.cagrid.installer.workers;

import gov.nih.nci.cagrid.installer.ui.panels.GridPanel;
import gov.nih.nci.cagrid.installer.ui.panels.info.InfoPanel;
import gov.nih.nci.cagrid.installer.ui.panels.input.InputFormPanel;
import gov.nih.nci.cagrid.installer.ui.panels.progress.ProgressPanel;
import gov.nih.nci.cagrid.installer.ui.panels.welcome.WelcomePanel;
import gov.nih.nci.cagrid.installer.ui.windows.InstallerWindow;
import gov.nih.nci.cagrid.installer.utils.Logutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Installer {

	private static Installer instance;

	private InstallerWindow iw;

	private ArrayList panels = new ArrayList();

	private int counter = 0;

	private Properties vars;

	private String userHome;

	private ProgressPanel progress;

	private Logger logger;

	private Installer() {

		vars = new Properties();
		Properties sysProperties = System.getProperties();
		userHome = sysProperties.getProperty("user.home");

		logger = Logutil.getLogger();
		logger.log(Level.INFO, "user home:" + userHome);
		iw = new InstallerWindow();

		initPanels();
		GridPanel welcome = (GridPanel) panels.get(0);
		welcome.setVisible(true);
		iw.getContentPane().add(welcome);

		iw.setVisible(true);
	}

	public static synchronized Installer getInstance() {
		if (instance == null) {
			instance = new Installer();
		}

		return instance;
	}

	public void next() {
		counter++;
		GridPanel gp = (GridPanel) panels.get(counter - 1);
		gp.setVisible(false);
		iw.getContentPane().remove(gp);
		GridPanel gp1 = (GridPanel) panels.get(counter);
		gp1.synch();
		gp1.setVisible(true);
		iw.getContentPane().add(gp1);

	}

	public void prev() {
		counter--;
		GridPanel gp = (GridPanel) panels.get(counter + 1);
		gp.setVisible(false);
		iw.getContentPane().remove(gp);
		GridPanel gp1 = (GridPanel) panels.get(counter);
		gp1.synch();
		gp1.setVisible(true);
		iw.getContentPane().add(gp1);
	}

	public void quit() {
		System.exit(0);
	}

	private void initPanels() {

		GridPanel welcome = new WelcomePanel();
		GridPanel info = new InfoPanel();
		GridPanel input = new InputFormPanel();
		progress = new ProgressPanel();

		panels.add(0, welcome);
		panels.add(1, info);
		panels.add(2, input);
		panels.add(3, (GridPanel) progress);

	}

	public void addOrUpdateProperty(String name, String value) {
		vars.setProperty(name, value);
	}

	public String getProperty(String name) {
		return vars.getProperty(name);
	}

	public void install() {
		storeGridHome();
		progress.install();
	}

	private void storeGridHome() {
		Properties pr = new Properties();
		String grid_home = this.getProperty("GRID_HOME");
		pr.put("GRID_HOME", grid_home);

		Properties p = System.getProperties();
		String userHome = p.getProperty("user.home");

		try {
			OutputStream ops = new FileOutputStream(userHome + File.separator
					+ "gridhome.properties", false);
			pr.store(ops, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String GRID_RELEASE_URL=args[0];
		
		String GRID_FILE=args[1];
		
		Installer is = Installer.getInstance();
		is.addOrUpdateProperty("GRID_RELEASE_URL",GRID_RELEASE_URL);
		is.addOrUpdateProperty("GRID_FILE",GRID_FILE);
		
	}

}
