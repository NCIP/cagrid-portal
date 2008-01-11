package gov.nih.nci.cagrid.sdkquery4.style.wizard;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.style.sdkstyle.wizard.PackageSchemasTable;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.sdkquery4.encoding.SDK40DeserializerFactory;
import gov.nih.nci.cagrid.sdkquery4.encoding.SDK40SerializerFactory;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.CompositeErrorDialog;
import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceFactory;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;

import com.jgoodies.validation.view.ValidationComponentUtils;

/** 
 *  SchemaMappingPanel
 *  Panel to configure mapping of packages to schemas
 * 
 * @author David Ervin
 * 
 * @created Jan 9, 2008 11:09:22 AM
 * @version $Id: SchemaMappingPanel.java,v 1.2 2008-01-11 20:34:03 dervin Exp $ 
 */
public class SchemaMappingPanel extends AbstractWizardPanel {
    
    private PackageSchemasTable packageNamespaceTable = null;
    private JScrollPane packageNamespaceScrollPane = null;
    private JLabel gmeUrlLabel = null;
    private JTextField gmeUrlTextField = null;
    private JLabel configDirLabel = null;
    private JTextField configDirTextField = null;
    private JButton gmeMapButton = null;
    private JButton configMapButton = null;
    private JPanel automapPanel = null;
    private JPanel mappingPanel = null;

