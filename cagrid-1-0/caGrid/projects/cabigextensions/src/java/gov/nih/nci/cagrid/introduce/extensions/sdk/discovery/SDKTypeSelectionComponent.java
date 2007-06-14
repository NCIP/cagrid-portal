package gov.nih.nci.cagrid.introduce.extensions.sdk.discovery;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;


public class SDKTypeSelectionComponent extends NamespaceTypeDiscoveryComponent {

    /**
     * Comment for <code>YOUR_PROJECT</code>
     */
    private static final String DEFAULT_PROJECT = "YourProject";
    private static final String CONTEXT = "Context";
    private static final String PROJECT_SHORT_NAME = "Project Short Name";
    private static final String PROJECT_VERSION = "Project Version";
    private static final String MODEL_XMI_FILE = "Model XMI File";
    private static final String PACKAGE_INCLUDES = "Package Includes";
    private static final String PACKAGE_EXCLUDES = "Package Excludes";

    private JTextField modelTextField = null;
    private JButton modelBrowseButton = null;
    private JTextField packageIncludeTextField = null;
    private JTextField packageExcludeTextField = null;
    private JPanel modelInfoPanel = null;
    private JPanel caDSRPanel = null;
    private JLabel modelFileLabel = null;
    private JLabel packIncludeLabel = null;
    private JLabel packExcludeLabel = null;
    private JTextField contextTextField = null;
    private JTextField projNameTextField = null;
    private JTextField projVersionTextField = null;
    private JLabel contextLabel = null;
    private JLabel projNameLabel = null;
    private JLabel projVersionLabel = null;
    // for package regexs
    private PatternCompiler compiler = new Perl5Compiler();

    private File extensionDir = null;
    private ValidationResultModel validationModel = new DefaultValidationResultModel();
    private SDKGenerationInformation genInfo = null;


