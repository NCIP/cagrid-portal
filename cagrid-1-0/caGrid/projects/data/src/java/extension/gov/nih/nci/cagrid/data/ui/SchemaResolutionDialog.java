package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.data.ui.cacore.CacoreWizardUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.projectmobius.portal.PortalResourceManager;


/**
 * SchemaResolutionDialog Dialog to resolve schemas from all available namespace
 * type discovery extension components
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Sep 27, 2006
 * @version $Id$
 */
public class SchemaResolutionDialog extends JDialog {

    public static final String SELECT_AN_ITEM = " -- SELECT AN ITEM --";

    private transient ServiceInformation serviceInfo;

    private JButton loadSchemasButton = null;
    private JButton cancelButton = null;
    private JPanel buttonPanel = null;
    private JPanel mainPanel = null;
    private JTabbedPane discoveryTabbedPane = null;
    private NamespaceType[] resolvedSchemas;


    private SchemaResolutionDialog(ServiceInformation info) {
        super(PortalResourceManager.getInstance().getGridPortal(), "Schema Resolution", true);
        this.serviceInfo = info;
        this.resolvedSchemas = null;
        initialize();
    }


    /**
     * Resolves schemas for the given cadsr package
     * 
     * @param info
     * @return null if an error occurs resolving schemas an empty array (length ==
     *         0) if user cancels the dialog array of NamespaceType (length !=
     *         0) if resolution was successful
     */
    public static NamespaceType[] resolveSchemas(ServiceInformation info) {
        SchemaResolutionDialog dialog = new SchemaResolutionDialog(info);
        return dialog.resolvedSchemas;
    }


    private void initialize() {
        setModal(true);
        this.setSize(new java.awt.Dimension(400, 330));
        this.setContentPane(getMainPanel());
        this.setVisible(true);
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getLoadSchemasButton() {
        if (this.loadSchemasButton == null) {
            this.loadSchemasButton = new JButton();
            this.loadSchemasButton.setText("Load Schemas");
            this.loadSchemasButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SchemaResolutionDialog.this.resolvedSchemas = loadSchemas();
                    // set the new namespace mapping of the cadsr package
                    // cadsrPackage.setMappedNamespace(resolvedSchemas[0].getNamespace());
                    dispose();
                }
            });
        }
        return this.loadSchemasButton;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (this.cancelButton == null) {
            this.cancelButton = new JButton();
            this.cancelButton.setText("Cancel");
            this.cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SchemaResolutionDialog.this.resolvedSchemas = new NamespaceType[0];
                    dispose();
                }
            });
        }
        return this.cancelButton;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (this.buttonPanel == null) {
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 1;
            gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints15.gridy = 0;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints14.gridy = 0;
            this.buttonPanel = new JPanel();
            this.buttonPanel.setLayout(new GridBagLayout());
            this.buttonPanel.add(getLoadSchemasButton(), gridBagConstraints14);
            this.buttonPanel.add(getCancelButton(), gridBagConstraints15);
        }
        return this.buttonPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (this.mainPanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints.gridx = 0;
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.gridx = 0;
            gridBagConstraints19.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints19.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints19.gridy = 1;
            this.mainPanel = new JPanel();
            this.mainPanel.setLayout(new GridBagLayout());
            this.mainPanel.add(getButtonPanel(), gridBagConstraints19);
            this.mainPanel.add(getDiscoveryTabbedPane(), gridBagConstraints);
        }
        return this.mainPanel;
    }


    private JTabbedPane getDiscoveryTabbedPane() {
        if (this.discoveryTabbedPane == null) {
            this.discoveryTabbedPane = new JTabbedPane();
            // get the discovery extensions from Introduce
            List discoveryTypes = ExtensionsLoader.getInstance().getDiscoveryExtensions();
            if (discoveryTypes != null) {
                Iterator discoIter = discoveryTypes.iterator();
                while (discoIter.hasNext()) {
                    DiscoveryExtensionDescriptionType dd = (DiscoveryExtensionDescriptionType) discoIter.next();
                    try {
                        NamespaceTypeDiscoveryComponent comp = ExtensionTools.getNamespaceTypeDiscoveryComponent(dd
                            .getName(), this.serviceInfo.getNamespaces());
                        if (comp != null) {
                            this.discoveryTabbedPane.addTab(dd.getDisplayName(), comp);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error adding type discovery component to dialog", ex);
                    }
                }
            }
        }
        return this.discoveryTabbedPane;
    }


    private NamespaceType[] loadSchemas() {
        // get the discovery type component
        NamespaceTypeDiscoveryComponent discComponent = (NamespaceTypeDiscoveryComponent) getDiscoveryTabbedPane()
            .getSelectedComponent();
        // get the service's schema directory
        File schemaDir = new File(CacoreWizardUtils.getServiceBaseDir(this.serviceInfo)
            + File.separator
            + "schema"
            + File.separator
            + this.serviceInfo.getIntroduceServiceProperties().getProperty(
                IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));

        // TODO: add this to the GUI somewhere
        MultiEventProgressBar multiEventProgressBar = new MultiEventProgressBar(true);

        NamespaceType[] namespaces = discComponent.createNamespaceType(schemaDir, ResourceManager
            .getConfigurationProperty(IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY),
            multiEventProgressBar);
        if (namespaces == null) {
            ErrorDialog.showErrorDialog("Error getting types from discovery component");
        }
        return namespaces;
    }
}
