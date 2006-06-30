package gov.nih.nci.cagrid.installer.ui.panels.progress;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;
import gov.nih.nci.cagrid.installer.workers.SwingWorker;
import gov.nih.nci.cagrid.installer.workers.ThreadManager;
import gov.nih.nci.cagrid.installer.workers.swing.DownloadFileTask;
import gov.nih.nci.cagrid.installer.workers.swing.LocalInstallerLauncher;
import gov.nih.nci.cagrid.installer.workers.swing.StringDisplayerTask;
import gov.nih.nci.cagrid.installer.workers.swing.UnPackFileTask;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProgressBarPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar;

	private JTextArea result;

	private JScrollPane jsp;

	public ProgressBarPanel() {

		buildGUI();

	}

	private void buildGUI() {
		this.setLayout(new BorderLayout());
		// this.setOpaque(false);
		result = new JTextArea("Installing \n", 9, 10);

		result.setCursor(null);
		result.setBackground(Color.BLACK);
		result.setForeground(Color.GREEN);
		Font f = new Font("Courier", Font.BOLD, 16);
		result.setLineWrap(true);

		result.setFont(f);
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(300, 20));
		progressBar.setBackground(Color.BLACK);

		Color clr = new Color(205, 205, 0);

		progressBar.setForeground(clr);
		// this.add(result);
		jsp = new JScrollPane(result);
		this.add(jsp, BorderLayout.PAGE_START);
		this.add(progressBar, BorderLayout.CENTER);

	}

	public void stop() {
		progressBar.setIndeterminate(false);

	}

	public void start() {
		System.out.println("Starting");
		progressBar.setIndeterminate(true);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setForeground(Color.BLUE);

		super.paintComponent(g);
		ImageScaler is = new ImageScaler();
		ImageIcon icon = new ImageIcon(is.getScaledInstance(
				"images/mainbackground.jpg", 600, 500));
		Image image = icon.getImage();
		if (image != null)
			g.drawImage(image, 0, 0, 600, 500, this);

	}

	public void installComponents() {

		ThreadManager tm = prepareTasks();
		tm.executeTasks();

	}

	private ThreadManager prepareTasks() {
		
		Installer is = Installer.getInstance();
		String GRID_RELEASE_URL=is.getProperty("GRID_RELEASE_URL");
		String GRID_FILE=is.getProperty("GRID_FILE");
		System.out.println("the value of GRID_RELEASE_URL is"+GRID_RELEASE_URL);
		String GRID_HOME = is.getProperty("GRID_HOME");
		String downloadDirName = GRID_HOME + File.separator + "downloads";
		File downloadDir = new File(downloadDirName);
		if (!downloadDir.exists()) {
			downloadDir.mkdir();
		}
		ArrayList tasks = new ArrayList();
		ThreadManager tm = new ThreadManager(tasks);

		SwingWorker sw1 = new StringDisplayerTask(result,
				" Downloading caGrid .....\n", progressBar, true, tm);
		/**
		 * Set the parameter for caGrid packs
		 */
		/**
		 * SwingWorker sw2 = new DownloadFileTask(downloadDirName,
		 * "ftp://ftp.globus.org/pub/gt4/4.0/4.0.0/ws-core/bin//ws-core-4.0.0-bin.zip",
		 * "ws-core-4.0.0-bin.zip", result, progressBar, tm);
		 */
		SwingWorker sw2 = new DownloadFileTask(downloadDirName,GRID_RELEASE_URL,GRID_FILE, result, progressBar, tm);
		File src = new File(downloadDirName, "cagrid-1-0.zip");

		SwingWorker sw3 = new StringDisplayerTask(result,
				" Unpacking caGrid .....\n", progressBar, true, tm);

		File dest = new File(GRID_HOME);
		SwingWorker sw4 = new UnPackFileTask(src, dest, result, progressBar, tm);

		File execFile = new File(GRID_HOME, "cagrid-1-0" + File.separator
				+ "caGrid" + File.separator + "projects" + File.separator
				+ "installer" + File.separator + "executables" + File.separator
				+ "masterinstaller.jar");
		String execFileName = execFile.getAbsolutePath();

		System.out.println("Exec File Name:" + execFileName);
		SwingWorker sw5 = new LocalInstallerLauncher(execFileName, tm);
		tasks.add(0, sw1);
		tasks.add(1, sw2);
		tasks.add(2, sw3);
		tasks.add(3, sw4);
		tasks.add(4, sw5);

		
		return tm;
	}

}
