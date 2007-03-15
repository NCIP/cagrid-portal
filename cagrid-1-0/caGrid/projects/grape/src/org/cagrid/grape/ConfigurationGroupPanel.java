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

public class ConfigurationGroupPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ConfigurationGroup group;

	private JLabel groupName = null;

	private Panel descriptionPanel = null;

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
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.ipadx = 296;
		gridBagConstraints1.ipady = 180;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 300;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		groupName = new JLabel();
		groupName.setText(group.getName());
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(groupName, gridBagConstraints);
		this.add(getDescriptionPanel(), gridBagConstraints1);
	}

	/**
	 * This method initializes descriptionPanel	
	 * 	
	 * @return java.awt.Panel	
	 */
	private Panel getDescriptionPanel() {
		if (descriptionPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0;
			descriptionPanel = new Panel();
			descriptionPanel.setLayout(new GridBagLayout());
			descriptionPanel.add(getDescription(), gridBagConstraints2);
		}
		return descriptionPanel;
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
			description.setPreferredSize(new Dimension(0, 0));
		}
		return description;
	}

}
