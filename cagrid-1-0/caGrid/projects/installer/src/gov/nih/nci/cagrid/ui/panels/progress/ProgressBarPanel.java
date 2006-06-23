package gov.nih.nci.cagrid.ui.panels.progress;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;
import gov.nih.nci.cagrid.installer.workers.NetworkResourceGetter;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;
import gov.nih.nci.cagrid.installer.workers.swing.DownloadFileTask;
import gov.nih.nci.cagrid.installer.workers.swing.StringDisplayerTask;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ProgressBarPanel extends JPanel{

	
	private static final long serialVersionUID = 1L;
	
    private JProgressBar progressBar;
    private JTextArea result;
    private JScrollPane jsp;
    
    public ProgressBarPanel(){
    	
    	buildGUI();
    	//progressBar.setIndeterminate(true);
    }
    
    private void buildGUI(){
    	this.setLayout(new BorderLayout());
    	//this.setOpaque(false);
    	result = new JTextArea("Installing \n",8,10);
    	
    	result.setCursor(null);
    	result.setBackground(Color.BLACK);
    	result.setForeground(Color.GREEN);
    	Font f = new Font("Courier",Font.BOLD,16);
    	
    	result.setFont(f);
    	progressBar = new JProgressBar();
    	progressBar.setPreferredSize(new Dimension(300,20));
    	progressBar.setBackground(Color.BLACK);
    	
    	Color clr = new Color(205,205,0);
    	
    	progressBar.setForeground(clr);
    	//this.add(result);
    	jsp = new JScrollPane(result);
    	this.add(jsp,BorderLayout.PAGE_START);
    	this.add(progressBar,BorderLayout.CENTER);
    	
    }
    

    public void stop(){
    	progressBar.setIndeterminate(false);
  	  
    }
    public void start(){
    	System.out.println("Starting");
    	progressBar.setIndeterminate(true);
  	  
    }
    
    public void paintComponent(Graphics g)
	 {  super.paintComponent(g);
	   this.setForeground(Color.BLUE);

	    
	    
	    super.paintComponent(g);
	    ImageScaler is = new ImageScaler();
			ImageIcon icon = new ImageIcon(is.getScaledInstance("images/mainbackground.jpg",600,500));
			Image image = icon.getImage();
	    if(image != null) g.drawImage(image, 0,0,600,500,this);
	    

	    
	 }
    
    private void addText(String s){
    	result.append(s);
    }
    
    public void installComponents(){
    	System.out.println("Here we go");
    	ThreadManager tm = prepareTasks();
    	tm.executeTasks();
    }
    
    private ThreadManager prepareTasks(){
    	System.out.println("Here we go 1");
    	Installer is = Installer.getInstance();
    	String GRID_HOME = is.getProperty("GRID_HOME");
    	String downloadDirName = GRID_HOME+File.separator+"downloads";
    	File downloadDir = new File(downloadDirName);
    	if(!downloadDir.exists()){
    		downloadDir.mkdir();
    	}
    	ArrayList<SwingWorker> tasks = new ArrayList<SwingWorker>();
    	ThreadManager tm = new ThreadManager(tasks);
    	
    	SwingWorker sw1 = new StringDisplayerTask(result," Downloading caGrid packs .....\n",progressBar,true,tm);
    	/**
    	 *  Set the parameter for caGrid packs
    	 */
    	SwingWorker sw2 = new DownloadFileTask(downloadDirName,
    										   "ftp://ftp.globus.org/pub/gt4/4.0/4.0.0/ws-core/bin//ws-core-4.0.0-bin.zip",
    										   "ws-core-4.0.0-bin.zip",
    										   tm);
    	SwingWorker sw3 = new StringDisplayerTask(result," Downloading caGrid packs completed !\n",progressBar,false,tm);
    	
    	tasks.add(0,sw1);
    	tasks.add(1,sw2);
    	tasks.add(2,sw3);
    	
    	return tm;
    }
    
    
    public void installComponentsXX(){
    	File downloadSourceDir = null;
    	File wsCoreSourcefile = null;
    	File gridDir = null;
    	String ANT_HOME = null;
    	Installer is = Installer.getInstance();
    	String GRID_HOME = is.getProperty("GRID_HOME");
    	
    	String  ant_home = System.getenv("ANT_HOME");
		boolean antExist = true;
		if(ant_home==null){
			antExist = false;
		}
		
		if(!antExist){
			ANT_HOME = is.getProperty("ANT_HOME");
		}
    	
    	NetworkResourceGetter nrg = new NetworkResourceGetter();
		
		
    	
		this.start();
		/**
		nrg.getWs_Core(GRID_HOME);
		result.append("\t Downloading caGrid.....");
		this.stop();
		pause();
		try{
			String downloadSourceDirName = GRID_HOME+File.separator+"downloads";
			downloadSourceDir = new File(downloadSourceDirName);
			wsCoreSourcefile = new File(downloadSourceDir,"ws-core-4.0.0-bin.zip");
			gridDir = new File(GRID_HOME);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.start();
		*/
		result.append("\t \n Unpacking caGrid.....");
		//Unzip uz = new Unzip(wsCoreSourcefile,gridDir);
		pause();
		this.stop();
		pause();
		if(!antExist){
			installANT();
		}
		result.append("\t Initial system setup is complete !");
		result.append("\t Please lauch the local installer for other components !");
		
    }
    
    private void installANT(){
    	this.start();
		result.append("\t Downloading Ant.....");
		this.stop();
		pause();
		this.start();
		result.append("\t Unpacking ANT.....");
		this.stop();
		pause();
		result.append("\t Installing ANT");
		this.start();
		pause();
    }
    
    private void pause(){
    	try{
    		Thread.sleep(2000);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
	

}
