package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.GroupSelectionListener;
import gov.nih.nci.cagrid.data.ui.NotifyingButtonGroup;
import gov.nih.nci.cagrid.data.utilities.CastorMappingUtil;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/** 
 *  SDKClientSelectionPanel
 *  Panel to select the SDK system's client libraries 
 *  and mode to use local or remote API
 * 
 * @author David Ervin
 * 
 * @created Jun 4, 2007 1:45:08 PM
 * @version $Id: SDKClientSelectionPanel.java,v 1.1 2007-06-06 17:54:37 dervin Exp $ 
 */
public class SDKClientSelectionPanel extends AbstractWizardPanel {
    public static final String[] LOCAL_CLIENT_REQUIRED_FILES = new String[] {
        "DAOConfig.xml", "roleLookup.properties", "SDKSpringBeanConfig.xml"
    };
    public static final String[] LOCAL_CONFIG_DIR_FILES = new String[] {
        "ApplicationSecurityConfig.xml", "hibernate.properties",
        "applicationService.xml", "log4j.properties",
        "ehcache.xml", "remoteService.xml",
        "sdk.csm.new.hibernate.cfg.xml"
    };
    public static final String LOCAL_CONFIG_COPIED_DIR = "localSdkConfig";
    
    // from HQLCoreQueryProcessor...
    public static final String LOCAL_APPSERVICE_CONF_DIR = "localAppserviceConfigDir";
    public static final String USE_LOCAL_APPSERVICE = "useLocalAppservice";

    private JLabel qpJarLabel = null;
    private JLabel clientJarLabel = null;
    private JRadioButton remoteApiRadioButton = null;
    private JRadioButton localRadioButton = null;
    private JTextField qpJarTextField = null;
    private JTextField clientJarTextField = null;
    private JLabel dependsLabel = null;
    private JList dependsList = null;
    private JScrollPane dependsScrollPane = null;
    private JButton clientBrowseButton = null;
    private JButton addDependButton = null;
    private JButton removeDependButton = null;
    private JLabel localConfDirLabel = null;
    private JTextField localConfDirTextField = null;
    private JButton confBrowseButton = null;
    private JPanel apiSelectionPanel = null;


