package org.cagrid.data.sdkquery41.style.wizard;

import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * APITypePanel
 * Wizard panel which allows the service developer to select the 
 * API type (local or remote) which will be used to connect to
 * the caCORE SDK system
 * 
 * @author David
 */
public class APITypePanel extends AbstractWizardPanel {

    private ValidationResultModel validationModel = null;
    private IconFeedbackPanel validationOverlayPanel = null;
    
    private JPanel mainPanel = null;  //  @jve:decl-index=0:visual-constraint="52,23"
    private JRadioButton localApiRadioButton = null;
    private JRadioButton remoteApiRadioButton = null;
    private JLabel hostnameLabel = null;
    private JLabel portLabel = null;
    private JCheckBox useHttpsCheckBox = null;
    private JTextField hostnameTextField = null;
    private JTextField portNumberTextField = null;
    private JPanel apiTypePanel = null;
    private JPanel remoteInfoPanel = null;
    
    public APITypePanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        this.validationModel = new DefaultValidationResultModel();
        initialize();
    }


    public String getPanelShortName() {
        return "API Type";
    }


    public String getPanelTitle() {
        return "Local or Remote API selection";
    }


    public void update() {
        validateInput();
    }
    
    
    private void initialize() {
        configureValidation();
        setLayout(new GridLayout());
        add(getValidationOverlayPanel());
    }
    
    
    private IconFeedbackPanel getValidationOverlayPanel() {
        if (validationOverlayPanel == null) {
            validationOverlayPanel = new IconFeedbackPanel(validationModel, getMainPanel());
        }
        return validationOverlayPanel;
    }
    
    
    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.gridy = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.weightx = 1.0D;
            gridBagConstraints5.gridy = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setSize(new Dimension(391, 179));
            mainPanel.add(getApiTypePanel(), gridBagConstraints5);
            mainPanel.add(getRemoteInfoPanel(), gridBagConstraints6);
        }
        return mainPanel;
    }


    /**
     * This method initializes localApiRadioButton	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLocalApiRadioButton() {
        if (localApiRadioButton == null) {
            localApiRadioButton = new JRadioButton();
            localApiRadioButton.setText("Local API");
        }
        return localApiRadioButton;
    }


    /**
     * This method initializes remoteApiRadioButton	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRemoteApiRadioButton() {
        if (remoteApiRadioButton == null) {
            remoteApiRadioButton = new JRadioButton();
            remoteApiRadioButton.setText("Remote API");
        }
        return remoteApiRadioButton;
    }


    /**
     * This method initializes hostnameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getHostnameLabel() {
        if (hostnameLabel == null) {
            hostnameLabel = new JLabel();
            hostnameLabel.setText("Hostname:");
        }
        return hostnameLabel;
    }


    /**
     * This method initializes portLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getPortLabel() {
        if (portLabel == null) {
            portLabel = new JLabel();
            portLabel.setText("Port Number:");
        }
        return portLabel;
    }


    /**
     * This method initializes useHttpsCheckBox	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getUseHttpsCheckBox() {
        if (useHttpsCheckBox == null) {
            useHttpsCheckBox = new JCheckBox();
            useHttpsCheckBox.setText("Use HTTPS");
        }
        return useHttpsCheckBox;
    }


    /**
     * This method initializes hostnameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getHostnameTextField() {
        if (hostnameTextField == null) {
            hostnameTextField = new JTextField();
        }
        return hostnameTextField;
    }


    /**
     * This method initializes portNumberTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getPortNumberTextField() {
        if (portNumberTextField == null) {
            portNumberTextField = new JTextField();
        }
        return portNumberTextField;
    }


    /**
     * This method initializes apiTypePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getApiTypePanel() {
        if (apiTypePanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(4);
            gridLayout.setColumns(2);
            apiTypePanel = new JPanel();
            apiTypePanel.setBorder(BorderFactory.createTitledBorder(
                null, "API Type", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            apiTypePanel.setLayout(gridLayout);
            apiTypePanel.add(getLocalApiRadioButton(), null);
            apiTypePanel.add(getRemoteApiRadioButton(), null);
        }
        return apiTypePanel;
    }


    /**
     * This method initializes remoteInfoPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getRemoteInfoPanel() {
        if (remoteInfoPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridy = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            remoteInfoPanel = new JPanel();
            remoteInfoPanel.setLayout(new GridBagLayout());
            remoteInfoPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Remote Configuration", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            remoteInfoPanel.add(getHostnameLabel(), gridBagConstraints);
            remoteInfoPanel.add(getHostnameTextField(), gridBagConstraints1);
            remoteInfoPanel.add(getPortLabel(), gridBagConstraints2);
            remoteInfoPanel.add(getPortNumberTextField(), gridBagConstraints3);
            remoteInfoPanel.add(getUseHttpsCheckBox(), gridBagConstraints4);
        }
        return remoteInfoPanel;
    }
    
    
    // ----------
    // validation
    // ----------
    
    
    private void configureValidation() {
        // ValidationComponentUtils.setMessageKey(getSdkDirTextField(), KEY_SDK_DIRECTORY);
        
        validateInput();
        updateComponentTreeSeverity();
    }
    
    
    private void validateInput() {
        ValidationResult result = new ValidationResult();
        
        // TODO: validation goes here
        
        validationModel.setResult(result);
        
        updateComponentTreeSeverity();
        
        // update next button enabled
        setNextEnabled(!validationModel.hasErrors());
    }
    
    
    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, validationModel.getResult());
    }

}
