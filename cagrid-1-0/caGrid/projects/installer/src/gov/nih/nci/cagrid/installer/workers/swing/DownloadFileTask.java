package gov.nih.nci.cagrid.installer.workers.swing;

import gov.nih.nci.cagrid.installer.workers.NetworkResourceGetter;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;

public class DownloadFileTask extends SwingWorker{

	private String destDirName;
	private String url;
	private String fileName;
	private ThreadManager tm;
	
	public DownloadFileTask(String destDirName,
								String url,
								String fileName,
								ThreadManager tm){
		this.destDirName=destDirName;
		this.url=url;
		this.fileName=fileName;
		this.tm=tm;
	}
	
	@Override
	public Object construct() {
		// TODO Auto-generated method stub
		
		NetworkResourceGetter nrg = new NetworkResourceGetter();
		nrg.downLoadFile(destDirName,url,fileName);
		
		return null;
	}
	
	public void finished(){
		tm.notifyFinished();
		
	}

}
