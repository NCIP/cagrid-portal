package gov.nih.nci.cagrid.sdkquery4.style.wizard;

import gov.nih.nci.cagrid.common.portal.DocumentChangeAdapter;
import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.style.sdkstyle.wizard.PackageSchemasTable;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.CompositeErrorDialog;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

/** 
 *  SchemaMappingPanel
 *  Panel to configure mapping of packages to schemas
 * 
 * @author David Ervin
 * 
 * @created Jan 9, 2008 11:09:22 AM
 * @version $Id: SchemaMappingPanel.java,v 1.1 2008-01-09 16:59:03 dervin Exp $ 
 */
public class SchemaMappingPanel extends AbstractWizardPanel {
    // validation keys
    public static final String KEY_GME_URL = "GME URL";
    
    private PackageSchemasTable packageNamespaceTable = null;
    private JPanel mainPanel = null;
    private JScrollPane packageNamespaceScrollPane = null;
    private JLabel gmeUrlLabel = null;
    private JTextField gmeUrlTextField = null;
    private JLabel configDirLabel = null;
    private JTextField configDirTextField = null;
    private JButton gmeMapButton = null;
    private JButton configMapButton = null;
    private JPanel automapPanel = null;
    private JPanel mappingPanel = null;
    
    private IconFeedbackPanel validationPanel = null;
    private ValidationResultModel validationModel = null;
    private DocumentChangeAdapter documentChangeListener = null;

    public SchemaMappingPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
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
        return "Schemas";
    }


    public String getPanelTitle() {
        return "Package to Schema Mapping";
    }


    public void update() {
        // TODO Auto-generated method stub
    }
    
    
    private void initialize() {
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
        }
        return this.packageNamespaceTable;
    }
    
    
    /**
     * This method initializes mainPanel    
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
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
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setSize(new Dimension(385, 200));
            mainPanel.add(getMappingPanel(), gridBagConstraints5);
            mainPanel.add(getPackageNamespaceScrollPane(), gridBagConstraints6);
        }
        return mainPanel;
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
            String url = ResourceManager.getServiceURLProperty(DataServiceConstants.GME_SERVICE_URL);
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
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
    

    // -----------
    // validation
    // -----------
    
    
    private void configureValidation() {
        ValidationComponentUtils.setMessageKey(getGmeUrlTextField(), KEY_GME_URL);
        
        // TODO: iterate namespaces in table and add message keys for each row
        
        validateInput();
        updateComponentTreeSeverity();
    }
    
    
    private void validateInput() {
        ValidationResult result = new ValidationResult();
        
        String gmeUrl = getGmeUrlTextField().getText();
        if (ValidationUtils.isBlank(gmeUrl)) {
            // warning, not error, since it doesn't prevent the page from being valid
            result.add(new SimpleValidationMessage(
                KEY_GME_URL + " cannot be blank for GME schema resolution", Severity.WARNING, KEY_GME_URL));
        } else {
            try {
                new URL(gmeUrl);
            } catch (Exception ex) {
                result.add(new SimpleValidationMessage(
                    KEY_GME_URL + " does not appear to be a valid URL", Severity.WARNING, KEY_GME_URL));
            }
        }
        
        // TODO: iterate namespaces in table and validate the state of each
        
        validationModel.setResult(result);
        
        updateComponentTreeSeverity();
        // update next button enabled
        setNextEnabled(!validationModel.hasErrors());
        // store the configuration changes
        if (!validationModel.hasErrors()) {
            // TODO: storeConfigurationProperties();
        }
    }
    
    
    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, validationModel.getResult());
    }
}
