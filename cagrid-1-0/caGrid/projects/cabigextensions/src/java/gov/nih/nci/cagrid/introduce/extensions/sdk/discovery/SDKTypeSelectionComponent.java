package gov.nih.nci.cagrid.introduce.extensions.sdk.discovery;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.common.IconFeedbackPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.projectmobius.common.MobiusException;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;


public class SDKTypeSelectionComponent extends NamespaceTypeDiscoveryComponent {

    private static final String CACORESDK_DIR_NAME = "cacoresdk";
    private static final String DEFAULT_PROJECT = "YourProject";
    private static final String CONTEXT = "Context";
    private static final String PROJECT_SHORT_NAME = "Project Short Name";
    private static final String PROJECT_VERSION = "Project Version";
    private static final String MODEL_XMI_FILE = "Model XMI File";
    private static final String PACKAGE_INCLUDES = "Package Includes";
    private static final String PACKAGE_EXCLUDES = "Package Excludes";

    private static final String CACORE_SDK_ZIPFILE_NAME = "caCORE_SDK_321.zip";

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

    protected static Log LOG = LogFactory.getLog(SDKTypeSelectionComponent.class.getName());


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
        gridBagConstraints22.weightx = 1.0D;
        gridBagConstraints22.weighty = 1.0D;
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

