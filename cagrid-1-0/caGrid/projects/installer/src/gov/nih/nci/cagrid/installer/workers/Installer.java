package gov.nih.nci.cagrid.installer.workers;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.SwingUtilities;


import gov.nih.nci.cagrid.installer.ui.panels.info.InfoPanel;
import gov.nih.nci.cagrid.installer.ui.panels.input.InputFormPanel;
import gov.nih.nci.cagrid.installer.ui.panels.progress.ProgressPanel;
import gov.nih.nci.cagrid.installer.ui.panels.welcome.WelcomePanel;
import gov.nih.nci.cagrid.installer.ui.windows.InstallerWindow;

public class Installer {

	private static Installer instance;
	private InstallerWindow iw;
	private ArrayList<gov.nih.nci.cagrid.installer.ui.panels.GridPanel> panels = new ArrayList<gov.nih.nci.cagrid.installer.ui.panels.GridPanel>();
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
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel welcome = (gov.nih.nci.cagrid.installer.ui.panels.GridPanel)panels.get(0);
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
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel gp = (gov.nih.nci.cagrid.installer.ui.panels.GridPanel)panels.get(counter-1);
		gp.setVisible(false);
		iw.getContentPane().remove(gp);
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel gp1 = (gov.nih.nci.cagrid.installer.ui.panels.GridPanel)panels.get(counter);
		gp1.synch();
		gp1.setVisible(true);
		iw.getContentPane().add(gp1);
		
	}
	
	public void prev(){
		counter--;
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel gp = (gov.nih.nci.cagrid.installer.ui.panels.GridPanel)panels.get(counter+1);
		gp.setVisible(false);
		iw.getContentPane().remove(gp);
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel gp1 = (gov.nih.nci.cagrid.installer.ui.panels.GridPanel)panels.get(counter);
		gp1.synch();
		gp1.setVisible(true);
		iw.getContentPane().add(gp1);
	}
	
	public void quit(){
		System.exit(0);
	}
	
	private void initPanels(){
		
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel welcome = new WelcomePanel();
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel info = new InfoPanel();
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel input = new InputFormPanel();
		progress = new ProgressPanel();
		
		panels.add(0,welcome);
		panels.add(1,info);
		panels.add(2,input);
		panels.add(3,(gov.nih.nci.cagrid.installer.ui.panels.GridPanel)progress);
	
		
		
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
