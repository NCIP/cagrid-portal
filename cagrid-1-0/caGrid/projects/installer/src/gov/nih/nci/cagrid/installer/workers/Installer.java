package gov.nih.nci.cagrid.installer.workers;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.SwingUtilities;

import gov.nih.nci.cagrid.ui.panels.GridPanel;
import gov.nih.nci.cagrid.ui.panels.info.InfoPanel;
import gov.nih.nci.cagrid.ui.panels.input.InputFormPanel;
import gov.nih.nci.cagrid.ui.panels.progress.ProgressPanel;
import gov.nih.nci.cagrid.ui.panels.welcome.WelcomePanel;
import gov.nih.nci.cagrid.ui.windows.InstallerWindow;

public class Installer {

	private static Installer instance;
	private InstallerWindow iw;
	private ArrayList<GridPanel> panels = new ArrayList<GridPanel>();
	private int counter =0;
	private Hashtable session;
	private Properties vars;
	private String userHome;
	private ProgressPanel progress;
	
	private Installer(){
		session = new Hashtable();
		vars = new Properties();
		Properties sysProperties = System.getProperties();
		userHome = sysProperties.getProperty("user.home");
		
		System.out.println(userHome);
		iw = new InstallerWindow();
		
		initPanels();
		GridPanel welcome = (GridPanel)panels.get(0);
		welcome.setVisible(true);
		iw.getContentPane().add(welcome);
		
		iw.setVisible(true);
	}
	
	public static synchronized Installer getInstance(){
		if(instance==null){
			instance = new Installer();
		}
		
		return instance;
	}
	
	public void next(){
		counter++;
		GridPanel gp = (GridPanel)panels.get(counter-1);
		gp.setVisible(false);
		iw.getContentPane().remove(gp);
		GridPanel gp1 = (GridPanel)panels.get(counter);
		gp1.synch();
		gp1.setVisible(true);
		iw.getContentPane().add(gp1);
		
	}
	
	public void prev(){
		counter--;
		GridPanel gp = (GridPanel)panels.get(counter+1);
		gp.setVisible(false);
		iw.getContentPane().remove(gp);
		GridPanel gp1 = (GridPanel)panels.get(counter);
		gp1.synch();
		gp1.setVisible(true);
		iw.getContentPane().add(gp1);
	}
	
	public void quit(){
		System.exit(0);
	}
	
	private void initPanels(){
		
		GridPanel welcome = new WelcomePanel();
		GridPanel info = new InfoPanel();
		GridPanel input = new InputFormPanel();
		progress = new ProgressPanel();
		
		panels.add(0,welcome);
		panels.add(1,info);
		panels.add(2,input);
		panels.add(3,(GridPanel)progress);
	
		
		
	}
	
	public void addOrUpdateProperty(String name, String value){
		vars.setProperty(name,value);
	}
	
	public String getProperty(String name){
		return vars.getProperty(name);
	}
	
	public void install(){
		System.out.println(SwingUtilities.isEventDispatchThread());
		progress.install();
	}
	
	
		
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Installer.getInstance();

	}

}
