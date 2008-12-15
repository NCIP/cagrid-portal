package org.cagrid.data.sdkquery41.style.wizard.model;

import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.cagrid.data.sdkquery41.style.wizard.DomainModelSourcePanel;

import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

public class ModelFromFileSystemPanel extends DomainModelSourcePanel {
    
    private static final String KEY_MODEL_FILE = "Domain Model file";
    
    private ValidationResultModel validationModel = null;
    private IconFeedbackPanel validationOverlayPanel = null;
    
    private JPanel mainPanel = null;
    private JLabel modelFilenameLabel = null;
    private JTextField modelFilenameTextField = null;
    private JButton modelBrowseButton = null;
    private JList packagesList = null;
    private JScrollPane packagesScrollPane = null;
    private JLabel shortNameLabel = null;
    private JLabel versionLabel = null;
    private JLabel longNameLabel = null;
    private JLabel descriptionLabel = null;
    private JTextField shortNameTextField = null;
    private JTextField versionTextField = null;
    private JTextField longNameTextField = null;
    private JTextArea descriptionTextArea = null;
    private JScrollPane descriptionScrollPane = null;

    public ModelFromFileSystemPanel() {
        super();
        validationModel = new DefaultValidationResultModel();
        initialize();
    }


    public CadsrInformation getCadsrDomainInformation() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }


    public String getName() {
        return "Pre-Generated";
    }


    public void populateFromConfiguration() {
        // TODO Auto-generated method stub
    }
    
    
    private void initialize() {
        // configureValidation();
        setLayout(new GridLayout());
        add(getValidationOverlayPanel());
    }
    
    
    private IconFeedbackPanel getValidationOverlayPanel() {
        if (validationOverlayPanel == null) {
            validationOverlayPanel = new IconFeedbackPanel(validationModel, getMainPanel());
        }
        return validationOverlayPanel;
    }
    
    
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.BOTH;
            gridBagConstraints11.gridy = 5;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.weighty = 1.0;
            gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints11.gridwidth = 3;
            gridBagConstraints11.gridx = 0;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridy = 4;
            gridBagConstraints10.weightx = 1.0;
            gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints10.gridwidth = 2;
            gridBagConstraints10.gridx = 1;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridy = 4;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.gridy = 3;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints8.gridwidth = 2;
            gridBagConstraints8.gridx = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints7.gridy = 3;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints6.gridwidth = 2;
            gridBagConstraints6.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridy = 2;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridwidth = 2;
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridy = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 2;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
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
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getModelFilenameLabel(), gridBagConstraints);
            mainPanel.add(getModelFilenameTextField(), gridBagConstraints1);
            mainPanel.add(getModelBrowseButton(), gridBagConstraints2);
            mainPanel.add(getShortNameLabel(), gridBagConstraints3);
            mainPanel.add(getShortNameTextField(), gridBagConstraints4);
            mainPanel.add(getVersionLabel(), gridBagConstraints5);
            mainPanel.add(getVersionTextField(), gridBagConstraints6);
            mainPanel.add(getLongNameLabel(), gridBagConstraints7);
            mainPanel.add(getLongNameTextField(), gridBagConstraints8);
            mainPanel.add(getDescriptionLabel(), gridBagConstraints9);
            mainPanel.add(getDescriptionScrollPane(), gridBagConstraints10);
            mainPanel.add(getPackagesScrollPane(), gridBagConstraints11);
        }
        return mainPanel;
    }


    /**
     * This method initializes modelFilenameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getModelFilenameLabel() {
        if (modelFilenameLabel == null) {
            modelFilenameLabel = new JLabel();
            modelFilenameLabel.setText("Model Filename:");
        }
        return modelFilenameLabel;
    }


    /**
     * This method initializes modelFilenameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getModelFilenameTextField() {
        if (modelFilenameTextField == null) {
            modelFilenameTextField = new JTextField();
            modelFilenameTextField.setEditable(false);
        }
        return modelFilenameTextField;
    }


    /**
     * This method initializes modelBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getModelBrowseButton() {
        if (modelBrowseButton == null) {
            modelBrowseButton = new JButton();
            modelBrowseButton.setText("Browse");
            modelBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return modelBrowseButton;
    }


    /**
     * This method initializes packagesList	
     * 	
     * @return javax.swing.JList	
     */
    private JList getPackagesList() {
        if (packagesList == null) {
            packagesList = new JList();
        }
        return packagesList;
    }


    /**
     * This method initializes packagesScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getPackagesScrollPane() {
        if (packagesScrollPane == null) {
            packagesScrollPane = new JScrollPane();
            packagesScrollPane.setViewportView(getPackagesList());
            packagesScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "Packages", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        }
        return packagesScrollPane;
    }


    /**
     * This method initializes shortNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getShortNameLabel() {
        if (shortNameLabel == null) {
            shortNameLabel = new JLabel();
            shortNameLabel.setText("Short Name:");
        }
        return shortNameLabel;
    }


    /**
     * This method initializes versionLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getVersionLabel() {
        if (versionLabel == null) {
            versionLabel = new JLabel();
            versionLabel.setText("Version:");
        }
        return versionLabel;
    }


    /**
     * This method initializes longNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getLongNameLabel() {
        if (longNameLabel == null) {
            longNameLabel = new JLabel();
            longNameLabel.setText("Long Name:");
        }
        return longNameLabel;
    }


    /**
     * This method initializes descriptionLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getDescriptionLabel() {
        if (descriptionLabel == null) {
            descriptionLabel = new JLabel();
            descriptionLabel.setText("Description:");
        }
        return descriptionLabel;
    }


    /**
     * This method initializes shortNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getShortNameTextField() {
        if (shortNameTextField == null) {
            shortNameTextField = new JTextField();
            shortNameTextField.setEditable(false);
        }
        return shortNameTextField;
    }


    /**
     * This method initializes versionTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getVersionTextField() {
        if (versionTextField == null) {
            versionTextField = new JTextField();
            versionTextField.setEditable(false);
        }
        return versionTextField;
    }


    /**
     * This method initializes longNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getLongNameTextField() {
        if (longNameTextField == null) {
            longNameTextField = new JTextField();
            longNameTextField.setEditable(false);
        }
        return longNameTextField;
    }


    /**
     * This method initializes descriptionTextArea	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getDescriptionTextArea() {
        if (descriptionTextArea == null) {
            descriptionTextArea = new JTextArea();
            descriptionTextArea.setEditable(false);
            descriptionTextArea.setLineWrap(true);
            descriptionTextArea.setWrapStyleWord(true);
        }
        return descriptionTextArea;
    }


    /**
     * This method initializes descriptionScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getDescriptionScrollPane() {
        if (descriptionScrollPane == null) {
            descriptionScrollPane = new JScrollPane();
            descriptionScrollPane.setViewportView(getDescriptionTextArea());
        }
        return descriptionScrollPane;
    }
}
