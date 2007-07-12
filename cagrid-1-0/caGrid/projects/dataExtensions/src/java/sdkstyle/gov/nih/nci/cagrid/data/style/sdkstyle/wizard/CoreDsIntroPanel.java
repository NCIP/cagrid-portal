package gov.nih.nci.cagrid.data.style.sdkstyle.wizard;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.data.ui.wizard.CacoreWizardUtils;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;


/**
 * CoreDsIntroPanel 
 * Simple panel to show the user some information about what
 * they're getting into for developing a caGrid data service backended by the
 * cacore SDK
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Sep 25, 2006
 * @version $Id: CoreDsIntroPanel.java,v 1.1 2007-07-12 17:20:52 dervin Exp $
 */
public class CoreDsIntroPanel extends AbstractWizardPanel {

    // constants for the 3.1 version of the SDK Query Processor
    public static final String SDK_31_QUERY_LIB = ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data"
        + File.separator + "sdk31" + File.separator + "lib" + File.separator + "caGrid-1.1-sdkQuery-core.jar";
    public static final String SDK_31_QUERY_PROCESSOR = "gov.nih.nci.cagrid.data.cql.cacore.HQLCoreQueryProcessor";

    /**
     * This is the text message shown to the user to let them know what the
     * wizard does and what it expects from them. A better idea may be to load
     * this from a text file so if it changes, we dont need to recompile.
     */
    private static String INFO_TEXT = "This wizard will guide you through a simplified process to create a caGrid Data Service "
        + "using a caCORE SDK backend as your data source.  In the following screens, you will be "
        + "required to provide your generated client.jar, along with the URL of the data source to "
        + "have the data service connect to.  Your domain model should be registered in the caDSR "
        + "for proper generation of a Domain Model metadata extract, or you can provide this file "
        + "yourself.  Finally, the schemas for all data types in your model need to be added, "
        + "either from the GME, or from your local file system.";

    private JLabel wizardLabel = null;
    private JTextArea infoTextArea = null;
    private JScrollPane infoTextScrollPane = null;

    public CoreDsIntroPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        initialize();
    }

    
    public void update() {
        setLibrariesAndProcessor();
    }
    

    private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.insets = new Insets(10, 10, 5, 10);
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(608, 301));
        this.add(getWizardLabel(), gridBagConstraints);
        this.add(getInfoTextScrollPane(), gridBagConstraints1);
    }


    public String getPanelTitle() {
        return "Create caCORE SDK Backended caGrid Data Service";
    }


    public String getPanelShortName() {
        return "Intro";
    }


    /**
     * This method initializes jLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getWizardLabel() {
        if (wizardLabel == null) {
            wizardLabel = new JLabel();
            Icon icon = new ImageIcon(getClass().getResource(
                "/" + getClass().getPackage().getName().replace('.', '/') + "/resources/sdk3.gif"));
            wizardLabel.setIcon(icon);
        }
        return wizardLabel;
    }


    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getInfoTextArea() {
        if (infoTextArea == null) {
            infoTextArea = new JTextArea();
            infoTextArea.setLineWrap(true);
            infoTextArea.setEditable(false);
            infoTextArea.setWrapStyleWord(true);
            infoTextArea.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
            infoTextArea.setText(INFO_TEXT);
        }
        return infoTextArea;
    }


    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getInfoTextScrollPane() {
        if (infoTextScrollPane == null) {
            infoTextScrollPane = new JScrollPane();
            infoTextScrollPane.setBorder(
                new javax.swing.border.SoftBevelBorder(BevelBorder.LOWERED));
            infoTextScrollPane.setViewportView(getInfoTextArea());
            infoTextScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return infoTextScrollPane;
    }


    /**
     * Copies the caCORE SDK query processor jar into the data service and sets
     * the query processor class property
     */
    private void setLibrariesAndProcessor() {
        String libName = SDK_31_QUERY_LIB;
        String qpClassName = SDK_31_QUERY_PROCESSOR;
        // get the path to the SDK Query project
        File sdkQuery = new File(libName);
        if (!sdkQuery.exists()) {
            String[] error = {"The SDK Query project does not exist or has not",
                    "yet been built.  Please build this project first!"};
            ErrorDialog.showErrorDialog("SDK Query Library Not Found", error);
        } else {
            // copy the library to the service's lib dir
            File sdkQueryOut = new File(CacoreWizardUtils.getServiceBaseDir(getServiceInformation()) + File.separator
                + "lib" + File.separator + sdkQuery.getName());
            try {
                Utils.copyFile(sdkQuery, sdkQueryOut);
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error copying the required query processor library", ex);
                return;
            }
            // add the library to the service's additional libs list
            try {
                Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
                AdditionalLibraries libs = data.getAdditionalLibraries();
                if (libs == null) {
                    libs = new AdditionalLibraries();
                    data.setAdditionalLibraries(libs);
                }
                Set jarNames = new HashSet();
                if (libs.getJarName() != null) {
                    Collections.addAll(jarNames, (Object[]) libs.getJarName());
                }
                // add the new library
                jarNames.add(sdkQuery.getName());
                String[] names = new String[jarNames.size()];
                jarNames.toArray(names);
                libs.setJarName(names);
                // store the modified list
                ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error adding the library to the service information", ex);
                return;
            }
            // add the query processor class name as a service property
            CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
                DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, qpClassName, false);
        }
    }
}
