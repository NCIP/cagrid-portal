package gov.nih.nci.cagrid.introduce.portal.modification.discovery.gme;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.configuration.NamespaceReplacementPolicy;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ConfigurationUtil;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.gme.GMESchemaLocatorPanel;
import gov.nih.nci.cagrid.introduce.portal.extension.tools.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;


/**
 * GMETypeExtractionPanel
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jul 7, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class GMETypeSelectionComponent extends NamespaceTypeDiscoveryComponent {
    
    private static final Logger logger = Logger.getLogger(GMETypeSelectionComponent.class);
    
    public static String GME_URL = "GME_URL";
    public static String TYPE = "GME";

    private GMESchemaLocatorPanel gmePanel = null;


    public GMETypeSelectionComponent(DiscoveryExtensionDescriptionType descriptor, NamespacesType types) {
        super(descriptor, types);
        initialize();
        this.getGmePanel().discoverFromGME();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints4.gridwidth = 1;
        gridBagConstraints4.weightx = 1.0D;
        gridBagConstraints4.weighty = 1.0D;
        gridBagConstraints4.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.add(getGmePanel(), gridBagConstraints4);
    }


    /**
     * This method initializes gmePanel
     * 
     * @return javax.swing.JPanel
     */
    private GMESchemaLocatorPanel getGmePanel() {
        if (this.gmePanel == null) {
            this.gmePanel = new GMESchemaLocatorPanel(false);
        }
        return this.gmePanel;
    }


    @Override
    public NamespaceType[] createNamespaceType(File schemaDestinationDir, NamespaceReplacementPolicy replacementPolicy,
        MultiEventProgressBar progress) {
        Namespace selectedNS = getGmePanel().getSelectedSchemaNamespace();
        if (!selectedNS.getRaw().equals(IntroduceConstants.W3CNAMESPACE)) {
            try {
                if (selectedNS != null) {
                    File tmpPath = new File("tmp" + File.separator + "gmeCache");
                    tmpPath.mkdirs();

                    List importedNamespaces = cacheSchema(tmpPath, selectedNS.getRaw());
                    Iterator importedNsIter = importedNamespaces.iterator();
                    NamespaceType[] types = null;
                    while (importedNsIter.hasNext()) {
                        Namespace ns = (Namespace) importedNsIter.next();
                        if (ns.getRaw().equals(selectedNS.getRaw())) {
                            ImportInfo importInfo = new ImportInfo(ns);
                            String filename = importInfo.getFileName();
                            types = createNamespaceTypeFromFiles(tmpPath.getAbsolutePath() + File.separator + filename,
                                ns, schemaDestinationDir, replacementPolicy);
                        }
                    }
                    Utils.deleteDir(tmpPath);
                    return types;
                } else {
                    return null;
                }
            } catch (Exception e) {
                logger.error(e);
                return null;
            }
        } else {
            return new NamespaceType[0];
        }
    }


    private List cacheSchema(File dir, String namespace) throws Exception {
        GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
        XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
            ConfigurationUtil.getGlobalExtensionProperty(GMETypeSelectionComponent.GME_URL).getValue());
        return handle.cacheSchema(new Namespace(namespace), dir);
    }


    public NamespaceType[] createNamespaceTypeFromFiles(String startingSchema, Namespace startingNamespace,
        File schemaDestinationDir, NamespaceReplacementPolicy replacementPolicy) {
        try {
            if (namespaceAlreadyExists(startingNamespace.getRaw()) && replacementPolicy.equals(NamespaceReplacementPolicy.ERROR)) {
                addError("Namespace already exists.");
                return null;
            }

            boolean result = checkAgainstPolicy(startingSchema, new HashSet(), replacementPolicy);
            if (result == false) {
                return null;
            }

            List namespaces = new ArrayList();

            String currentFileName = (new File(startingSchema)).getName();
            NamespaceType root = new NamespaceType();
            // set the package name
            String packageName = CommonTools.getPackageName(startingNamespace.getRaw());
            root.setPackageName(packageName);
            root.setNamespace(startingNamespace.getRaw());
            root.setLocation("./" + currentFileName);

            namespaces.add(root);

            ExtensionTools.setSchemaElements(root, XMLUtilities.fileNameToDocument(startingSchema));
            Set storedSchemas = new HashSet();
            copySchemas(startingSchema, schemaDestinationDir, new HashSet(), storedSchemas, replacementPolicy);
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


    private boolean checkAgainstPolicy(String fileName, Set visitedSchemas, NamespaceReplacementPolicy replacementPolicy) {
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
                                if (replacementPolicy.equals(NamespaceReplacementPolicy.ERROR)) {
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
                                    replacementPolicy);
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
        NamespaceReplacementPolicy replacementPolicy) throws Exception {
        File schemaFile = new File(fileName);
        Document schema = XMLUtilities.fileNameToDocument(schemaFile.getCanonicalPath());
        String namespaceURI = schema.getRootElement().getAttribute("targetNamespace").getValue();
        if (namespaceAlreadyExists(namespaceURI) && replacementPolicy.equals(NamespaceReplacementPolicy.IGNORE)) {
            // do nothing just ignore.....
        } else {
            logger.debug("Copying schema " + fileName + " to " + copyToDirectory.getCanonicalPath());
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
                                replacementPolicy);
                        }
                    } else {
                        System.err.println("WARNING: Schema is importing itself. " + schemaFile);
                    }
                }
            }
        }
    }
}
