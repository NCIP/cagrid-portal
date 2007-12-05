package gov.nih.nci.cagrid.sdkquery4.style.wizard;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.DocumentChangeAdapter;
import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ui.GroupSelectionListener;
import gov.nih.nci.cagrid.data.ui.NotifyingButtonGroup;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import org.cagrid.grape.utils.CompositeErrorDialog;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

/** 
 *  QueryProcessorConfigurationPanel
 *  Panel to configure the caCORE SDK Query Processor
 * 
 * @author David Ervin
 * 
 * @created Nov 27, 2007 4:50:32 PM
 * @version $Id: QueryProcessorConfigurationPanel.java,v 1.4 2007-12-05 21:28:18 dervin Exp $ 
 */
public class QueryProcessorConfigurationPanel extends AbstractWizardPanel {
    // keys for validation
    public static final String KEY_APPLICATION_NAME = "Application name";
    public static final String KEY_BEANS_JAR = "Beans Jar file";
    public static final String KEY_CONFIG_DIR = "Configuration directory";
    public static final String KEY_ORM_JAR = "ORM Jar file";
    public static final String KEY_HOST_NAME = "Application service host name";
    public static final String KEY_PORT_NUMBER = "Application port number";
    
    // the config dir as a jar file
    private static File CONFIG_DIR_JAR = null;

    private JLabel applicationNameLabel = null;
    private JTextField applicationNameTextField = null;
    private JLabel beansJarLabel = null;
    private JTextField beansJarTextField = null;
    private JButton beansBrowseButton = null;
    private JLabel configDirLabel = null;
    private JTextField configDirTextField = null;
    private JButton configBrowseButton = null;
    private JPanel basicConfigPanel = null;
    private JRadioButton localApiRadioButton = null;
    private JRadioButton remoteApiRadioButton = null;
    private JLabel ormJarLabel = null;
    private JTextField ormJarTextField = null;
    private JButton ormJarBrowseButton = null;
    private JLabel hostNameLabel = null;
    private JTextField hostNameTextField = null;
    private JLabel portLabel = null;
    private JTextField portTextField = null;
    private JPanel localApiPanel = null;
    private JPanel remoteApiPanel = null;
    private JPanel apiConfigPanel = null;
    private JPanel mainPanel = null;
    private JCheckBox caseInsensitiveCheckBox = null;
    
    private IconFeedbackPanel validationPanel = null;
    private ValidationResultModel validationModel = null;
    private DocumentChangeAdapter documentChangeListener = null;
    

    /**
     * @param extensionDescription
     * @param info
     */
    public QueryProcessorConfigurationPanel(ServiceExtensionDescriptionType extensionDescription,
        ServiceInformation info) {
        super(extensionDescription, info);
        this.validationModel = new DefaultValidationResultModel();
        this.documentChangeListener = new DocumentChangeAdapter() {
            public void documentEdited(DocumentEvent e) {
                validateInput();
            }
        };
        initialize();
    }


    public String getPanelShortName() {
        return "Configuration";
    }


    public String getPanelTitle() {
        return "caCORE Query Processor Configuration";
    }


