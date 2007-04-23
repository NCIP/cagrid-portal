package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.cadsr.portal.discovery.CaDSRDiscoveryConstants;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.DocumentChangeAdapter;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.common.portal.PromptButtonDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.codegen.DomainModelCreationUtil;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.tree.CheckTreeSelectionEvent;
import gov.nih.nci.cagrid.data.ui.tree.CheckTreeSelectionListener;
import gov.nih.nci.cagrid.data.ui.tree.uml.UMLClassTreeNode;
import gov.nih.nci.cagrid.data.ui.tree.uml.UMLPackageTreeNode;
import gov.nih.nci.cagrid.data.ui.tree.uml.UMLProjectTree;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  DomainModelConfigPanel
 *  Panel to contain all the configuration of the domain model
 * 
 * @author David Ervin
 * 
 * @created Apr 11, 2007 9:59:24 AM
 * @version $Id: DomainModelConfigPanel.java,v 1.2 2007-04-23 17:26:43 dervin Exp $ 
 */
public class DomainModelConfigPanel extends JPanel {

    private transient List<DomainModelClassSelectionListener> classSelectionListeners = null;
    private ExtensionTypeExtensionData extensionTypeExtensionData;
    private ServiceInformation serviceInfo;
    private ExtensionDataManager extensionDataManager;
    private Project mostRecentProject;
    
    private NotifyingButtonGroup domainSourceGroup = null;

    private JPanel domainModelSourcePanel = null;
    private JRadioButton noDomainModelRadioButton = null;
    private JRadioButton cadsrDomainModelRadioButton = null;
    private JRadioButton suppliedDomainModelRadioButton = null;    
    private JPanel domainModelSelectionPanel = null;
    private JTextField domainModelNameTextField = null;
    private JButton selectDomainModelButton = null;
    private JButton visualizeDomainModelButton = null;    
    private JPanel cadsrDomainModelPanel = null;    
    private CaDSRBrowserPanel cadsrBrowserPanel = null;    
    private JPanel packageSelectionButtonPanel = null;
    private JButton addFullProjectButton = null;
    private JButton addPackageButton = null;
    private JButton removePackageButton = null;    
    private JScrollPane umlClassScrollPane = null;
    private UMLProjectTree umlTree = null;
    