        updateModel();
        validateInput();
        updateComponentTreeSeverity();
    }


    private final class FocusChangeHandler implements FocusListener {

        public void focusGained(FocusEvent e) {
            update();

        }


        public void focusLost(FocusEvent e) {
            update();
        }


        private void update() {
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
    public NamespaceType[] createNamespaceType(File schemaDestinationDir, String policy, MultiEventProgressBar progress) {
        // updates genInfo
        updateModel();

        // updates validationModel
        validateInput();

        // populate the error dialog with messages
        if (this.validationModel.getResult().hasErrors()) {
            System.out.println("Inputs not valid, see error messages.");
            for (ValidationMessage message : this.validationModel.getResult().getErrors()) {
                addError(message.formattedText());
            }
            return null;
        }

        SDKExecutionResult result = null;
        try {
            try {
                int initEventID = progress.startEvent("Initializing SDK...");
                File sdkDir = new File(this.extensionDir, CACORESDK_DIR_NAME);
                // create working dir from zip if not extracted yet
                if (!sdkDir.exists()) {
                    // progress.getProgress().setString("Extracting SDK...");
                    File zip = new File(this.extensionDir, CACORE_SDK_ZIPFILE_NAME);
                    if (zip.exists() && zip.canRead() && zip.isFile()) {
                        try {
                            ZipUtilities.unzipInPlace(zip);
                        } catch (IOException e) {
                            addError("Problem extracting SDK from zip file: " + zip);
                            setErrorCauseThrowable(e);
                            return null;
                        }
                    } else {
                        addError("SDK directory [" + sdkDir + "] not found, and couldn't locate zip file: " + zip);
                        return null;
                    }
                }
                progress.stopEvent(initEventID, "Initialization Complete");

                int sdkEventID = progress.startEvent("Executing SDK...");
                result = SDKExecutor.runSDK(sdkDir, this.genInfo, progress);
                progress.stopEvent(sdkEventID, "SDK execution complete.");
            } catch (SDKExecutionException e) {
                addError("Problem executing SDK: " + e.getMessage());
                setErrorCauseThrowable(e);
                return null;
            }

            // validate any existing schemas against the namespace replacement
            // policy
            try {
                int validateEventID = progress.startEvent("Validating results...");
                validateGeneratedXSDs(result, policy);
                progress.stopEvent(validateEventID, "Validation complete.");
            } catch (SDKExecutionException e) {
                addError("Problem validating schemas generated by SDK against replacement policy:" + e.getMessage());
                setErrorCauseThrowable(e);
                return null;
            }

            // copy the XSD(s) to service schema dir
            List<File> copiedXSDs = null;
            try {
                int copyEventID = progress.startEvent("Copying schemas to service...");
                copiedXSDs = copyGeneratedXSDs(schemaDestinationDir, result, policy);
                progress.stopEvent(copyEventID, "Copy successful.");
            } catch (IOException e) {
                addError("Problem copying schemas generated by SDK:" + e.getMessage());
                setErrorCauseThrowable(e);
                return null;
            }

            if (!this.genInfo.isXSDOnly()) {
                // copy castor mapping to service common package
                // add castorMapping property to service and client configs
                // (wsdd)
                // copy jar to service lib
                // set serializer/deserializer to sdk for all schema types
            }

            // add schema types to introduce namespaces
            NamespaceType[] results = null;
            try {
                // progress.getProgress().setString("Adding results to
                // service...");
                results = createNamespaceTypes(schemaDestinationDir, result, copiedXSDs);
            } catch (MobiusException e) {
                addError("Problem processing schemas created by SDK: " + e.getMessage());
                setErrorCauseThrowable(e);
                return null;
            }

            return results;
        } finally {
            if (result != null) {
                int cleanupEventID = progress.startEvent("Cleaning up working directory.");
                // clean up our working area when we are done with it
                result.destroy();
                progress.stopAll("Complete.");
            }
        }

    }


    /**
     * @param result
     * @throws MobiusException
     */
    private NamespaceType[] createNamespaceTypes(File schemaDestinationDir, SDKExecutionResult result,
        List<File> schemas) throws MobiusException {
        List<NamespaceType> namespaceTypes = new ArrayList<NamespaceType>();
        for (File schema : schemas) {
            NamespaceType nsType = CommonTools.createNamespaceType(schema.getAbsolutePath(), schemaDestinationDir);
            namespaceTypes.add(nsType);
        }

        NamespaceType[] nsTypeArray = new NamespaceType[namespaceTypes.size()];
        namespaceTypes.toArray(nsTypeArray);
        return nsTypeArray;
    }


    /**
     * Copies schemas into service directory, honoring
     * 
     * @param schemaDestinationDir
     * @param result
     * @param namespaceExistsPolicy
     * @return
     * @throws IOException
     */
    private List<File> copyGeneratedXSDs(File schemaDestinationDir, SDKExecutionResult result,
        String namespaceExistsPolicy) throws IOException {
        List<File> results = new ArrayList<File>();
        for (File schema : result.getGeneratedXMLSchemas()) {
            String targetNamespace = null;
            try {
                targetNamespace = CommonTools.getTargetNamespace(schema);
            } catch (Exception e) {
                String error = "Problem determining targetNamespace of generated schema:" + schema.getAbsolutePath();
                LOG.error(error, e);
                throw new IOException(error);
            }

            // this should have been handled by the validation method and we
            // shouldn't be copying schemas
            assert (!(namespaceAlreadyExists(targetNamespace) && namespaceExistsPolicy.equals(ERROR_POLICY)));

            if (namespaceAlreadyExists(targetNamespace) && namespaceExistsPolicy.equals(IGNORE_POLICY)) {
                LOG.info("Ignoring schema [" + schema.getAbsolutePath() + "] with namespace [" + targetNamespace
                    + "], as it already exists");
            } else {
                File dest = new File(schemaDestinationDir, this.genInfo.getCaDSRProjectName().replace(" ", "_") + "/"
                    + schema.getName());
                FileUtils.copyFile(schema, dest);
                LOG.info("Adding schema:" + dest);
                results.add(dest);
            }
        }
        return results;
    }


    /**
     * Checks generated schemas against ERROR policy so no schemas are copied
     * into service if they already exist
     * 
     * @param result
     * @param namespaceExistsPolicy
     * @throws SDKExecutionException
     */
    private void validateGeneratedXSDs(SDKExecutionResult result, String namespaceExistsPolicy)
        throws SDKExecutionException {
        for (File schema : result.getGeneratedXMLSchemas()) {
            String targetNamespace = null;
            try {
                targetNamespace = CommonTools.getTargetNamespace(schema);
            } catch (Exception e) {
                String error = "Problem determining targetNamespace of generated schema:" + schema.getAbsolutePath();
                LOG.error(error, e);
                throw new SDKExecutionException(error, e);
            }
            if (namespaceAlreadyExists(targetNamespace) && namespaceExistsPolicy.equals(ERROR_POLICY)) {
                String error = "Could not add generated schema with namespace ["
                    + targetNamespace
                    + "], as it already exists and Introduce preference was to error.  To change this behavior, edit your preferences.";
                throw new SDKExecutionException(error);
            }

        }
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
            this.modelTextField.addFocusListener(new FocusChangeHandler());
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
            addError("Problem selecting file, please try again or manually type the file path.");
            setErrorCauseThrowable(e);
            return;
        }

        if (selectedFilename != null) {
            getModelTextField().setText(new File(selectedFilename).getAbsolutePath());
            updateModel();
            validateInput();
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
            this.packageIncludeTextField.addFocusListener(new FocusChangeHandler());
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
            this.packageExcludeTextField.addFocusListener(new FocusChangeHandler());
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
            this.contextTextField.addFocusListener(new FocusChangeHandler());
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
            this.projNameTextField.addFocusListener(new FocusChangeHandler());
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
            this.projVersionTextField.addFocusListener(new FocusChangeHandler());
        }
        return this.projVersionTextField;
    }


    public static void main(String[] args) {
        // NOTE: Needs to run from introduce directory!
        try {
            PropertyConfigurator.configure("." + File.separator + "conf" + File.separator + "introduce"
                + File.separator + "log4j.properties");
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
                    NamespaceType[] createdNs = panel.createNamespaceType(new File("."), "", new MultiEventProgressBar(
                        false));
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