    public void update() {
        // TODO Auto-generated method stub

    }
    
    
    public void movingNext() {
        // called when the 'next' button is clicked
        // copy the beans jar in to the service
        File beansJar = new File(getBeansJarTextField().getText());
        File beansDest = new File(getServiceInformation().getBaseDirectory(), 
            "lib" + File.separator + beansJar.getName());
        try {
            Utils.copyFile(beansJar, beansDest);
        } catch (IOException ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error copying beans jar", ex.getMessage(), ex);
        }
        // jar up the config dir, then copy it in too
        File configDir = new File(getConfigDirTextField().getText());
        File configJar = new File(getServiceInformation().getBaseDirectory(), 
            "lib" + File.separator + getApplicationNameTextField().getText() + "-config.jar");
        try {
            JarUtilities.jarDirectory(configDir, configJar);
        } catch (IOException ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error packaging configuration directory", ex.getMessage(), ex);
        }
        CONFIG_DIR_JAR = configJar;
        // if local API, copy in the orm jar
        if (getLocalApiRadioButton().isSelected()) {
            File ormFile = new File(getOrmJarTextField().getText());
            File ormDest = new File(getServiceInformation().getBaseDirectory(), 
                "lib" + File.separator + ormFile.getName());
            try {
                Utils.copyFile(ormFile, ormDest);
            } catch (IOException ex) {
                ex.printStackTrace();
                CompositeErrorDialog.showErrorDialog("Error copying orm jar", ex.getMessage(), ex);
            }
        }
    }
    
    
    private void initialize() {
        initRadioGroup();
        this.setLayout(new GridLayout());
        this.add(getValidationPanel());
        // set up for validation
        configureValidation();
    }
    
    
    private IconFeedbackPanel getValidationPanel() {
        if (validationPanel == null) {
            validationPanel = new IconFeedbackPanel(validationModel, getMainPanel());
        }
        return validationPanel;
    }
    
    
    private void initRadioGroup() {
        NotifyingButtonGroup radioGroup = new NotifyingButtonGroup();
        radioGroup.addGroupSelectionListener(new GroupSelectionListener() {
            public void selectionChanged(final ButtonModel previousSelection, final ButtonModel currentSelection) {
                validateInput();
            }
        });
        radioGroup.add(getLocalApiRadioButton());
        radioGroup.add(getRemoteApiRadioButton());
        getLocalApiRadioButton().setSelected(true);
        getRemoteApiRadioButton().setSelected(false);
    }
    
    
    /**
     * This method initializes applicationNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getApplicationNameLabel() {
        if (applicationNameLabel == null) {
            applicationNameLabel = new JLabel();
            applicationNameLabel.setText("Application Name:");
        }
        return applicationNameLabel;
    }


    /**
     * This method initializes applicationNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getApplicationNameTextField() {
        if (applicationNameTextField == null) {
            applicationNameTextField = new JTextField();
            applicationNameTextField.getDocument().addDocumentListener(documentChangeListener);
        }
        return applicationNameTextField;
    }


    /**
     * This method initializes beansJarLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getBeansJarLabel() {
        if (beansJarLabel == null) {
            beansJarLabel = new JLabel();
            beansJarLabel.setText("Beans Jar:");
        }
        return beansJarLabel;
    }


    /**
     * This method initializes beansJarTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getBeansJarTextField() {
        if (beansJarTextField == null) {
            beansJarTextField = new JTextField();
            beansJarTextField.setEditable(false);
            beansJarTextField.getDocument().addDocumentListener(documentChangeListener);
        }
        return beansJarTextField;
    }


    /**
     * This method initializes beansBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBeansBrowseButton() {
        if (beansBrowseButton == null) {
            beansBrowseButton = new JButton();
            beansBrowseButton.setText("Browse");
            beansBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String fullFilename = ResourceManager.promptFile(null, FileFilters.JAR_FILTER);
                        if (getBeansJarTextField().getText().length() != 0) {
                            File originalFile = new File(getBeansJarTextField().getText());
                            File copiedFile = new File(getServiceInformation().getBaseDirectory(), "lib" + File.separator + originalFile.getName());
                            if (copiedFile.exists()) {
                                copiedFile.delete();
                            }
                        }
                        getBeansJarTextField().setText(fullFilename);
                        validateInput();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return beansBrowseButton;
    }


    /**
     * This method initializes configDirLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getConfigDirLabel() {
        if (configDirLabel == null) {
            configDirLabel = new JLabel();
            configDirLabel.setText("Config Directory:");
        }
        return configDirLabel;
    }


    /**
     * This method initializes configDirTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getConfigDirTextField() {
        if (configDirTextField == null) {
            configDirTextField = new JTextField();
            configDirTextField.setEditable(false);
            configDirTextField.getDocument().addDocumentListener(documentChangeListener);
        }
        return configDirTextField;
    }


    /**
     * This method initializes configBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getConfigBrowseButton() {
        if (configBrowseButton == null) {
            configBrowseButton = new JButton();
            configBrowseButton.setText("Browse");
            configBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String filename = ResourceManager.promptDir(null);
                        getConfigDirTextField().setText(filename);
                        // if the old config dir as jar exists, delete it
                        if (CONFIG_DIR_JAR != null && CONFIG_DIR_JAR.exists()) {
                            CONFIG_DIR_JAR.delete();
                            CONFIG_DIR_JAR = null;
                        }
                        validateInput();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return configBrowseButton;
    }


    /**
     * This method initializes basicConfigPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getBasicConfigPanel() {
        if (basicConfigPanel == null) {
            GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
            gridBagConstraints110.gridx = 0;
            gridBagConstraints110.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints110.gridwidth = 3;
            gridBagConstraints110.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints110.gridy = 4;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 2;
            gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints7.gridy = 3;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 3;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints6.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.gridy = 3;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridy = 1;
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
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            basicConfigPanel = new JPanel();
            basicConfigPanel.setLayout(new GridBagLayout());
            basicConfigPanel.add(getApplicationNameLabel(), gridBagConstraints);
            basicConfigPanel.add(getApplicationNameTextField(), gridBagConstraints1);
            basicConfigPanel.add(getBeansJarLabel(), gridBagConstraints2);
            basicConfigPanel.add(getBeansJarTextField(), gridBagConstraints3);
            basicConfigPanel.add(getBeansBrowseButton(), gridBagConstraints4);
            basicConfigPanel.add(getConfigDirLabel(), gridBagConstraints5);
            basicConfigPanel.add(getConfigDirTextField(), gridBagConstraints6);
            basicConfigPanel.add(getConfigBrowseButton(), gridBagConstraints7);
            basicConfigPanel.add(getCaseInsensitiveCheckBox(), gridBagConstraints110);
        }
        return basicConfigPanel;
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
            localApiRadioButton.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    boolean local = localApiRadioButton.isSelected();
                    getOrmJarLabel().setEnabled(local);
                    getOrmJarTextField().setEnabled(local);
                    getOrmJarBrowseButton().setEnabled(local);
                }
            });
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
            remoteApiRadioButton.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    boolean remote = remoteApiRadioButton.isSelected();
                    getHostNameLabel().setEnabled(remote);
                    getHostNameTextField().setEnabled(remote);
                    getPortLabel().setEnabled(remote);
                    getPortTextField().setEnabled(remote);
                }
            });
        }
        return remoteApiRadioButton;
    }


    /**
     * This method initializes ormJarLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getOrmJarLabel() {
        if (ormJarLabel == null) {
            ormJarLabel = new JLabel();
            ormJarLabel.setText("ORM Jar:");
        }
        return ormJarLabel;
    }


    /**
     * This method initializes ormJarTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getOrmJarTextField() {
        if (ormJarTextField == null) {
            ormJarTextField = new JTextField();
            ormJarTextField.setEditable(false);
            ormJarTextField.getDocument().addDocumentListener(documentChangeListener);
        }
        return ormJarTextField;
    }


    /**
     * This method initializes ormJarBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getOrmJarBrowseButton() {
        if (ormJarBrowseButton == null) {
            ormJarBrowseButton = new JButton();
            ormJarBrowseButton.setText("Browse");
            ormJarBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String filename = ResourceManager.promptFile(null, FileFilters.JAR_FILTER);
                        if (getOrmJarTextField().getText().length() != 0) {
                            // TODO: remove old orm jar text field from service lib dir
                        }
                        getOrmJarTextField().setText(filename);
                        validateInput();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return ormJarBrowseButton;
    }


    /**
     * This method initializes hostNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getHostNameLabel() {
        if (hostNameLabel == null) {
            hostNameLabel = new JLabel();
            hostNameLabel.setText("Host Name:");
        }
        return hostNameLabel;
    }


    /**
     * This method initializes hostNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getHostNameTextField() {
        if (hostNameTextField == null) {
            hostNameTextField = new JTextField();
            hostNameTextField.getDocument().addDocumentListener(documentChangeListener);
        }
        return hostNameTextField;
    }


    /**
     * This method initializes portLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getPortLabel() {
        if (portLabel == null) {
            portLabel = new JLabel();
            portLabel.setText("Port:");
        }
        return portLabel;
    }


    /**
     * This method initializes portTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getPortTextField() {
        if (portTextField == null) {
            portTextField = new JTextField();
            portTextField.getDocument().addDocumentListener(documentChangeListener);
        }
        return portTextField;
    }


    /**
     * This method initializes localApiPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getLocalApiPanel() {
        if (localApiPanel == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 2;
            gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints10.gridy = 0;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints9.gridx = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints8.gridy = 0;
            localApiPanel = new JPanel();
            localApiPanel.setLayout(new GridBagLayout());
            localApiPanel.add(getOrmJarLabel(), gridBagConstraints8);
            localApiPanel.add(getOrmJarTextField(), gridBagConstraints9);
            localApiPanel.add(getOrmJarBrowseButton(), gridBagConstraints10);
        }
        return localApiPanel;
    }


    /**
     * This method initializes remoteApiPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getRemoteApiPanel() {
        if (remoteApiPanel == null) {
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridy = 1;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints14.gridx = 1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints13.gridy = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridy = 0;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints12.gridx = 1;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints11.gridy = 0;
            remoteApiPanel = new JPanel();
            remoteApiPanel.setLayout(new GridBagLayout());
            remoteApiPanel.add(getHostNameLabel(), gridBagConstraints11);
            remoteApiPanel.add(getHostNameTextField(), gridBagConstraints12);
            remoteApiPanel.add(getPortLabel(), gridBagConstraints13);
            remoteApiPanel.add(getPortTextField(), gridBagConstraints14);
        }
        return remoteApiPanel;
    }


    /**
     * This method initializes apiConfigPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getApiConfigPanel() {
        if (apiConfigPanel == null) {
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.gridx = 1;
            gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints18.weightx = 1.0D;
            gridBagConstraints18.gridy = 1;
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 1;
            gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints17.weightx = 1.0D;
            gridBagConstraints17.gridy = 0;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.anchor = GridBagConstraints.NORTH;
            gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints16.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints16.gridy = 1;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.anchor = GridBagConstraints.NORTH;
            gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints15.gridy = 0;
            apiConfigPanel = new JPanel();
            apiConfigPanel.setLayout(new GridBagLayout());
            apiConfigPanel.add(getLocalApiRadioButton(), gridBagConstraints15);
            apiConfigPanel.add(getRemoteApiRadioButton(), gridBagConstraints16);
            apiConfigPanel.add(getLocalApiPanel(), gridBagConstraints17);
            apiConfigPanel.add(getRemoteApiPanel(), gridBagConstraints18);
        }
        return apiConfigPanel;
    }


    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints20.weightx = 1.0D;
            gridBagConstraints20.gridy = 1;
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.gridx = 0;
            gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints19.weightx = 1.0D;
            gridBagConstraints19.gridy = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setSize(new Dimension(452, 217));
            mainPanel.add(getBasicConfigPanel(), gridBagConstraints19);
            mainPanel.add(getApiConfigPanel(), gridBagConstraints20);
        }
        return mainPanel;
    }
    
    
    // -----------
    // validation
    // -----------
    
    
    private void configureValidation() {
        ValidationComponentUtils.setMessageKey(getApplicationNameTextField(), KEY_APPLICATION_NAME);
        ValidationComponentUtils.setMessageKey(getBeansJarTextField(), KEY_BEANS_JAR);
        ValidationComponentUtils.setMessageKey(getConfigDirTextField(), KEY_CONFIG_DIR);
        ValidationComponentUtils.setMessageKey(getOrmJarTextField(), KEY_ORM_JAR);
        ValidationComponentUtils.setMessageKey(getHostNameTextField(), KEY_HOST_NAME);
        ValidationComponentUtils.setMessageKey(getPortTextField(), KEY_PORT_NUMBER);
        
        validateInput();
        updateComponentTreeSeverity();
    }
    
    
    private void validateInput() {
        ValidationResult result = new ValidationResult();
        
        String appName = getApplicationNameTextField().getText();
        if (ValidationUtils.isBlank(appName)) {
            result.add(new SimpleValidationMessage(
                KEY_APPLICATION_NAME + " cannot be blank", Severity.ERROR, KEY_APPLICATION_NAME));
        } else if (appName.split("\\s").length != 1) {
            result.add(new SimpleValidationMessage(
                KEY_APPLICATION_NAME + " cannot contain whitespace", Severity.ERROR, KEY_APPLICATION_NAME));
        }
        
        if (ValidationUtils.isBlank(getBeansJarTextField().getText())) {
            result.add(new SimpleValidationMessage(
                KEY_BEANS_JAR + " cannot be blank", Severity.ERROR, KEY_BEANS_JAR));
        } else {
            // TODO: validate the beans jar somehow
        }
        
        if (ValidationUtils.isBlank(getConfigDirTextField().getText())) {
            result.add(new SimpleValidationMessage(
                KEY_CONFIG_DIR + " cannot be blank", Severity.ERROR, KEY_CONFIG_DIR));
        } else {
            // TODO: validate the configuration directory
        }
        
        if (getLocalApiRadioButton().isSelected()) {
            if (ValidationUtils.isBlank(getOrmJarTextField().getText())) {
                result.add(new SimpleValidationMessage(
                    KEY_ORM_JAR + " cannot be blank", Severity.ERROR, KEY_ORM_JAR));
            } else {
                // TODO: validate the ORM jar
            }
        } else { // remote API
            if (ValidationUtils.isBlank(getHostNameTextField().getText())) {
                result.add(new SimpleValidationMessage(
                    KEY_HOST_NAME + " cannot be blank", Severity.ERROR, KEY_HOST_NAME));
            } else {
                try {
                    new URL(getHostNameTextField().getText());
                } catch (Exception ex) {
                    result.add(new SimpleValidationMessage(
                        KEY_HOST_NAME + " is not a valid URL", Severity.ERROR, KEY_HOST_NAME));
                }
            }
            
            if (ValidationUtils.isBlank(getPortTextField().getText())) {
                result.add(new SimpleValidationMessage(
                    KEY_PORT_NUMBER + " cannot be blank", Severity.ERROR, KEY_PORT_NUMBER));
            } else {
                try {
                    Integer.parseInt(getPortTextField().getText());
                } catch (Exception ex) {
                    result.add(new SimpleValidationMessage(
                        KEY_PORT_NUMBER + " is not an integer", Severity.ERROR, KEY_PORT_NUMBER));
                }
            }
        }
        
        validationModel.setResult(result);
        
        updateComponentTreeSeverity();
        // update next button enabled
        setNextEnabled(!validationModel.hasErrors());
        // store the configuration changes
        if (!validationModel.hasErrors()) {
            storeConfigurationProperties();
        }
    }
    
    
    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, validationModel.getResult());
    }

    
    private void storeConfigurationProperties() {
        ServiceDescription desc = getServiceInformation().getServiceDescriptor();
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_APPLICATION_NAME, 
            getApplicationNameTextField().getText(), false);
        File beansJarFile = new File(getBeansJarTextField().getText());
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_BEANS_JAR_NAME,
            beansJarFile.getName(), false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_CASE_INSENSITIVE_QUERYING,
            String.valueOf(getCaseInsensitiveCheckBox().isSelected()), false);
        boolean isLocal = getLocalApiRadioButton().isSelected();
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_USE_LOCAL_API,
            String.valueOf(isLocal), false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_ORM_JAR_NAME,
            isLocal ? new File(getOrmJarTextField().getText()).getName() : "", false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_HOST_NAME,
            isLocal ? "" : getHostNameTextField().getText(), false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_HOST_PORT,
            isLocal ? "" : getPortTextField().getText(), false);
    }


    /**
     * This method initializes caseInsensitiveCheckBox	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getCaseInsensitiveCheckBox() {
        if (caseInsensitiveCheckBox == null) {
            caseInsensitiveCheckBox = new JCheckBox();
            caseInsensitiveCheckBox.setText("Case Insensitive Querying");
            caseInsensitiveCheckBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    validateInput();
                }
            });
        }
        return caseInsensitiveCheckBox;
    }
}
