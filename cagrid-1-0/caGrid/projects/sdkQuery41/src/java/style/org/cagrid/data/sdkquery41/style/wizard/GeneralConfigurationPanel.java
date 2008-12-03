package org.cagrid.data.sdkquery41.style.wizard;

import gov.nih.nci.cagrid.common.portal.DocumentChangeAdapter;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.data.sdkquery41.style.wizard.config.GeneralConfigurationStep;
import org.cagrid.grape.utils.CompositeErrorDialog;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * GeneralConfigurationPanel
 * Prompts the service developer to supply some configuration information
 * which will be used to set up the query processor
 * 
 * @author David
 */
public class GeneralConfigurationPanel extends AbstractWizardPanel {
    
    private static Log LOG = LogFactory.getLog(GeneralConfigurationPanel.class);
    
    private static final String KEY_SDK_DIRECTORY = "caCORE SDK Directory";
    
    private ValidationResultModel validationModel = null;
    private GeneralConfigurationStep configuration = null;
    
    private JLabel sdkDirLabel = null;
    private JTextField sdkDirTextField = null;
    private JButton sdkDirBrowseButton = null;
    private JLabel propertiesFileLabel = null;
    private JTextField propertiesFileTextField = null;
    private JTable propertiesTable = null;
    private JScrollPane propertiesTableScrollPane = null;

    public GeneralConfigurationPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        this.validationModel = new DefaultValidationResultModel();
        this.configuration = new GeneralConfigurationStep(info);
        initialize();
    }


    public String getPanelShortName() {
        return "Configuration";
    }


    public String getPanelTitle() {
        return "caCORE System Configuration";
    }


    public void update() {
        // This method is (mostly) blank, since it's the first panel to run, 
        // and no other panel changes the values used by this panel
        validateInput();
    }
    
    
    public void movingNext() {
        // called when the 'next' button is clicked
        // causes the configuration to be applied, which involves copying around jars,
        // creating new ones for the config files, etc.
        try {
            configuration.applyConfiguration();
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error applying general configuration", ex.getMessage(), ex);
        }
    }
    
    
    private void initialize() {
        configureValidation();
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = GridBagConstraints.BOTH;
        gridBagConstraints5.gridy = 2;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.weighty = 1.0;
        gridBagConstraints5.gridwidth = 3;
        gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints5.gridx = 0;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints4.gridwidth = 2;
        gridBagConstraints4.gridx = 1;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.gridy = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.add(getSdkDirLabel(), gridBagConstraints);
        this.add(getSdkDirTextField(), gridBagConstraints1);
        this.add(getSdkDirBrowseButton(), gridBagConstraints2);
        this.add(getPropertiesFileLabel(), gridBagConstraints3);
        this.add(getPropertiesFileTextField(), gridBagConstraints4);
        this.add(getPropertiesTableScrollPane(), gridBagConstraints5);        
    }


    /**
     * This method initializes sdkDirLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getSdkDirLabel() {
        if (sdkDirLabel == null) {
            sdkDirLabel = new JLabel();
            sdkDirLabel.setText("caCORE SDK Directory:");
        }
        return sdkDirLabel;
    }


    /**
     * This method initializes sdkDirTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getSdkDirTextField() {
        if (sdkDirTextField == null) {
            sdkDirTextField = new JTextField();
            sdkDirTextField.setToolTipText("The directory in which the caCORE SDK 4.1 resides");
            sdkDirTextField.setEditable(false);
            sdkDirTextField.getDocument().addDocumentListener(new DocumentChangeAdapter() {
                public void documentEdited(DocumentEvent e) {
                    configuration.setSdkDirectory(new File(getSdkDirTextField().getText()));
                    validateInput();
                }
            });
        }
        return sdkDirTextField;
    }


    /**
     * This method initializes sdkDirBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getSdkDirBrowseButton() {
        if (sdkDirBrowseButton == null) {
            sdkDirBrowseButton = new JButton();
            sdkDirBrowseButton.setToolTipText("Browse for the caCORE SDK directory");
            sdkDirBrowseButton.setText("Browse");
            sdkDirBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String dir = null;
                    try {
                        dir = ResourceManager.promptDir(GeneralConfigurationPanel.this, null);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        CompositeErrorDialog.showErrorDialog("Error selecting the caCORE SDK directory", ex);
                    }
                    if (dir != null) {
                        getSdkDirTextField().setText(dir);
                    }
                }
            });
        }
        return sdkDirBrowseButton;
    }


    /**
     * This method initializes propertiesFileLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getPropertiesFileLabel() {
        if (propertiesFileLabel == null) {
            propertiesFileLabel = new JLabel();
            propertiesFileLabel.setText("Configuration File:");
        }
        return propertiesFileLabel;
    }


    /**
     * This method initializes propertiesFileTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getPropertiesFileTextField() {
        if (propertiesFileTextField == null) {
            propertiesFileTextField = new JTextField();
            propertiesFileTextField.setEditable(false);
        }
        return propertiesFileTextField;
    }


    /**
     * This method initializes propertiesTable	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getPropertiesTable() {
        if (propertiesTable == null) {
            DefaultTableModel propertiesTableModel = new DefaultTableModel();
            propertiesTableModel.addColumn("Property");
            propertiesTableModel.addColumn("Value");
            propertiesTable = new JTable(propertiesTableModel);
        }
        return propertiesTable;
    }


    /**
     * This method initializes propertiesTableScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getPropertiesTableScrollPane() {
        if (propertiesTableScrollPane == null) {
            propertiesTableScrollPane = new JScrollPane();
            propertiesTableScrollPane.setViewportView(getPropertiesTable());
            propertiesTableScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "Deploy Properties", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        }
        return propertiesTableScrollPane;
    }
    
    
    // -----------
    // validation
    // -----------
    
    
    private void configureValidation() {
        ValidationComponentUtils.setMessageKey(getSdkDirTextField(), KEY_SDK_DIRECTORY);
        
        validateInput();
        updateComponentTreeSeverity();
    }
    
    
    private void validateInput() {
        ValidationResult result = new ValidationResult();
        
        // verify there's an SDK directory selected
        if (ValidationUtils.isNotBlank(getSdkDirTextField().getText())) {
            try {
                configuration.validateSdkDirectory();
                result.add(new SimpleValidationMessage(KEY_SDK_DIRECTORY + " appears to be valid", 
                    Severity.OK, KEY_SDK_DIRECTORY));
            } catch (Exception ex) {
                LOG.debug("Error validating selected SDK directory", ex);
                result.add(new SimpleValidationMessage(KEY_SDK_DIRECTORY + " is not valid: " + ex.getMessage(), 
                    Severity.ERROR, KEY_SDK_DIRECTORY));
            }
        } else {
            result.add(new SimpleValidationMessage(KEY_SDK_DIRECTORY + " cannot be empty", 
                Severity.ERROR, KEY_SDK_DIRECTORY));
        }
        
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
