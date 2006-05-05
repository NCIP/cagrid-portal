package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import org.projectmobius.portal.GridPortalComponent;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;


public class ModifyService extends GridPortalComponent {

	private ServiceType service;
	private JPanel mainPanel = null;
	private JPanel buttonPanel = null;
	private JButton doneButton = null;
	private JPanel contentPanel = null;
	private JLabel serviceNameLabel = null;
	private JTextField serviceNameTextField = null;
	private JLabel resourceFrameworkTypeLabel = null;
	private JComboBox resourceFrameworkTypeComboBox = null;


	/**
	 * This method initializes
	 */
	public ModifyService(ServiceType service) {
		super();
		this.service = service;
		initialize();
		getServiceNameTextField().setText(service.getName());
		if (service.getResourceFrameworkType() != null
			&& !service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
			getResourceFrameworkTypeComboBox().setSelectedItem(service.getResourceFrameworkType());
		} else if (service.getResourceFrameworkType() != null
			&& service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
			getResourceFrameworkTypeComboBox().addItem(IntroduceConstants.INTRODUCE_MAIN_RESOURCE);

			getResourceFrameworkTypeComboBox().setSelectedItem(IntroduceConstants.INTRODUCE_MAIN_RESOURCE);
		} else {
			getResourceFrameworkTypeComboBox().setSelectedIndex(-1);
		}
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(335, 221));
		this.setContentPane(getMainPanel());
		this.setTitle("Modify Service");

	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.weighty = 1.0D;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.gridy = 1;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getButtonPanel(), gridBagConstraints);
			mainPanel.add(getContentPanel(), gridBagConstraints2);
		}
		return mainPanel;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getDoneButton(), gridBagConstraints1);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes doneButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new JButton();
			doneButton.setText("Done");
			doneButton.setIcon(IntroduceLookAndFeel.getDoneIcon());
			doneButton.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					service.setName(serviceNameTextField.getText());
					dispose();
				}

			});
		}
		return doneButton;
	}


	/**
	 * This method initializes contentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 1;
			resourceFrameworkTypeLabel = new JLabel();
			resourceFrameworkTypeLabel.setText("Resource Framework Type");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 0;
			serviceNameLabel = new JLabel();
			serviceNameLabel.setText("Service Name");
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel.add(serviceNameLabel, gridBagConstraints3);
			contentPanel.add(getServiceNameTextField(), gridBagConstraints4);
			contentPanel.add(resourceFrameworkTypeLabel, gridBagConstraints5);
			contentPanel.add(getResourceFrameworkTypeComboBox(), gridBagConstraints6);
		}
		return contentPanel;
	}


	/**
	 * This method initializes serviceNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServiceNameTextField() {
		if (serviceNameTextField == null) {
			serviceNameTextField = new JTextField();
		}
		return serviceNameTextField;
	}


	/**
	 * This method initializes resourceFrameworkTypeComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getResourceFrameworkTypeComboBox() {
		if (resourceFrameworkTypeComboBox == null) {
			resourceFrameworkTypeComboBox = new JComboBox();
			resourceFrameworkTypeComboBox.addItem(IntroduceConstants.INTRODUCE_BASE_RESOURCE);
			resourceFrameworkTypeComboBox.addItem(IntroduceConstants.INTRODUCE_SINGLETON_RESOURCE);
		}
		return resourceFrameworkTypeComboBox;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
