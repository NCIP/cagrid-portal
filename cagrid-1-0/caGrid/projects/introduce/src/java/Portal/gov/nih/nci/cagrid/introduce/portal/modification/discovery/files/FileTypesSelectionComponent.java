package gov.nih.nci.cagrid.introduce.portal.modification.discovery.files;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;


public class FileTypesSelectionComponent extends NamespaceTypeDiscoveryComponent {

    public String currentNamespace = null;

    public String currentFile = null;

    private JLabel namespaceLabel = null;

    public String filterType = null;

    private JButton browseButton = null;

    private JTextField namespaceText = null;

    private JTextField filenameText = null;


    /**
     * This method initializes
     */
    public FileTypesSelectionComponent(DiscoveryExtensionDescriptionType descriptor, NamespacesType types) {
        super(descriptor, types);
        initialize();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints2.gridx = 1;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridy = 0;
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints9.gridy = 1;
        gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints9.gridx = 0;
        this.namespaceLabel = new JLabel();
        this.namespaceLabel.setText("Namespace");
        this.setLayout(new GridBagLayout());
        this.add(this.namespaceLabel, gridBagConstraints9);
        this.add(getBrowseButton(), gridBagConstraints);
        this.add(getNamespaceText(), gridBagConstraints1);
        this.add(getFilenameText(), gridBagConstraints2);
    }


