package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.beans.extension.ResourcePropertyEditorExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorPanel;
import gov.nih.nci.cagrid.introduce.portal.extension.tools.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.editor.XMLEditorViewer;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeTreeNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.xml.namespace.QName;

import org.projectmobius.common.XMLUtilities;


public class ModifyResourcePropertiesPanel extends JPanel {

    private ResourcePropertiesListType properties;

    private NamespacesType namespaces;

    private JPanel resourcePropertiesPanel = null;

    private JScrollPane namespacesScrollPane = null;

    private JScrollPane resourcePropertiesScrollPane = null;

    private NamespacesJTree namespacesJTree = null;

    private ResourcePropertyTable resourcePropertiesTable = null;

    private JPanel buttonsPanel = null;

    private JButton addResourcePropertyButton = null;

    private JButton removeResourcePropertyButton = null;

    private JSplitPane mainSplitPane = null;

    private boolean showW3Cnamespaces;

    private JButton editInstanceButton = null;

    private File etcDir;

    private File schemaDir;

    private ServiceType service;

    private boolean isMainMetadataPanel = true;


    public ModifyResourcePropertiesPanel(ServiceType service, NamespacesType namespaces, File etcDir, File schemaDir,
        boolean showW3Cnamespaces, boolean isMainMetadataPanel) {
        this.service = service;
        this.etcDir = etcDir;
        this.schemaDir = schemaDir;
        this.properties = service.getResourcePropertiesList();
        this.namespaces = namespaces;
        this.showW3Cnamespaces = showW3Cnamespaces;
        this.isMainMetadataPanel = isMainMetadataPanel;
        initialize();
    }