    public DomainModelConfigPanel(ExtensionTypeExtensionData extensionData,
        ServiceInformation info, ExtensionDataManager dataManager) {
        this.extensionTypeExtensionData = extensionData;
        this.serviceInfo = info;
        this.extensionDataManager = dataManager;
        this.classSelectionListeners = new LinkedList<DomainModelClassSelectionListener>();
        initialize();
    }
    
    
    public void populateFromExtensionData() {
        getDomainSourceGroup();
        if (getSuppliedDomainModelRadioButton().isSelected()) {
            try {
                DomainModel model = MetadataUtils.deserializeDomainModel(
                    new FileReader(getDomainModelNameTextField().getText()));
                installDomainModel(model);
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error loading domain model", ex.getMessage(), ex);
            }
        } else if (getCadsrDomainModelRadioButton().isSelected()) {
            try {
                // get the project from cadsr that has been selected
                String projectLongName = extensionDataManager.getCadsrProjectLongName();
                String projectVersion = extensionDataManager.getCadsrProjectVersion();
                CaDSRServiceClient cadsrClient = new CaDSRServiceClient(
                    getCadsrBrowserPanel().getCadsr().getText());
                Project[] allProjects = cadsrClient.findAllProjects();
                for (Project proj : allProjects) {
                    if (proj.getLongName().equals(projectLongName) 
                        && proj.getVersion().equals(projectVersion)) {
                        mostRecentProject = proj;
                        break;
                    }
                }
                
                // populate the UI's packages and classes
                List<String> packageNames = extensionDataManager.getCadsrPackageNames();
                if (packageNames != null) {
                    for (String packName : packageNames) {
                        getUmlTree().addUmlPackage(packName);
                        List<ClassMapping> mappings = 
                            extensionDataManager.getClassMappingsInPackage(packName);
                        for (ClassMapping mapping : mappings) {
                            UMLClassTreeNode classNode = getUmlTree().addUmlClass(
                                packName, mapping.getClassName());
                            classNode.getCheckBox().setSelected(mapping.isSelected());
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error loading cadsr domain model information", 
                    ex.getMessage(), ex);
            }
        }
    }
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
        gridBagConstraints19.gridx = 1;
        gridBagConstraints19.gridy = 1;
        GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
        gridBagConstraints15.gridx = 0;
        gridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints15.gridwidth = 2;
        gridBagConstraints15.weightx = 1.0D;
        gridBagConstraints15.weighty = 1.0D;
        gridBagConstraints15.gridy = 2;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints11.weightx = 1.0D;
        gridBagConstraints11.gridy = 0;
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.gridx = 0;
        gridBagConstraints10.gridheight = 2;
        gridBagConstraints10.gridy = 0;;
        this.setLayout(new GridBagLayout());
        this.add(getDomainModelSourcePanel(), gridBagConstraints10);
        this.add(getDomainModelSelectionPanel(), gridBagConstraints11);
        this.add(getCadsrDomainModelPanel(), gridBagConstraints15);
        this.add(getVisualizeDomainModelButton(), gridBagConstraints19);
    }
    
    
    private NotifyingButtonGroup getDomainSourceGroup() {
        if (domainSourceGroup == null) {
            domainSourceGroup = new NotifyingButtonGroup();
            domainSourceGroup.add(getNoDomainModelRadioButton());
            domainSourceGroup.add(getCadsrDomainModelRadioButton());
            domainSourceGroup.add(getSuppliedDomainModelRadioButton());
            domainSourceGroup.addGroupSelectionListener(new GroupSelectionListener() {
                public void selectionChanged(final ButtonModel previousSelection, final ButtonModel currentSelection) {
                    try {
                        extensionDataManager.storeDomainModelSource(
                            getNoDomainModelRadioButton().isSelected(),
                            getSuppliedDomainModelRadioButton().isSelected());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error storing domain model source", 
                            ex.getMessage(), ex);
                    }
                    
                    // enable / disable panels and components as needed
                    PortalUtils.setContainerEnabled(getCadsrDomainModelPanel(), 
                        currentSelection == getCadsrDomainModelRadioButton().getModel());
                    PortalUtils.setContainerEnabled(getDomainModelSelectionPanel(),
                        currentSelection == getSuppliedDomainModelRadioButton().getModel());
                    getVisualizeDomainModelButton().setEnabled(
                        currentSelection != getNoDomainModelRadioButton().getModel());
                }
            });
            // select radio buttons according to stored cadsr information
            try {
                if (extensionDataManager.isNoDomainModel()) {
                    domainSourceGroup.setSelected(getNoDomainModelRadioButton().getModel(), true);
                } else if (extensionDataManager.isSuppliedDomainModel()) {
                    domainSourceGroup.setSelected(getSuppliedDomainModelRadioButton().getModel(), true);
                    ResourcePropertyType[] resourceProps = serviceInfo.getServices().getService(0)
                        .getResourcePropertiesList().getResourceProperty();
                    for (ResourcePropertyType prop : resourceProps) {
                        if (prop.getQName().equals(DataServiceConstants.DOMAIN_MODEL_QNAME)) {
                            File dmFile = new File(serviceInfo.getBaseDirectory().getAbsolutePath() 
                                + File.separator + "etc" + File.separator + prop.getFileLocation());
                            getDomainModelNameTextField().setText(dmFile.getCanonicalPath());
                            break;
                        }
                    }
                } else {
                    domainSourceGroup.setSelected(getCadsrDomainModelRadioButton().getModel(), true);
                }                
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error determining configured domain model source", 
                    ex.getMessage(), ex);
            }
        }
        return domainSourceGroup;
    }
    
    
    private JPanel getDomainModelSourcePanel() {
        if (domainModelSourcePanel == null) {
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints9.gridy = 2;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints8.gridy = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints5.gridy = 0;
            domainModelSourcePanel = new JPanel();
            domainModelSourcePanel.setLayout(new GridBagLayout());
            domainModelSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, "Domain Model Source", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, 
                PortalLookAndFeel.getPanelLabelColor()));
            domainModelSourcePanel.add(getNoDomainModelRadioButton(), gridBagConstraints5);
            domainModelSourcePanel.add(getCadsrDomainModelRadioButton(), gridBagConstraints8);
            domainModelSourcePanel.add(getSuppliedDomainModelRadioButton(), gridBagConstraints9);
        }
        return domainModelSourcePanel;
    }
    
    
    private JRadioButton getNoDomainModelRadioButton() {
        if (noDomainModelRadioButton == null) {
            noDomainModelRadioButton = new JRadioButton();
            noDomainModelRadioButton.setText("No Domain Model");
        }
        return noDomainModelRadioButton;
    }
    
    
    private JRadioButton getCadsrDomainModelRadioButton() {
        if (cadsrDomainModelRadioButton == null) {
            cadsrDomainModelRadioButton = new JRadioButton();
            cadsrDomainModelRadioButton.setText("caDSR Domain Model");
        }
        return cadsrDomainModelRadioButton;
    }
    
    
    private JRadioButton getSuppliedDomainModelRadioButton() {
        if (suppliedDomainModelRadioButton == null) {
            suppliedDomainModelRadioButton = new JRadioButton();
            suppliedDomainModelRadioButton.setText("Supplied Domain Model");
        }
        return suppliedDomainModelRadioButton;
    }
    
    
    private JPanel getDomainModelSelectionPanel() {
        if (domainModelSelectionPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints7.gridy = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints6.gridx = 0;
            domainModelSelectionPanel = new JPanel();
            domainModelSelectionPanel.setLayout(new GridBagLayout());
            domainModelSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Supplied Domain Model", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                null, PortalLookAndFeel.getPanelLabelColor()));
            domainModelSelectionPanel.add(getDomainModelNameTextField(), gridBagConstraints6);
            domainModelSelectionPanel.add(getSelectDomainModelButton(), gridBagConstraints7);
        }
        return domainModelSelectionPanel;
    }
    
    
    private JTextField getDomainModelNameTextField() {
        if (domainModelNameTextField == null) {
            domainModelNameTextField = new JTextField();
            domainModelNameTextField.setToolTipText(
                "Optional xml file name of an existing domain model");
            domainModelNameTextField.setEditable(false);
        }
        return domainModelNameTextField;
    }
    
    
    private JButton getSelectDomainModelButton() {
        if (selectDomainModelButton == null) {
            selectDomainModelButton = new JButton();
            selectDomainModelButton.setText("Select Domain Model");
            selectDomainModelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // prompt the user for the file
                    String selectedFile = null;
                    try {
                        selectedFile = ResourceManager.promptFile(null, FileFilters.XML_FILTER);
                        if (selectedFile != null) {
                            File inFile = new File(selectedFile);
                            // install the new domain model
                            DomainModel model = MetadataUtils.deserializeDomainModel(new FileReader(inFile));
                            boolean installed = installDomainModel(model);
                            if (installed) {
                                // if that succeded, copy the file in to the service's directory
                                File outFile = new File(serviceInfo.getBaseDirectory().getAbsolutePath() 
                                    + File.separator + "etc" + File.separator + inFile.getName());
                                Utils.copyFile(inFile, outFile);
                                // set up the domain model resource property
                                ResourcePropertyType dmResourceProp = CommonTools.getResourcePropertiesOfType(
                                    serviceInfo.getServices().getService(0), DataServiceConstants.DOMAIN_MODEL_QNAME)[0];
                                dmResourceProp.setFileLocation(outFile.getName());
                                dmResourceProp.setPopulateFromFile(true);
                                // change the domain model text field
                                getDomainModelNameTextField().setText(outFile.getCanonicalPath());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error in file selection: " + ex.getMessage(), ex);
                    }
                }
            });
        }
        return selectDomainModelButton;
    }
    
    
    private JPanel getCadsrDomainModelPanel() {
        if (cadsrDomainModelPanel == null) {
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints14.gridy = 2;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.weighty = 1.0;
            gridBagConstraints14.gridx = 0;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridy = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridy = 0;
            cadsrDomainModelPanel = new JPanel();
            cadsrDomainModelPanel.setLayout(new GridBagLayout());
            cadsrDomainModelPanel.add(getCadsrBrowserPanel(), gridBagConstraints12);
            cadsrDomainModelPanel.add(getPackageSelectionButtonPanel(), gridBagConstraints13);
            cadsrDomainModelPanel.add(getUmlClassScrollPane(), gridBagConstraints14);
        }
        return cadsrDomainModelPanel;
    }
    
    
    private CaDSRBrowserPanel getCadsrBrowserPanel() {
        if (cadsrBrowserPanel == null) {
            cadsrBrowserPanel = new CaDSRBrowserPanel(true, false);
            // get the URL of the cadsr service
            String cadsrUrl = null;
            try {
                cadsrUrl = extensionDataManager.getCadsrUrl();
            } catch (Exception ex) {
                ex.printStackTrace();
                String[] message = new String[] {
                    "There was an error loading the caDSR service url from the extension data.",
                "The URL specified in the Introduce configuration will be used."};
                ErrorDialog.showErrorDialog("Error getting caDSR Service url", message, ex);
            }
            if (cadsrUrl == null || cadsrUrl.length() == 0) {
                cadsrUrl = ResourceManager.getServiceURLProperty(CaDSRDiscoveryConstants.CADSR_URL_PROPERTY);
                try {
                    extensionDataManager.storeCadsrServiceUrl(cadsrUrl);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ErrorDialog.showErrorDialog("Error storing caDSR url", ex.getMessage(), ex);
                }
            }
            cadsrBrowserPanel.getCadsr().setText(cadsrUrl);
            // add listener to the cadsr URL text field
            cadsrBrowserPanel.getCadsr().getDocument().addDocumentListener(new DocumentChangeAdapter() {
                public void documentEdited(DocumentEvent e) {
                    try {
                        extensionDataManager.storeCadsrServiceUrl(
                            getCadsrBrowserPanel().getCadsr().getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error storing domain model source", 
                            ex.getMessage(), ex);
                    }
                }
            });
        }
        return cadsrBrowserPanel;
    }
    
    
    private JPanel getPackageSelectionButtonPanel() {
        if (packageSelectionButtonPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints4.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints3.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.gridy = 0;
            packageSelectionButtonPanel = new JPanel();
            packageSelectionButtonPanel.setLayout(new GridBagLayout());
            packageSelectionButtonPanel.add(getAddFullProjectButton(), gridBagConstraints2);
            packageSelectionButtonPanel.add(getAddPackageButton(), gridBagConstraints3);
            packageSelectionButtonPanel.add(getRemovePackageButton(), gridBagConstraints4);
        }
        return packageSelectionButtonPanel;
    }
    
    
    private JButton getAddFullProjectButton() {
        if (addFullProjectButton == null) {
            addFullProjectButton = new JButton();
            addFullProjectButton.setText("Add Full Project");
            addFullProjectButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Project selectedProject = getCadsrBrowserPanel().getSelectedProject();
                    if (selectedProject != null) {
                        // contact the caDSR for all packages in the project
                        try {
                            CaDSRServiceClient cadsrClient = new CaDSRServiceClient(
                                getCadsrBrowserPanel().getCadsr().getText());
                            // TODO: this can be time consuming... use Busy Dialog Runnable here
                            UMLPackageMetadata[] umlPackages = cadsrClient.findPackagesInProject(selectedProject);
                            for (UMLPackageMetadata pack : umlPackages) {
                                addUmlPackageToModel(selectedProject, pack);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ErrorDialog.showErrorDialog(
                                "Error loading packages from project: " + ex.getMessage(), ex);
                        }
                    } else {
                        PortalUtils.showMessage("Please select a project");
                    }
                }
            });
        }
        return addFullProjectButton;
    }
    
    
    private JButton getAddPackageButton() {
        if (addPackageButton == null) {
            addPackageButton = new JButton();
            addPackageButton.setText("Add Package");
            addPackageButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Project selectedProject = getCadsrBrowserPanel().getSelectedProject();
                    UMLPackageMetadata selectedPackage = cadsrBrowserPanel.getSelectedPackage();
                    if (selectedProject != null && selectedPackage != null) {
                        addUmlPackageToModel(selectedProject, selectedPackage);
                    } else {
                        PortalUtils.showMessage("Please select both a project and package");
                    }
                }
            });
        }
        return addPackageButton;
    }
    
    
    private JButton getRemovePackageButton() {
        if (removePackageButton == null) {
            removePackageButton = new JButton();
            removePackageButton.setText("Remove Package");
            removePackageButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Project selectedProject = getCadsrBrowserPanel().getSelectedProject();
                    UMLPackageMetadata selectedPackage = getCadsrBrowserPanel().getSelectedPackage();
                    if (selectedProject != null && selectedPackage != null) {
                        boolean removed = removeUmlPackage(selectedProject, selectedPackage.getName());
                        if (!removed) {
                            PortalUtils.showErrorMessage("The selected package was not removed");
                        }
                    } else {
                        PortalUtils.showMessage("Please select both a project and package");
                    }
                }
            });
        }
        return removePackageButton;
    }
    
    
    private JScrollPane getUmlClassScrollPane() {
        if (umlClassScrollPane == null) {
            umlClassScrollPane = new JScrollPane();
            umlClassScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, "UML Class Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                null, PortalLookAndFeel.getPanelLabelColor()));
            umlClassScrollPane.setViewportView(getUmlTree());
        }
        return umlClassScrollPane;
    }
    
    
    private UMLProjectTree getUmlTree() {
        if (umlTree == null) {
            umlTree = new UMLProjectTree();
            umlTree.addCheckTreeSelectionListener(new CheckTreeSelectionListener() {
                public void nodeChecked(CheckTreeSelectionEvent e) {
                    if (e.getNode() instanceof UMLClassTreeNode) {
                        UMLClassTreeNode classNode = (UMLClassTreeNode) e.getNode();
                        // add the type to the configuration table
                        String packName = ((UMLPackageTreeNode) classNode.getParent()).getPackageName();
                        String className = classNode.getClassName();
                        try {
                            // need the class mapping
                            ClassMapping mapping = extensionDataManager.getClassMapping(packName, className);
                            // and the namespace type
                            String namespace = extensionDataManager.getMappedNamespaceForPackage(packName);
                            NamespaceType packageNamespace = CommonTools.getNamespaceType(serviceInfo.getNamespaces(), namespace);
                            // inform interested parties of the selection
                            fireClassAdded(packName, mapping, packageNamespace);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ErrorDialog.showErrorDialog("Error selecting class: " + ex.getMessage(), ex);
                        }
                    }
                }


                public void nodeUnchecked(CheckTreeSelectionEvent e) {
                    if (e.getNode() instanceof UMLClassTreeNode) {
                        UMLClassTreeNode classNode = (UMLClassTreeNode) e.getNode();
                        // add the type to the configuration table
                        String packName = ((UMLPackageTreeNode) classNode.getParent()).getPackageName();
                        String className = classNode.getClassName();
                        fireClassRemoved(packName, className);
                    }
                }
            });
        }
        return umlTree;
    }
    
    
    private JButton getVisualizeDomainModelButton() {
        if (visualizeDomainModelButton == null) {
            visualizeDomainModelButton = new JButton();
            visualizeDomainModelButton.setText("Visualize Domain Model");
            visualizeDomainModelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    DomainModel model = null;
                    // get the domain model selected
                    if (getSuppliedDomainModelRadioButton().isSelected()
                        && getDomainModelNameTextField().getText().length() != 0) {
                        // domain model from file system
                        String filename = getDomainModelNameTextField().getText();
                        try {
                            model = MetadataUtils.deserializeDomainModel(
                                new FileReader(filename));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ErrorDialog.showErrorDialog(
                                "Error loading domain model: " + ex.getMessage(), ex);
                        }
                    } else if (getCadsrDomainModelRadioButton().isSelected()) {
                        // build the domain model
                        try {
                            Data data = ExtensionDataUtils.getExtensionData(
                                extensionTypeExtensionData);
                            final CadsrInformation info = data.getCadsrInformation();
                            // TODO: thread this out
                            model = DomainModelCreationUtil.createDomainModel(info);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ErrorDialog.showErrorDialog(
                                "Error getting caDSR information: " + ex.getMessage(), ex);
                        }   
                    }
                    if (model != null) {
                        new DomainModelVisualizationDialog(
                            PortalResourceManager.getInstance().getGridPortal(), model);
                    }
                }
            });
        }
        return visualizeDomainModelButton;
    }
    
    
    //
    // ---
    // non UI related helpers
    // ---
    //
    
    
    private boolean removeUmlPackage(Project proj, String packageName) {
        Set packageNames = getUmlTree().getPackagesInTree();
        if (!packageNames.contains(packageName) 
            || (mostRecentProject != null && !projectEquals(proj, mostRecentProject))) {
            PortalUtils.showMessage("The selected package is not involved in the model");
            return false;
        }
        // remove the package from the cadsr information
        try {
            extensionDataManager.removeCadsrPackage(packageName);
            String[] classNames = getUmlTree().getSelectedClassNames(packageName);
            for (String name : classNames) {
                fireClassRemoved(packageName, name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error removing cadsr package: " + ex.getMessage(), ex);
            return false;
        }
        // clean out the UI
        getUmlTree().removeUmlPackage(packageName);
        return true;
    }

    
    private boolean installDomainModel(DomainModel model) {
        // clear the UML tree
        getUmlTree().clearTree();
        
        // clear out classes elsewhere
        fireClassesCleared();
        
        // set the most recent project information
        Project proj = new Project();
        proj.setDescription(model.getProjectDescription());
        proj.setLongName(model.getProjectLongName());
        proj.setShortName(model.getProjectShortName());
        proj.setVersion(model.getProjectVersion());
        
        // walk classes from the model, grouping classes by package
        Map<String, List<String>> packageToClass = new HashMap();
        if (model.getExposedUMLClassCollection() != null 
            && model.getExposedUMLClassCollection().getUMLClass() != null) {
            for (UMLClass umlClass : model.getExposedUMLClassCollection().getUMLClass()) {
                String packName = umlClass.getPackageName();
                List<String> classList = packageToClass.get(packName);
                if (classList == null) {
                    classList = new LinkedList();
                    packageToClass.put(packName, classList);
                }
                classList.add(umlClass.getClassName());
            }
        }
        
        // walk packages names --> class lists, create CadsrPackages
        List<CadsrPackage> cadsrPackages = new ArrayList(packageToClass.keySet().size());
        for (String packName : packageToClass.keySet()) {
            // get the namespace type for the package
            NamespaceType packageNamespace = getNamespaceForPackage(
                proj.getShortName(), proj.getVersion(), packName);
            // create the cadsr package
            CadsrPackage pack = new CadsrPackage();
            pack.setName(packName);
            pack.setMappedNamespace(packageNamespace.getNamespace());
            // create class mappings
            List<String> classNames = packageToClass.get(packName);
            ClassMapping[] mappings = createClassMappings(packName, packageNamespace, classNames);
            pack.setCadsrClass(mappings);
            // queue up the package for addition later
            cadsrPackages.add(pack);
            // add the package and classes to the UML tree
            getUmlTree().addUmlPackage(packName);
            for (ClassMapping mapping : mappings) {
                getUmlTree().addUmlClass(packName, mapping.getClassName());
                fireClassAdded(packName, mapping, packageNamespace);
            }
        }
        // convert package list to an array
        CadsrPackage[] packageArray = new CadsrPackage[cadsrPackages.size()];
        cadsrPackages.toArray(packageArray);
        
        // keep this LAST in the order of operation, since errors can cause
        // the installation to fail part way through
        try {
            extensionDataManager.storeCadsrProjectInformation(proj);
            extensionDataManager.storeCadsrPackages(packageArray);
            mostRecentProject = proj;
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error storing domain model information: " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }
    
    
    private ClassMapping[] createClassMappings(String packageName, 
        NamespaceType packageNamespace, List<String> classNames) {
        CadsrPackage pack = new CadsrPackage();
        pack.setName(packageName);
        pack.setMappedNamespace(packageNamespace.getNamespace());
        Map<String, String> classToElement = NamespaceUtils.mapClassNamesToElementNames(
            classNames, packageNamespace);
        // create ClassMappings for the package's classes
        List<ClassMapping> mappingList = new ArrayList(classNames.size());
        for (String className : classNames) {                
            ClassMapping mapping = new ClassMapping();
            mapping.setClassName(className);
            mapping.setElementName(classToElement.get(className));
            mapping.setSelected(true);
            mapping.setTargetable(true);
            mappingList.add(mapping);
        }
        ClassMapping[] mappings = new ClassMapping[classNames.size()];
        mappingList.toArray(mappings);
        return mappings;
    }
    
    
    private NamespaceType getNamespaceForPackage(String projectShortName, 
        String projectVersion, String packageName) {
        String mappedNamespace = null;
        try {
            mappedNamespace = extensionDataManager.getMappedNamespaceForPackage(packageName);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error loading mapped namespace", ex.getMessage(), ex);
        }
        if (mappedNamespace == null) {
            // fall back to the default mapping
            mappedNamespace = NamespaceUtils.createNamespaceString(
                projectShortName, projectVersion, packageName);            
        }
        // try to get the namespace type from the service model
        NamespaceType packageNamespace = CommonTools.getNamespaceType(
            serviceInfo.getNamespaces(), mappedNamespace);
        if (packageNamespace == null) {
            // namespace not found, get the user to import it
            String[] message = {
                "The imported domain model has a package which maps to the namespace",
                mappedNamespace + ".", "This namespace is not loaded into the service.",
                "Please locate a suitable namespace."};
            JOptionPane.showMessageDialog(PortalResourceManager.getInstance().getGridPortal(), message);
            NamespaceType[] resolved = SchemaResolutionDialog.resolveSchemas(serviceInfo);
            if (resolved == null || resolved.length == 0) {
                // user didn't map it... show a warning
                String[] error = {"The package " + packageName + " was not mapped to a namespace.",
                    "This can cause errors when the service builds."};
                ErrorDialog.showErrorDialog("No namespace mapping provided", error);
                return null;
            } else {
                // add the resolved namespaces to the service
                for (NamespaceType ns : resolved) {                        
                    CommonTools.addNamespace(serviceInfo.getServiceDescriptor(), ns);
                }
                packageNamespace = resolved[0];
            }
        }
        return packageNamespace;
    }
    
    
    private boolean addUmlPackageToModel(Project project, UMLPackageMetadata pack) {
        // verify the project is the same as the current one
        boolean projectOk = verifyProjectSelection(project);
        if (!projectOk) {
            return false;
        }
        
        // store the project selection
        try {
            extensionDataManager.storeCadsrProjectInformation(project);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error storing project selection", ex.getMessage(), ex);
            return false;
        }
        
        // can now add the package's contents to the UI
        // do we have a namespace for the package?
        NamespaceType packageNamespace = getNamespaceForPackage(
            project.getShortName(), project.getVersion(), pack.getName());
        if (packageNamespace == null) {
            return false;
        }
        // store the new package stub in the extension data
        CadsrPackage cadsrPack = new CadsrPackage();
        cadsrPack.setName(pack.getName());
        cadsrPack.setMappedNamespace(packageNamespace.getNamespace());
        try {
            extensionDataManager.storeCadsrPackage(cadsrPack);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error storing new cadsr package", ex.getMessage(), ex);
            return false;
        }
        
        // add the package to the UML model tree
        getUmlTree().addUmlPackage(pack.getName());
        
        // get classes out of the package using the cadsr
        try {
            CaDSRServiceClient cadsrClient = new CaDSRServiceClient(
                getCadsrBrowserPanel().getCadsr().getText());
            // TODO: potentially slow, use some kind of threading
            UMLClassMetadata[] classes = cadsrClient.findClassesInPackage(project, pack.getName());
            List<String> classNames = new ArrayList(classes.length);
            for (UMLClassMetadata currentClass : classes) {
                classNames.add(currentClass.getName());
            }
            ClassMapping[] classMappings = createClassMappings(
                pack.getName(), packageNamespace, classNames);
            cadsrPack.setCadsrClass(classMappings);
            extensionDataManager.storeCadsrPackage(cadsrPack);
            // add the classes to the UML tree
            for (String className: classNames) {
                getUmlTree().addUmlClass(pack.getName(), className);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error getting classes for package: " + ex.getMessage(), ex);
        }
        
        return true;
    }


    private boolean verifyProjectSelection(Project project) {
        if (mostRecentProject != null && !projectEquals(mostRecentProject, project)) {
            // package comes from a different project, which is not allowed
            // prompt the developer about it
            String[] choices = {"Remove all other packages and insert", "Cancel"};
            String[] message = {"Domain models may only be derived from one project.",
                "To add the package you've selected, all other packages",
                "currently in the domain model will have to be removed.", 
                "Should this operation procede?"};
            String choice = PromptButtonDialog.prompt(
                PortalResourceManager.getInstance().getGridPortal(),
                "Package incompatability...", message, choices, choices[1]);
            if (choice == choices[0]) {
                // user has elected to go with this project / package combination
                // there are likley namespaces added to the service from previous
                // package selections.  These should be removed.
                for (String packageName : getUmlTree().getPackagesInTree()) {
                    try {
                        String mappedNamespace = extensionDataManager
                            .getMappedNamespaceForPackage(packageName);
                        NamespaceType nsType = CommonTools.getNamespaceType(
                            serviceInfo.getNamespaces(), mappedNamespace);
                        if (!CommonTools.isNamespaceTypeInUse(
                            nsType, serviceInfo.getServiceDescriptor())) {
                            NamespaceType[] allNamespaces = 
                                serviceInfo.getNamespaces().getNamespace();
                            NamespaceType[] cleanedNamespaces = (NamespaceType[]) Utils.removeFromArray(
                                allNamespaces, nsType);
                            serviceInfo.getNamespaces().setNamespace(cleanedNamespaces);
                        }
                        // clear the package out of the UML tree
                        getUmlTree().removeUmlPackage(packageName);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog(
                            "Error removing namespace for package: " + ex.getMessage(), ex);
                        return false;
                    }
                }
                // inform whom it may concern that classes were cleared
                fireClassesCleared();
                
                // change the most recent project
                mostRecentProject = project;
            } else {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * p1 must be non-null!!
     * 
     * @param p1
     * @param p2
     * @return True if the projects have the same values for long name and
     *         version
     */
    private boolean projectEquals(Project p1, Project p2) {
        if (p2 != null) {
            return p1.getLongName().equals(p2.getLongName()) 
                && p1.getVersion().equals(p2.getVersion());
        }
        return false;
    }
    
    
    //
    // --- 
    // Event notification
    // --- 
    //
    
    
    public void addClassSelectionListener(DomainModelClassSelectionListener listener) {
        classSelectionListeners.add(listener);
    }
    
    
    public boolean removeClassSelectionListener(DomainModelClassSelectionListener listener) {
        return classSelectionListeners.remove(listener);
    }
    
    
    protected void fireClassAdded(String packageName, ClassMapping classMapping, NamespaceType packageNsType) {
        for (DomainModelClassSelectionListener listener : classSelectionListeners) {
            listener.classAdded(packageName, classMapping, packageNsType);
        }
    }
    
    
    protected void fireClassRemoved(String packageName, String className) {
        for (DomainModelClassSelectionListener listener : classSelectionListeners) {
            listener.classRemoved(packageName, className);
        }
    }
    
    
    protected void fireClassesCleared() {
        for (DomainModelClassSelectionListener listener : classSelectionListeners) {
            listener.classesCleared();
        }
    }
}
