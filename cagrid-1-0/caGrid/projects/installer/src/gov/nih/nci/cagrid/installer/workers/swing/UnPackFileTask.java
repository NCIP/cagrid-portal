package gov.nih.nci.cagrid.installer.workers.swing;

import java.io.File;

import gov.nih.nci.cagrid.installer.workers.Expander;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;

public class UnPackFileTask extends SwingWorker{
	
	private File src;
	private File dest;
	private ThreadManager tm;
	
	public UnPackFileTask(File src,File dest,ThreadManager tm){
		this.src=src;
		this.dest=dest;
		this.tm=tm;
	}
	@Override
	public Object construct() {
		// TODO Auto-generated method stub
		Expander exp = new Expander(src,dest);
		exp.expand();
		return null;
	}
	
	public void finished(){
		tm.notifyFinished();
	}

}