    public SDKTypeSelectionComponent(DiscoveryExtensionDescriptionType desc, NamespacesType currentNamespaces) {
        super(desc, currentNamespaces);
        this.extensionDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + desc.getName());
        initialize();
    }


    /**
     * This method initializes this
     */
    private void initialize() {

        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 0;
        gridBagConstraints21.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints21.weightx = 1.0D;
        gridBagConstraints21.weighty = 1.0D;
        gridBagConstraints21.gridy = 1;
        GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
        gridBagConstraints22.gridx = 0;
        gridBagConstraints22.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints22.weightx = 0.0D;
        gridBagConstraints22.weighty = 0.0D;
        gridBagConstraints22.gridy = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 1.0D;
        gridBagConstraints1.gridy = 0;
        setLayout(new GridBagLayout());
        setBorder(javax.swing.BorderFactory.createTitledBorder(null,
            "Create XML Schemas from an XMI Model File (caCORE SDK Compliant)",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
            null, null));

        add(new IconFeedbackPanel(this.validationModel, getModelInfoPanel()), gridBagConstraints1);
        add(new IconFeedbackPanel(this.validationModel, getCaDSRPanel()), gridBagConstraints21);

        initValidation();

        JComponent validationReportList = ValidationResultViewFactory.createReportList(this.validationModel);
        add(validationReportList, gridBagConstraints22);

    }


    private void initValidation() {
        ValidationComponentUtils.setMessageKey(getContextTextField(), CONTEXT);
        ValidationComponentUtils.setMessageKey(getModelTextField(), MODEL_XMI_FILE);
        ValidationComponentUtils.setMessageKey(getPackageExcludeTextField(), PACKAGE_EXCLUDES);
        ValidationComponentUtils.setMessageKey(getPackageIncludeTextField(), PACKAGE_INCLUDES);
        ValidationComponentUtils.setMessageKey(getProjNameTextField(), PROJECT_SHORT_NAME);
        ValidationComponentUtils.setMessageKey(getProjVersionTextField(), PROJECT_VERSION);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new FocusChangeHandler());

        updateModel();
        validateInput();
        updateComponentTreeSeverity();
    }


    private final class FocusChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if (!"permanentFocusOwner".equals(propertyName)) {
                return;
            }
            updateModel();
            validateInput();
        }
    }


    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, this.validationModel.getResult());
    }


    private void validateInput() {

        ValidationResult result = new ValidationResult();

        if (!ValidationUtils.isNotBlank(this.genInfo.getXmiFile())) {
            result.add(new SimpleValidationMessage(MODEL_XMI_FILE + " must not be blank.", Severity.ERROR,
                MODEL_XMI_FILE));
        } else {
            File f = new File(this.genInfo.getXmiFile());
            if (f.isFile() && f.canRead()) {
                this.genInfo.setXmiFile(f.getAbsolutePath());
            } else {
                result.add(new SimpleValidationMessage(MODEL_XMI_FILE + " must be a valid readible file.",
                    Severity.ERROR, MODEL_XMI_FILE));
            }
        }

        if (!ValidationUtils.isNotBlank(this.genInfo.getCaDSRcontext())) {
            result.add(new SimpleValidationMessage(CONTEXT + " must not be blank.", Severity.ERROR, CONTEXT));
        }

        if (!ValidationUtils.isNotBlank(this.genInfo.getCaDSRProjectName())) {
            result.add(new SimpleValidationMessage(PROJECT_SHORT_NAME + " must not be blank.", Severity.ERROR,
                PROJECT_SHORT_NAME));
        } else if (this.genInfo.getCaDSRProjectName().equals(DEFAULT_PROJECT)) {
            result.add(new SimpleValidationMessage(PROJECT_SHORT_NAME
                + " should be changed to your project's short name.", Severity.WARNING, PROJECT_SHORT_NAME));
        }

        if (!ValidationUtils.isNotBlank(this.genInfo.getCaDSRProjectVersion())) {
            result.add(new SimpleValidationMessage(PROJECT_VERSION + " must not be blank.", Severity.ERROR,
                PROJECT_VERSION));
        }

        if (ValidationUtils.isBlank(this.genInfo.getPackageIncludes())) {
            result.add(new SimpleValidationMessage(PACKAGE_INCLUDES
                + " should not be blank, as nothing will be included.", Severity.WARNING, PACKAGE_INCLUDES));
        } else {
            try {
                this.compiler.compile(this.genInfo.getPackageIncludes());
            } catch (MalformedPatternException e) {
                result.add(new SimpleValidationMessage(PACKAGE_INCLUDES + " must be a valid regex:" + e.getMessage(),
                    Severity.ERROR, PACKAGE_INCLUDES));
            }
        }

        if (!ValidationUtils.isBlank(this.genInfo.getPackageExcludes())) {
            try {
                this.compiler.compile(this.genInfo.getPackageExcludes());
            } catch (MalformedPatternException e) {
                result.add(new SimpleValidationMessage(PACKAGE_EXCLUDES + " must be empty, or a valid regex:"
                    + e.getMessage(), Severity.ERROR, PACKAGE_EXCLUDES));
            }
        }

        this.validationModel.setResult(result);
        updateComponentTreeSeverity();

    }


    @Override
    public NamespaceType[] createNamespaceType(File schemaDestinationDir, String policy) {
        updateModel();
        validateInput();

        if (this.validationModel.getResult().hasErrors()) {
            System.out.println("Not valid");
            return null;
        }

        System.out.println(this.genInfo);

        // try {
        // List namespaceTypes = new ArrayList();
        // NamespaceType rootNamespace = new NamespaceType();
        // String ns = getNsTextField().getText();
        // if (ns.equals("") || ns.equals("unavailable")) {
        // return null;
        // }
        // Namespace namespace = new Namespace(ns);
        // List namespaceDomainList = getGME().getNamespaceDomainList();
        // if (!namespaceDomainList.contains(namespace.getDomain())) {
        // // prompt for alternate
        // String alternativeDomain = (String) JOptionPane.showInputDialog(this,
        // "The GME does not appear to contain schemas under the specified
        // domain.\n"
        // + "Select an alternative domain, or cancel if no viable option is
        // available.\n"
        // + "\nExpected domain: " + namespace.getDomain(), "Schema Location
        // Error",
        // JOptionPane.ERROR_MESSAGE, null, namespaceDomainList.toArray(),
        // null);
        //
        // if (alternativeDomain != null) {
        // namespace = new Namespace(namespace.getProtocol() + "://" +
        // alternativeDomain + "/"
        // + namespace.getName());
        // getNsTextField().setText(namespace.getRaw());
        // } else {
        // return null;
        // }
        // }
        // String schemaContents = null;
        // try {
        // schemaContents = getSchema(namespace);
        // } catch (NoSuchSchemaException e) {
        // // prompt for alternate
        // List schemas =
        // getGME().getSchemaListForNamespaceDomain(namespace.getDomain());
        // Namespace alternativeSchema = (Namespace)
        // JOptionPane.showInputDialog(this,
        // "Unable to locate schema for the selected caDSR package.\n"
        // + "This package may not have a published Schema."
        // + "\nSelect an alternative Schema, or cancel.\n\nExpected schema: " +
        // namespace.getName(),
        // "Schema Location Error", JOptionPane.ERROR_MESSAGE, null,
        // schemas.toArray(), null);
        //
        // if (alternativeSchema != null) {
        // namespace = alternativeSchema;
        // getNsTextField().setText(namespace.getRaw());
        // } else {
        // return null;
        // }
        // schemaContents = getSchema(namespace);
        // }
        //
        // // set the package name
        // String packageName = CommonTools.getPackageName(namespace);
        // rootNamespace.setPackageName(packageName);
        //
        // rootNamespace.setNamespace(namespace.getRaw());
        // ImportInfo ii = new ImportInfo(namespace);
        // rootNamespace.setLocation("./" + ii.getFileName());
        //
        // // popualte the schema elements
        // gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools.setSchemaElements(rootNamespace,
        // XMLUtilities
        // .stringToDocument(schemaContents));
        // namespaceTypes.add(rootNamespace);
        // // write the schema and its imports to the filesystem
        // List writtenNamespaces = getGME().cacheSchema(namespace,
        // schemaDestinationDir);
        // Iterator nsIter = writtenNamespaces.iterator();
        // while (nsIter.hasNext()) {
        // Namespace importedNamesapce = (Namespace) nsIter.next();
        // if (!importedNamesapce.getRaw().equals(rootNamespace.getNamespace()))
        // {
        // ImportInfo imp = new ImportInfo(importedNamesapce);
        // String filename = imp.getFileName();
        // // create a namespace type from the imported schema
        // String fullSchemaName = schemaDestinationDir.getAbsolutePath() +
        // File.separator + filename;
        // NamespaceType type = CommonTools.createNamespaceType(fullSchemaName);
        // // fix the location to be relative to the root schema
        // type.setLocation("./" + filename);
        // namespaceTypes.add(type);
        // }
        // }
        //
        // NamespaceType[] nsTypeArray = new
        // NamespaceType[namespaceTypes.size()];
        // namespaceTypes.toArray(nsTypeArray);
        // return nsTypeArray;
        // } catch (Exception e) {
        // e.printStackTrace();
        // return null;
        // }
        //		
        return null;
    }


    /**
     * @return
     */
    private SDKGenerationInformation updateModel() {
        this.genInfo = new SDKGenerationInformation();
        this.genInfo.setCaDSRcontext(getContextTextField().getText());
        this.genInfo.setCaDSRProjectName(getProjNameTextField().getText());
        this.genInfo.setCaDSRProjectVersion(getProjVersionTextField().getText());
        this.genInfo.setPackageExcludes(getPackageExcludeTextField().getText());
        this.genInfo.setPackageIncludes(getPackageIncludeTextField().getText());
        this.genInfo.setXmiFile(getModelTextField().getText());
        return this.genInfo;
    }


    /**
     * This method initializes modelTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getModelTextField() {
        if (this.modelTextField == null) {
            this.modelTextField = new JTextField();
            this.modelTextField.setColumns(20);
        }
        return this.modelTextField;
    }


    /**
     * This method initializes modelBrowseButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getModelBrowseButton() {
        if (this.modelBrowseButton == null) {
            this.modelBrowseButton = new JButton();
            this.modelBrowseButton.setText("Browse...");
            this.modelBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    launchModelFileBrowser();
                }
            });
        }
        return this.modelBrowseButton;
    }


    protected void launchModelFileBrowser() {
        String selectedFilename = null;
        try {
            selectedFilename = ResourceManager.promptFile(null, FileFilters.XMI_FILTER);
        } catch (IOException e) {
            ErrorDialog.showErrorDialog("Problem selecting file, please try again or manually type the file path.", e);
            return;
        }

        if (selectedFilename != null) {
            getModelTextField().setText(new File(selectedFilename).getAbsolutePath());
        }
    }


    /**
     * This method initializes packageIncludeTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getPackageIncludeTextField() {
        if (this.packageIncludeTextField == null) {
            this.packageIncludeTextField = new JTextField();
            this.packageIncludeTextField.setText(".*domain.*");
            this.packageIncludeTextField.setColumns(20);
        }
        return this.packageIncludeTextField;
    }


    /**
     * This method initializes packageExcludeTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getPackageExcludeTextField() {
        if (this.packageExcludeTextField == null) {
            this.packageExcludeTextField = new JTextField();
        }
        return this.packageExcludeTextField;
    }


    /**
     * This method initializes modelInfoPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getModelInfoPanel() {
        if (this.modelInfoPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints6.gridy = 2;
            this.packExcludeLabel = new JLabel();
            this.packExcludeLabel.setText(PACKAGE_EXCLUDES);
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints5.gridy = 1;
            this.packIncludeLabel = new JLabel();
            this.packIncludeLabel.setText(PACKAGE_INCLUDES);
            this.modelFileLabel = new JLabel();
            this.modelFileLabel.setText(MODEL_XMI_FILE);
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.gridx = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints.weightx = 1.0;
            this.modelInfoPanel = new JPanel();
            this.modelInfoPanel.setLayout(new GridBagLayout());
            this.modelInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Model Information",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            this.modelInfoPanel.add(this.modelFileLabel, gridBagConstraints7);
            this.modelInfoPanel.add(getModelTextField(), gridBagConstraints);
            this.modelInfoPanel.add(getModelBrowseButton(), gridBagConstraints2);
            this.modelInfoPanel.add(getPackageIncludeTextField(), gridBagConstraints3);
            this.modelInfoPanel.add(getPackageExcludeTextField(), gridBagConstraints4);
            this.modelInfoPanel.add(this.packIncludeLabel, gridBagConstraints5);
            this.modelInfoPanel.add(this.packExcludeLabel, gridBagConstraints6);
        }
        return this.modelInfoPanel;
    }


    /**
     * This method initializes caDSRPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCaDSRPanel() {
        if (this.caDSRPanel == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints13.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints13.gridy = 2;
            this.projVersionLabel = new JLabel();
            this.projVersionLabel.setText(PROJECT_VERSION);
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints12.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints12.gridy = 1;
            this.projNameLabel = new JLabel();
            this.projNameLabel.setText(PROJECT_SHORT_NAME);
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints11.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints11.gridy = 0;
            this.contextLabel = new JLabel();
            this.contextLabel.setText(CONTEXT);
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridx = 2;
            gridBagConstraints10.gridy = 2;
            gridBagConstraints10.weightx = 1.0;
            gridBagConstraints10.weighty = 0.0;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridx = 2;
            gridBagConstraints9.gridy = 1;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.weighty = 0.0;
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.gridx = 2;
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.weighty = 0.0;
            gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
            this.caDSRPanel = new JPanel();
            this.caDSRPanel.setLayout(new GridBagLayout());
            this.caDSRPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "caDSR Information",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            this.caDSRPanel.add(getContextTextField(), gridBagConstraints8);
            this.caDSRPanel.add(getProjNameTextField(), gridBagConstraints9);
            this.caDSRPanel.add(getProjVersionTextField(), gridBagConstraints10);
            this.caDSRPanel.add(this.contextLabel, gridBagConstraints11);
            this.caDSRPanel.add(this.projNameLabel, gridBagConstraints12);
            this.caDSRPanel.add(this.projVersionLabel, gridBagConstraints13);
        }
        return this.caDSRPanel;
    }


    /**
     * This method initializes contextTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getContextTextField() {
        if (this.contextTextField == null) {
            this.contextTextField = new JTextField();
            this.contextTextField.setText("caBIG");
            // this.contextTextField.setInputVerifier(new
            // NonWhiteSpaceValidator(this, this.contextTextField));
        }
        return this.contextTextField;
    }


    /**
     * This method initializes projNameTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getProjNameTextField() {
        if (this.projNameTextField == null) {
            this.projNameTextField = new JTextField();
            this.projNameTextField.setText(DEFAULT_PROJECT);
        }
        return this.projNameTextField;
    }


    /**
     * This method initializes projVersionTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getProjVersionTextField() {
        if (this.projVersionTextField == null) {
            this.projVersionTextField = new JTextField();
            this.projVersionTextField.setText("1.0");
        }
        return this.projVersionTextField;
    }


    public static void main(String[] args) {
        // NOTE: Needs to run from introduce directory!
        try {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ExtensionDescription ext = (ExtensionDescription) Utils.deserializeDocument(
                "../cabigextensions/extensions/sdk_discovery/extension.xml", ExtensionDescription.class);
            final SDKTypeSelectionComponent panel = new SDKTypeSelectionComponent(ext
                .getDiscoveryExtensionDescription(), null);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(panel, BorderLayout.CENTER);

            JButton createButton = new JButton("Test Create");
            createButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    NamespaceType[] createdNs = panel.createNamespaceType(new File("."), "");
                    if (createdNs != null) {
                        for (NamespaceType element : createdNs) {
                            System.out.println("Created Namespace:" + element.getNamespace() + " at location:"
                                + element.getLocation());
                        }
                    } else {
                        System.out.println("Problem creating namespace");
                    }
                }
            });
            frame.getContentPane().add(createButton, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

} // @jve:decl-index=0:visual-constraint="10,10
