package gov.nih.nci.cagrid.data.style.cacore31.wizard;

import gov.nih.nci.cagrid.common.portal.DocumentChangeAdapter;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.style.sdkstyle.wizard.AppserviceConfigCompletionListener;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;


/** 
 *  AppserviceConfigPanel
 *  Panel for configuring the application service used by the
 *  data service
 * 
 * @author David Ervin
 * 
 * @created Mar 23, 2007 3:35:47 PM
 * @version $Id: AppserviceConfigPanel.java,v 1.1 2007-07-12 17:20:52 dervin Exp $ 
 */
public class AppserviceConfigPanel extends AbstractWizardPanel {

    public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
    public static final String CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";
    public static final String USE_CSM_FLAG = "useCsmSecurity";
    public static final String CSM_CONTEXT_NAME = "csmContextName";

    private JLabel urlLabel = null;
    private JTextField urlTextField = null;
    private JCheckBox caseInsensitiveCheckBox = null;
    private JCheckBox useCsmCheckBox = null;
    private JLabel csmContextLabel = null;
    private JTextField csmContextTextField = null;
    private JButton copyUrlButton = null;
    private JPanel optionsPanel = null;
    
    private List<AppserviceConfigCompletionListener> completionListeners;
    
    public AppserviceConfigPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        this.completionListeners = new ArrayList<AppserviceConfigCompletionListener>();
        initialize();
    }
    
    
    private void initialize() {
        // initialize values for each of the fields
        initializeValues();
        // set up the interface layout
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 0;
        gridBagConstraints21.gridwidth = 3;
        gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints21.gridy = 0;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 2;
        gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints11.gridy = 2;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints3.gridx = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridy = 1;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(399, 95));
        this.add(getUrlLabel(), gridBagConstraints);
        this.add(getUrlTextField(), gridBagConstraints1);
        this.add(getCsmContextLabel(), gridBagConstraints2);
        this.add(getCsmContextTextField(), gridBagConstraints3);
        this.add(getCopyUrlButton(), gridBagConstraints11);
        this.add(getOptionsPanel(), gridBagConstraints21);
    }


    public String getPanelShortName() {
        return "Configuration";
    }


    public String getPanelTitle() {
        return "caCORE Application Service Configuration";
    }


    public void update() {
        initializeValues();
    }
    
    
    private void initializeValues() {
        ServiceDescription desc = getServiceInformation().getServiceDescriptor();
        try {
            if (CommonTools.servicePropertyExists(desc, 
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + APPLICATION_SERVICE_URL)) {
                String appUrl = CommonTools.getServicePropertyValue(
                    getServiceInformation().getServiceDescriptor(), 
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + APPLICATION_SERVICE_URL);
                getUrlTextField().setText(appUrl);
            }
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CASE_INSENSITIVE_QUERYING)) {
                String caseInsensitiveValue = CommonTools.getServicePropertyValue(
                    getServiceInformation().getServiceDescriptor(), 
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CASE_INSENSITIVE_QUERYING);
                boolean caseInsensitive = Boolean.valueOf(caseInsensitiveValue).booleanValue();
                getCaseInsensitiveCheckBox().setSelected(caseInsensitive);
            }
            boolean useCsm = false;
            if (CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG)) {
                String useCsmValue = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG);
                useCsm = Boolean.valueOf(useCsmValue).booleanValue();
                getUseCsmCheckBox().setSelected(useCsm);
            }
            if (useCsm && CommonTools.servicePropertyExists(desc,
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONTEXT_NAME)) {
                String csmContextValue = CommonTools.getServicePropertyValue(desc,
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONTEXT_NAME);
                getCsmContextTextField().setText(csmContextValue);
            }
            setCsmConfigEnabled(useCsm);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error loading configuration values: " + ex.getMessage(), ex);
        }
    }


    /**
     * This method initializes urlLabel
     */
    private JLabel getUrlLabel() {
        if (urlLabel == null) {
            urlLabel = new JLabel();
            urlLabel.setText("Remote Service URL:");
            urlLabel.setSize(new Dimension(85, 27));
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
                    try {
                        CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
                            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + APPLICATION_SERVICE_URL,
                            getUrlTextField().getText(), false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error setting the application service URL: " 
                            + ex.getMessage(), ex);
                    }
                }
            });
        }
        return urlTextField;
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
            caseInsensitiveCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    try {
                        CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
                            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CASE_INSENSITIVE_QUERYING, 
                            String.valueOf(caseInsensitiveCheckBox.isSelected()), false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error setting the case insensitive flag: "
                            + ex.getMessage(), ex);
                    }
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
            useCsmCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    setCsmConfigEnabled(getUseCsmCheckBox().isSelected());
                    // set the use CSM property in the service properties
                    try {
                        CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
                            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG, 
                            String.valueOf(useCsmCheckBox.isSelected()), false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error storing use CSM property: " 
                            + ex.getMessage(), ex);
                    }
                }
            });
        }
        return useCsmCheckBox;
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
                    try {
                        CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
                            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONTEXT_NAME,
                            getCsmContextTextField().getText(), false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error setting CSM context: " + ex.getMessage(), ex);
                    }
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
     * This method initializes optionsPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getOptionsPanel() {
        if (optionsPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.gridy = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 0;
            optionsPanel = new JPanel();
            optionsPanel.setLayout(new GridBagLayout());
            optionsPanel.add(getCaseInsensitiveCheckBox(), gridBagConstraints4);
            optionsPanel.add(getUseCsmCheckBox(), gridBagConstraints5);
        }
        return optionsPanel;
    }


    private void setCsmConfigEnabled(boolean enable) {
        getCsmContextLabel().setEnabled(enable);
        getCsmContextTextField().setEnabled(enable);
        if (!enable) {
            getCsmContextTextField().setText("");
        }
        getCopyUrlButton().setEnabled(enable);
    }
    
    
    public void addCompletionListener(AppserviceConfigCompletionListener listener) {
        completionListeners.add(listener);
    }
    
    
    public boolean removeCompletionListener(AppserviceConfigCompletionListener listener) {
        return completionListeners.remove(listener);
    }
    
    
    protected void setConfigurationComplete(boolean complete) {
        for (AppserviceConfigCompletionListener listener : completionListeners) {
            listener.completionStatusChanged(complete);
        }
    }
}
