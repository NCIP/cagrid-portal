package gov.nih.nci.cagrid.installer.workers.swing;

import gov.nih.nci.cagrid.installer.workers.Expander;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;

import java.io.File;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class UnPackFileTask extends SwingWorker {

	private File src;

	private File dest;

	private JTextArea jta;

	private JProgressBar jbar;

	private ThreadManager tm;

	public UnPackFileTask(File src, File dest, JTextArea jta,
			JProgressBar jbar, ThreadManager tm) {
		this.src = src;
		this.dest = dest;
		this.jta = jta;
		this.jbar = jbar;
		this.tm = tm;
	}

	public Object construct() {
		// TODO Auto-generated method stub
		System.out.println(" UnPack File");
		Expander exp = new Expander(src, dest);
		exp.expand();
		return null;
	}

	public void finished() {
		jbar.setIndeterminate(false);
		jta.append("\n File unpacked !");

		tm.notifyFinished();
	}

}
