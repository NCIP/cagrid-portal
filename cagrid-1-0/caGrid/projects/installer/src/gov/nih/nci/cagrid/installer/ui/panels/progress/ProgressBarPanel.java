package gov.nih.nci.cagrid.installer.ui.panels.progress;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;
import gov.nih.nci.cagrid.installer.workers.NetworkResourceGetter;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;
import gov.nih.nci.cagrid.installer.workers.swing.DownloadFileTask;
import gov.nih.nci.cagrid.installer.workers.swing.StringDisplayerTask;
import gov.nih.nci.cagrid.installer.workers.swing.LocalInstallerLauncher;


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
    	
    }
    
    private void buildGUI(){
    	this.setLayout(new BorderLayout());
    	//this.setOpaque(false);
    	result = new JTextArea("Installing \n",9,10);
    	
    	result.setCursor(null);
    	result.setBackground(Color.BLACK);
    	result.setForeground(Color.GREEN);
    	Font f = new Font("Courier",Font.BOLD,16);
    	result.setLineWrap(true);
    	
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
    
    
    public void installComponents(){
    	
    	ThreadManager tm = prepareTasks();
    	tm.executeTasks();
    	
    }
    
    private ThreadManager prepareTasks(){
    	boolean ant_exist = true;
    	Installer is = Installer.getInstance();
    	is.addOrUpdateProperty("antExist","yes");
    	String antExist = is.getProperty("antExist");
    	if(antExist.equalsIgnoreCase("no")){
    		ant_exist= false;
    	}
    	String GRID_HOME = is.getProperty("GRID_HOME");
    	String downloadDirName = GRID_HOME+File.separator+"downloads";
    	File downloadDir = new File(downloadDirName);
    	if(!downloadDir.exists()){
    		downloadDir.mkdir();
    	}
    	ArrayList tasks = new ArrayList();
    	ThreadManager tm = new ThreadManager(tasks);
    	
    	SwingWorker sw1 = new StringDisplayerTask(result," Downloading caGrid packs .....\n",progressBar,true,tm);
    	/**
    	 *  Set the parameter for caGrid packs
    	 */
    	SwingWorker sw2 = new DownloadFileTask(downloadDirName,
    										   "ftp://ftp.globus.org/pub/gt4/4.0/4.0.0/ws-core/bin//ws-core-4.0.0-bin.zip",
    										   "ws-core-4.0.0-bin.zip",
    										   result,
    										   progressBar,
    										   tm);
    	
    	SwingWorker sw3 = new LocalInstallerLauncher(tm);
    	tasks.add(0,sw1);
    	tasks.add(1,sw2);
    	tasks.add(2,sw3);
    	
    	
    	if(!ant_exist){
    		String ant_home = is.getProperty("ANT_install_dir");
    		SwingWorker sw4 = new StringDisplayerTask(result," Downloading Apache Ant .....\n",progressBar,true,tm);
    		SwingWorker sw5 = new DownloadFileTask(downloadDirName,
					   "http://apache.secsup.org/dist/ant/binaries/apache-ant-1.6.5-bin.zip",
					   "apache-ant-1.6.5-bin.zip",
					   result,
					   progressBar,
					   tm);
    		SwingWorker sw6 = new StringDisplayerTask(result," Apache Ant download completed !\n",progressBar,true,tm);
    		
    		
    		
    	}
    	
    	
    	return tm;
    }
    
    
    
    
    
    
    
	

}
