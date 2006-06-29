package gov.nih.nci.cagrid.installer.ui.panels.info;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.SystemSniffer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InfoTextPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InfoTextPanel() {
		super();
		this.setBackground(Color.WHITE);

		JTextArea jta = new JTextArea(10, 50);

		SystemSniffer ss = new SystemSniffer();

		jta.setText(ss.checkSystem());

		// this.add(jta);
		this.add(new JScrollPane(jta));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setForeground(Color.BLUE);

		super.paintComponent(g);
		ImageScaler is = new ImageScaler();
		ImageIcon icon = new ImageIcon(is.getScaledInstance(
				"images/mainbackground.jpg", 800, 300));
		Image image = icon.getImage();
		if (image != null)
			g.drawImage(image, 0, 0, 800, 300, this);

	}

}
