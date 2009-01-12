package org.cagrid.cadsr.portal.discovery;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.common.ConfigurationUtil;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URISyntaxException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.cadsr.portal.CaDSRBrowserPanel;
import org.cagrid.cadsr.portal.PackageSelectedListener;
import org.cagrid.cadsr.portal.ProjectSelectedListener;
import org.cagrid.gme.discoverytools.GMETypeSelectionComponentBase;
import org.cagrid.gme.domain.XMLSchemaNamespace;


public class CaDSRTypeSelectionComponent extends GMETypeSelectionComponentBase
    implements
        PackageSelectedListener,
        ProjectSelectedListener {

    private static final Log LOG = LogFactory.getLog(GMETypeSelectionComponentBase.class);

    private static final String UNAVAILABLE = "unavailable";
    private CaDSRBrowserPanel caDSRPanel = null;
    private JPanel nsPanel = null;
    private JLabel nsLabel = null;
    private JTextField nsTextField = null;


    public CaDSRTypeSelectionComponent(DiscoveryExtensionDescriptionType desc, NamespacesType currentNamespaces) {
        super(desc, currentNamespaces);
        initialize();
        try {
            this.getCaDSRPanel().setDefaultCaDSRURL(getCaDSRURL());
        } catch (Exception e) {
            LOG.error("Unable to set caDSR URL", e);
        }
        this.getCaDSRPanel().discoverFromCaDSR();
    }


    private String getCaDSRURL() throws Exception {
        return ConfigurationUtil.getGlobalExtensionProperty(CaDSRDiscoveryConstants.CADSR_DATA_SERVICE_URL_PROPERTY).getValue();
    }


    protected String getGMEURL() throws Exception {
        return ConfigurationUtil.getGlobalExtensionProperty(CaDSRDiscoveryConstants.GME_URL_PROPERTY).getValue();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1D;
        gridBagConstraints.weighty = 1D;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints4.gridwidth = 1;
        gridBagConstraints4.weightx = 1D;
        gridBagConstraints4.weighty = 1D;
        gridBagConstraints4.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.add(getCaDSRPanel(), gridBagConstraints4);
        this.add(getNsPanel(), gridBagConstraints);
    }


    /**
     * This method initializes gmePanel
     * 
     * @return javax.swing.JPanel
     */
    private CaDSRBrowserPanel getCaDSRPanel() {
        if (this.caDSRPanel == null) {
            this.caDSRPanel = new CaDSRBrowserPanel(false, false);
            this.caDSRPanel.addPackageSelectionListener(this);
            this.caDSRPanel.addProjectSelectionListener(this);
        }
        return this.caDSRPanel;
    }


    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getNsPanel() {
        if (this.nsPanel == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new java.awt.Insets(5, 3, 5, 5);
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(7, 5, 7, 2);
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.gridx = 0;
            this.nsLabel = new JLabel();
            this.nsLabel.setText("Namespace:");
            this.nsPanel = new JPanel();
            this.nsPanel.setLayout(new GridBagLayout());
            this.nsPanel.add(this.nsLabel, gridBagConstraints1);
            this.nsPanel.add(getNsTextField(), gridBagConstraints2);
        }
        return this.nsPanel;
    }


    /**
     * This method initializes nsTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getNsTextField() {
        if (this.nsTextField == null) {
            this.nsTextField = new JTextField();
            this.nsTextField.setEditable(false);
            this.nsTextField.setText(UNAVAILABLE);
        }
        return this.nsTextField;
    }


    public void handleProjectSelection(Project project) {
        try {
        } catch (Exception e) {
            LOG.error("Problem getting caDSR URL.", e);
        }
    }


    public void handlePackageSelection(UMLPackageMetadata pkg) {
        try {
        } catch (Exception e) {
            LOG.error("Problem getting caDSR URL.", e);
        }
        
        
        // TODO: look up the schema from the caDSR mappings here

        Project proj = getCaDSRPanel().getSelectedProject();
        if (proj != null) {
            // get the Context
            String context = "caBIG";
            // try {
            // CaDSRServiceI cadsrService = new
            // CaDSRServiceClient(getCaDSRURL());
            // Context ctx = cadsrService.findContextForProject(proj);
            // context = ctx.getName();
            // } catch (Exception e) {
            // // just use the default, and don't bother the user.
            // e.printStackTrace();
            // }
            String version = proj.getVersion();
            if (version.indexOf(".") < 0) {
                version += ".0";
            }
            getNsTextField().setText(
                "gme://" + proj.getShortName() + "." + context + "/" + version + "/" + pkg.getName());
        } else {
            getNsTextField().setText(UNAVAILABLE);
        }
    }


    @Override
    protected XMLSchemaNamespace getCurrentSchemaNamespace() {
        String ns = getNsTextField().getText();
        if (ns.equals("") || ns.equals(UNAVAILABLE)) {
            addError("Unable to locate schema for the selected caDSR package.\n"
                + "This package may not have a published Schema.");
            return null;
        }
        try {
            return new XMLSchemaNamespace(ns);
        } catch (URISyntaxException e) {
            LOG.error("Invalid namespace format returned.", e);
            addError("Invalid Namespace format returned.");
            return null;
        }
    }

}
