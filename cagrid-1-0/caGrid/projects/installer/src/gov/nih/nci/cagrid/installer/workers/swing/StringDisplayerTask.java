package gov.nih.nci.cagrid.installer.workers.swing;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;

public class StringDisplayerTask extends SwingWorker{

	private JTextArea jta;
	private String s;
	private ThreadManager tm;
	private JProgressBar jbar;
	private boolean working;
	
	public StringDisplayerTask(JTextArea jta,String s, JProgressBar jbar,boolean working,ThreadManager tm){
		this.jta=jta;
		this.s=s;
		this.jbar=jbar;
		this.working=working;
		this.tm=tm;
	}
	
	public Object construct() {
		// TODO Auto-generated method stub
		return null;
	}
	public void finished(){
		jta.append(s);
		jbar.setIndeterminate(working);
		tm.notifyFinished();
	}

}
