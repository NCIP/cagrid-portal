package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionsType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.security.MethodAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.properties.ServicePropertiesTable;
import gov.nih.nci.cagrid.introduce.portal.modification.security.ServiceSecurityPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.MethodButtonPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.MethodsButtonPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.ResourcesButtonPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.ServiceButtonPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.ServicesButtonPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.ServicesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodViewer;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsTable;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ModifyResourcePropertiesPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeConfigurePanel;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeConfigurePanel;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeTreeNode;
import gov.nih.nci.cagrid.introduce.statistics.StatisticsClient;
import gov.nih.nci.cagrid.introduce.upgrade.UpgradeManager;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeStatus;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 */
public class ModificationViewer extends GridPortalComponent {
    private static final Logger logger = Logger.getLogger(ModificationViewer.class);

    private JPanel mainPanel = null;

    private JPanel operationsPanel = null;

    private JPanel buttonPanel = null;

    private JButton cancel = null;

    private JPanel selectPanel = null;

    private MethodsTable methodsTable = null;

    private JScrollPane methodsScrollPane = null;

    private File methodsDirectory = null;

    private JButton addMethodButton = null;

    private JButton saveButton = null;

    private JButton removeButton = null;

    private JButton modifyButton = null;

    private JPanel operationsButtonPanel = null;

    private JButton undoButton = null;

    private boolean dirty = false;

    private JTabbedPane contentTabbedPane = null;

    private ServiceSecurityPanel securityPanel = null;

    private JLabel serviceNameLabel = null;

    private JTextField serviceName = null;

    private JLabel namespaceLable = null;

    private JTextField namespace = null;

    private JLabel lastSavedLabel = null;

    private JTextField lastSaved = null;

    private JLabel saveLocationLabel = null;

    private JTextField saveLocation = null;

    private JPanel discoveryPanel = null;

    private JPanel discoveryButtonPanel = null;

    private JButton namespaceAddButton = null;

    private JButton namespaceRemoveButton = null;

    private JScrollPane namespaceTableScrollPane = null;

    private NamespacesJTree namespaceJTree = null;

    private JPanel namespaceTypePropertiesPanel = null;

    private NamespaceTypeConfigurePanel namespaceTypeConfigurationPanel = null;

    private SchemaElementTypeConfigurePanel schemaElementTypeConfigurationPanel = null;

    private ServiceInformation info = null;

    private JTabbedPane discoveryTabbedPane = null;

    private JPanel namespaceConfPanel = null;

    private JPanel servicePropertiesPanel = null;

    private JPanel servicePropertiesTableContainerPanel = null;

    private JScrollPane servicePropertiesTableScrollPane = null;

    private ServicePropertiesTable servicePropertiesTable = null;

    private JPanel servicePropertiesControlPanel = null;

    private JButton addServiceProperyButton = null;

    private JButton removeServicePropertyButton = null;

    private JTextField servicePropertyKeyTextField = null;

    private JTextField servicePropertyValueTextField = null;

    private JLabel servicePropertiesKeyLabel = null;

    private JLabel servicePropertiesValueLabel = null;

    private JPanel servicePropertiesButtonPanel = null;

    private JPanel resourceesTabbedPanel = null;

    private JPanel resourcesPanel = null;

    private JScrollPane resourcesScrollPane = null;

    private ServicesJTree resourcesJTree = null;

    private ModifyResourcePropertiesPanel rpHolderPanel = null;

    private JSplitPane typesSplitPane = null;

    private List extensionPanels = null;

    private JCheckBox propertyIsFromETCCheckBox = null;

    private JPanel resourcesOptionsPanel = null;

    private JPanel descriptionPanel = null;

    private JScrollPane descriptionScrollPane = null;

    private JTextArea descriptionTextArea = null;

    private JLabel descriptionLabel = null;

    private JTextField servicePropertyDescriptionTextField = null;


    /**
     * This is the default constructor
     */
    public ModificationViewer() {
        super();
        extensionPanels = new ArrayList();
        // throw a thread out so that i can make sure that if the chooser is
        // canceled i can dispose of this frame
        Thread th = createChooserThread();
        th.start();
    }


    public ModificationViewer(File methodsDirectory) {
        super();
        extensionPanels = new ArrayList();
        this.methodsDirectory = methodsDirectory;
        try {
            initialize();
        } catch (Exception e) {
            // should never get here but in case.....
            e.printStackTrace();
        }
    }


