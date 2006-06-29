package gov.nih.nci.cagrid.installer.workers.swing;

import gov.nih.nci.cagrid.installer.utils.Logutil;
import gov.nih.nci.cagrid.installer.workers.NetworkResourceGetter;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class DownloadFileTask extends SwingWorker {

	private String destDirName;

	private String url;

	private String fileName;

	private ThreadManager tm;

	private JTextArea jta;

	private JProgressBar jbar;

	private StringBuffer stbr = new StringBuffer();

	private Logger logger = Logutil.getLogger();

	public DownloadFileTask(String destDirName, String url, String fileName,
			JTextArea jta, JProgressBar jbar, ThreadManager tm) {
		this.destDirName = destDirName;
		this.url = url;
		this.fileName = fileName;
		this.tm = tm;
		this.jta = jta;
		this.jbar = jbar;
	}

	public Object construct() {
		// TODO Auto-generated method stub

		NetworkResourceGetter nrg = new NetworkResourceGetter();

		try {
			nrg.downLoadFile(destDirName, url, fileName);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			stbr.append("problem downloading the file:" + e.getMessage());
		}

		return null;
	}

	public void finished() {
		if (stbr.length() > 0) {
			jta.setForeground(Color.RED);
			jta.append(stbr.toString());
			jta.setForeground(Color.GREEN);
		} else {
			jta.append("Download complete !");

		}
		jbar.setIndeterminate(false);
		tm.notifyFinished();

	}

}
