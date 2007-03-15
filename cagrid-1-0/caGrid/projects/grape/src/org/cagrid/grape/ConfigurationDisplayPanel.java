package org.cagrid.grape;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfigurationDisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private String message;

	private JLabel jLabel = null;

	private JLabel logo = null;

	/**
	 * This is the default constructor
	 */
	public ConfigurationDisplayPanel(String message) {
		super();
		this.message = message;
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.gridy = 0;
		logo = new JLabel(LookAndFeel.getLogoNoText());
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.CENTER;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridy = 1;
		jLabel = new JLabel();
		jLabel.setText(message);
		jLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints1);
		this.add(logo, gridBagConstraints);
	}

}
