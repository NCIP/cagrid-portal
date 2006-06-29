package gov.nih.nci.cagrid.installer.ui.panels.info;

import gov.nih.nci.cagrid.installer.ui.panels.GridPanel;
import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;
import gov.nih.nci.cagrid.installer.workers.SystemSniffer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class InfoButtonPanel extends GridPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 168768689L;

	public InfoButtonPanel() {

		this.setBackground(Color.BLUE);
		addButtons();
		// TODO Auto-generated constructor stub
	}

	private void addButtons() {
		back = new JButton("Back");
		next = new JButton("Next");
		quit = new JButton("Quit");
		finish = new JButton("Finish");
		back.addActionListener(this);
		next.addActionListener(this);
		quit.addActionListener(this);
		finish.addActionListener(this);
		this.add(back);
		this.add(next);
		this.add(quit);
		this.add(finish);

	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		Installer is = Installer.getInstance();
		if (source == next) {
			is.next();
		}
		if (source == quit || source == finish) {

			is.quit();
		}
		if (source == back) {

			is.prev();

		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setForeground(Color.BLUE);

		super.paintComponent(g);
		ImageScaler is = new ImageScaler();
		ImageIcon icon = new ImageIcon(is.getScaledInstance(
				"images/control.jpg", 800, 75));
		Image image = icon.getImage();
		if (image != null)
			g.drawImage(image, 0, 0, 800, 75, this);

	}

	private JButton next;

	private JButton quit;

	private JButton back;

	private JButton finish;

	public void synch() {

		// TODO Auto-generated method stub

		Installer is = Installer.getInstance();
		SystemSniffer ss = new SystemSniffer();
		boolean isVirgin = ss.isFirstInstallation();
		boolean caGridExist = !isVirgin;
		String ant_home = System.getenv("ANT_HOME");
		boolean antExist = true;
		if (ant_home != null) {
			antExist = false;
		}

		if (caGridExist && antExist) {
			quit.setVisible(false);
		}

		if ((!caGridExist) && (antExist)) {
			finish.setVisible(false);
		}

		if ((!caGridExist) && (!antExist)) {
			finish.setVisible(false);
		}

	}

}
