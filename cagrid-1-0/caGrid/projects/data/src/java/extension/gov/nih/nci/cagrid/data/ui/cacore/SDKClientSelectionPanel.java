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
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
 * @version $Id: SDKClientSelectionPanel.java,v 1.6 2007-06-21 18:44:59 dervin Exp $ 
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
    
    // from HQLCoreQueryProcessor...
    public static final String USE_LOCAL_APPSERVICE = "useLocalAppservice";

    private JLabel qpJarLabel = null;
    private JLabel clientLibDirLabel = null;
    private JRadioButton remoteApiRadioButton = null;
    private JRadioButton localRadioButton = null;
    private JTextField qpJarTextField = null;
    private JTextField clientLibDirTextField = null;
    private JLabel dependsLabel = null;
    private JList dependsList = null;
    private JScrollPane dependsScrollPane = null;
    private JButton clientLibBrowseButton = null;
    private JButton addDependButton = null;
    private JButton removeDependButton = null;
    private JPanel apiSelectionPanel = null;
    private JLabel clientConfDirLabel = null;
    private JTextField clientConfDirTextField = null;
    private JButton clientConfDirBrowseButton = null;


    public SDKClientSelectionPanel(
        ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        initialize();
    }
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
        gridBagConstraints32.gridx = 2;
        gridBagConstraints32.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints32.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints32.gridy = 3;
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints21.gridy = 3;
        gridBagConstraints21.weightx = 1.0;
        gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints21.gridx = 1;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.gridy = 3;
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
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.gridx = 2;
        gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints31.gridy = 2;
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
        this.add(getClientLibDirLabel(), gridBagConstraints1);
        this.add(getDependsLabel(), gridBagConstraints2);
        this.add(getQpJarTextField(), gridBagConstraints3);
        this.add(getClientLibDirTextField(), gridBagConstraints4);
        this.add(getClientLibBrowseButton(), gridBagConstraints31);
        this.add(getDependsScrollPane(), gridBagConstraints5);
        this.add(getAddDependButton(), gridBagConstraints6);
        this.add(getRemoveDependButton(), gridBagConstraints7);
        this.add(getApiSelectionPanel(), gridBagConstraints81);
        this.add(getClientConfDirLabel(), gridBagConstraints11);
        this.add(getClientConfDirTextField(), gridBagConstraints21);
        this.add(getClientConfDirBrowseButton(), gridBagConstraints32);
    }


    public String getPanelShortName() {
        return "Client Selection";
    }


    public String getPanelTitle() {
        return "Client jar and dependency selection";
    }


    public void update() {
        boolean isSdk31 = getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY)
            .equals(CoreDsIntroPanel.CACORE_31_VERSION);
        boolean isSdk32 = getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY)
            .equals(CoreDsIntroPanel.CACORE_32_VERSION);
        if (!isSdk31 && !isSdk32) {
            ErrorDialog.showErrorDialog("No SDK version could be determined!");
        }
        
        // -- enable / disable UI components based on SDK version -- //
        // local API not available in sdk 3.1
        getLocalRadioButton().setEnabled(isSdk32);
                
        // -- configure the UI -- //
        // verify the sdk query library has been copied into the service
        File sdkQueryLib = null;
        if (isSdk31) {
            String sdkQueryLibName = new File(CoreDsIntroPanel.SDK_31_QUERY_LIB).getName();
            sdkQueryLib = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                + File.separator + "lib" + File.separator + sdkQueryLibName);
        } else if (isSdk32) {
            String sdkQueryLibName = new File(CoreDsIntroPanel.SDK_32_QUERY_LIB).getName();
            sdkQueryLib = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                + File.separator + "lib" + File.separator + sdkQueryLibName);
        }
        
        if (sdkQueryLib.exists()) {
            getQpJarTextField().setText(sdkQueryLib.getName());
        } else {
            getQpJarTextField().setText("ERROR: LIBRARY NOT FOUND");
        }

        try {
            String prefixedLocalAppserviceProperty = 
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE;
            if (isSdk32) {
                // state of the local API use
                if (CommonTools.servicePropertyExists(getServiceInformation().getServiceDescriptor(), 
                    prefixedLocalAppserviceProperty)) {
                    boolean localApi = Boolean.valueOf(CommonTools.getServicePropertyValue(
                        getServiceInformation().getServiceDescriptor(), 
                        prefixedLocalAppserviceProperty)).booleanValue();
                    setClientConfDirEnabled(localApi);
                    if (localApi) {
                        getLocalRadioButton().setSelected(true);
                    } else {
                        getRemoteApiRadioButton().setSelected(true);
                    }
                }
            } else {
                // don't need the use local property at all
                CommonTools.removeServiceProperty(getServiceInformation().getServiceDescriptor(), 
                    prefixedLocalAppserviceProperty);
                setClientConfDirEnabled(false);
            }
            
            // any 'additional libs' added previously should show up
            Vector<String> dependJars = getDependJars();
            getDependsList().setListData(dependJars);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error updating UI", ex);
        }
        
        // update the availability of the next button
        updateNextEnabledState();
    }
    
    
    private void setClientConfDirEnabled(boolean enable) {
        getClientConfDirLabel().setEnabled(enable);
        getClientConfDirTextField().setEnabled(enable);
        getClientConfDirBrowseButton().setEnabled(enable);
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
     * This method initializes clientLibDirLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getClientLibDirLabel() {
        if (clientLibDirLabel == null) {
            clientLibDirLabel = new JLabel();
            clientLibDirLabel.setText("Client Lib Directory:");
        }
        return clientLibDirLabel;
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
     * This method initializes clientLibDirTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getClientLibDirTextField() {
        if (clientLibDirTextField == null) {
            clientLibDirTextField = new JTextField();
            clientLibDirTextField.setEditable(false);
        }
        return clientLibDirTextField;
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
     * This method initializes clientLibBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getClientLibBrowseButton() {
        if (clientLibBrowseButton == null) {
            clientLibBrowseButton = new JButton();
            clientLibBrowseButton.setText("Browse");
            clientLibBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    selectClientLibDir();
                    updateNextEnabledState();
                }
            });
        }
        return clientLibBrowseButton;
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
                    boolean localEnabled = currentSelection == getLocalRadioButton().getModel();
                    // set the service property
                    CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(), 
                        DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE, 
                        String.valueOf(localEnabled), false);
                    // enable / disable parts of the UI
                    getClientConfDirLabel().setEnabled(localEnabled);
                    getClientConfDirTextField().setEnabled(localEnabled);
                    getClientConfDirBrowseButton().setEnabled(localEnabled);
                    // decide if the next button should be enabled
                    updateNextEnabledState();
                }
            });
        }
        return apiSelectionPanel;
    }
    
    
    private void updateNextEnabledState() {
        setNextEnabled(false);
        // verify the client jar exists and is valid
        File clientJarFile = locateClientJarInDir(getClientLibDirTextField().getText());
        try {
            if (clientJarFile != null && clientJarFile.exists() && isValidClientJar(clientJarFile)) {
                setNextEnabled(true);
                // if local api, need to validate the client dir selection
                if (getLocalRadioButton().isSelected()) {
                    String clientConfDir = getClientConfDirTextField().getText();
                    if (clientConfDir.length() == 0 || !isValidConfDir(clientConfDir)) {
                        setNextEnabled(false);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void selectClientLibDir() {
        try {
            String clientLibDir = ResourceManager.promptDir(null);
            if (clientLibDir != null) {
                // locate the client jar file
                final File clientJarFile = locateClientJarInDir(clientLibDir);
                if (clientJarFile != null && isValidClientJar(clientJarFile)) {
                    if (getClientLibDirTextField().getText().length() != 0) {
                        // delete the old client jar
                        File oldClientJarFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                            + File.separator + "lib" + File.separator + getClientLibDirTextField().getText());
                        oldClientJarFile.delete();                                
                    }
                    // copy in the client jar
                    File copyOfClient = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                        + File.separator + "lib" + File.separator + clientJarFile.getName());
                    Utils.copyFile(clientJarFile, copyOfClient);

                    // copy the castor mapping file
                    JarFile jar = new JarFile(copyOfClient);
                    StringBuffer mappingFile = JarUtilities.getFileContents(
                        jar, DataServiceConstants.CACORE_CASTOR_MAPPING_FILE);
                    jar.close();
                    // copy the mapping file to the service's source dir + base package name
                    String mappingOut = CastorMappingUtil.getCustomCastorMappingFileName(getServiceInformation());
                    Utils.stringBufferToFile(mappingFile, mappingOut);

                    // get a list of the rest of the jars in the client lib dir
                    // and set them as jars the client depends on
                    File[] extraLibraries = new File(clientLibDir).listFiles(new FileFilter() {
                        public boolean accept(File pathname) {
                            String name = pathname.getName();
                            return name.toLowerCase().endsWith(".jar") 
                            && !pathname.getName().equals(clientJarFile.getName());
                        }
                    });

                    // copy them into the service's lib dir
                    for (File lib : extraLibraries) {
                        File libOut = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                            + File.separator + "lib" + File.separator + lib.getName());
                        Utils.copyFile(lib, libOut);
                    }
                    
                    // set the directory name in the UI
                    getClientLibDirTextField().setText(clientLibDir);
                    
                    // store the library information
                    storeLibrariesInExtensionData();
                } else {
                    String[] message = {
                        "The selected directory does not appear to contain",
                        "a valid client jar file for the currently",
                        "selected version of the caCORE API.",
                        "Please choose a valid library directory file."
                    };
                    PortalUtils.showMessage(message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error in selection of client lib dir", 
                ex.getMessage(), ex);
        }
    }
    
    private File locateClientJarInDir(String dirName) {
        File libDir = new File(dirName);
        File[] jars = libDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                if (name.endsWith(".jar")) {
                    return (name.startsWith("client") || name.endsWith("-client.jar") 
                        || name.endsWith("-thickclient.jar")) && !name.contains("jboss");
                }
                return false;
            }
        });
        if (jars != null && jars.length == 1) {
            return jars[0];
        }
        return null;
    }
    
    
    private boolean isValidClientJar(File jarFile) throws IOException {
        boolean isSdk32 = getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY)
            .equals(CoreDsIntroPanel.CACORE_32_VERSION);
        
        JarFile jar = new JarFile(jarFile);
        // verify the file is a client.jar with a castor mapping file
        JarEntry mappingEntry = jar.getJarEntry(DataServiceConstants.CACORE_CASTOR_MAPPING_FILE);
        // all versions, local and remote client jars need this xml mapping file
        if (mappingEntry == null) {
            jar.close();
            return false;
        }
        if (isSdk32 && getLocalRadioButton().isSelected()) {
            // local has more restrictions
            for (String requirement : LOCAL_CLIENT_REQUIRED_FILES) {
                if (jar.getEntry(requirement) == null) {
                    jar.close();
                    return false;
                }
            }
        }
        jar.close();
        return true;
    }
    

    /**
     * Assumes you're using SDK 3.2
     * 
     * @param dirName
     * @return
     *      True if the selected conf directory is valid, false otherwise
     * @throws IOException
     */
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
            List<String> jarNames = new ArrayList();
            for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
                jarNames.add((String) getDependsList().getModel().getElementAt(i));
            }
            // add the query processor jar
            jarNames.add(getQpJarTextField().getText());
            if (getClientLibDirTextField().getText().length() != 0) {
                jarNames.add(locateClientJarInDir(
                    getClientLibDirTextField().getText()).getName());
            }
            String[] jarNameArray = new String[jarNames.size()];
            jarNames.toArray(jarNameArray);
            libs.setJarName(jarNameArray);
            ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error storing jar information", ex);
        }
    }
    
    
    private Vector<String> getDependJars() throws Exception {
        // any 'additional libs' added previously should show up
        Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
        AdditionalLibraries additionalLibs = data.getAdditionalLibraries();
        Vector<String> dependJars = new Vector();
        File clientJar = locateClientJarInDir(getClientLibDirTextField().getText());
        if (additionalLibs != null && additionalLibs.getJarName() != null) {
            String[] jarNames = additionalLibs.getJarName();
            for (int i = 0; i < jarNames.length; i++) {
                if (!jarNames[i].equals(getQpJarTextField().getText())
                    && !jarNames[i].equals(clientJar != null ? clientJar.getName() : null)) {
                    dependJars.add(jarNames[i]);
                }
            }
            Collections.sort(dependJars);
        }
        return dependJars;
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
        usedNames.add(getClientLibDirTextField().getText());
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


    /**
     * This method initializes clientConfDirectoryLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getClientConfDirLabel() {
        if (clientConfDirLabel == null) {
            clientConfDirLabel = new JLabel();
            clientConfDirLabel.setText("Client Conf Directory:");
        }
        return clientConfDirLabel;
    }


    /**
     * This method initializes clientConfDirTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getClientConfDirTextField() {
        if (clientConfDirTextField == null) {
            clientConfDirTextField = new JTextField();
            clientConfDirTextField.setEditable(false);
        }
        return clientConfDirTextField;
    }


    /**
     * This method initializes clientConfDirBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getClientConfDirBrowseButton() {
        if (clientConfDirBrowseButton == null) {
            clientConfDirBrowseButton = new JButton();
            clientConfDirBrowseButton.setText("Browse");
            clientConfDirBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    selectClientConfDir();
                    updateNextEnabledState();
                }
            });
        }
        return clientConfDirBrowseButton;
    }
    
    
    private void selectClientConfDir() {
        try {
            String confDir = ResourceManager.promptDir(null);
            if (confDir != null) {
                if (isValidConfDir(confDir)) {
                    // turn the conf dir into a jar library
                    File confJarFile = new File(
                        getServiceInformation().getBaseDirectory().getAbsolutePath() + 
                            File.separator + "lib" + File.separator + 
                        getServiceInformation().getServices().getService(0).getName() + 
                        DataServiceConstants.LOCAL_SDK_CONF_JAR_POSTFIX + ".jar");
                    if (confJarFile.exists()) {
                        confJarFile.delete();
                    }
                    
                    JarUtilities.jarDirectory(new File(confDir), confJarFile);
                    
                    getClientConfDirTextField().setText(confDir);
                } else {
                    String[] message = {
                        "The selected directory does not appear to be a",
                        "valid configuration directory for the currently",
                        "selected caCORE API version.  Please select a",
                        "valid configuration directory"
                    };
                    PortalUtils.showMessage(message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error selecting client configuration directory", ex.getMessage(), ex);
        }
    }
}
