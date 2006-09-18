package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.security.ServiceSecurityPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ModifyService extends JDialog {

	private SpecificServiceInformation service;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton doneButton = null;

	private JPanel contentPanel = null;

	private JLabel serviceNameLabel = null;

	private JTextField serviceNameTextField = null;

	private JLabel resourceFrameworkTypeLabel = null;

	private JComboBox resourceFrameworkTypeComboBox = null;

	private JLabel serviceNamespaceLabel = null;

	private JTextField namespaceTextField = null;

	private JLabel servicePackageNameLabel = null;

	private JTextField servicePackageNameTextField = null;

	private ServiceTypeTreeNode node;

	private JPanel securityPanel = null;


	/**
	 * This method initializes
	 */
	public ModifyService(ServiceTypeTreeNode node, SpecificServiceInformation service) {
		super();
		this.service = service;
		this.node = node;
		initialize();
		if (service.getService().getName() != null && service.getService().getName().length() > 0) {
			getServiceNameTextField().setText(service.getService().getName());
		} else {
			getServiceNameTextField().setText(
				service.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
					+ "Context");
		}
		if (service.getService().getNamespace() != null && service.getService().getNamespace().length() > 0) {
			getNamespaceTextField().setText(service.getService().getNamespace());
		} else {
			getNamespaceTextField().setText(
				service.getIntroduceServiceProperties().getProperty(
					IntroduceConstants.INTRODUCE_SKELETON_NAMESPACE_DOMAIN)
					+ "/Context");
		}
		if (service.getService().getPackageName() != null && service.getService().getPackageName().length() > 0) {
			getServicePackageNameTextField().setText(service.getService().getPackageName());
		} else {
			getServicePackageNameTextField().setText(service.getServices().getService(0).getPackageName() + ".context");
		}
		if (service.getService().getResourceFrameworkType() != null
			&& !service.getService().getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
			getResourceFrameworkTypeComboBox().setSelectedItem(service.getService().getResourceFrameworkType());
		} else if (service.getService().getResourceFrameworkType() != null
			&& service.getService().getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
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
		this.setContentPane(getMainPanel());
		this.setTitle("Modify Service Context");

	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.weighty = 1.0D;
			gridBagConstraints12.gridy = 1;
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
			gridBagConstraints.gridy = 2;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getButtonPanel(), gridBagConstraints);
			mainPanel.add(getContentPanel(), gridBagConstraints2);
			mainPanel.add(getSecurityPanel(), gridBagConstraints12);
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
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 2;
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
					// make sure that service has a valid name
					if (!CommonTools.isValidServiceName(serviceNameTextField.getText())) {
						JOptionPane.showMessageDialog(ModifyService.this,
							"Service Name is not valid.  Service name must be a java compatible class name. ("
								+ CommonTools.ALLOWED_JAVA_CLASS_REGEX + ")");
						return;
					}
					// make sure there are no collision problems
					for (int i = 0; i < service.getServiceDescriptor().getServices().getService().length; i++) {
						ServiceType testService = service.getServiceDescriptor().getServices().getService(i);
						if (!testService.equals(service.getService())) {
							if (namespaceTextField.getText().equals(testService.getNamespace())) {
								JOptionPane
									.showMessageDialog(ModifyService.this,
										"Service Namespace is not valid.  Service namespace must be unique for this service context.)");
								return;
							}
							if (servicePackageNameTextField.getText().equals(testService.getPackageName())) {
								JOptionPane
									.showMessageDialog(ModifyService.this,
										"Service Package Name is not valid.  Service Package Name must be unique for this service context.)");
								return;
							}
							if (serviceNameTextField.getText().equals(testService.getName())) {
								JOptionPane
									.showMessageDialog(ModifyService.this,
										"Service Name is not valid.  Service Name must be unique for this service context.)");
								return;
							}

						}
					}
					service.getService().setName(serviceNameTextField.getText());
					service.getService().setNamespace(namespaceTextField.getText());
					service.getService().setPackageName(servicePackageNameTextField.getText());
					node.getModel().nodeStructureChanged(node);
					node.getModel().nodeChanged(node);
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
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridy = 2;
			servicePackageNameLabel = new JLabel();
			servicePackageNameLabel.setText("Package Name");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 1;
			serviceNamespaceLabel = new JLabel();
			serviceNamespaceLabel.setText("Namespace");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 3;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 3;
			resourceFrameworkTypeLabel = new JLabel();
			resourceFrameworkTypeLabel.setText("Resource Framework Type");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
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
			contentPanel.add(serviceNamespaceLabel, gridBagConstraints9);
			contentPanel.add(getNamespaceTextField(), gridBagConstraints10);
			contentPanel.add(servicePackageNameLabel, gridBagConstraints7);
			contentPanel.add(getServicePackageNameTextField(), gridBagConstraints8);
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
			resourceFrameworkTypeComboBox.addItem(IntroduceConstants.INTRODUCE_CUSTOM_RESOURCE);
		}
		return resourceFrameworkTypeComboBox;
	}


	/**
	 * This method initializes namespaceTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNamespaceTextField() {
		if (namespaceTextField == null) {
			namespaceTextField = new JTextField();
		}
		return namespaceTextField;
	}


	/**
	 * This method initializes servicePackageNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServicePackageNameTextField() {
		if (servicePackageNameTextField == null) {
			servicePackageNameTextField = new JTextField();
		}
		return servicePackageNameTextField;
	}


	/**
	 * This method initializes securityPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecurityPanel() {
		if (securityPanel == null) {
			securityPanel = new ServiceSecurityPanel(service.getService().getServiceSecurity());
		}
		return securityPanel;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
