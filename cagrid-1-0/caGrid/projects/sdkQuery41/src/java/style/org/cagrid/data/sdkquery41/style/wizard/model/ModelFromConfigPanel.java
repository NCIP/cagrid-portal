package org.cagrid.data.sdkquery41.style.wizard.model;

import gov.nih.nci.cagrid.common.portal.DocumentChangeAdapter;
import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

import org.cagrid.data.sdkquery41.style.common.SDK41StyleConstants;
import org.cagrid.data.sdkquery41.style.wizard.DomainModelSourcePanel;
import org.cagrid.data.sdkquery41.style.wizard.config.SharedConfiguration;
import org.cagrid.grape.utils.CompositeErrorDialog;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class ModelFromConfigPanel extends DomainModelSourcePanel {
    
    public static final String KEY_PROJECT_NAME = "Project name";
    public static final String KEY_PROJECT_VERSION = "Project version";
    
    private ValidationResultModel validationModel = null;
    private IconFeedbackPanel validationOverlayPanel = null;

    private JPanel mainPanel = null;
    private JLabel xmiFileLabel = null;
    private JTextField xmiFileTextField = null;
    private JLabel xmiTypeLabel = null;
    private JTextField xmiTypeTextField = null;
    private JButton testButton = null;
    private JLabel projectNameLabel = null;
    private JLabel projectVersionLabel = null;
    private JTextField projectNameTextField = null;
    private JTextField projectVersionTextField = null;
    
    private DomainModel domainModel = null;
    
    public ModelFromConfigPanel() {
        super();
        validationModel = new DefaultValidationResultModel();
        initialize();
    }
    
    
    public void populateFromConfiguration() {
        File sdkDir = SharedConfiguration.getInstance().getSdkDirectory();
        Properties deployProps = SharedConfiguration.getInstance().getSdkDeployProperties();
        String modelFilename = deployProps.getProperty(SDK41StyleConstants.DeployProperties.MODEL_FILE);
        File modelFile = new File(sdkDir, modelFilename);
        getXmiFileTextField().setText(modelFile.getAbsolutePath());
        String xmiType = deployProps.getProperty(SDK41StyleConstants.DeployProperties.MODEL_TYPE);
        getXmiTypeTextField().setText(xmiType);
        String projectName = deployProps.getProperty(SDK41StyleConstants.DeployProperties.PROJECT_NAME);
        getProjectNameTextField().setText(projectName);
        validateInput();
    }
    
    
    private void initialize() {
        configureValidation();
        setLayout(new GridLayout());
        add(getValidationOverlayPanel());
        validateInput();
    }
    
    
    private IconFeedbackPanel getValidationOverlayPanel() {
        if (validationOverlayPanel == null) {
            validationOverlayPanel = new IconFeedbackPanel(validationModel, getMainPanel());
        }
        return validationOverlayPanel;
    }
    
    
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.gridy = 4;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 3;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints31.gridy = 2;
            gridBagConstraints31.weightx = 1.0;
            gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints31.gridx = 1;
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints21.gridy = 3;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints11.gridy = 2;
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
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setSize(new Dimension(332, 126));
            mainPanel.add(getXmiFileLabel(), gridBagConstraints);
            mainPanel.add(getXmiFileTextField(), gridBagConstraints1);
            mainPanel.add(getXmiTypeLabel(), gridBagConstraints2);
            mainPanel.add(getXmiTypeTextField(), gridBagConstraints3);
            mainPanel.add(getProjectNameLabel(), gridBagConstraints11);
            mainPanel.add(getProjectVersionLabel(), gridBagConstraints21);
            mainPanel.add(getProjectNameTextField(), gridBagConstraints31);
            mainPanel.add(getProjectVersionTextField(), gridBagConstraints4);
            mainPanel.add(getTestButton(), gridBagConstraints5);
        }
        return mainPanel;
    }


    /**
     * This method initializes xmiFileLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getXmiFileLabel() {
        if (xmiFileLabel == null) {
            xmiFileLabel = new JLabel();
            xmiFileLabel.setText("XMI File:");
        }
        return xmiFileLabel;
    }


    /**
     * This method initializes xmiFileTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getXmiFileTextField() {
        if (xmiFileTextField == null) {
            xmiFileTextField = new JTextField();
            xmiFileTextField.setEditable(false);
        }
        return xmiFileTextField;
    }


    /**
     * This method initializes xmiTypeLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getXmiTypeLabel() {
        if (xmiTypeLabel == null) {
            xmiTypeLabel = new JLabel();
            xmiTypeLabel.setText("XMI Type:");
        }
        return xmiTypeLabel;
    }


    /**
     * This method initializes xmiTypeTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getXmiTypeTextField() {
        if (xmiTypeTextField == null) {
            xmiTypeTextField = new JTextField();
            xmiTypeTextField.setEditable(false);
        }
        return xmiTypeTextField;
    }


    /**
     * This method initializes testButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getTestButton() {
        if (testButton == null) {
            testButton = new JButton();
            testButton.setText("Test Model Generation");
            testButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        getDomainModel();
                        Component root = SwingUtilities.getRoot(ModelFromConfigPanel.this);
                        String message = "Model generated without errors";
                        JOptionPane.showMessageDialog(root, message);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CompositeErrorDialog.showErrorDialog("Error generating domain model", ex.getMessage(), ex);
                    }
                }
            });
        }
        return testButton;
    }
    
    
    /**
     * This method initializes projectNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getProjectNameLabel() {
        if (projectNameLabel == null) {
            projectNameLabel = new JLabel();
            projectNameLabel.setText("Project Name:");
        }
        return projectNameLabel;
    }


    /**
     * This method initializes projectVersionLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getProjectVersionLabel() {
        if (projectVersionLabel == null) {
            projectVersionLabel = new JLabel();
            projectVersionLabel.setText("Project Version:");
        }
        return projectVersionLabel;
    }


    /**
     * This method initializes projectNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getProjectNameTextField() {
        if (projectNameTextField == null) {
            projectNameTextField = new JTextField();
            projectNameTextField.getDocument().addDocumentListener(new DocumentChangeAdapter() {
                public void documentEdited(DocumentEvent e) {
                    validateInput();
                }
            });
        }
        return projectNameTextField;
    }


    /**
     * This method initializes projectVersionTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getProjectVersionTextField() {
        if (projectVersionTextField == null) {
            projectVersionTextField = new JTextField();
            projectVersionTextField.getDocument().addDocumentListener(new DocumentChangeAdapter() {
                public void documentEdited(DocumentEvent e) {
                    validateInput();
                }
            });
        }
        return projectVersionTextField;
    }
    
    
    public DomainModel getDomainModel() throws Exception {
        if (domainModel == null) {
            if (validationModel.hasErrors()) {
                StringBuffer errors = new StringBuffer();
                errors.append("Domain model cannot be generated while in an error state:\n");
                for (ValidationMessage message : validationModel.getResult().getErrors()) {
                    errors.append(message.formattedText()).append("\n");
                }
                throw new IllegalStateException(errors.toString());
            }
            // TODO: implement me
        }
        return domainModel;
    }
    
    
    // ----------
    // validation
    // ----------
    
    
    private void configureValidation() {
        ValidationComponentUtils.setMessageKey(getProjectNameTextField(), KEY_PROJECT_NAME);
        ValidationComponentUtils.setMessageKey(getProjectVersionTextField(), KEY_PROJECT_VERSION);
        
        validateInput();
        updateComponentTreeSeverity();
    }
    
    
    private void validateInput() {
        ValidationResult result = new ValidationResult();
        
        if (ValidationUtils.isBlank(getProjectNameTextField().getText())) {
            result.add(new SimpleValidationMessage(KEY_PROJECT_NAME + " cannot be blank!", Severity.ERROR, KEY_PROJECT_NAME));
        }
        if (ValidationUtils.isBlank(getProjectVersionTextField().getText())) {
            result.add(new SimpleValidationMessage(KEY_PROJECT_VERSION + " cannot be blank!", Severity.ERROR, KEY_PROJECT_VERSION));
        }
        
        validationModel.setResult(result);
        
        updateComponentTreeSeverity();
    }
    
    
    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, validationModel.getResult());
    }
}