    public SDKClientSelectionPanel(
        ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        initialize();
    }
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
        gridBagConstraints81.gridx = 0;
        gridBagConstraints81.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints81.gridwidth = 3;
        gridBagConstraints81.gridy = 1;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 2;
        gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.anchor = GridBagConstraints.NORTH;
        gridBagConstraints7.gridy = 5;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 2;
        gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints6.gridy = 4;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = GridBagConstraints.BOTH;
        gridBagConstraints5.gridy = 4;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.weighty = 1.0;
        gridBagConstraints5.gridheight = 2;
        gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints5.gridx = 1;
        GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
        gridBagConstraints41.gridx = 2;
        gridBagConstraints41.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints41.gridy = 3;
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.gridx = 2;
        gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints31.gridy = 2;
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints21.gridy = 3;
        gridBagConstraints21.weightx = 1.0;
        gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints21.gridx = 1;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints11.gridy = 3;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints4.gridx = 1;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints3.gridx = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridheight = 2;
        gridBagConstraints2.anchor = GridBagConstraints.NORTH;
        gridBagConstraints2.gridy = 4;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints1.gridy = 2;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(465, 258));
        this.add(getQpJarLabel(), gridBagConstraints);
        this.add(getClientJarLabel(), gridBagConstraints1);
        this.add(getDependsLabel(), gridBagConstraints2);
        this.add(getQpJarTextField(), gridBagConstraints3);
        this.add(getClientJarTextField(), gridBagConstraints4);
        this.add(getLocalConfDirLabel(), gridBagConstraints11);
        this.add(getLocalConfDirTextField(), gridBagConstraints21);
        this.add(getClientBrowseButton(), gridBagConstraints31);
        this.add(getConfBrowseButton(), gridBagConstraints41);
        this.add(getDependsScrollPane(), gridBagConstraints5);
        this.add(getAddDependButton(), gridBagConstraints6);
        this.add(getRemoveDependButton(), gridBagConstraints7);
        this.add(getApiSelectionPanel(), gridBagConstraints81);
    }


    public String getPanelShortName() {
        return "Client Selection";
    }


    public String getPanelTitle() {
        return "Client jar and dependency selection";
    }


    public void update() {
        // verify the sdk query library has been copied into the service
        File sdkQueryLib = null;
        String sdkVersion = (String) getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY);
        if (sdkVersion != null) {
            if (sdkVersion.equals(CoreDsIntroPanel.CACORE_31_VERSION)) {
                String sdkQueryLibName = new File(CoreDsIntroPanel.SDK_31_QUERY_LIB).getName();
                sdkQueryLib = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                    + File.separator + "lib" + File.separator + sdkQueryLibName);
            } else if (sdkVersion.equals(CoreDsIntroPanel.CACORE_32_VERSION)) {
                String sdkQueryLibName = new File(CoreDsIntroPanel.SDK_32_QUERY_LIB).getName();
                sdkQueryLib = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                    + File.separator + "lib" + File.separator + sdkQueryLibName);
            } else {
                ErrorDialog.showErrorDialog("No SDK version could be determined!");
            }
        }
        
        if (sdkQueryLib.exists()) {
            getQpJarTextField().setText(sdkQueryLib.getName());
        } else {
            getQpJarTextField().setText("ERROR: LIBRARY NOT FOUND");
        }
        

        try {
            // state of the local API use
            boolean localApi = false;
            if (CommonTools.servicePropertyExists(getServiceInformation().getServiceDescriptor(), 
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE)) {
                localApi = Boolean.valueOf(CommonTools.getServicePropertyValue(
                    getServiceInformation().getServiceDescriptor(), 
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE)).booleanValue();
                if (localApi) {
                    getLocalRadioButton().setSelected(true);
                } else {
                    getRemoteApiRadioButton().setSelected(true);
                }
            }
            // local API config dir
            getLocalConfDirTextField().setText("");
            if (localApi) {
                if (CommonTools.servicePropertyExists(getServiceInformation().getServiceDescriptor(), 
                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + LOCAL_APPSERVICE_CONF_DIR)) {
                    String configDir = CommonTools.getServicePropertyValue(
                        getServiceInformation().getServiceDescriptor(), 
                        DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + LOCAL_APPSERVICE_CONF_DIR);
                    configDir = getServiceInformation().getBaseDirectory().getAbsolutePath() 
                        + File.separator + "etc" + File.separator + configDir;
                    getLocalConfDirTextField().setText(configDir);
                }
            }
            
            // any 'additional libs' added previously should show up
            Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
            AdditionalLibraries additionalLibs = data.getAdditionalLibraries();
            if (additionalLibs != null && additionalLibs.getJarName() != null) {
                String[] jarNames = additionalLibs.getJarName();
                Vector dependJars = new Vector();
                for (int i = 0; i < jarNames.length; i++) {
                    if (!jarNames[i].equals(sdkQueryLib.getName()) 
                        && !jarNames[i].equals(getClientJarTextField().getText())) {
                        dependJars.add(jarNames[i]);
                    }
                }
                Collections.sort(dependJars);
                getDependsList().setListData(dependJars);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error updating UI", ex);
        }
    }


    /**
     * This method initializes qpJarLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getQpJarLabel() {
        if (qpJarLabel == null) {
            qpJarLabel = new JLabel();
            qpJarLabel.setText("Query Processor Jar:");
        }
        return qpJarLabel;
    }


    /**
     * This method initializes clientJarLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getClientJarLabel() {
        if (clientJarLabel == null) {
            clientJarLabel = new JLabel();
            clientJarLabel.setText("Client Jar:");
        }
        return clientJarLabel;
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
     * This method initializes localRadioButton	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLocalRadioButton() {
        if (localRadioButton == null) {
            localRadioButton = new JRadioButton();
            localRadioButton.setText("Local API");
        }
        return localRadioButton;
    }


    /**
     * This method initializes qpJarTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getQpJarTextField() {
        if (qpJarTextField == null) {
            qpJarTextField = new JTextField();
            qpJarTextField.setEditable(false);
        }
        return qpJarTextField;
    }


    /**
     * This method initializes clientJarTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getClientJarTextField() {
        if (clientJarTextField == null) {
            clientJarTextField = new JTextField();
            clientJarTextField.setEditable(false);
        }
        return clientJarTextField;
    }


    /**
     * This method initializes dependsLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getDependsLabel() {
        if (dependsLabel == null) {
            dependsLabel = new JLabel();
            dependsLabel.setText("Dependencies:");
        }
        return dependsLabel;
    }


    /**
     * This method initializes dependsList	
     * 	
     * @return javax.swing.JList	
     */
    private JList getDependsList() {
        if (dependsList == null) {
            dependsList = new JList();
        }
        return dependsList;
    }


    /**
     * This method initializes dependsScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getDependsScrollPane() {
        if (dependsScrollPane == null) {
            dependsScrollPane = new JScrollPane();
            dependsScrollPane.setViewportView(getDependsList());
            dependsScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return dependsScrollPane;
    }


    /**
     * This method initializes clientBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getClientBrowseButton() {
        if (clientBrowseButton == null) {
            clientBrowseButton = new JButton();
            clientBrowseButton.setText("Browse");
            clientBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    selectClientJar();
                    updateNextEnabledState();
                }
            });
        }
        return clientBrowseButton;
    }


    /**
     * This method initializes addDependButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getAddDependButton() {
        if (addDependButton == null) {
            addDependButton = new JButton();
            addDependButton.setText("Add");
            addDependButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    addDependJars();
                }
            });
        }
        return addDependButton;
    }


    /**
     * This method initializes removeDependButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getRemoveDependButton() {
        if (removeDependButton == null) {
            removeDependButton = new JButton();
            removeDependButton.setText("Remove");
            removeDependButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    removeDependJars();
                }
            });
        }
        return removeDependButton;
    }


    /**
     * This method initializes localConfDirLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getLocalConfDirLabel() {
        if (localConfDirLabel == null) {
            localConfDirLabel = new JLabel();
            localConfDirLabel.setText("Local Conf Directory:");
            localConfDirLabel.setEnabled(false);
        }
        return localConfDirLabel;
    }


    /**
     * This method initializes localConfDirTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getLocalConfDirTextField() {
        if (localConfDirTextField == null) {
            localConfDirTextField = new JTextField();
            localConfDirTextField.setEditable(false);
            localConfDirTextField.setEnabled(false);
        }
        return localConfDirTextField;
    }


    /**
     * This method initializes confBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getConfBrowseButton() {
        if (confBrowseButton == null) {
            confBrowseButton = new JButton();
            confBrowseButton.setText("Browse");
            confBrowseButton.setEnabled(false);
            confBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String dir = ResourceManager.promptDir(null);
                        if (dir != null) {
                            if (isValidConfDir(dir)) {
                                // copy the directory into the service
                                File confOutDir = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                                    + File.separator + "etc" + File.separator + LOCAL_CONFIG_COPIED_DIR);
                                if (confOutDir.exists()) {
                                    // delete the old directory
                                    Utils.deleteDir(confOutDir);
                                }
                                confOutDir.mkdirs();
                                Utils.copyDirectory(new File(dir), confOutDir);
                                // set the appropriate service property
                                CommonTools.setServiceProperty(
                                    getServiceInformation().getServiceDescriptor(), 
                                    DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + LOCAL_APPSERVICE_CONF_DIR,
                                    confOutDir.getName(), true);
                                // set the GUI's text field
                                getLocalConfDirTextField().setText(dir);
                            } else {
                                String[] message = {
                                    "The selected directory does not appear to be",
                                    "a valid configuration directory for the Local",
                                    "caCORE SDK Application Service.",
                                    "Please select a valid configuration directory."
                                };
                                PortalUtils.showMessage(message);
                            }
                        }
                        updateNextEnabledState();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error selecting config directory!", 
                            ex.getMessage(), ex);
                    }
                }
            });
        }
        return confBrowseButton;
    }


    /**
     * This method initializes apiSelectionPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getApiSelectionPanel() {
        if (apiSelectionPanel == null) {
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 1;
            gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints9.gridy = 0;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints8.gridy = 0;
            apiSelectionPanel = new JPanel();
            apiSelectionPanel.setLayout(new GridBagLayout());
            apiSelectionPanel.add(getLocalRadioButton(), gridBagConstraints9);
            apiSelectionPanel.add(getRemoteApiRadioButton(), gridBagConstraints8);
            // group the radio buttons
            NotifyingButtonGroup group = new NotifyingButtonGroup();
            group.add(getLocalRadioButton());
            group.add(getRemoteApiRadioButton());
            group.setSelected(getRemoteApiRadioButton().getModel(), true);
            group.addGroupSelectionListener(new GroupSelectionListener() {
                public void selectionChanged(
                    final ButtonModel previousSelection, final ButtonModel currentSelection) {
                    boolean enable = currentSelection == getLocalRadioButton().getModel();
                    getLocalConfDirLabel().setEnabled(enable);
                    getLocalConfDirTextField().setEnabled(enable);
                    getConfBrowseButton().setEnabled(enable);
                    // set the service property
                    CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(), 
                        DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE, 
                        String.valueOf(enable), false);
                }
            });
        }
        return apiSelectionPanel;
    }
    
    
    private void updateNextEnabledState() {
        setNextEnabled(false);
        String clientJar = getClientJarTextField().getText();
        try {
            if (clientJar.length() != 0 || isValidClientJar(clientJar)) {
                setNextEnabled(true);
                if (getLocalRadioButton().isSelected()) {
                    String confDir = getLocalConfDirTextField().getText();
                    if (confDir.length() == 0 || !isValidConfDir(confDir)) {
                        setNextEnabled(false);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void selectClientJar() {
        try {
            String jarFile = ResourceManager.promptFile(null, FileFilters.JAR_FILTER);
            if (jarFile != null) {
                if (isValidClientJar(jarFile)) {
                    if (getClientJarTextField().getText().length() != 0) {
                        // delete the old client jar
                        File oldClientJarFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                            + File.separator + "lib" + File.separator + getClientJarTextField().getText());
                        oldClientJarFile.delete();                                
                    }
                    // copy in the client jar
                    File selectedClientJarFile = new File(jarFile);
                    File copyOfClient = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                        + File.separator + "lib" + File.separator + selectedClientJarFile.getName());
                    Utils.copyFile(selectedClientJarFile, copyOfClient);
                    // set the filename in the UI
                    getClientJarTextField().setText(selectedClientJarFile.getName());
                    
                    // copy the castor mapping file
                    JarFile jar = new JarFile(jarFile);
                    StringBuffer mappingFile = JarUtilities.getFileContents(
                        jar, DataServiceConstants.CACORE_CASTOR_MAPPING_FILE);
                    // copy the mapping file to the service's source dir + base package name
                    String mappingOut = CastorMappingUtil.getCustomCastorMappingFileName(getServiceInformation());
                    Utils.stringBufferToFile(mappingFile, mappingOut);
                    storeLibrariesInExtensionData();
                } else {
                    String[] message = {
                        "The selected jar does not appear to be a valid",
                        "client jar file for the currently selected API.",
                        "Please choose a valid client jar file."
                    };
                    PortalUtils.showMessage(message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error selecting client jar!", ex.getMessage(), ex);
        }
    }
    
    
    private boolean isValidClientJar(String jarName) throws IOException {
        JarFile jar = new JarFile(jarName);
        // verify the file is a client.jar with a castor mapping file
        JarEntry mappingEntry = jar.getJarEntry(DataServiceConstants.CACORE_CASTOR_MAPPING_FILE);
        // both local and remote client jars need this xml mapping file
        if (mappingEntry == null) {
            return false;
        }
        if (getLocalRadioButton().isSelected()) {
            // local has more restrictions
            for (String requirement : LOCAL_CLIENT_REQUIRED_FILES) {
                if (jar.getEntry(requirement) == null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    private boolean isValidConfDir(String dirName) throws IOException {
        File dir = new File(dirName);
        if (!dir.isDirectory() || !dir.canRead()) {
            return false;
        }
        String[] names = dir.list();
        Set<String> required = new HashSet();
        Collections.addAll(required, LOCAL_CONFIG_DIR_FILES);
        for (String name : names) {
            required.remove(name);
        }
        return required.size() == 0;
    }
    
    
    private void storeLibrariesInExtensionData() {
        try {
            Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
            AdditionalLibraries libs = data.getAdditionalLibraries();
            if (libs == null) {
                libs = new AdditionalLibraries();
                data.setAdditionalLibraries(libs);
            }
            String[] jarNames = new String[getDependsList().getModel().getSize() + 2];
            for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
                jarNames[i] = (String) getDependsList().getModel().getElementAt(i);
            }
            jarNames[jarNames.length - 2] = getQpJarTextField().getText();
            jarNames[jarNames.length - 1] = new File(getClientJarTextField().getText()).getName();
            libs.setJarName(jarNames);
            ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error storing jar information", ex);
        }
    }
    
    
    private void addDependJars() {
        // get the jar selection
        String[] selection = null;
        try {
            selection = ResourceManager.promptMultiFiles(null, FileFilters.JAR_FILTER);
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error selecting files", ex);
        }
        
        if (selection != null) {
            Vector allLibs = new Vector();
            for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
                allLibs.add(getDependsList().getModel().getElementAt(i));
            }
            for (int i = 0; i < selection.length; i++) {
                File inFile = new File(selection[i]);
                // ignore anything that is a repeat jar
                if (shouldAddJar(inFile.getName())) {
                    File outFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                        + File.separator + "lib" + File.separator + inFile.getName());
                    try {
                        Utils.copyFile(inFile, outFile);
                        allLibs.add(outFile.getName());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error copying library " + inFile.getAbsolutePath(), ex);
                    }                   
                }
            }
            Collections.sort(allLibs);
            getDependsList().setListData(allLibs);
            storeLibrariesInExtensionData();
        }
    }
    
    
    private boolean shouldAddJar(String jarName) {
        Set usedNames = new HashSet();
        usedNames.add(getQpJarTextField().getText());
        usedNames.add(getClientJarTextField().getText());
        for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
            usedNames.add(getDependsList().getModel().getElementAt(i));
        }
        return !usedNames.contains(jarName);
    }
    
    
    private void removeDependJars() {
        // seperate wheat from the chaff
        Set deleteItems = new HashSet();
        Collections.addAll(deleteItems, getDependsList().getSelectedValues());
        Vector remainingItems = new Vector(getDependsList().getModel().getSize());
        for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
            String libName = (String) getDependsList().getModel().getElementAt(i);
            if (!deleteItems.contains(libName)) {
                remainingItems.add(libName);
            }
        }
        // gather the wheat into the barn
        Collections.sort(remainingItems);
        getDependsList().setListData(remainingItems);
        // burn the chaff in the firey furnace
        Iterator deleteItemIter = deleteItems.iterator();
        while (deleteItemIter.hasNext()) {
            File libFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                + File.separator + "lib" + File.separator + (String) deleteItemIter.next());
            libFile.delete();
        }
        storeLibrariesInExtensionData();
    }
}
