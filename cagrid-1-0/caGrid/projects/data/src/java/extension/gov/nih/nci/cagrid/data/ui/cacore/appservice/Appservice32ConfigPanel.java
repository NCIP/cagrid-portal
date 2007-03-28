package gov.nih.nci.cagrid.data.ui.cacore.appservice;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;


/**
 * Appservice32ConfigPanel 
 * Panel for configuration of the caCORE SDK v3.2 query processor
 * 
 * @author David Ervin
 * 
 * @created Mar 28, 2007 10:00:18 AM
 * @version $Id: Appservice32ConfigPanel.java,v 1.1 2007-03-28 17:33:11 dervin Exp $
 */
public class Appservice32ConfigPanel extends BaseAppserviceConfigPanel {

    public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
    public static final String USE_CSM_FLAG = "useCsmSecurity";
    public static final String CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";
    public static final String CSM_CONFIGURATION_FILENAME = "csmConfigurationFilename";
    public static final String CSM_CONTEXT_NAME = "csmContextName";
    public static final String USE_LOCAL_APPSERVICE = "useLocalAppservice";

    private JCheckBox useLocalCheckBox = null;
    private JCheckBox caseInsensitiveCheckBox = null;
    private JCheckBox useCsmCheckBox = null;
    private JPanel checkBoxPanel = null;
    private JLabel urlLabel = null;
    private JTextField urlTextField = null;
    private JLabel csmContextLabel = null;
    private JTextField csmContextTextField = null;
    private JButton copyUrlButton = null;
    private JLabel csmConfigLabel = null;
    private JTextField csmConfigTextField = null;
    private JButton browseButton = null;
    private JPanel inputPanel = null;


    public Appservice32ConfigPanel(ExtensionTypeExtensionData extensionData, ServiceInformation serviceInfo) {
        super(extensionData, serviceInfo);
        initialize();
    }