    /**
     * This method initializes browseButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getBrowseButton() {
        if (this.browseButton == null) {
            this.browseButton = new JButton();
            this.browseButton.setText("Browse");
            this.browseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String selectedFilename = ResourceManager.promptFile(null, FileFilters.XSD_FILTER);
                        if (selectedFilename != null) {
                            FileTypesSelectionComponent.this.currentFile = new File(selectedFilename).getAbsolutePath();
                            getFilenameText().setText(FileTypesSelectionComponent.this.currentFile);
                            Document doc = XMLUtilities
                                .fileNameToDocument(FileTypesSelectionComponent.this.currentFile);
                            FileTypesSelectionComponent.this.currentNamespace = doc.getRootElement().getAttributeValue(
                                "targetNamespace");
                            getNamespaceText().setText(FileTypesSelectionComponent.this.currentNamespace);
                        }
                    } catch (Exception ex) {
                        ErrorDialog.showErrorDialog("Please make sure the file is a valid XML Schema", ex);
                    }
                }
            });
        }
        return this.browseButton;
    }


    @Override
    public NamespaceType[] createNamespaceType(File schemaDestinationDir, String namespaceExistsPolicy,
        MultiEventProgressBar progress) {
        try {
            if (this.currentNamespace == null) {
                addError("Please select a schema file.");
                return null;
            }
            if (namespaceAlreadyExists(this.currentNamespace) && namespaceExistsPolicy.equals(ERROR)) {
                addError("Namespace already exists.");
                return null;
            }

            boolean result = checkAgainstPolicy(this.currentFile, new HashSet(), namespaceExistsPolicy);
            if (result == false) {
                return null;
            }

            List namespaces = new ArrayList();

            String currentFileName = (new File(this.currentFile)).getName();
            NamespaceType root = new NamespaceType();
            // set the package name
            String packageName = CommonTools.getPackageName(this.currentNamespace);
            root.setPackageName(packageName);
            root.setNamespace(this.currentNamespace);
            root.setLocation("./" + currentFileName);

            namespaces.add(root);

            ExtensionTools.setSchemaElements(root, XMLUtilities.fileNameToDocument(this.currentFile));
            Set storedSchemas = new HashSet();
            copySchemas(this.currentFile, schemaDestinationDir, new HashSet(), storedSchemas, namespaceExistsPolicy);
            Iterator schemaFileIter = storedSchemas.iterator();
            while (schemaFileIter.hasNext()) {
                File storedSchemaFile = new File((String) schemaFileIter.next());
                if (!storedSchemaFile.getName().equals(currentFileName)) {
                    NamespaceType nsType = CommonTools.createNamespaceType(storedSchemaFile.getAbsolutePath(),
                        schemaDestinationDir);
                    namespaces.add(nsType);
                }
            }
            NamespaceType[] types = new NamespaceType[namespaces.size()];
            namespaces.toArray(types);
            return types;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private boolean checkAgainstPolicy(String fileName, Set visitedSchemas, String namespaceExistsPolicy) {
        try {
            File schemaFile = new File(fileName);
            // mark the schema as visited
            visitedSchemas.add(schemaFile.getCanonicalPath());
            // look for imports
            Document schema = XMLUtilities.fileNameToDocument(schemaFile.getCanonicalPath());
            List importEls = schema.getRootElement().getChildren("import",
                schema.getRootElement().getNamespace(IntroduceConstants.W3CNAMESPACE));
            if (importEls != null) {
                for (int i = 0; i < importEls.size(); i++) {
                    org.jdom.Element importEl = (org.jdom.Element) importEls.get(i);
                    String location = importEl.getAttributeValue("schemaLocation");
                    if (location != null) {
                        String namespace = importEl.getAttributeValue("namespace");
                        if (namespace != null) {
                            // see if namespace already exists if so check
                            // property
                            // to see if supposed to error;
                            if (namespaceAlreadyExists(namespace)) {
                                if (namespaceExistsPolicy.equals(ERROR)) {
                                    addError("Imported namespace already exists: "
                                        + namespace
                                        + ". \nIf you want this schema to be overwriten please change the policy to \"ignore\" in the introduce preferences menu");
                                    return false;
                                }
                            }

                        }

                        File currentPath = schemaFile.getCanonicalFile().getParentFile();
                        if (!schemaFile.equals(new File(currentPath.getCanonicalPath() + File.separator + location))) {
                            File importedSchema = new File(currentPath + File.separator + location);
                            if (!visitedSchemas.contains(importedSchema.getCanonicalPath())) {
                                // only copy schemas not yet visited
                                boolean result = checkAgainstPolicy(importedSchema.getCanonicalPath(), visitedSchemas,
                                    namespaceExistsPolicy);
                                if (result == false) {
                                    return false;
                                }
                            }
                        } else {
                            System.err.println("WARNING: Schema is importing itself. " + schemaFile);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            addError(e.getMessage());
            return false;
        }
        return true;
    }


    public void copySchemas(String fileName, File copyToDirectory, Set visitedSchemas, Set storedSchemas,
        String namespaceExistsPolicy) throws Exception {
        File schemaFile = new File(fileName);
        Document schema = XMLUtilities.fileNameToDocument(schemaFile.getCanonicalPath());
        String namespaceURI = schema.getRootElement().getAttribute("targetNamespace").getValue();
        if (namespaceAlreadyExists(namespaceURI) && namespaceExistsPolicy.equals(IGNORE)) {
            // do nothing just ignore.....
        } else {
            System.out.println("Copying schema " + fileName + " to " + copyToDirectory.getCanonicalPath());
            File outFile = new File(copyToDirectory.getCanonicalPath() + File.separator + schemaFile.getName());
            Utils.copyFile(schemaFile, outFile);
            storedSchemas.add(outFile.getAbsolutePath());
            // mark the schema as visited
            visitedSchemas.add(schemaFile.getCanonicalPath());
        }

        // look for imports
        List importEls = schema.getRootElement().getChildren("import",
            schema.getRootElement().getNamespace(IntroduceConstants.W3CNAMESPACE));
        if (importEls != null) {
            for (int i = 0; i < importEls.size(); i++) {
                org.jdom.Element importEl = (org.jdom.Element) importEls.get(i);
                String location = importEl.getAttributeValue("schemaLocation");
                if (location != null) {
                    File currentPath = schemaFile.getCanonicalFile().getParentFile();
                    if (!schemaFile.equals(new File(currentPath.getCanonicalPath() + File.separator + location))) {
                        File importedSchema = new File(currentPath + File.separator + location);
                        if (!visitedSchemas.contains(importedSchema.getCanonicalPath())) {
                            // only copy schemas not yet visited
                            copySchemas(importedSchema.getCanonicalPath(), new File(copyToDirectory.getCanonicalFile()
                                + File.separator + location).getParentFile(), visitedSchemas, storedSchemas,
                                namespaceExistsPolicy);
                        }
                    } else {
                        System.err.println("WARNING: Schema is importing itself. " + schemaFile);
                    }
                }
            }
        }
    }


    /**
     * This method initializes namespaceText
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getNamespaceText() {
        if (this.namespaceText == null) {
            this.namespaceText = new JTextField();
            this.namespaceText.setEditable(false);
        }
        return this.namespaceText;
    }


    /**
     * This method initializes filenameText
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getFilenameText() {
        if (this.filenameText == null) {
            this.filenameText = new JTextField();
            this.filenameText.setEnabled(true);
            this.filenameText.setEditable(false);
        }
        return this.filenameText;
    }

} // @jve:decl-index=0:visual-constraint="16,10"
