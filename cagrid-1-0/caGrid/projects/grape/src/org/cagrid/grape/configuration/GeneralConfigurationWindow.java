package org.cagrid.grape.configuration;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cagrid.grape.ApplicationContext;
import org.cagrid.grape.ConfigurationComponent;

public class GeneralConfigurationWindow extends ConfigurationComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel label = null;
	
	private GeneralConfiguration conf;

	/**
	 * This is the default constructor
	 */
	public GeneralConfigurationWindow(ApplicationContext context, String systemName, GeneralConfiguration conf) {
		super(context,systemName,conf);
		initialize();
		this.conf = conf;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.ipady = 0;
			gridBagConstraints.gridy = 0;
			label = new JLabel();
			label.setFont(new Font("Dialog", Font.BOLD, 36));
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(label, gridBagConstraints);
		}
		return jContentPane;
	}

}
