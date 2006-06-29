package gov.nih.nci.cagrid.installer.ui.panels.input;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputForm extends JPanel implements ActionListener {

	private JButton openButton;

	private JTextField gridHome;

	private JButton antButton;

	private JTextField antHome;

	JFileChooser fc;

	private JPanel ctPanel1;

	private JPanel ctPanel2;

	public InputForm() {

		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		buildGUI();
	}

	private void buildGUI() {
		this.setLayout(new BorderLayout());

		JLabel spacer = new JLabel();
		Dimension d = new Dimension(590, 50);
		spacer.setPreferredSize(d);
		spacer.setOpaque(false);

		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new BorderLayout());

		ctPanel1 = new JPanel();
		ctPanel1.setLayout(new FlowLayout());
		ctPanel1.setOpaque(false);

		JLabel gridHomeLabel = new JLabel("Grid Home");
		gridHomeLabel.setOpaque(false);

		gridHome = new JTextField(20);

		openButton = new JButton("Browse");
		openButton.addActionListener(this);

		ctPanel1.add(gridHomeLabel);
		ctPanel1.add(gridHome);
		ctPanel1.add(openButton);

		ctPanel2 = new JPanel();
		ctPanel2.setLayout(new FlowLayout());
		ctPanel2.setOpaque(false);

		JLabel antHomeLabel = new JLabel("Ant Home");
		gridHomeLabel.setOpaque(false);

		antHome = new JTextField(20);

		antButton = new JButton("Browse");
		antButton.addActionListener(this);

		ctPanel2.add(antHomeLabel);
		ctPanel2.add(antHome);
		ctPanel2.add(antButton);

		centerPanel.add(ctPanel1, BorderLayout.NORTH);
		centerPanel.add(ctPanel2, BorderLayout.CENTER);

		this.add(spacer, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				gridHome.setText(file.getAbsolutePath());
				Installer is = Installer.getInstance();
				is.addOrUpdateProperty("GRID_HOME", file.getAbsolutePath());
			}
		}

		if (e.getSource() == antButton) {
			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				antHome.setText(file.getAbsolutePath());
				Installer is = Installer.getInstance();
				is.addOrUpdateProperty("ANT_HOME", file.getAbsolutePath());
			}
		}

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

	public void synch() {

		String ant_home = System.getenv("ANT_HOME");
		boolean antExist = true;
		if (ant_home == null) {
			antExist = false;
		}

		if (antExist) {
			ctPanel2.setVisible(false);
		}

	}

}
