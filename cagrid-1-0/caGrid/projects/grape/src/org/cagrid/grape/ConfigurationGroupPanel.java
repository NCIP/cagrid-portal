package org.cagrid.grape;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import org.cagrid.grape.model.ConfigurationGroup;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Panel;
import java.awt.Insets;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JScrollPane;

public class ConfigurationGroupPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ConfigurationGroup group;

	private JLabel jLabel = null;

	private JScrollPane jScrollPane = null;

	private JTextArea description = null;

	/**
	 * This is the default constructor
	 */
	public ConfigurationGroupPanel(ConfigurationGroup group) {
		super();
		this.group = group;
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.weightx = 1.0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText(this.group.getName());
		jLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints1);
		this.add(getJScrollPane(), gridBagConstraints2);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getDescription());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes description	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getDescription() {
		if (description == null) {
			description = new JTextArea();
			description.setLineWrap(true);
		}
		return description;
	}

}
