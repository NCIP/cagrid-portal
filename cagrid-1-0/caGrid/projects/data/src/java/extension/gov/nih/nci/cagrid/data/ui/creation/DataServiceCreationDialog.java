package gov.nih.nci.cagrid.data.ui.creation;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.extension.ServiceFeatures;
import gov.nih.nci.cagrid.data.ui.cacore.AppserviceConfigPanel;
import gov.nih.nci.cagrid.data.ui.cacore.CoreDsIntroPanel;
import gov.nih.nci.cagrid.data.ui.cacore.DomainModelPanel;
import gov.nih.nci.cagrid.data.ui.cacore.SDKClientSelectionPanel;
import gov.nih.nci.cagrid.data.ui.cacore.SchemaTypesPanel;
import gov.nih.nci.cagrid.data.ui.cacore.ServiceWizard;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.extension.CreationExtensionUIDialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import org.projectmobius.portal.PortalResourceManager;


/**
 * DataServiceCreationDialog Dialog for post-creation changes to a data service
 * and configuration of data service features
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Aug 1, 2006
 * @version $Id$
 */
public class DataServiceCreationDialog extends CreationExtensionUIDialog {
    public static final String WS_ENUM_EXTENSION_NAME = "cagrid_wsEnum";
    public static final String BDT_EXTENSIONS_NAME = "bdt";

    private JPanel mainPanel = null;
    private JCheckBox wsEnumCheckBox = null;
    private JButton okButton = null;
    private JPanel featuresPanel = null;
    private JCheckBox gridIdentCheckBox = null;
    private JRadioButton customDataRadioButton = null;
    private JRadioButton sdkDataRadioButton = null;
    private JPanel dataSourcePanel = null;
    private ButtonGroup dataButtonGroup = null;

    private JCheckBox bdtCheckBox = null;


