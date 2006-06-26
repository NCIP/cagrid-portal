package gov.nih.nci.cagrid.installer.workers.swing;

import java.util.logging.Level;
import java.util.logging.Logger;

import gov.nih.nci.cagrid.installer.utils.Logutil;
import gov.nih.nci.cagrid.installer.workers.NetworkResourceGetter;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;

public class DownloadFileTask extends SwingWorker{

	private String destDirName;
	private String url;
	private String fileName;
	private ThreadManager tm;
	private StringBuffer stbr= new StringBuffer();
	private Logger logger = Logutil.getLogger();
	
	public DownloadFileTask(String destDirName,
								String url,
								String fileName,
								ThreadManager tm){
		this.destDirName=destDirName;
		this.url=url;
		this.fileName=fileName;
		this.tm=tm;
	}
	
	
	public Object construct() {
		// TODO Auto-generated method stub
		
		NetworkResourceGetter nrg = new NetworkResourceGetter();
	
		try {
			nrg.downLoadFile(destDirName,url,fileName);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		
		return null;
	}
	
	public void finished(){
		
		tm.notifyFinished();
		
	}

}
