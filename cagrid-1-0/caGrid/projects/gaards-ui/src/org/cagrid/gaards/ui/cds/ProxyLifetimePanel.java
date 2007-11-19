package org.cagrid.gaards.ui.cds;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cagrid.gaards.cds.common.ProxyLifetime;

public class ProxyLifetimePanel extends JPanel {

	private static final int MAX_HOURS = 48;

	private static final long serialVersionUID = 1L;

	private JLabel jLabel = null;

	private JComboBox hours = null;

	private JComboBox minutes = null;

	private JLabel jLabel1 = null;

	private JComboBox seconds = null;

	private JLabel jLabel2 = null;

	/**
	 * This is the default constructor
	 */
	public ProxyLifetimePanel() {
		super();
		initialize();
	}

	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 5;
		gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.gridy = 0;
		jLabel2 = new JLabel();
		jLabel2.setText("Seconds");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.gridx = 4;
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints3.weightx = 1.0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridx = 3;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
		jLabel1 = new JLabel();
		jLabel1.setText("Minutes");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints11.gridx = 2;
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.weightx = 1.0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("Hours");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getHours(), gridBagConstraints1);
		this.add(getMinutes(), gridBagConstraints11);
		this.add(jLabel1, gridBagConstraints2);
		this.add(getSeconds(), gridBagConstraints3);
		this.add(jLabel2, gridBagConstraints4);
	}

	public void setLifetime(long lifetimeSeconds) {
		if(lifetimeSeconds<0){
			lifetimeSeconds=0;
		}
		long lhours = (lifetimeSeconds / 60) / 60;
		long hoursInSeconds = lhours * 60 * 60;
		long lminutes = (lifetimeSeconds - hoursInSeconds) / 60;
		long minutesInSeconds = lminutes * 60;
		long lseconds = lifetimeSeconds - hoursInSeconds - minutesInSeconds;
		for (int i = 0; i <= lhours; i++) {
			hours.addItem(new Integer(i));
		}

		for (int i = 0; i <= lminutes; i++) {
			minutes.addItem(new Integer(i));
		}

		for (int i = 0; i <= lseconds; i++) {
			seconds.addItem(new Integer(i));
		}

	}

	/**
	 * This method initializes hours
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getHours() {
		if (hours == null) {
			hours = new JComboBox();
		}
		return hours;
	}

	/**
	 * This method initializes minutes
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getMinutes() {
		if (minutes == null) {
			minutes = new JComboBox();
		}
		return minutes;
	}

	/**
	 * This method initializes seconds
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getSeconds() {
		if (seconds == null) {
			seconds = new JComboBox();
		}
		return seconds;
	}


	public ProxyLifetime getProxyLifetime() {
		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setHours(((Integer) getHours().getSelectedItem()).intValue());
		lifetime.setMinutes(((Integer) getMinutes().getSelectedItem())
				.intValue());
		lifetime.setSeconds(((Integer) getSeconds().getSelectedItem())
				.intValue());
		return lifetime;
	}

}