    public DataServiceCreationDialog(Frame f, ServiceExtensionDescriptionType desc, ServiceInformation info) {
        super(f, desc, info);
        // keep users from closing the window unexpectedly
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setFeatureStatus();
            }
        });
        initialize();
    }


    private void initialize() {
        this.setTitle("Data Service Configuration");
        this.setContentPane(getMainPanel());
        getDataButtonGroup();
        pack();
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (this.mainPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.gridy = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.fill = GridBagConstraints.BOTH;
            gridBagConstraints4.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 0;
            this.mainPanel = new JPanel();
            this.mainPanel.setLayout(new GridBagLayout());
            this.mainPanel.add(getDataSourcePanel(), gridBagConstraints1);
            this.mainPanel.add(getFeaturesPanel(), gridBagConstraints4);
            this.mainPanel.add(getOkButton(), gridBagConstraints5);
        }
        return this.mainPanel;
    }


    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getWsEnumCheckBox() {
        if (this.wsEnumCheckBox == null) {
            this.wsEnumCheckBox = new JCheckBox();
            this.wsEnumCheckBox.setText("Use WS-Enumeration");
            // can only use ws-enumeration if the extension has been installed
            this.wsEnumCheckBox.setEnabled(wsEnumExtensionInstalled());
        }
        return this.wsEnumCheckBox;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (this.okButton == null) {
            this.okButton = new JButton();
            this.okButton.setText("OK");
            this.okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setFeatureStatus();
                    if (getSdkDataRadioButton().isSelected()) {
                        ServiceWizard wiz = new ServiceWizard(PortalResourceManager.getInstance().getGridPortal(),
                            "caCORE Data Source");

                        wiz.addWizardPanel(new CoreDsIntroPanel(getExtensionDescription(), getServiceInfo()));
                        wiz.addWizardPanel(new SDKClientSelectionPanel(getExtensionDescription(), getServiceInfo()));
                        wiz.addWizardPanel(new AppserviceConfigPanel(getExtensionDescription(), getServiceInfo()));
                        wiz.addWizardPanel(new DomainModelPanel(getExtensionDescription(), getServiceInfo()));
                        wiz.addWizardPanel(new SchemaTypesPanel(getExtensionDescription(), getServiceInfo()));

                        PortalUtils.centerComponent(wiz);
                        wiz.showAt(wiz.getX(), wiz.getY());
                    }
                    dispose();
                }
            });
        }
        return this.okButton;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getFeaturesPanel() {
        if (this.featuresPanel == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridy = 1;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.weightx = 1.0D;
            gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            this.featuresPanel = new JPanel();
            this.featuresPanel.setLayout(new GridBagLayout());
            this.featuresPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Optional Features",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            this.featuresPanel.add(getWsEnumCheckBox(), gridBagConstraints);
            this.featuresPanel.add(getGridIdentCheckBox(), gridBagConstraints11);
            this.featuresPanel.add(getBdtCheckBox(), gridBagConstraints12);
        }
        return this.featuresPanel;
    }


    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGridIdentCheckBox() {
        if (this.gridIdentCheckBox == null) {
            this.gridIdentCheckBox = new JCheckBox();
            this.gridIdentCheckBox.setEnabled(false);
            this.gridIdentCheckBox.setText("Use Grid Identifier");
        }
        return this.gridIdentCheckBox;
    }


    /**
     * This method initializes jRadioButton
     * 
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getCustomDataRadioButton() {
        if (this.customDataRadioButton == null) {
            this.customDataRadioButton = new JRadioButton();
            this.customDataRadioButton.setText("Custom Data Source");
        }
        return this.customDataRadioButton;
    }


    /**
     * This method initializes jRadioButton
     * 
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSdkDataRadioButton() {
        if (this.sdkDataRadioButton == null) {
            this.sdkDataRadioButton = new JRadioButton();
            this.sdkDataRadioButton.setText("caCORE SDK Data Source");
        }
        return this.sdkDataRadioButton;
    }


    private ButtonGroup getDataButtonGroup() {
        if (this.dataButtonGroup == null) {
            this.dataButtonGroup = new ButtonGroup();
            this.dataButtonGroup.add(getCustomDataRadioButton());
            this.dataButtonGroup.add(getSdkDataRadioButton());
            this.dataButtonGroup.setSelected(getCustomDataRadioButton().getModel(), true);
        }
        return this.dataButtonGroup;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDataSourcePanel() {
        if (this.dataSourcePanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.weightx = 1.0D;
            gridBagConstraints3.gridy = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.gridy = 0;
            this.dataSourcePanel = new JPanel();
            this.dataSourcePanel.setLayout(new GridBagLayout());
            this.dataSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Source",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            this.dataSourcePanel.add(getCustomDataRadioButton(), gridBagConstraints2);
            this.dataSourcePanel.add(getSdkDataRadioButton(), gridBagConstraints3);
        }
        return this.dataSourcePanel;
    }


    private void setFeatureStatus() {
        if (getBdtCheckBox().isSelected()) {
            if (!bdtExtensionUsed()) {
                // add the BDT extension
                // TODO: get rid of this and use the extension tools to add
                // the BDT extension in the data extension's feature creator
                ExtensionDescription desc = ExtensionsLoader.getInstance().getExtension(BDT_EXTENSIONS_NAME);
                ExtensionType bdtExtension = new ExtensionType();
                bdtExtension.setName(desc.getServiceExtensionDescription().getName());
                bdtExtension.setExtensionType(desc.getExtensionType());
                bdtExtension.setVersion(desc.getVersion());
                // BDT extension has to run before data extension does
                ExtensionType[] currentExtensions = getServiceInfo().getExtensions().getExtension();
                ExtensionType[] newExtensions = new ExtensionType[currentExtensions.length + 1];
                newExtensions[0] = bdtExtension;
                System.arraycopy(currentExtensions, 0, newExtensions, 1, currentExtensions.length);
                System.out.println("Service has " + newExtensions.length + " extensions:");
                for (ExtensionType element : newExtensions) {
                    System.out.println(element.getName());
                }
                getServiceInfo().getExtensions().setExtension(newExtensions);
            }
        }

        // set the selected service features
        ExtensionTypeExtensionData data = getExtensionTypeExtensionData();
        ServiceFeatures features = new ServiceFeatures();
        try {
            features.setUseSdkDataSource(getSdkDataRadioButton().isSelected());
            features.setUseGridIdeitifiers(getGridIdentCheckBox().isSelected());
            features.setUseWsEnumeration(getWsEnumCheckBox().isSelected());
            features.setUseBdt(getBdtCheckBox().isSelected());

            Data extData = ExtensionDataUtils.getExtensionData(data);
            extData.setServiceFeatures(features);
            ExtensionDataUtils.storeExtensionData(data, extData);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error storing configuration: " + ex.getMessage(), ex);
        }
    }


    private boolean wsEnumExtensionInstalled() {
        List extensionDescriptors = ExtensionsLoader.getInstance().getServiceExtensions();
        for (int i = 0; i < extensionDescriptors.size(); i++) {
            ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) extensionDescriptors.get(i);
            if (ex.getName().equals(WS_ENUM_EXTENSION_NAME)) {
                return true;
            }
        }
        return false;
    }


    private boolean bdtExtensionInstalled() {
        List extensionDescriptors = ExtensionsLoader.getInstance().getServiceExtensions();
        for (int i = 0; i < extensionDescriptors.size(); i++) {
            ServiceExtensionDescriptionType desc = (ServiceExtensionDescriptionType) extensionDescriptors.get(i);
            if (desc.getName().equals(BDT_EXTENSIONS_NAME)) {
                return true;
            }
        }
        return false;
    }


    private boolean bdtExtensionUsed() {
        ExtensionType[] extensions = getServiceInfo().getExtensions().getExtension();
        for (ExtensionType element : extensions) {
            if (element.getName().equals(BDT_EXTENSIONS_NAME)) {
                return true;
            }
        }
        return false;
    }


    /**
     * This method initializes bdtCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBdtCheckBox() {
        if (this.bdtCheckBox == null) {
            this.bdtCheckBox = new JCheckBox();
            this.bdtCheckBox.setText("Use caGrid BDT");
            // can only enable BDT if it has been installed
            this.bdtCheckBox.setEnabled(bdtExtensionInstalled());
        }
        return this.bdtCheckBox;
    }
}
