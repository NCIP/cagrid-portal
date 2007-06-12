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
 * @version $Id: SDKClientSelectionPanel.java,v 1.5 2007-06-12 15:10:53 dervin Exp $ 
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
    private JLabel clientDirLabel = null;
    private JRadioButton remoteApiRadioButton = null;
    private JRadioButton localRadioButton = null;
    private JTextField qpJarTextField = null;
    private JTextField clientDirTextField = null;
    private JLabel dependsLabel = null;
    private JList dependsList = null;
    private JScrollPane dependsScrollPane = null;
    private JButton clientBrowseButton = null;
    private JButton addDependButton = null;
    private JButton removeDependButton = null;
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
        gridBagConstraints7.gridy = 4;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 2;
        gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints6.gridy = 3;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = GridBagConstraints.BOTH;
        gridBagConstraints5.gridy = 3;
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
        gridBagConstraints2.gridy = 3;
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
        this.add(getClientDirLabel(), gridBagConstraints1);
        this.add(getDependsLabel(), gridBagConstraints2);
        this.add(getQpJarTextField(), gridBagConstraints3);
        this.add(getClientDirTextField(), gridBagConstraints4);
        this.add(getClientBrowseButton(), gridBagConstraints31);
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
        // enable / disable the local API selection radio button based on the SDK version selected
        boolean localApiSupported = !getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY)
            .equals(CoreDsIntroPanel.CACORE_31_VERSION);
        getLocalRadioButton().setEnabled(localApiSupported);
        
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
     * This method initializes clientDirLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getClientDirLabel() {
        if (clientDirLabel == null) {
            clientDirLabel = new JLabel();
            clientDirLabel.setText("Client Directory:");
        }
        return clientDirLabel;
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
     * This method initializes clientDirTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getClientDirTextField() {
        if (clientDirTextField == null) {
            clientDirTextField = new JTextField();
            clientDirTextField.setEditable(false);
        }
        return clientDirTextField;
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
                    selectClientDir();
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
                    // set the service property
                    CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(), 
                        DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_LOCAL_APPSERVICE, 
                        String.valueOf(enable), false);
                    // decide if the next button should be enabled
                    updateNextEnabledState();
                }
            });
        }
        return apiSelectionPanel;
    }
    
    
    private void updateNextEnabledState() {
        setNextEnabled(false);
        File clientJarFile = locateClientJarInDir(getClientDirTextField().getText());
        try {
            if (clientJarFile != null && clientJarFile.exists() && isValidClientJar(clientJarFile)) {
                setNextEnabled(true);
                if (getLocalRadioButton().isSelected() 
                    && !isValidClientJar(
                        new File(getClientDirTextField().getText() + File.separator + "conf"))) {
                    setNextEnabled(false);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void selectClientDir() {
        try {
            // prompt for a client package directory
            String clientDir = ResourceManager.promptDir(null);
            if (clientDir != null) {
                // locate the client jar file
                final File clientJarFile = locateClientJarInDir(clientDir);
                if (clientJarFile != null && isValidClientJar(clientJarFile) && 
                    (getLocalRadioButton().isSelected() && isValidConfDir(clientDir + File.separator + "conf"))
                        || getRemoteApiRadioButton().isSelected()) {
                    if (getClientDirTextField().getText().length() != 0) {
                        // delete the old client jar
                        File oldClientJarFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
                            + File.separator + "lib" + File.separator + getClientDirTextField().getText());
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
                    File clientLibDir = new File(clientDir + File.separator + "lib");
                    File[] extraLibraries = clientLibDir.listFiles(new FileFilter() {
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
                    
                    if (getLocalRadioButton().isSelected()) {
                        // turn the conf dir into a jar library
                        File confJarFile = new File(
                            getServiceInformation().getBaseDirectory().getAbsolutePath() + 
                            File.separator + "lib" + File.separator + 
                            getServiceInformation().getServices().getService(0).getName() + 
                            DataServiceConstants.LOCAL_SDK_CONF_JAR_POSTFIX + ".jar");
                        if (confJarFile.exists()) {
                            confJarFile.delete();
                        }
                        JarUtilities.jarDirectory(new File(clientDir + File.separator + "conf"), confJarFile);
                    }

                    // set the directory name in the UI
                    getClientDirTextField().setText(clientDir);
                    
                    // store the library information
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
            ErrorDialog.showErrorDialog(
                "Error selecting client directory", ex.getMessage(), ex);
        }
    }
    
    
    private File locateClientJarInDir(String dirName) {
        File libDir = new File(dirName + File.separator + "lib");
        File[] jars = libDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                return (name.endsWith("-client.jar") || name.endsWith("-thickclient.jar")) && !name.contains("jboss");
            }
        });
        if (jars != null && jars.length == 1) {
            return jars[0];
        }
        return null;
    }
    
    
    private boolean isValidClientJar(File jarFile) throws IOException {
        JarFile jar = new JarFile(jarFile);
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
        jar.close();
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
            List<String> jarNames = new ArrayList();
            for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
                jarNames.add((String) getDependsList().getModel().getElementAt(i));
            }
            // add the query processor jar
            jarNames.add(getQpJarTextField().getText());
            if (getClientDirTextField().getText().length() != 0) {
                jarNames.add(locateClientJarInDir(
                    getClientDirTextField().getText()).getName());
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
        File clientJar = locateClientJarInDir(getClientDirTextField().getText());
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
        usedNames.add(getClientDirTextField().getText());
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
