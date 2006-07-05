package gov.nih.nci.cagrid.installer.ui.panels.common;

import gov.nih.nci.cagrid.installer.ui.panels.GridPanel;
import gov.nih.nci.cagrid.installer.utils.ImageScaler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class TopPanel extends GridPanel {

	private ImageIcon icon;

	private Image image;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TopPanel() {

		Dimension d = new Dimension(600, 40);

		this.setPreferredSize(d);
		this.setOpaque(true);

		/**
		 * 
		 * icon= new
		 * ImageIcon(this.getClass().getClassLoader().getResource("caGrid.jpg"));
		 * image =icon.getImage();
		 * 
		 * 
		 */

		ImageScaler is = new ImageScaler();

		icon = new ImageIcon(is.getScaledInstance("images/caGrid.jpg", 600, 40));
		image = icon.getImage();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(image, 0, 0, this);

	}

	public void synch() {
		;
		

	}

}
