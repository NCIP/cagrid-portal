package gov.nih.nci.cagrid.installer.ui.panels.welcome;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class WelcomeTextPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	


	public WelcomeTextPanel() {
		this.setBackground(Color.BLUE);
		// this.setLayout(new BorderLayout());
		// this.add(this.getBackgroundImage());
	}

	

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		ImageScaler is = new ImageScaler();
		ImageIcon icon = new ImageIcon(is.getScaledInstance(
				"images/brownbackground.jpg", 601, 240));
		Image image = icon.getImage();
		if (image != null)
			g.drawImage(image, 0, 0, 601, 240, this);
		

	}

	

}