    private void initialize() {
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 1;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0D;
        gridBagConstraints1.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(314, 211));
        this.add(getMainSplitPane(), gridBagConstraints1);
        this.add(getButtonsPanel(), gridBagConstraints2);
    }


    public void reInitialize(ServiceType initService, NamespacesType ns) {
        this.service = initService;
        this.properties = initService.getResourcePropertiesList();
        this.namespaces = ns;
        this.namespacesJTree.setNamespaces(ns);
        this.resourcePropertiesTable.setResourceProperties(this.properties);
    }


    public ResourcePropertyType[] getConfiguredResourceProperties() throws Exception {
        int propertyCount = getResourcePropertiesTable().getRowCount();
        ResourcePropertyType[] configuredProperties = new ResourcePropertyType[propertyCount];
        for (int i = 0; i < propertyCount; i++) {
            configuredProperties[i] = getResourcePropertiesTable().getRowData(i);
        }
        return configuredProperties;
    }


    /**
     * This method initializes resourcePropertiesPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getResourcePropertiesPanel() {
        if (this.resourcePropertiesPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0;
            gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
            this.resourcePropertiesPanel = new JPanel();
            this.resourcePropertiesPanel.setLayout(new GridBagLayout());
            this.resourcePropertiesPanel.add(getResourcePropertiesScrollPane(), gridBagConstraints4);
        }
        return this.resourcePropertiesPanel;
    }


    /**
     * This method initializes namespacesScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getNamespacesScrollPane() {
        if (this.namespacesScrollPane == null) {
            this.namespacesScrollPane = new JScrollPane();
            this.namespacesScrollPane.setPreferredSize(new java.awt.Dimension(200, 240));
            this.namespacesScrollPane.setViewportView(getNamespacesJTree());
        }
        return this.namespacesScrollPane;
    }


    /**
     * This method initializes resourcePropertiesScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getResourcePropertiesScrollPane() {
        if (this.resourcePropertiesScrollPane == null) {
            this.resourcePropertiesScrollPane = new JScrollPane();
            this.resourcePropertiesScrollPane.setViewportView(getResourcePropertiesTable());
        }
        return this.resourcePropertiesScrollPane;
    }


    /**
     * This method initializes namespacesJTree
     * 
     * @return javax.swing.JTree
     */
    private NamespacesJTree getNamespacesJTree() {
        if (this.namespacesJTree == null) {
            this.namespacesJTree = new NamespacesJTree(this.namespaces, this.showW3Cnamespaces);
            this.namespacesJTree.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getClickCount() == 2) {
                        addResourceProperty();
                    }
                }
            });
        }
        return this.namespacesJTree;
    }


    /**
     * This method initializes resourcePropertiesTable
     * 
     * @return javax.swing.JTable
     */
    private ResourcePropertyTable getResourcePropertiesTable() {
        if (this.resourcePropertiesTable == null) {
            if (this.isMainMetadataPanel) {
                this.resourcePropertiesTable = new ResourcePropertyTable(this.properties, true);
            } else {
                this.resourcePropertiesTable = new ResourcePropertyTable(this.properties, false);
            }
            SelectionListener listener = new SelectionListener(this.resourcePropertiesTable);
            this.resourcePropertiesTable.getSelectionModel().addListSelectionListener(listener);
            this.resourcePropertiesTable.getColumnModel().getSelectionModel().addListSelectionListener(listener);
            this.resourcePropertiesTable.getModel().addTableModelListener(new TableModelListener() {

                public void tableChanged(TableModelEvent e) {

                    try {

                        if (e.getType() != TableModelEvent.DELETE
                            && ModifyResourcePropertiesPanel.this.resourcePropertiesTable.getSelectedRow() >= 0) {
                            if (ModifyResourcePropertiesPanel.this.resourcePropertiesTable.getRowData(
                                ModifyResourcePropertiesPanel.this.resourcePropertiesTable.getSelectedRow())
                                .isPopulateFromFile()) {
                                getEditInstanceButton().setEnabled(true);
                            } else {
                                getEditInstanceButton().setEnabled(false);
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            });
        }
        return this.resourcePropertiesTable;
    }


    public class SelectionListener implements ListSelectionListener {
        ResourcePropertyTable table;


        SelectionListener(ResourcePropertyTable table) {
            this.table = table;
        }


        public void valueChanged(ListSelectionEvent e) {
            try {
                if (this.table.getSelectedRow() >= 0) {
                    if (this.table.getRowData(this.table.getSelectedRow()).isPopulateFromFile()) {
                        getEditInstanceButton().setEnabled(true);
                    } else {
                        getEditInstanceButton().setEnabled(false);
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonsPanel() {
        if (this.buttonsPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 2;
            gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints11.gridy = 0;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            this.buttonsPanel = new JPanel();
            this.buttonsPanel.setLayout(new GridBagLayout());
            this.buttonsPanel.add(getAddButton(), gridBagConstraints);
            this.buttonsPanel.add(getRemoveButton(), gridBagConstraints8);
            if (this.isMainMetadataPanel) {
                this.buttonsPanel.add(getEditInstanceButton(), gridBagConstraints11);
            }
        }
        return this.buttonsPanel;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddButton() {
        if (this.addResourcePropertyButton == null) {
            this.addResourcePropertyButton = new JButton();
            this.addResourcePropertyButton.setToolTipText("add new operation");
            this.addResourcePropertyButton.setText("Add");
            this.addResourcePropertyButton.setIcon(PortalLookAndFeel.getAddIcon());
            this.addResourcePropertyButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    addResourceProperty();
                }
            });
        }
        return this.addResourcePropertyButton;
    }


    private void addResourceProperty() {
        if (getNamespacesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
            NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getNamespacesJTree().getCurrentNode()
                .getParent()).getUserObject());
            SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getNamespacesJTree()
                .getCurrentNode()).getUserObject());
            ResourcePropertyType metadata = new ResourcePropertyType();
            metadata.setQName(new QName(nt.getNamespace(), st.getType()));
            metadata.setPopulateFromFile(false);
            metadata.setRegister(false);

            if (this.properties != null && this.properties.getResourceProperty() != null) {
                for (int i = 0; i < this.properties.getResourceProperty().length; i++) {
                    ResourcePropertyType rp = this.properties.getResourceProperty(i);
                    if (rp.getQName().equals(metadata.getQName())) {
                        return;
                    }
                }
            }

            getResourcePropertiesTable().addRow(metadata);

            // add new metadata to array in bean
            // this seems to be a wierd way be adding things....
            ResourcePropertyType[] metadatas;
            int newLength = 0;
            if (this.properties != null && this.properties.getResourceProperty() != null) {
                newLength = this.properties.getResourceProperty().length + 1;
                metadatas = new ResourcePropertyType[newLength];
                System.arraycopy(this.properties.getResourceProperty(), 0, metadatas, 0, this.properties
                    .getResourceProperty().length);
            } else {
                newLength = 1;
                metadatas = new ResourcePropertyType[newLength];
            }
            metadatas[newLength - 1] = metadata;
            this.properties.setResourceProperty(metadatas);

            // set the file name for the resource property instance......
            int i = 0;
            for (i = 0; i < this.properties.getResourceProperty().length; i++) {
                if (metadata.equals(this.properties.getResourceProperty(i))) {
                    break;
                }
            }
            metadata.setFileLocation(this.service.getName() + "_"
                + CommonTools.getResourcePropertyVariableName(this.service.getResourcePropertiesList(), i) + ".xml");
        }
    }


    /**
     * This method initializes jButton2
     * 
     * @return javax.swing.JButton
     */
    private JButton getRemoveButton() {
        if (this.removeResourcePropertyButton == null) {
            this.removeResourcePropertyButton = new JButton();
            this.removeResourcePropertyButton.setToolTipText("remove selected operation");
            this.removeResourcePropertyButton.setText("Remove");
            this.removeResourcePropertyButton.setIcon(PortalLookAndFeel.getRemoveIcon());
            this.removeResourcePropertyButton.addActionListener(new java.awt.event.ActionListener() {
                // remove from table
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ResourcePropertyType resource = null;
                    try {
                        resource = getResourcePropertiesTable().getRowData(
                            getResourcePropertiesTable().getSelectedRow());
                    } catch (Exception e1) {
                        PortalUtils.showErrorMessage("Please select a metdata type to remove.");
                        return;
                    }
                    try {
                        getResourcePropertiesTable().removeSelectedRow();
                    } catch (Exception e1) {
                        PortalUtils.showErrorMessage("Please select a metdata type to remove.");
                        return;
                    }
                    // remove from resource properties list
                    ResourcePropertyType[] metadatas;
                    int newLength = ModifyResourcePropertiesPanel.this.properties.getResourceProperty().length - 1;
                    metadatas = new ResourcePropertyType[newLength];
                    int resourcesCount = 0;
                    for (int i = 0; i < ModifyResourcePropertiesPanel.this.properties.getResourceProperty().length; i++) {
                        ResourcePropertyType oldResource = ModifyResourcePropertiesPanel.this.properties
                            .getResourceProperty(i);
                        if (!resource.equals(oldResource)) {
                            metadatas[resourcesCount++] = oldResource;
                        }
                    }
                    ModifyResourcePropertiesPanel.this.properties.setResourceProperty(metadatas);
                }
            });
        }
        return this.removeResourcePropertyButton;
    }


    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getMainSplitPane() {
        if (this.mainSplitPane == null) {
            this.mainSplitPane = new JSplitPane();
            this.mainSplitPane.setOneTouchExpandable(true);
            this.mainSplitPane.setLeftComponent(getNamespacesScrollPane());
            this.mainSplitPane.setRightComponent(getResourcePropertiesPanel());
        }
        return this.mainSplitPane;
    }


    /**
     * This method initializes editInstanceButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getEditInstanceButton() {
        if (this.editInstanceButton == null) {
            this.editInstanceButton = new JButton();
            this.editInstanceButton.setText("View/Edit Instance");
            this.editInstanceButton.setIcon(IntroduceLookAndFeel.getModifyIcon());
            this.editInstanceButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (getResourcePropertiesTable().getSelectedRow() >= 0) {
                        try {
                            ResourcePropertyType type = getResourcePropertiesTable().getRowData(
                                getResourcePropertiesTable().getSelectedRow());
                            if (type.isPopulateFromFile()) {
                                String rpData = null;
                                File resourcePropertyFile = null;

                                if (type.getFileLocation() != null) {
                                    resourcePropertyFile = new File(ModifyResourcePropertiesPanel.this.etcDir
                                        .getAbsolutePath()
                                        + File.separator + type.getFileLocation());
                                }
                                if (resourcePropertyFile != null && resourcePropertyFile.exists()) {
                                    // file has already been created
                                    System.out.println("Loading resource properties file : " + resourcePropertyFile);
                                    rpData = XMLUtilities.fileNameToString(resourcePropertyFile.getAbsolutePath());
                                } else {
                                    // file has not been created yet, we will
                                    // create it, set the path to file, and then
                                    // call the editor
                                    int i = 0;
                                    for (i = 0; i < ModifyResourcePropertiesPanel.this.properties.getResourceProperty().length; i++) {
                                        if (type.equals(ModifyResourcePropertiesPanel.this.properties
                                            .getResourceProperty(i))) {
                                            break;
                                        }
                                    }
                                    System.out.println("Creating a new resource properties file");
                                    boolean created = resourcePropertyFile.createNewFile();
                                    if (!created) {
                                        throw new Exception("Could not create file"
                                            + resourcePropertyFile.getAbsolutePath());
                                    }
                                    rpData = XMLUtilities.fileNameToString(resourcePropertyFile.getAbsolutePath());
                                }

                                QName qname = type.getQName();
                                NamespaceType nsType = CommonTools.getNamespaceType(
                                    ModifyResourcePropertiesPanel.this.namespaces, qname.getNamespaceURI());

                                ResourcePropertyEditorExtensionDescriptionType mde = ExtensionTools
                                    .getResourcePropertyEditorExtensionDescriptor(qname);
                                ResourcePropertyEditorPanel mdec = null;

                                if (mde != null) {
                                    mdec = ExtensionTools.getMetadataEditorComponent(mde.getName(), rpData, new File(
                                        ModifyResourcePropertiesPanel.this.schemaDir.getAbsolutePath() + File.separator
                                            + nsType.getLocation()), ModifyResourcePropertiesPanel.this.schemaDir);
                                } else {
                                    // use the default editor....
                                    mdec = new XMLEditorViewer(rpData, new File(
                                        ModifyResourcePropertiesPanel.this.schemaDir.getAbsolutePath() + File.separator
                                            + nsType.getLocation()), ModifyResourcePropertiesPanel.this.schemaDir);
                                }
                                ResourcePropertyEditorDialog diag = new ResourcePropertyEditorDialog(mdec,
                                    resourcePropertyFile);
                                diag.setVisible(true);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                }

            });

        }
        return this.editInstanceButton;
    }
}
