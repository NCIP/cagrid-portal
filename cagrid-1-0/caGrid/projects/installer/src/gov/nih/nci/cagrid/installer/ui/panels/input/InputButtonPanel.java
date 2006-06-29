package gov.nih.nci.cagrid.installer.ui.panels.input;

import gov.nih.nci.cagrid.installer.ui.panels.GridPanel;
import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class InputButtonPanel extends GridPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 168768689L;

	public InputButtonPanel() {

		this.setBackground(Color.BLUE);
		addButtons();
		// TODO Auto-generated constructor stub
	}

	private void addButtons() {
		back = new JButton("Back");
		next = new JButton("Next");
		quit = new JButton("Quit");
		back.addActionListener(this);
		next.addActionListener(this);
		quit.addActionListener(this);
		this.add(back);
		this.add(next);
		this.add(quit);
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		Installer is = Installer.getInstance();
		if (source == next) {
			String directory = is.getProperty("GRID_HOME");
			if (directory != null) {
				is.next();

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

	

	private JButton next;

	private JButton quit;

	private JButton back;

	public void synch() {
		// TODO Auto-generated method stub

	}

}
