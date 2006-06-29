package gov.nih.nci.cagrid.installer.ui.panels.progress;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ProgressButtonPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 168768689L;

	public ProgressButtonPanel() {

		// this.setBackground(Color.BLUE);
		this.setOpaque(false);
		addButtons();
		// TODO Auto-generated constructor stub
	}

	private void addButtons() {
		back = new JButton("Back");
		install = new JButton("Install");
		quit = new JButton("Quit");
		finish = new JButton("Finish");
		back.addActionListener(this);
		install.addActionListener(this);
		quit.addActionListener(this);
		finish.addActionListener(this);
		this.add(back);
		this.add(install);
		this.add(quit);
		this.add(finish);

		finish.setVisible(false);
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		Installer is = Installer.getInstance();
		if (source == install) {
			String directory = is.getProperty("GRID_HOME");
			if (directory != null) {
				is.install();
			}

		}
		if (source == quit) {

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

	


	private JButton quit;

	private JButton back;

	private JButton install;

	private JButton finish;

	public void activateFinish() {
		finish.setVisible(true);
	}

}
