package gov.nih.nci.cagrid.gums.portal.registration;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.client.wsrf.GUMSRegistrationClient;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.portal.GUMSServiceListComboBox;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;

public class ServiceSelector extends GridPortalBaseFrame {

	private JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel selectionPanel = null;
	private JPanel buttonPanel = null;
	private JButton registerButton = null;
	private JButton cancelButton = null;
	private GUMSServiceListComboBox serviceSelector = null;
	/**
	 * This is the default constructor
	 */
	public ServiceSelector() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setFrameIcon(GumsLookAndFeel.getRegistrationIcon());
		this.setTitle("GUMS Registration");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getSelectionPanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints1);
		}
		return mainPanel;
	}

	/**
	 * This method initializes selectionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getSelectionPanel() {
		if (selectionPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			selectionPanel = new JPanel();
			selectionPanel.setLayout(new GridBagLayout());
			selectionPanel
			.setBorder(javax.swing.BorderFactory
					.createTitledBorder(
							null,
							"Select GUMS Service",
							javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
							javax.swing.border.TitledBorder.DEFAULT_POSITION,
							null, GumsLookAndFeel.getPanelLabelColor()));
			selectionPanel.add(getServiceSelector(), gridBagConstraints2);
		}
		return selectionPanel;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getRegisterButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes registerButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getRegisterButton() {
		if (registerButton == null) {
			registerButton = new JButton();
			registerButton.setText("Register");
			registerButton.setIcon(GumsLookAndFeel.getApplyIcon());
			registerButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getRegistrationInfo();
				}
			});
		}
		return registerButton;
	}
	
	private void getRegistrationInfo(){
		try{
			GUMSRegistrationClient client = new GUMSRegistrationClient(serviceSelector.getSelectedService());
			AttributeDescriptor[] info = client.getRequiredUserAttributes();
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new RegistrationViewer(info));
		    dispose();
		}catch(Exception e){
			FaultUtil.printFault(e);
			PortalUtils.showErrorMessage(e);
			dispose();
		}
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setIcon(GumsLookAndFeel.getCloseIcon());
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes serviceSelector	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private GUMSServiceListComboBox getServiceSelector() {
		if (serviceSelector == null) {
			serviceSelector = new GUMSServiceListComboBox();
		}
		return serviceSelector;
	}

}