    public SchemaMappingPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        initialize();
    }


    public String getPanelShortName() {
        return "Schemas";
    }


    public String getPanelTitle() {
        return "Package to Schema Mapping";
    }


    public void update() {
        // populate the package to namespace table from the extension data
        try {
            Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
            CadsrInformation info = data.getCadsrInformation();
            Set<String> currentPackageNames = new HashSet<String>();
            for (int i = 0; i < getPackageNamespaceTable().getRowCount(); i++) {
                currentPackageNames.add((String) getPackageNamespaceTable().getValueAt(i, 0));
            }
            if (info != null && info.getPackages() != null) {
                CadsrPackage[] packs = info.getPackages();
                if (packs != null && packs.length != 0) {
                    // add any new packages to the table
                    for (int i = 0; i < packs.length; i++) {
                        if (!getPackageNamespaceTable().isPackageInTable(packs[i])) {
                            getPackageNamespaceTable().addNewCadsrPackage(getServiceInformation(), packs[i]);
                        }
                        currentPackageNames.remove(packs[i].getName());
                    }
                }
            }
            Iterator invalidPackageNameIter = currentPackageNames.iterator();
            while (invalidPackageNameIter.hasNext()) {
                String invalidName = (String) invalidPackageNameIter.next();
                getPackageNamespaceTable().removeCadsrPackage(invalidName);
            }
            setWizardComplete(allSchemasResolved());
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error populating the packages table", ex);
        }
    }
    
    
    private void initialize() {
        this.setLayout(new GridLayout());
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = GridBagConstraints.BOTH;
        gridBagConstraints6.gridy = 1;
        gridBagConstraints6.weightx = 1.0;
        gridBagConstraints6.weighty = 1.0;
        gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints6.gridx = 0;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints5.fill = GridBagConstraints.BOTH;
        gridBagConstraints5.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(385, 200));
        this.add(getMappingPanel(), gridBagConstraints5);
        this.add(getPackageNamespaceScrollPane(), gridBagConstraints6);
    }
    
    
    private PackageSchemasTable getPackageNamespaceTable() {
        if (this.packageNamespaceTable == null) {
            this.packageNamespaceTable = new PackageSchemasTable(getBitBucket());
            this.packageNamespaceTable.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        setWizardComplete(allSchemasResolved());
                        try {
                            storePackageMappings();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            CompositeErrorDialog.showErrorDialog("Error storing namespace mappings", ex);
                        }
                    }
                }
            });
            DefaultTableCellRenderer baseRenderer = (DefaultTableCellRenderer) this.packageNamespaceTable.getDefaultRenderer(Object.class);
            this.packageNamespaceTable.setDefaultRenderer(Object.class, new ValidatingTableCellRenderer(baseRenderer));
        }
        return this.packageNamespaceTable;
    }


    /**
     * This method initializes packageNamespaceScrollPane   
     *  
     * @return javax.swing.JScrollPane  
     */
    private JScrollPane getPackageNamespaceScrollPane() {
        if (packageNamespaceScrollPane == null) {
            packageNamespaceScrollPane = new JScrollPane();
            packageNamespaceScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            packageNamespaceScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            packageNamespaceScrollPane.setViewportView(getPackageNamespaceTable());
            packageNamespaceScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "Namespace Mappings", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
        }
        return packageNamespaceScrollPane;
    }


    /**
     * This method initializes gmeUrlLabel  
     *  
     * @return javax.swing.JLabel   
     */
    private JLabel getGmeUrlLabel() {
        if (gmeUrlLabel == null) {
            gmeUrlLabel = new JLabel();
            gmeUrlLabel.setText("GME Url:");
        }
        return gmeUrlLabel;
    }


    /**
     * This method initializes gmeUrlTextField  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getGmeUrlTextField() {
        if (gmeUrlTextField == null) {
            gmeUrlTextField = new JTextField();
            String url = ResourceManager.getServiceURLProperty(
                DataServiceConstants.GME_SERVICE_URL);
            gmeUrlTextField.setText(url);
        }
        return gmeUrlTextField;
    }


    /**
     * This method initializes configDirLabel   
     *  
     * @return javax.swing.JLabel   
     */
    private JLabel getConfigDirLabel() {
        if (configDirLabel == null) {
            configDirLabel = new JLabel();
            configDirLabel.setText("Client Config Dir:");
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
        }
        return configDirTextField;
    }


    /**
     * This method initializes gmeMapButton 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getGmeMapButton() {
        if (gmeMapButton == null) {
            gmeMapButton = new JButton();
            gmeMapButton.setText("Map From GME");
            gmeMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    mapFromGme();
                }
            });
        }
        return gmeMapButton;
    }


    /**
     * This method initializes configMapButton  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getConfigMapButton() {
        if (configMapButton == null) {
            configMapButton = new JButton();
            configMapButton.setText("Map From Config");
            configMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    mapFromConfig();
                }
            });
        }
        return configMapButton;
    }


    /**
     * This method initializes automapPanel 
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getAutomapPanel() {
        if (automapPanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(4);
            gridLayout.setColumns(2);
            automapPanel = new JPanel();
            automapPanel.setLayout(gridLayout);
            automapPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Automatic Mapping", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
            automapPanel.add(getGmeMapButton(), null);
            automapPanel.add(getConfigMapButton(), null);
        }
        return automapPanel;
    }


    /**
     * This method initializes mappingPanel 
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getMappingPanel() {
        if (mappingPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridwidth = 2;
            gridBagConstraints4.gridy = 2;
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
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            mappingPanel = new JPanel();
            mappingPanel.setLayout(new GridBagLayout());
            mappingPanel.add(getGmeUrlLabel(), gridBagConstraints);
            mappingPanel.add(getGmeUrlTextField(), gridBagConstraints1);
            mappingPanel.add(getConfigDirLabel(), gridBagConstraints2);
            mappingPanel.add(getConfigDirTextField(), gridBagConstraints3);
            mappingPanel.add(getAutomapPanel(), gridBagConstraints4);
        }
        return mappingPanel;
    }
    
    
    // ---------
    // helpers
    // ---------
    
    
    private boolean allSchemasResolved() {
        for (int i = 0; i < getPackageNamespaceTable().getRowCount(); i++) {
            String status = (String) getPackageNamespaceTable().getValueAt(i, 2);
            if (!status.equals(PackageSchemasTable.STATUS_SCHEMA_FOUND)) {
                return false;
            }
        }
        return true;
    }


    private void storePackageMappings() throws Exception {
        Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
        CadsrInformation info = data.getCadsrInformation();
        if (info == null) {
            info = new CadsrInformation();
            info.setNoDomainModel(true);
            data.setCadsrInformation(info);
        }
        for (int i = 0; info.getPackages() != null && i < info.getPackages().length; i++) {
            CadsrPackage currentPackage = info.getPackages(i);
            // find the package's row in the table
            for (int row = 0; row < getPackageNamespaceTable().getRowCount(); row++) {
                if (currentPackage.getName().equals(getPackageNamespaceTable().getValueAt(row, 0))) {
                    // set the mapped namespace
                    currentPackage.setMappedNamespace((String) getPackageNamespaceTable().getValueAt(row, 1));
                    break;
                }
            }
        }
        ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
    }
    
    
    private void mapFromConfig() {
        File schemaDir = new File(getServiceInformation().getBaseDirectory(),
            "schema" + File.separator + getServiceInformation().getIntroduceServiceProperties()
                .getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
        
        // the config dir jar will have the xsds in it
        try {
            String applicationName = CommonTools.getServicePropertyValue(
                getServiceInformation().getServiceDescriptor(), 
                DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX 
                    + SDK4QueryProcessor.PROPERTY_APPLICATION_NAME);
            String configJarFilename = getServiceInformation().getBaseDirectory().getAbsolutePath()
                + File.separator + "lib" + File.separator + applicationName + "-config.jar";
            JarFile configJar = new JarFile(configJarFilename);
            Enumeration<JarEntry> entries = configJar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".xsd")) {
                    // found a schema, what package does it go with?
                    String schemaPackageName = new File(entry.getName()).getName();
                    schemaPackageName = schemaPackageName.substring(0, schemaPackageName.length() - 4);
                    for (int i = 0; i < getPackageNamespaceTable().getRowCount(); i++) {
                        String packageName = (String) getPackageNamespaceTable().getValueAt(i, 0);
                        if (packageName.equals(schemaPackageName)) {
                            // create a schema file and namespace type
                            StringBuffer schemaText = JarUtilities.getFileContents(configJar, entry.getName());
                            File schemaFile = new File(schemaDir, new File(entry.getName()).getName());
                            Utils.stringBufferToFile(schemaText, schemaFile.getAbsolutePath());
                            NamespaceType nsType = CommonTools.createNamespaceType(schemaFile.getAbsolutePath(), schemaDir);
                            
                            // add the namespace to the service
                            addNamespaceToService(nsType);
                            
                            // set the namespace in the table
                            getPackageNamespaceTable().setValueAt(nsType.getNamespace(), i, 1);
                            // set the status to found in the table
                            getPackageNamespaceTable().setValueAt(PackageSchemasTable.STATUS_SCHEMA_FOUND, i, 2);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog(
                "Error loading application configuration files", ex.getMessage(), ex);
        }
    }
    
    
    private void mapFromGme() {
        // get the service's schema dir
        File schemaDir = new File(getServiceInformation().getBaseDirectory(),
            "schema" + File.separator + getServiceInformation().getIntroduceServiceProperties()
                .getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
        try {
            XMLDataModelService gmeHandle = getGmeHandle();
            // get the domains administered by the GME
            List domains = gmeHandle.getNamespaceDomainList();
            // get the selected packages
            Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
            CadsrInformation info = data.getCadsrInformation();
            if (info != null && info.getPackages() != null) {
                CadsrPackage[] packs = info.getPackages();
                for (int i = 0; i < packs.length; i++) {
                    Namespace ns = new Namespace(packs[i].getMappedNamespace());
                    // see if the GME has the domain in question
                    if (domains.contains(ns.getDomain())) {
                        // domain found, get the namespaces within it
                        List<Namespace> namespaces = gmeHandle.getSchemaListForNamespaceDomain(ns.getDomain());
                        if (namespaces.contains(ns)) {
                            // found the namespace as well, download the schema locally
                            // have the GME cache the schema and its imports locally
                            List<Namespace> cachedNamespaces = gmeHandle.cacheSchema(ns, schemaDir);
                            
                            // create namespace types and add them to the service
                            for (Namespace storedNs : cachedNamespaces) {
                                ImportInfo storedSchemaInfo = new ImportInfo(storedNs);
                                File location = new File(schemaDir.getAbsolutePath() + File.separator + storedSchemaInfo.getFileName());
                                NamespaceType nsType = CommonTools.createNamespaceType(location.getAbsolutePath(), schemaDir);
                                addNamespaceToService(nsType);
                            }
                            // change the package namespace table to reflect the
                            // found schema
                            int row = 0;
                            while (!getPackageNamespaceTable().getValueAt(row, 0).equals(packs[i].getName())) {
                                row++;
                            }
                            getPackageNamespaceTable().setValueAt(PackageSchemasTable.STATUS_SCHEMA_FOUND, row, 2);
                        }
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(SchemaMappingPanel.this, "No packages to find schemas for");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error retrieving schemas from the GME", ex);
        }
    }
    
    
    private void addNamespaceToService(NamespaceType nsType) {
        // if the namespace already exists in the service, ignore it
        if (CommonTools.getNamespaceType(getServiceInformation().getNamespaces(), nsType.getNamespace()) == null) {
            // set the package name
            String packName = CommonTools.getPackageName(nsType.getNamespace());
            nsType.setPackageName(packName);
            // fix the serialization / deserialization on the namespace types
            setSdkSerialization(nsType);
            CommonTools.addNamespace(getServiceInformation().getServiceDescriptor(), nsType);
            // add the namespace to the introduce namespace excludes list so
            // that beans will not be built for these data types
            String excludes = getServiceInformation().getIntroduceServiceProperties().getProperty(
                IntroduceConstants.INTRODUCE_NS_EXCLUDES);
            excludes += " -x " + nsType.getNamespace();
            getServiceInformation().getIntroduceServiceProperties().setProperty(
                IntroduceConstants.INTRODUCE_NS_EXCLUDES, excludes);
        }
    }
    
    
    private void setSdkSerialization(NamespaceType namespace) {
        for (SchemaElementType type : namespace.getSchemaElement()) {
            type.setClassName(type.getType());
            type.setSerializer(SDK40SerializerFactory.class.getName());
            type.setDeserializer(SDK40DeserializerFactory.class.getName());
        }
    }
    
    
    private XMLDataModelService getGmeHandle() throws MobiusException {
        XMLDataModelService service = null;
        GridServiceFactory oldFactory = GridServiceResolver.getInstance().getDefaultFactory();
        GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
        try {
            service = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
                getGmeUrlTextField().getText());
        } catch (MobiusException ex) {
            throw ex;
        } finally {
            GridServiceResolver.getInstance().setDefaultFactory(oldFactory);
        }
        return service;
    }
    
    
    private static class ValidatingTableCellRenderer implements TableCellRenderer {
        private DefaultTableCellRenderer baseRenderer;
        private Color defaultBackground;
        
        public ValidatingTableCellRenderer(DefaultTableCellRenderer baseRenderer) {
            this.baseRenderer = baseRenderer;
            this.defaultBackground = baseRenderer.getBackground();
        }
        
        
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            // restore the default background
            baseRenderer.setBackground(defaultBackground);
            
            // render the cell with default settings
            Component cell = baseRenderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (column == 2) { // status column
                if (value.equals(PackageSchemasTable.STATUS_NEVER_TRIED)) {
                    baseRenderer.setBackground(ValidationComponentUtils.getErrorBackground());
                } else if (value.equals(PackageSchemasTable.STATUS_MAPPING_ERROR)) {
                    baseRenderer.setBackground(ValidationComponentUtils.getWarningBackground());
                }
            }
            return cell;
        }
    }
}