    private Thread createChooserThread() {
        Thread th = new Thread() {
            public void run() {
                try {
                    chooseService();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (methodsDirectory == null) {
                    ModificationViewer.this.dispose();
                    return;
                }
                File file = new File(methodsDirectory.getAbsolutePath() + File.separator + "introduce.xml");
                if (file.exists() && file.canRead()) {
                    try {
                        initialize();
                        if (ModificationViewer.this != null) {
                            ModificationViewer.this.pack();
                            ModificationViewer.this.setMaximum(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorDialog.showErrorDialog(e);
                        if (ModificationViewer.this != null) {
                            ModificationViewer.this.dispose();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(ModificationViewer.this, "Directory "
                        + methodsDirectory.getAbsolutePath() + " does not seem to be an introduce service");
                    ModificationViewer.this.dispose();
                }
            }
        };
        return th;
    }


    private Properties loadServiceProps() {
        try {
            Properties serviceProperties = new Properties();
            serviceProperties.load(new FileInputStream(methodsDirectory.getAbsolutePath() + File.separator
                + IntroduceConstants.INTRODUCE_PROPERTIES_FILE));
            serviceProperties.setProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR, methodsDirectory
                .getAbsolutePath());
            return serviceProperties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void chooseService() throws Exception {
        String dir = ResourceManager.promptDir(null);
        if (dir != null) {
            methodsDirectory = new File(dir);
        }
    }


    public void reInitialize(File serviceDir) throws Exception {
        methodsDirectory = serviceDir;
        this.initialize();
        this.reInitializeGUI();
    }


    private void reInitializeGUI() throws Exception {
        getNamespaceJTree().setNamespaces(info.getNamespaces());
        getResourcesJTree().setServices(info.getServices(), info);
        getMethodsTable().clearTable();
        getMethodsTable().setMethods(info.getServices().getService(0));
        getRpHolderPanel().reInitialize(info.getServices().getService(0).getResourcePropertiesList(),
            info.getNamespaces());
        getServicePropertiesTable().setServiceInformation(info);
        this.resetMethodSecurityIfServiceSecurityChanged();
        for (int i = 0; i < extensionPanels.size(); i++) {
            ServiceModificationUIPanel panel = (ServiceModificationUIPanel) extensionPanels.get(i);
            panel.setServiceInfo(info);
        }
    }


    /**
     * This method initializes this viewer componenet
     * 
     * @return void
     */
    private void initialize() throws Exception {
        if (methodsDirectory != null) {

            UpgradeManager upgrader = new UpgradeManager(methodsDirectory.getAbsolutePath());

            if (upgrader.canIntroduceBeUpgraded() || upgrader.extensionsNeedUpgraded()) {
                int answer = JOptionPane
                    .showConfirmDialog(
                        this,
                        "This service is from an older of version of Introduce or uses an older version of an extension.\nWould you like to try to upgrade this service to work with the current version of Introduce and installed extensions?  Otherwise Introduce will attempt to work with this service.");
                if (answer == JOptionPane.OK_OPTION) {

                    try {
                        UpgradeStatus status = upgrader.upgrade();
                        logger.info("SERVICE UPGRADE STATUS:\n" + status);
                        answer = JOptionPane
                            .showConfirmDialog(
                                this,
                                status.toString()
                                    + "\n\nnThe service upgrade status is above.\nIf you would like to proceed click ok otherwise click clancel.");
                        if (answer == JOptionPane.OK_OPTION) {

                        } else {
                            ModificationViewer.this.dispose();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        answer = JOptionPane
                            .showConfirmDialog(
                                this,
                                "The service had the following error during the upgrade process: \n"
                                    + e.getMessage()
                                    + "\n  This could be due to modifications you may have made to Introduce managed files such as the build files or wsdl files.\nIf you would like to attemt to fix this service please select the ok option and Introduce will close this modification window and enable you to fix the service and try opening again later. \nElse Introduce will roll your service back to before it was upgraded.");
                        if (answer == JOptionPane.OK_OPTION) {
                            ModificationViewer.this.dispose();
                        } else {
                            try {
                                upgrader.recover();
                            } catch (Exception ex) {
                                ErrorDialog.showErrorDialog(e);
                            }
                            ModificationViewer.this.dispose();
                        }
                    }
                }
            }

            // reload the info incase it has changed during upgrading.....
            info = new ServiceInformation(methodsDirectory);

            this.setContentPane(getMainPanel());
            this.setTitle("Modify Service Interface");
            this.setFrameIcon(IntroduceLookAndFeel.getModifyIcon());
        }
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints13.weighty = 1.0;
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridy = 1;
            gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints13.weightx = 1.0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints11.gridheight = 0;
            gridBagConstraints11.gridwidth = 0;
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridy = 3;
            gridBagConstraints11.weighty = 1.0D;
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            mainPanel.add(getButtonPanel(), gridBagConstraints2);
            mainPanel.add(getSelectPanel(), gridBagConstraints3);
            mainPanel.add(getContentTabbedPane(), gridBagConstraints13);
        }
        return mainPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing. gridBagConstraints41.gridx = 1; JPanel
     */
    private JPanel getMethodsPanel() {
        if (operationsPanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            operationsPanel = new JPanel();
            operationsPanel.setLayout(new GridBagLayout());
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints4.gridwidth = 2;
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 0;
            operationsPanel.add(getMethodsScrollPane(), gridBagConstraints4);
            operationsPanel.add(getMethodsButtonPanel(), gridBagConstraints);
        }
        return operationsPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints10.gridy = 0;
            gridBagConstraints10.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints10.gridx = 2;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.gridx = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridx = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getSaveButton(), gridBagConstraints9);
            buttonPanel.add(getCancel(), gridBagConstraints10);
            buttonPanel.add(getUndoButton(), gridBagConstraints8);
        }
        return buttonPanel;
    }


    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancel() {
        if (cancel == null) {
            cancel = new JButton(PortalLookAndFeel.getCloseIcon());
            cancel.setText("Cancel");
            cancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return cancel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getSelectPanel() {
        if (selectPanel == null) {
            GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
            gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints24.gridy = 1;
            gridBagConstraints24.weightx = 1.0;
            gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints24.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints24.gridx = 3;
            GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
            gridBagConstraints23.gridx = 2;
            gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints23.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints23.gridy = 1;
            saveLocationLabel = new JLabel();
            saveLocationLabel.setText("Location");
            saveLocationLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints22.gridy = 1;
            gridBagConstraints22.weightx = 1.0;
            gridBagConstraints22.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints22.gridx = 1;
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints21.gridy = 1;
            lastSavedLabel = new JLabel();
            lastSavedLabel.setText("Last Saved");
            lastSavedLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints20.gridy = 0;
            gridBagConstraints20.weightx = 1.0;
            gridBagConstraints20.gridx = 3;
            gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.gridx = 2;
            gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints19.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints19.gridy = 0;
            namespaceLable = new JLabel();
            namespaceLable.setText("Namespace");
            namespaceLable.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.gridx = 0;
            gridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints18.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints18.gridy = 0;
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints17.gridx = 1;
            gridBagConstraints17.gridy = 0;
            gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints17.weightx = 1.0;
            serviceNameLabel = new JLabel();
            serviceNameLabel.setText("Service Name");
            serviceNameLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            selectPanel = new JPanel();
            selectPanel.setLayout(new GridBagLayout());
            selectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            selectPanel.add(serviceNameLabel, gridBagConstraints18);
            selectPanel.add(getServiceName(), gridBagConstraints17);
            selectPanel.add(namespaceLable, gridBagConstraints19);
            selectPanel.add(getNamespace(), gridBagConstraints20);
            selectPanel.add(lastSavedLabel, gridBagConstraints21);
            selectPanel.add(getLastSaved(), gridBagConstraints22);
            selectPanel.add(saveLocationLabel, gridBagConstraints23);
            selectPanel.add(getSaveLocation(), gridBagConstraints24);
        }
        return selectPanel;
    }


    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private MethodsTable getMethodsTable() {
        if (methodsTable == null) {
            methodsTable = new MethodsTable(info.getServices().getService(0), methodsDirectory, info
                .getIntroduceServiceProperties());
            methodsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {

                    int row = getMethodsTable().getSelectedRow();
                    if ((row >= 0) && (row < getMethodsTable().getRowCount())) {
                        MethodType type = getMethodsTable().getMethodType(getMethodsTable().getSelectedRow());
                        getModifyButton().setEnabled(true);
                    } else {
                        getModifyButton().setEnabled(false);
                    }

                }

            });
            methodsTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        dirty = true;
                        int row = getMethodsTable().getSelectedRow();
                        if ((row >= 0) && (row < getMethodsTable().getRowCount())) {
                            MethodType type = getMethodsTable().getMethodType(getMethodsTable().getSelectedRow());
                            performMethodModify();
                        }
                    }
                }
            });
        }
        return methodsTable;
    }


    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getMethodsScrollPane() {
        if (methodsScrollPane == null) {
            methodsScrollPane = new JScrollPane();
            methodsScrollPane.setViewportView(getMethodsTable());
        }
        return methodsScrollPane;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddMethodButton() {
        if (addMethodButton == null) {
            addMethodButton = new JButton(PortalLookAndFeel.getAddIcon());
            addMethodButton.setText("Add");
            addMethodButton.setToolTipText("add new operation");
            addMethodButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dirty = true;
                    MethodType method = new MethodType();
                    method.setName("newMethod");
                    Set methodSet = new HashSet();
                    for (int i = 0; i < getMethodsTable().getRowCount(); i++) {
                        methodSet.add(getMethodsTable().getMethodType(i).getName());
                    }
                    if (methodSet.contains(method.getName())) {
                        int index = 2;
                        while (methodSet.contains(method.getName() + index)) {
                            index++;
                        }
                        method.setName(method.getName() + index);
                    }
                    MethodTypeOutput output = new MethodTypeOutput();
                    output.setQName(new QName("", "void"));
                    method.setOutput(output);
                    getMethodsTable().addRow(method);

                    performMethodModify();
                }
            });
        }
        return addMethodButton;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton(PortalLookAndFeel.getSaveIcon());
            saveButton.setText("Save");
            saveButton.setToolTipText("modify and rebuild service");
            saveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    saveModifications();
                }
            });
        }
        return saveButton;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRemoveButton() {
        if (removeButton == null) {
            removeButton = new JButton(PortalLookAndFeel.getRemoveIcon());
            removeButton.setText("Remove");
            removeButton.setToolTipText("remove selected operation");
            removeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dirty = true;
                    int row = getMethodsTable().getSelectedRow();
                    if ((row < 0) || (row >= getMethodsTable().getRowCount())) {
                        PortalUtils.showErrorMessage("Please select a method to remove.");
                        return;
                    }
                    try {
                        getMethodsTable().removeSelectedRow();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return removeButton;
    }


    private void resetMethodSecurityIfServiceSecurityChanged() throws Exception {
        boolean update = false;
        ServiceSecurity service = info.getServices().getService(0).getServiceSecurity();
        ServiceSecurity curr = securityPanel.getServiceSecurity(false);
        // This should be cleaned up some
        if ((service == null) && (curr == null)) {
            update = false;
        } else if ((service != null) && (curr == null)) {
            update = true;
        } else if ((service == null) && (curr != null)) {
            update = true;
        } else if (!service.equals(curr)) {
            update = true;
        }
        if (update) {
            MethodsType mt = info.getServices().getService(0).getMethods();
            List changes = new ArrayList();
            if (mt != null) {
                info.getServices().getService(0).setServiceSecurity(curr);
                MethodType[] methods = mt.getMethod();
                if (methods != null) {
                    for (int i = 0; i < methods.length; i++) {
                        if ((methods[i].getMethodSecurity() != null)
                            && (!CommonTools.equals(curr, methods[i].getMethodSecurity()))) {
                            methods[i].setMethodSecurity(null);
                            changes.add(methods[i].getName());
                        }
                    }
                }
                if (changes.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Service security configuration changed, "
                        + "the security configurations for the following methods were reset:\n");
                    for (int i = 0; i < changes.size(); i++) {
                        String method = (String) changes.get(i);
                        sb.append("    " + (i + 1) + ") " + method);
                    }
                    PortalUtils.showMessage(sb.toString());
                }
            }
        }
    }


    private void performMethodModify() {
        try {
            this.resetMethodSecurityIfServiceSecurityChanged();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog.showErrorDialog(e);
            return;
        }
        MethodType method = getMethodsTable().getSelectedMethodType();
        if (method == null) {
            PortalUtils.showErrorMessage("Please select a method to modify.");
            return;
        }
        // TODO: check this.... setting this for now......
        MethodViewer mv = new MethodViewer(method, new SpecificServiceInformation(info, info.getServices()
            .getService(0)));

        PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(mv);
        // TODO: total hack for now to avoid tryin sort action listerners and
        // having to pass the table into the method modification viewer.
        mv.getDoneButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread th = new Thread(new Runnable() {

                    public void run() {
                        try {
                            Thread.sleep(100);
                            Thread.yield();
                        } catch (InterruptedException ex) {
                            // TODO Auto-generated catch block
                            ex.printStackTrace();
                        }
                        getMethodsTable().sort();
                    }
                });

                th.start();
            }
        });
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getModifyButton() {
        if (modifyButton == null) {
            modifyButton = new JButton(IntroduceLookAndFeel.getModifyIcon());
            modifyButton.setText("Modify");
            modifyButton.setEnabled(false);
            modifyButton.setToolTipText("modify seleted operation");
            modifyButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dirty = true;
                    performMethodModify();
                }
            });
        }
        return modifyButton;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMethodsButtonPanel() {
        if (operationsButtonPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridx = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridx = 0;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridx = 0;
            operationsButtonPanel = new JPanel();
            operationsButtonPanel.setLayout(new GridBagLayout());
            operationsButtonPanel.add(getAddMethodButton(), gridBagConstraints5);
            operationsButtonPanel.add(getModifyButton(), gridBagConstraints6);
            operationsButtonPanel.add(getRemoveButton(), gridBagConstraints7);
        }
        return operationsButtonPanel;
    }


    /**
     * This method initializes undoButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getUndoButton() {
        if (undoButton == null) {
            undoButton = new JButton(IntroduceLookAndFeel.getUndoIcon());
            undoButton.setText("Undo");
            undoButton.setToolTipText("roll back to last save state");
            undoButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int decision = JOptionPane.showConfirmDialog(ModificationViewer.this,
                        "Are you sure you wish to roll back?");
                    if (decision == JOptionPane.OK_OPTION) {
                        BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager.getInstance()
                            .getGridPortal(), "Undo") {
                            public void process() {
                                logger.info("Loading in last known save for this project");
                                try {
                                    if (!dirty) {
                                        setProgressText("restoring from local cache");
                                        String timestamp = info.getIntroduceServiceProperties().getProperty(
                                            IntroduceConstants.INTRODUCE_SKELETON_TIMESTAMP);
                                        String name = info.getIntroduceServiceProperties().getProperty(
                                            IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
                                        String destDir = info.getIntroduceServiceProperties().getProperty(
                                            IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR);
                                        ResourceManager.restoreLatest(timestamp, name, destDir);
                                    }
                                    dispose();
                                    PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
                                        new ModificationViewer(methodsDirectory));
                                } catch (Exception e1) {
                                    // e1.printStackTrace();
                                    ErrorDialog
                                        .showErrorDialog("Unable to roll back, there may be no older versions available");
                                    return;
                                }
                            }
                        };
                        Thread th = new Thread(r);
                        th.start();
                    }
                }
            });
        }
        return undoButton;
    }


    /**
     * This method initializes contentTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getContentTabbedPane() {
        if (contentTabbedPane == null) {
            contentTabbedPane = new JTabbedPane();
            contentTabbedPane.addTab("Types", null, getTypesSplitPane(), null);
            contentTabbedPane.addTab("Operations", null, getMethodsPanel(), null);
            contentTabbedPane.addTab("Metadata", null, getRpHolderPanel(), null);
            contentTabbedPane.addTab("Service Properties", null, getServicePropertiesPanel(), null);
            contentTabbedPane.addTab("Service Contexts", null, getResourceesTabbedPanel(), null);
            contentTabbedPane.addTab("Security", null, getSecurityPanel(), null);
            // add a tab for each extension...
            contentTabbedPane.addTab("Service Description", null, getDescriptionPanel(), null);
            ExtensionsType exts = info.getExtensions();
            if ((exts != null) && (exts.getExtension() != null)) {
                ExtensionType[] extsTypes = exts.getExtension();
                for (int i = 0; i < extsTypes.length; i++) {
                    ServiceExtensionDescriptionType extDtype = ExtensionsLoader.getInstance().getServiceExtension(
                        extsTypes[i].getName());
                    try {
                        if ((extDtype.getServiceModificationUIPanel() != null)
                            && !extDtype.getServiceModificationUIPanel().equals("")) {
                            ServiceModificationUIPanel extPanel = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
                                .getServiceModificationUIPanel(extDtype.getName(), info);
                            extensionPanels.add(extPanel);
                            contentTabbedPane.addTab(extDtype.getDisplayName(), null, extPanel, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorDialog.showErrorDialog("Cannot load extension: " + extDtype.getDisplayName(), e);
                    }
                }
            }

            contentTabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    try {
                        reInitializeGUI();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return contentTabbedPane;
    }


    /**
     * This method initializes securityPanel
     * 
     * @return javax.swing.JPanel
     */
    private ServiceSecurityPanel getSecurityPanel() {
        if (securityPanel == null) {
            try {
                securityPanel = new ServiceSecurityPanel(info.getServiceDescriptor(), info.getServices().getService(0));
            } catch (Exception e) {
                e.printStackTrace();
                ErrorDialog.showErrorDialog(e);
            }
        }
        return securityPanel;
    }


    /**
     * This method initializes serviceName
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getServiceName() {
        if (serviceName == null) {
            serviceName = new JTextField();
            serviceName.setEditable(false);
            serviceName.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
            serviceName.setText(info.getIntroduceServiceProperties().getProperty(
                IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
        }
        return serviceName;
    }


    /**
     * This method initializes packageName
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getNamespace() {
        if (namespace == null) {
            namespace = new JTextField();
            namespace.setText(info.getIntroduceServiceProperties().getProperty(
                IntroduceConstants.INTRODUCE_SKELETON_NAMESPACE_DOMAIN));
            namespace.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
            namespace.setEditable(false);
        }
        return namespace;
    }


    /**
     * This method initializes lastSaved
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getLastSaved() {
        if (lastSaved == null) {
            lastSaved = new JTextField();
            lastSaved.setEditable(false);
            lastSaved.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
            setLastSaved(info.getIntroduceServiceProperties().getProperty(
                IntroduceConstants.INTRODUCE_SKELETON_TIMESTAMP));
        }
        return lastSaved;
    }


    private void setLastSaved(String savedDate) {
        Date date;
        if (savedDate.equals("0")) {
            date = new Date();
        } else {
            date = new Date(Long.parseLong(savedDate));
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        lastSaved.setText(formatter.format(date));
    }


    /**
     * This method initializes location
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getSaveLocation() {
        if (saveLocation == null) {
            saveLocation = new JTextField();
            saveLocation.setText(methodsDirectory.getAbsolutePath());
            saveLocation.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
            saveLocation.setEditable(false);
        }
        return saveLocation;
    }


    /**
     * This method initializes discoveryPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDiscoveryPanel() {
        if (discoveryPanel == null) {
            GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
            gridBagConstraints27.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints27.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints27.gridx = 0;
            gridBagConstraints27.gridy = 1;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints16.weighty = 1.0;
            gridBagConstraints16.weightx = 1.0;
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.gridy = 0;
            gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
            discoveryPanel = new JPanel();
            discoveryPanel.setLayout(new GridBagLayout());
            discoveryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Type",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                PortalLookAndFeel.getPanelLabelColor()));
            discoveryPanel.add(getDiscoveryTabbedPane(), gridBagConstraints16);
            discoveryPanel.add(getDiscoveryButtonPanel(), gridBagConstraints27);

        }
        return discoveryPanel;
    }


    /**
     * This method initializes discoveryButtonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDiscoveryButtonPanel() {
        if (discoveryButtonPanel == null) {
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 1;
            gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints31.gridy = 0;
            GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
            gridBagConstraints30.gridx = 0;
            gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints30.gridy = 0;
            discoveryButtonPanel = new JPanel();
            discoveryButtonPanel.setLayout(new GridBagLayout());
            discoveryButtonPanel.add(getNamespaceAddButton(), gridBagConstraints30);
            discoveryButtonPanel.add(getNamespaceRemoveButton(), gridBagConstraints31);
        }
        return discoveryButtonPanel;
    }


    /**
     * This method initializes namespaceAddButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getNamespaceAddButton() {
        if (namespaceAddButton == null) {
            namespaceAddButton = new JButton();
            namespaceAddButton.setText("Add");
            namespaceAddButton.setIcon(PortalLookAndFeel.getAddIcon());
            namespaceAddButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    File schemaDir = new File(methodsDirectory
                        + File.separator
                        + "schema"
                        + File.separator
                        + info.getIntroduceServiceProperties().getProperty(
                            IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
                    NamespaceTypeDiscoveryComponent discoveryComponent = (NamespaceTypeDiscoveryComponent) getDiscoveryTabbedPane()
                        .getSelectedComponent();
                    NamespaceType[] types = discoveryComponent.createNamespaceType(schemaDir, ResourceManager
                        .getConfigurationProperty(IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY));
                    List messages = new ArrayList();
                    if (types != null) {
                        for (int i = 0; i < types.length; i++) {
                            NamespaceType currentType = types[i];
                            if (CommonTools.getNamespaceType(info.getNamespaces(), currentType.getNamespace()) != null) {
                                // namespace type already exists in
                                // service
                                messages.add("The namespace " + types[i].getNamespace()
                                    + " already exists, it was reloaded");
                            }
                        }

                        NamespacesType namespaces = info.getNamespaces();
                        if (namespaces == null) {
                            namespaces = new NamespacesType();
                        }
                        NamespaceType[] currentNamespaces = namespaces.getNamespace();
                        if (currentNamespaces == null) {
                            currentNamespaces = types;
                        } else {
                            // merge discovered namespaces into existing
                            // namespaces
                            currentNamespaces = mergeNamespaceArrays(currentNamespaces, types);
                        }
                        namespaces.setNamespace(currentNamespaces);
                        info.setNamespaces(namespaces);

                        // reload the types tree
                        getNamespaceJTree().setNamespaces(info.getNamespaces());
                    } else {
                        ErrorDialog.showErrorDialog("Error retrieving schema: " + discoveryComponent.getErrorMessage());
                    }
                    if (messages.size() != 0) {
                        String[] msg = new String[messages.size()];
                        messages.toArray(msg);
                        JOptionPane.showMessageDialog(ModificationViewer.this, msg);
                    }
                }
            });
        }
        return namespaceAddButton;
    }


    /**
     * This method initializes namespaceRemoveButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getNamespaceRemoveButton() {
        if (namespaceRemoveButton == null) {
            namespaceRemoveButton = new JButton();
            namespaceRemoveButton.setText("Remove");
            namespaceRemoveButton.setIcon(PortalLookAndFeel.getRemoveIcon());
            namespaceRemoveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        if (getNamespaceJTree().getCurrentNode() instanceof NamespaceTypeTreeNode) {
                            NamespaceType type = (NamespaceType) getNamespaceJTree().getCurrentNode().getUserObject();
                            if (!type.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
                                if (CommonTools.isNamespaceTypeInUse(type, info.getServiceDescriptor())) {
                                    String[] message = {"The namespace " + type.getNamespace(),
                                            "contains types in use by this service."};
                                    JOptionPane.showMessageDialog(ModificationViewer.this, message);
                                } else {
                                    getNamespaceJTree().removeSelectedNode();
                                }
                            } else {
                                PortalUtils.showMessage("Cannot remove " + IntroduceConstants.W3CNAMESPACE);
                            }
                        }
                    } catch (Exception ex) {
                        // TODO: there has to be a better check for
                        // namespace
                        // selection than this!!
                        JOptionPane.showMessageDialog(ModificationViewer.this, "Please select namespace to Remove");
                    }
                }
            });
        }
        return namespaceRemoveButton;
    }


    /**
     * This method initializes namespaceTableScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getNamespaceTableScrollPane() {
        if (namespaceTableScrollPane == null) {
            namespaceTableScrollPane = new JScrollPane();
            namespaceTableScrollPane.setViewportView(getNamespaceJTree());
        }
        return namespaceTableScrollPane;
    }


    /**
     * This method initializes namespaceJTree
     * 
     * @return javax.swing.JTree
     */
    private NamespacesJTree getNamespaceJTree() {
        if (namespaceJTree == null) {
            namespaceJTree = new NamespacesJTree(info.getNamespaces(), true);
            namespaceJTree.setVisibleRowCount(10);
            namespaceJTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
                public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
                    DefaultMutableTreeNode node = getNamespaceJTree().getCurrentNode();
                    if (node instanceof NamespaceTypeTreeNode) {
                        getNamespaceTypeConfigurationPanel().setNamespaceType((NamespaceType) node.getUserObject());
                        getSchemaElementTypeConfigurationPanel().clear();
                        getSchemaElementTypeConfigurationPanel().setHide(true);
                    } else if (node instanceof SchemaElementTypeTreeNode) {
                        NamespaceTypeTreeNode parentNode = (NamespaceTypeTreeNode) node.getParent();
                        NamespaceType nsType = (NamespaceType) parentNode.getUserObject();
                        if (nsType.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
                            getSchemaElementTypeConfigurationPanel().setHide(true);
                            getSchemaElementTypeConfigurationPanel().setSchemaElementType(
                                (SchemaElementType) node.getUserObject(), false);
                        } else {
                            getSchemaElementTypeConfigurationPanel().setHide(false);
                            getSchemaElementTypeConfigurationPanel().setSchemaElementType(
                                (SchemaElementType) node.getUserObject(), true);
                        }
                        getNamespaceTypeConfigurationPanel().setNamespaceType(nsType);
                    } else {
                        getNamespaceTypeConfigurationPanel().clear();
                        getSchemaElementTypeConfigurationPanel().clear();
                    }
                }
            });
        }
        return namespaceJTree;
    }


    /**
     * This method initializes namespaceTypePropertiesPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getNamespaceTypePropertiesPanel() {
        if (namespaceTypePropertiesPanel == null) {
            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            gridBagConstraints33.gridx = 0;
            gridBagConstraints33.weightx = 1.0D;
            gridBagConstraints33.gridy = 0;
            gridBagConstraints33.fill = GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
            gridBagConstraints34.gridx = 0;
            gridBagConstraints34.weightx = 1.0D;
            gridBagConstraints34.gridy = 1;
            gridBagConstraints34.fill = GridBagConstraints.BOTH;
            namespaceTypePropertiesPanel = new JPanel();
            namespaceTypePropertiesPanel.setLayout(new GridBagLayout());
            namespaceTypePropertiesPanel.add(getNamespaceTypeConfigurationPanel(), gridBagConstraints33);
            namespaceTypePropertiesPanel.add(getSchemaElementTypeConfigurationPanel(), gridBagConstraints34);
        }
        return namespaceTypePropertiesPanel;
    }


    /**
     * This method initializes namespaceTypeCconfigurationPanel
     * 
     * @return javax.swing.JPanel
     */
    private NamespaceTypeConfigurePanel getNamespaceTypeConfigurationPanel() {
        if (namespaceTypeConfigurationPanel == null) {
            namespaceTypeConfigurationPanel = new NamespaceTypeConfigurePanel();
            namespaceTypeConfigurationPanel.setName("namespaceTypeCconfigurationPanel");
        }
        return namespaceTypeConfigurationPanel;
    }


    /**
     * This method initializes schemaElementTypeConfigurationPanel
     * 
     * @return javax.swing.JPanel
     */
    private SchemaElementTypeConfigurePanel getSchemaElementTypeConfigurationPanel() {
        if (schemaElementTypeConfigurationPanel == null) {
            schemaElementTypeConfigurationPanel = new SchemaElementTypeConfigurePanel();
        }
        return schemaElementTypeConfigurationPanel;
    }


    /**
     * This method initializes discoveryTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getDiscoveryTabbedPane() {
        if (discoveryTabbedPane == null) {
            discoveryTabbedPane = new JTabbedPane();
            List discoveryTypes = ExtensionsLoader.getInstance().getDiscoveryExtensions();
            if (discoveryTypes != null) {
                for (int i = 0; i < discoveryTypes.size(); i++) {
                    DiscoveryExtensionDescriptionType dd = (DiscoveryExtensionDescriptionType) discoveryTypes.get(i);
                    try {
                        NamespaceTypeDiscoveryComponent comp = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
                            .getNamespaceTypeDiscoveryComponent(dd.getName(), info.getNamespaces());
                        if (comp != null) {
                            discoveryTabbedPane.addTab(dd.getDisplayName(), comp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorDialog.showErrorDialog("Error loading discovery type: " + dd.getDisplayName(), e);
                    }
                }
            }
        }
        return discoveryTabbedPane;
    }


    private void saveModifications() {
        int confirmed = JOptionPane.showConfirmDialog(ModificationViewer.this, "Are you sure you want to save?",
            "Confirm Save", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.OK_OPTION) {
            // verify no needed namespace types have been removed or modified
            if (!CommonTools.usedTypesAvailable(info.getServiceDescriptor())) {
                Set unavailable = CommonTools.getUnavailableUsedTypes(info.getServiceDescriptor());
                String[] message = {"The following schema element types used in the service",
                        "are not available in the specified namespace types!", "Please add schemas as appropriate.",
                        "\n"};
                String[] err = new String[unavailable.size() + message.length];
                System.arraycopy(message, 0, err, 0, message.length);
                int index = message.length;
                Iterator unavailableIter = unavailable.iterator();
                while (unavailableIter.hasNext()) {
                    err[index] = unavailableIter.next().toString();
                    index++;
                }
                JOptionPane.showMessageDialog(ModificationViewer.this, err, "Unavailable types found",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager.getInstance().getGridPortal(), "Save") {
                public void process() {
                    try {

                        // set the service description
                        info.getServices().getService(0).setDescription(getDescriptionTextArea().getText());

                        StatisticsClient.sendModifiedServiceStat(CommonTools.getIntroduceVersion(), info.getServices()
                            .getService(0).getName(), info.getServices().getService(0).getNamespace());

                        // walk the namespaces and make sure they are valid
                        setProgressText("validating namespaces");
                        NamespacesType namespaces = info.getNamespaces();
                        if ((namespaces != null) && (namespaces.getNamespace() != null)) {
                            for (int i = 0; i < namespaces.getNamespace().length; i++) {
                                NamespaceType currentNs = namespaces.getNamespace(i);
                                if (currentNs.getPackageName() != null) {
                                    if ((currentNs.getGenerateStubs() == null)
                                        || (!currentNs.getGenerateStubs().booleanValue())) {
                                        if (!CommonTools.isValidNoStubPackageName(currentNs.getPackageName())) {
                                            setErrorMessage("Error: Invalid package name for namespace "
                                                + currentNs.getNamespace() + " : " + currentNs.getPackageName());
                                            return;
                                        } else if (!CommonTools.isValidPackageName(currentNs.getPackageName())) {
                                            setErrorMessage("Error: Invalid package name for namespace "
                                                + currentNs.getNamespace() + " : " + currentNs.getPackageName());
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        // ExtensionTools.removeAuthorizationServiceExtensios(info);
                        info.getServices().getService(0).setServiceSecurity(securityPanel.getServiceSecurity(true));

                        // check the methods to make sure they are valid.......
                        if ((info.getServices() != null) && (info.getServices().getService() != null)) {
                            for (int serviceI = 0; serviceI < info.getServices().getService().length; serviceI++) {
                                ServiceType service = info.getServices().getService(serviceI);
                                copyRequiredJars(service.getServiceSecurity());
                                if ((service.getMethods() != null) && (service.getMethods().getMethod() != null)) {
                                    List methodNames = new ArrayList();
                                    if ((service.getMethods() != null) && (service.getMethods().getMethod() != null)) {
                                        for (int methodI = 0; methodI < service.getMethods().getMethod().length; methodI++) {
                                            MethodType method = service.getMethods().getMethod(methodI);
                                            copyRequiredJars(method.getMethodSecurity());
                                            if (!(methodNames.contains(method.getName()))) {
                                                methodNames.add(method.getName());
                                            } else {
                                                setErrorMessage("The service " + service.getName()
                                                    + " has duplicate methods " + method.getName());
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // save the metadata and methods and then call the
                        // resync and build
                        setProgressText("writting service document");
                        Utils.serializeDocument(methodsDirectory.getAbsolutePath() + File.separator + "introduce.xml",
                            info.getServiceDescriptor(), IntroduceConstants.INTRODUCE_SKELETON_QNAME);

                        // call the sync tools
                        setProgressText("synchronizing skeleton");
                        SyncTools sync = new SyncTools(methodsDirectory);
                        sync.sync();

                        // build the synchronized service
                        setProgressText("rebuilding skeleton");
                        String cmd = CommonTools.getAntCommand("clean all", methodsDirectory.getAbsolutePath());
                        Process p = CommonTools.createAndOutputProcess(cmd);
                        p.waitFor();

                        if (p.exitValue() != 0) {
                            setErrorMessage("Error: Unable to rebuild the skeleton");
                        }
                        dirty = false;
                        setProgressText("loading service properties");
                        info.setIntroduceServiceProperties(loadServiceProps());
                        setLastSaved(info.getIntroduceServiceProperties().getProperty(
                            IntroduceConstants.INTRODUCE_SKELETON_TIMESTAMP));
                        this.setProgressText("");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        setErrorMessage("Error: " + e1.getMessage());
                        return;
                    }
                    // reinitialize the GUI with changes from saved model
                    try {
                        reInitialize(methodsDirectory);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };

            Thread th = new Thread(r);
            th.start();
        }
    }


    private void copyRequiredJars(ServiceSecurity sec) throws Exception {
        if (sec != null) {
            ServiceAuthorization auth = sec.getServiceAuthorization();
            if (auth != null) {
                if (auth.getGridGrouperAuthorization() != null) {
                    copyGridGrouperJars();
                } else if (auth.getCSMAuthorization() != null) {
                    copyCSMJars();
                }
            }
        }
    }


    private void copyRequiredJars(MethodSecurity sec) throws Exception {
        if (sec != null) {
            MethodAuthorization auth = sec.getMethodAuthorization();
            if (auth != null) {
                if (auth.getGridGrouperAuthorization() != null) {
                    copyGridGrouperJars();
                } else if (auth.getCSMAuthorization() != null) {
                    copyCSMJars();
                }
            }
        }
    }


    private void copyGridGrouperJars() throws Exception {
        File sourceDir = new File("ext" + File.separator + "skeleton" + File.separator + "gridgrouper" + File.separator
            + "lib");
        addJarsToService(sourceDir);
    }


    private void copyCSMJars() throws Exception {
        File src = new File("ext" + File.separator + "skeleton" + File.separator + "csm" + File.separator + "lib");
        addJarsToService(src);
    }


    private void addJarsToService(File sourceDir) throws Exception {
        Set existingLibs = new HashSet();
        Set addedLibs = new HashSet();

        File serviceLibDir = new File(info.getBaseDirectory() + File.separator + "lib");
        existingLibs.addAll(Utils.recursiveListFiles(serviceLibDir, new FileFilters.JarFileFilter()));

        Utils.copyDirectory(sourceDir, serviceLibDir);
        addedLibs.addAll(Utils.recursiveListFiles(serviceLibDir, new FileFilters.JarFileFilter()));

        // compute the set difference from before and after library copy
        addedLibs.removeAll(existingLibs);

        // add the new jars to the Eclipse .classpath file
        File[] addedJars = new File[addedLibs.size()];
        addedLibs.toArray(addedJars);
        File classpathFile = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + ".classpath");
        ExtensionUtilities.syncEclipseClasspath(classpathFile, addedJars);
    }


    /**
     * This method initializes namespaceConfPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getNamespaceConfPanel() {
        if (namespaceConfPanel == null) {
            namespaceConfPanel = new JPanel();
            namespaceConfPanel.setLayout(new BoxLayout(namespaceConfPanel, BoxLayout.Y_AXIS));
            namespaceConfPanel.add(getDiscoveryPanel());
            namespaceConfPanel.add(getNamespaceTypePropertiesPanel());
        }
        return namespaceConfPanel;
    }


    /**
     * This method initializes servicePropertiesPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServicePropertiesPanel() {
        if (servicePropertiesPanel == null) {
            GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
            gridBagConstraints26.gridx = 0;
            gridBagConstraints26.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints26.weightx = 1.0D;
            gridBagConstraints26.weighty = 1.0D;
            gridBagConstraints26.gridy = 0;
            GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
            gridBagConstraints25.gridx = 0;
            gridBagConstraints25.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints25.gridy = 1;
            servicePropertiesPanel = new JPanel();
            servicePropertiesPanel.setLayout(new GridBagLayout());
            servicePropertiesPanel.add(getServicePropertiesTableContainerPanel(), gridBagConstraints26);
            servicePropertiesPanel.add(getServicePropertiesControlPanel(), gridBagConstraints25);
        }
        return servicePropertiesPanel;
    }


    /**
     * This method initializes servicePropertiesTableContainerPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServicePropertiesTableContainerPanel() {
        if (servicePropertiesTableContainerPanel == null) {
            GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
            gridBagConstraints28.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints28.gridx = 0;
            gridBagConstraints28.gridy = 0;
            gridBagConstraints28.weightx = 1.0;
            gridBagConstraints28.weighty = 1.0;
            gridBagConstraints28.insets = new java.awt.Insets(5, 5, 5, 5);
            servicePropertiesTableContainerPanel = new JPanel();
            servicePropertiesTableContainerPanel.setLayout(new GridBagLayout());
            servicePropertiesTableContainerPanel.add(getServicePropertiesTableScrollPane(), gridBagConstraints28);
        }
        return servicePropertiesTableContainerPanel;
    }


    /**
     * This method initializes servicePropertiesTableScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getServicePropertiesTableScrollPane() {
        if (servicePropertiesTableScrollPane == null) {
            servicePropertiesTableScrollPane = new JScrollPane();
            servicePropertiesTableScrollPane.setViewportView(getServicePropertiesTable());
        }
        return servicePropertiesTableScrollPane;
    }


    /**
     * This method initializes servicePropertiesTable
     * 
     * @return javax.swing.JTable
     */
    private ServicePropertiesTable getServicePropertiesTable() {
        if (servicePropertiesTable == null) {
            servicePropertiesTable = new ServicePropertiesTable(info);
        }
        return servicePropertiesTable;
    }


    /**
     * This method initializes servicePropertiesControlPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServicePropertiesControlPanel() {
        if (servicePropertiesControlPanel == null) {
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = GridBagConstraints.BOTH;
            gridBagConstraints15.gridy = 5;
            gridBagConstraints15.weightx = 1.0;
            gridBagConstraints15.insets = new Insets(2, 2, 10, 10);
            gridBagConstraints15.gridx = 0;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints14.gridy = 4;
            descriptionLabel = new JLabel();
            descriptionLabel.setText("Description:");
            GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
            gridBagConstraints42.gridx = 1;
            gridBagConstraints42.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints42.gridwidth = 1;
            gridBagConstraints42.gridheight = 4;
            gridBagConstraints42.gridy = 0;
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.gridx = 0;
            gridBagConstraints41.anchor = java.awt.GridBagConstraints.SOUTHWEST;
            gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints41.gridy = 2;
            servicePropertiesValueLabel = new JLabel();
            servicePropertiesValueLabel.setText("Default Value:");
            GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
            gridBagConstraints40.gridx = 0;
            gridBagConstraints40.anchor = java.awt.GridBagConstraints.SOUTHWEST;
            gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints40.gridy = 0;
            servicePropertiesKeyLabel = new JLabel();
            servicePropertiesKeyLabel.setText("Key:");
            GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
            gridBagConstraints39.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints39.gridy = 3;
            gridBagConstraints39.weightx = 1.0;
            gridBagConstraints39.insets = new java.awt.Insets(2, 2, 10, 10);
            gridBagConstraints39.gridx = 0;
            GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
            gridBagConstraints38.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints38.gridy = 1;
            gridBagConstraints38.weightx = 1.0;
            gridBagConstraints38.insets = new java.awt.Insets(2, 2, 10, 10);
            gridBagConstraints38.gridx = 0;
            GridBagConstraints gridBagConstraints43 = new GridBagConstraints();
            gridBagConstraints43.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints43.gridy = 6;
            gridBagConstraints43.weightx = 1.0;
            gridBagConstraints43.insets = new java.awt.Insets(2, 2, 10, 10);
            gridBagConstraints43.gridx = 0;
            servicePropertiesControlPanel = new JPanel();
            servicePropertiesControlPanel.setLayout(new GridBagLayout());
            servicePropertiesControlPanel.add(getServicePropertyKeyTextField(), gridBagConstraints38);
            servicePropertiesControlPanel.add(getServicePropertyValueTextField(), gridBagConstraints39);
            servicePropertiesControlPanel.add(servicePropertiesKeyLabel, gridBagConstraints40);
            servicePropertiesControlPanel.add(servicePropertiesValueLabel, gridBagConstraints41);
            servicePropertiesControlPanel.add(getServicePropertiesButtonPanel(), gridBagConstraints42);
            servicePropertiesControlPanel.add(getServicePropertiesIsFromETCCheckBox(), gridBagConstraints43);
            servicePropertiesControlPanel.add(descriptionLabel, gridBagConstraints14);
            servicePropertiesControlPanel.add(getServicePropertyDescriptionTextField(), gridBagConstraints15);
        }
        return servicePropertiesControlPanel;
    }


    /**
     * This method initializes addServiceProperyButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddServiceProperyButton() {
        if (addServiceProperyButton == null) {
            addServiceProperyButton = new JButton();
            addServiceProperyButton.setText("Add");
            addServiceProperyButton.setIcon(PortalLookAndFeel.getAddIcon());
            addServiceProperyButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if ((getServicePropertyKeyTextField().getText().length() != 0)
                        && CommonTools.isValidJavaField(getServicePropertyKeyTextField().getText())) {
                        String key = getServicePropertyKeyTextField().getText();
                        String value = getServicePropertyValueTextField().getText();
                        boolean isFromETC = getServicePropertiesIsFromETCCheckBox().isSelected();
                        String desc = getServicePropertyDescriptionTextField().getText();
                        getServicePropertiesTable().addRow(key, value, isFromETC, desc);
                        getServicePropertiesIsFromETCCheckBox().setSelected(false);
                    } else {
                        JOptionPane
                            .showMessageDialog(ModificationViewer.this,
                                "Service Property key must be a valid java identifier, beginning with a lowercase character.");
                    }
                }
            });
        }
        return addServiceProperyButton;
    }


    /**
     * This method initializes removeServicePropertyButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRemoveServicePropertyButton() {
        if (removeServicePropertyButton == null) {
            removeServicePropertyButton = new JButton();
            removeServicePropertyButton.setText("Remove");
            removeServicePropertyButton.setIcon(PortalLookAndFeel.getRemoveIcon());
            removeServicePropertyButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        getServicePropertiesTable().removeSelectedRow();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        ErrorDialog.showErrorDialog(e1);
                    }
                }
            });
        }
        return removeServicePropertyButton;
    }


    /**
     * This method initializes servicePropertyKeyTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getServicePropertyKeyTextField() {
        if (servicePropertyKeyTextField == null) {
            servicePropertyKeyTextField = new JTextField();
        }
        return servicePropertyKeyTextField;
    }


    /**
     * This method initializes servicePropertyValueTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getServicePropertyValueTextField() {
        if (servicePropertyValueTextField == null) {
            servicePropertyValueTextField = new JTextField();
        }
        return servicePropertyValueTextField;
    }


    /**
     * This method initializes servicePropertiesButtonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServicePropertiesButtonPanel() {
        if (servicePropertiesButtonPanel == null) {
            GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
            gridBagConstraints37.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints37.gridy = 0;
            gridBagConstraints37.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints37.gridx = 0;
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints32.gridy = 1;
            gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints32.gridx = 0;
            servicePropertiesButtonPanel = new JPanel();
            servicePropertiesButtonPanel.setLayout(new GridBagLayout());
            servicePropertiesButtonPanel.add(getRemoveServicePropertyButton(), gridBagConstraints32);
            servicePropertiesButtonPanel.add(getAddServiceProperyButton(), gridBagConstraints37);
        }
        return servicePropertiesButtonPanel;
    }


    /**
     * This method initializes servicesTabbedPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getResourceesTabbedPanel() {
        if (resourceesTabbedPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 0.0D;
            gridBagConstraints1.weighty = 0.0D;
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
            gridBagConstraints45.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints45.gridy = 0;
            gridBagConstraints45.weightx = 1.0D;
            gridBagConstraints45.weighty = 1.0D;
            gridBagConstraints45.gridx = 0;
            resourceesTabbedPanel = new JPanel();
            resourceesTabbedPanel.setLayout(new GridBagLayout());
            resourceesTabbedPanel.add(getResourcesPanel(), gridBagConstraints45);
            resourceesTabbedPanel.add(getResourcesOptionsPanel(), gridBagConstraints1);
        }
        return resourceesTabbedPanel;
    }


    /**
     * This method initializes resourcesPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getResourcesPanel() {
        if (resourcesPanel == null) {
            GridBagConstraints gridBagConstraints46 = new GridBagConstraints();
            gridBagConstraints46.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints46.gridx = 0;
            gridBagConstraints46.gridy = 0;
            gridBagConstraints46.weightx = 1.0;
            gridBagConstraints46.weighty = 1.0;
            gridBagConstraints46.insets = new java.awt.Insets(2, 2, 2, 2);
            resourcesPanel = new JPanel();
            resourcesPanel.setLayout(new GridBagLayout());
            resourcesPanel.add(getResourcesScrollPane(), gridBagConstraints46);
        }
        return resourcesPanel;
    }


    /**
     * This method initializes resourcesScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getResourcesScrollPane() {
        if (resourcesScrollPane == null) {
            resourcesScrollPane = new JScrollPane();
            resourcesScrollPane.setViewportView(getResourcesJTree());
        }
        return resourcesScrollPane;
    }


    /**
     * This method initializes resourcesJTree
     * 
     * @return javax.swing.JTree
     */
    private ServicesJTree getResourcesJTree() {
        if (resourcesJTree == null) {
            resourcesJTree = new ServicesJTree(info.getServices(), info, getResourcesOptionsPanel());
            resourcesJTree.setVisibleRowCount(10);
            resourcesJTree.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    super.focusGained(e);
                    resourcesJTree.setServices(info.getServices(), info);
                }
            });
            // initialize the option cards for this tree
            resourcesOptionsPanel.add(new ServicesButtonPanel(resourcesJTree), "services");
            resourcesOptionsPanel.add(new ServiceButtonPanel(resourcesJTree), "service");
            resourcesOptionsPanel.add(new MethodsButtonPanel(resourcesJTree), "methods");
            resourcesOptionsPanel.add(new MethodButtonPanel(resourcesJTree), "method");
            resourcesOptionsPanel.add(new ResourcesButtonPanel(resourcesJTree), "resources");
            resourcesOptionsPanel.add(new JPanel(), "blank");
        }
        return resourcesJTree;
    }


    /**
     * This method initializes rpHolderPanel
     * 
     * @return javax.swing.JPanel
     */
    private ModifyResourcePropertiesPanel getRpHolderPanel() {
        if (rpHolderPanel == null) {
            if (info.getServices().getService(0).getResourcePropertiesList() == null) {
                ResourcePropertiesListType properties = new ResourcePropertiesListType();
                properties.setResourceProperty(null);
                info.getServices().getService(0).setResourcePropertiesList(properties);
            }
            rpHolderPanel = new ModifyResourcePropertiesPanel(info.getServices().getService(0), info.getNamespaces(),
                new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "etc"), new File(info
                    .getBaseDirectory().getAbsolutePath()
                    + File.separator + "schema" + File.separator + info.getServices().getService(0).getName()), false);
        }
        return rpHolderPanel;
    }


    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getTypesSplitPane() {
        if (typesSplitPane == null) {
            typesSplitPane = new JSplitPane();
            typesSplitPane.setOneTouchExpandable(true);
            typesSplitPane.setLeftComponent(getNamespaceTableScrollPane());
            typesSplitPane.setRightComponent(getNamespaceConfPanel());
            typesSplitPane.setDividerLocation(0.4);
            typesSplitPane.setResizeWeight(0.4);
        }
        return typesSplitPane;
    }


    /**
     * This method initializes propertyIsFromETCCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getServicePropertiesIsFromETCCheckBox() {
        if (propertyIsFromETCCheckBox == null) {
            propertyIsFromETCCheckBox = new JCheckBox();
            propertyIsFromETCCheckBox
                .setToolTipText("Checking this box will let introduce that the value of this property will be a "
                    + "file location which is meant to be relative from the service's etc location in "
                    + "the service container.  The value that is set at deploy time will be replaced "
                    + "with the absolute path to the etc directory pluss the value of the variable.");
            propertyIsFromETCCheckBox.setText("Value is a relative file path from the service's ETC location.");
        }
        return propertyIsFromETCCheckBox;
    }


    private NamespaceType[] mergeNamespaceArrays(NamespaceType[] current, NamespaceType[] additional) {
        Set additionalNamespaces = new HashSet();
        for (int i = 0; i < additional.length; i++) {
            additionalNamespaces.add(additional[i].getNamespace());
        }
        List merged = new ArrayList();
        Collections.addAll(merged, additional);
        if (current.length != 0) {
            for (int i = 0; i < current.length; i++) {
                String currentNamespace = current[i].getNamespace();
                if (!additionalNamespaces.contains(currentNamespace)) {
                    merged.add(current[i]);
                }
            }
        }
        NamespaceType[] mergedArray = new NamespaceType[merged.size()];
        merged.toArray(mergedArray);
        return mergedArray;
    }


    /**
     * This method initializes resourcesOptionsPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getResourcesOptionsPanel() {
        if (resourcesOptionsPanel == null) {
            resourcesOptionsPanel = new JPanel(new CardLayout());
            resourcesOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Information and Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
        }
        return resourcesOptionsPanel;
    }


    /**
     * This method initializes descriptionPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDescriptionPanel() {
        if (descriptionPanel == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = GridBagConstraints.BOTH;
            gridBagConstraints12.weighty = 1.0;
            gridBagConstraints12.weightx = 1.0;
            descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new GridBagLayout());
            descriptionPanel.add(getDescriptionScrollPane(), gridBagConstraints12);
        }
        return descriptionPanel;
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


    /**
     * This method initializes descriptionTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getDescriptionTextArea() {
        if (descriptionTextArea == null) {
            descriptionTextArea = new JTextArea();
            if (info.getServices().getService(0).getDescription() == null) {
                info.getServices().getService(0).setDescription("");
            }
            descriptionTextArea.setText(info.getServices().getService(0).getDescription());
        }
        return descriptionTextArea;
    }


    /**
     * This method initializes servicePropertyDescriptionTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getServicePropertyDescriptionTextField() {
        if (servicePropertyDescriptionTextField == null) {
            servicePropertyDescriptionTextField = new JTextField();
        }
        return servicePropertyDescriptionTextField;
    }
}