package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.common.BasePreferenceConfigurationPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class PreferenceConfigurationContainerPanel extends JPanel {

	public JPanel mainConfigurationPanel = null;
	public JPanel buttonPanel = null;
	public JButton applyButton = null;
	private BasePreferenceConfigurationPanel configPanel;


	/**
	 * This method initializes
	 * 
	 */
	public PreferenceConfigurationContainerPanel(BasePreferenceConfigurationPanel configPanel) {
		super();
		this.configPanel = configPanel;
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 */
	public void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(309, 500));
		this.add(getMainConfigurationPanel(), gridBagConstraints);
		this.add(getButtonPanel(), gridBagConstraints1);

	}


	/**
	 * This method initializes mainConfigurationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getMainConfigurationPanel() {
		if (mainConfigurationPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			mainConfigurationPanel = new JPanel();
			mainConfigurationPanel.setLayout(new GridBagLayout());
			mainConfigurationPanel.setPreferredSize(new java.awt.Dimension(200, 200));
			mainConfigurationPanel.add(getConfigurationPanel(), gridBagConstraints3);
		}
		return mainConfigurationPanel;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTHEAST;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.gridx = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getApplyButton(), gridBagConstraints2);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes applyButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getApplyButton() {
		if (applyButton == null) {
			applyButton = new JButton();
			applyButton.setText("Apply");
			applyButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					configPanel.apply();
				}

			});
		}
		return applyButton;
	}


	/**
	 * This method initializes configurationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private BasePreferenceConfigurationPanel getConfigurationPanel() {
		return this.configPanel;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