    private void initialize() {
        initializeValues();
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 0;
        gridBagConstraints9.fill = GridBagConstraints.BOTH;
        gridBagConstraints9.weightx = 1.0D;
        gridBagConstraints9.anchor = GridBagConstraints.NORTH;
        gridBagConstraints9.gridheight = 2;
        gridBagConstraints9.gridy = 1;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints8.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(556, 164));
        this.add(getCheckBoxPanel(), gridBagConstraints8);
        this.add(getInputPanel(), gridBagConstraints9);
    }


    private void initializeValues() {
        ServiceDescription desc = getServiceInfo().getServiceDescriptor();
        try {
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + APPLICATION_SERVICE_URL)) {
                String serviceUrl = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + APPLICATION_SERVICE_URL);
                getUrlTextField().setText(serviceUrl);
            }
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CASE_INSENSITIVE_QUERYING)) {
                String caseInsensitiveValue = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CASE_INSENSITIVE_QUERYING);
                boolean caseInsensitive = Boolean.valueOf(caseInsensitiveValue).booleanValue();
                getCaseInsensitiveCheckBox().setSelected(caseInsensitive);
            }
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONFIGURATION_FILENAME)) {
                String csmConfigFilename = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONFIGURATION_FILENAME);
                getCsmConfigTextField().setText(csmConfigFilename);
            }
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONTEXT_NAME)) {
                String csmContextName = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONTEXT_NAME);
                getCsmContextTextField().setText(csmContextName);
            }
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG)) {
                String useCsmValue = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG);
                boolean useCsm = Boolean.valueOf(useCsmValue).booleanValue();
                getUseCsmCheckBox().setSelected(useCsm);
            }
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE)) {
                String useLocalValue = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE);
                boolean useLocal = Boolean.valueOf(useLocalValue).booleanValue();
                getUseLocalCheckBox().setSelected(useLocal);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error loading configuration values: " + ex.getMessage(), ex);
        }
        enableRelaventComponents();
    }


    /**
     * This method initializes useLocalCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getUseLocalCheckBox() {
        if (useLocalCheckBox == null) {
            useLocalCheckBox = new JCheckBox();
            useLocalCheckBox.setText("Use Local Service API");
            useLocalCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
            useLocalCheckBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
                        DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE,
                        String.valueOf(getUseLocalCheckBox().isSelected()), false);
                    // setLocalServiceUsed(getUseLocalCheckBox().isSelected());
                    enableRelaventComponents();
                }
            });
        }
        return useLocalCheckBox;
    }


    /**
     * This method initializes caseInsensitiveCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getCaseInsensitiveCheckBox() {
        if (caseInsensitiveCheckBox == null) {
            caseInsensitiveCheckBox = new JCheckBox();
            caseInsensitiveCheckBox.setText("Case Insensitive Queries");
            caseInsensitiveCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
            caseInsensitiveCheckBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
                        DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CASE_INSENSITIVE_QUERYING,
                        String.valueOf(getCaseInsensitiveCheckBox().isSelected()), false);
                }
            });
        }
        return caseInsensitiveCheckBox;
    }


    /**
     * This method initializes useCsmCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getUseCsmCheckBox() {
        if (useCsmCheckBox == null) {
            useCsmCheckBox = new JCheckBox();
            useCsmCheckBox.setText("Use CSM Security");
            useCsmCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
            useCsmCheckBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
                        DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG,
                        String.valueOf(getUseCsmCheckBox().isSelected()), false);
                    // setUseCsm(getUseCsmCheckBox().isSelected());
                    enableRelaventComponents();
                }
            });
        }
        return useCsmCheckBox;
    }


    /**
     * This method initializes checkBoxPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCheckBoxPanel() {
        if (checkBoxPanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(2);
            gridLayout.setColumns(3);
            checkBoxPanel = new JPanel();
            checkBoxPanel.setLayout(gridLayout);
            checkBoxPanel.add(getUseLocalCheckBox(), null);
            checkBoxPanel.add(getCaseInsensitiveCheckBox(), null);
            checkBoxPanel.add(getUseCsmCheckBox(), null);
        }
        return checkBoxPanel;
    }


    /**
     * This method initializes urlLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getUrlLabel() {
        if (urlLabel == null) {
            urlLabel = new JLabel();
            urlLabel.setText("Remote Service URL:");
        }
        return urlLabel;
    }


    /**
     * This method initializes urlTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getUrlTextField() {
        if (urlTextField == null) {
            urlTextField = new JTextField();
            urlTextField.getDocument().addDocumentListener(new DocumentChangeAdapter() {
               public void documentEdited(DocumentEvent e) {
                   CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
                       DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + APPLICATION_SERVICE_URL,
                       getUrlTextField().getText(), false);
               }
            });
        }
        return urlTextField;
    }


    /**
     * This method initializes csmContextLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getCsmContextLabel() {
        if (csmContextLabel == null) {
            csmContextLabel = new JLabel();
            csmContextLabel.setText("CSM Context Name:");
        }
        return csmContextLabel;
    }


    /**
     * This method initializes csmContextTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmContextTextField() {
        if (csmContextTextField == null) {
            csmContextTextField = new JTextField();
            csmContextTextField.getDocument().addDocumentListener(new DocumentChangeAdapter() {
               public void documentEdited(DocumentEvent e) {
                   CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
                       DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONTEXT_NAME,
                       getCsmContextTextField().getText(), false);
               }
            });
        }
        return csmContextTextField;
    }


    /**
     * This method initializes copyUrlButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCopyUrlButton() {
        if (copyUrlButton == null) {
            copyUrlButton = new JButton();
            copyUrlButton.setText("Copy App URL");
            copyUrlButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String url = getUrlTextField().getText();
                    getCsmContextTextField().setText(url);
                }
            });
        }
        return copyUrlButton;
    }


    /**
     * This method initializes csmConfigLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getCsmConfigLabel() {
        if (csmConfigLabel == null) {
            csmConfigLabel = new JLabel();
            csmConfigLabel.setText("CSM Configuration File:");
        }
        return csmConfigLabel;
    }


    /**
     * This method initializes csmConfigTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmConfigTextField() {
        if (csmConfigTextField == null) {
            csmConfigTextField = new JTextField();
            csmConfigTextField.setEditable(false);
        }
        return csmConfigTextField;
    }


    /**
     * This method initializes browseButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getBrowseButton() {
        if (browseButton == null) {
            browseButton = new JButton();
            browseButton.setText("Browse");
            browseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String etcDir = getServiceInfo().getBaseDirectory().getAbsolutePath() 
                        + File.separator + "etc";
                    if (getCsmConfigTextField().getText().length() != 0) {
                        // delete any old config file
                        File oldConfig = new File(etcDir + File.separator 
                            + getCsmConfigTextField().getText());
                        if (oldConfig.exists()) {
                            oldConfig.delete();
                        }
                    }
                    String originalFilename = null;
                    try {
                        originalFilename = ResourceManager.promptFile(null, null);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error in file selection: " + ex.getMessage(), ex);
                    }
                    if (originalFilename != null) {
                        File originalFile = new File(originalFilename);
                        File outputFile = new File(getServiceInfo().getBaseDirectory().getAbsolutePath() 
                            + File.separator + "etc" + File.separator + originalFile.getName());
                        try {
                            Utils.copyFile(originalFile, outputFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            ErrorDialog.showErrorDialog("Error copying selected " +
                                "file to service directory: " + ex.getMessage(), ex);
                        }
                        getCsmConfigTextField().setText(outputFile.getName());
                        CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
                            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONFIGURATION_FILENAME,
                            outputFile.getName(), true);
                    }
                }
            });
        }
        return browseButton;
    }


    /**
     * This method initializes inputPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getInputPanel() {
        if (inputPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 2;
            gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 2;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 2;
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints6.gridy = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridy = 2;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.gridx = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridwidth = 2;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 2;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            inputPanel = new JPanel();
            inputPanel.setLayout(new GridBagLayout());
            inputPanel.add(getUrlLabel(), gridBagConstraints);
            inputPanel.add(getCsmContextLabel(), gridBagConstraints1);
            inputPanel.add(getCsmConfigLabel(), gridBagConstraints2);
            inputPanel.add(getUrlTextField(), gridBagConstraints3);
            inputPanel.add(getCsmContextTextField(), gridBagConstraints4);
            inputPanel.add(getCsmConfigTextField(), gridBagConstraints5);
            inputPanel.add(getCopyUrlButton(), gridBagConstraints6);
            inputPanel.add(getBrowseButton(), gridBagConstraints7);
        }
        return inputPanel;
    }
    
    
    private void enableRelaventComponents() {
        boolean localChecked = getUseLocalCheckBox().isSelected();
        boolean csmChecked = getUseCsmCheckBox().isSelected();
        
        PortalUtils.setContainerEnabled(getInputPanel(), true);
        
        getUseCsmCheckBox().setEnabled(true);

        getCsmContextLabel().setEnabled(csmChecked);
        getCsmContextTextField().setEnabled(csmChecked);
        getCsmContextTextField().setText("");
        getCopyUrlButton().setEnabled(csmChecked);

        getCsmConfigLabel().setEnabled(csmChecked);
        getCsmConfigTextField().setEnabled(csmChecked);
        getCsmConfigTextField().setText("");
        getBrowseButton().setEnabled(csmChecked);
        
        if (localChecked) {
            PortalUtils.setContainerEnabled(getInputPanel(), false);
            getUseCsmCheckBox().setSelected(false);
            getUseCsmCheckBox().setEnabled(false);
            getUrlTextField().setText("");
        }
    }
}
