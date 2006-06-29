package gov.nih.nci.cagrid.installer.workers.swing;

import java.io.IOException;

import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;

public class LocalInstallerLauncher extends SwingWorker{
	private String fileName;
	private ThreadManager tm;
	
	public LocalInstallerLauncher(String fileName,ThreadManager tm){
		this.fileName=fileName;
		this.tm=tm;
	}

	public Object construct() {
		// TODO Auto-generated method stub
		try {
	        // Execute a command without arguments
	        String execPath = "java -jar "+fileName;
	        System.out.println(execPath);
	        //Process child = Runtime.getRuntime().exec("java -jar C:\\AntInstaller_development\\caGrid_Installer_v1\\selfextractpack.jar");
	        Process child = Runtime.getRuntime().exec(execPath);
	        
	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }

		return null;
	}
	
	public void finished(){
		tm.notifyFinished();
		System.exit(0);
	}

}
