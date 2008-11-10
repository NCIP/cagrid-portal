package org.cagrid.gme.discoverytools;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.configuration.NamespaceReplacementPolicy;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ConfigurationUtil;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gme.client.GlobalModelExchangeClient;
import org.cagrid.gme.domain.XMLSchemaNamespace;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;


@SuppressWarnings("serial")
public class GMETypeSelectionComponent extends NamespaceTypeDiscoveryComponent {

    private static final Log logger = LogFactory.getLog(GMETypeSelectionComponent.class);

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
            this.gmePanel = new GMESchemaLocatorPanel();
        }
        return this.gmePanel;
    }


    @Override
    public NamespaceType[] createNamespaceType(File schemaDestinationDir, NamespaceReplacementPolicy replacementPolicy,
        MultiEventProgressBar progress) {
        XMLSchemaNamespace selectedNS = getGmePanel().getSelectedSchemaNamespace();

        if (!selectedNS.toString().equals(IntroduceConstants.W3CNAMESPACE)) {
            try {

                int startEventID = progress.startEvent("Contacting GME for schemas...");
                Map<XMLSchemaNamespace, File> cachedSchemas = cacheSchemas(schemaDestinationDir, selectedNS);
                progress.stopEvent(startEventID, "Successfully retrieved " + cachedSchemas.size() + " schemas.");

                NamespaceType[] types = null;

                // check that it is ok to apply the changes
                for (XMLSchemaNamespace ns : cachedSchemas.keySet()) {
                    if (namespaceAlreadyExists(ns.toString())) {
                        if (replacementPolicy.equals(NamespaceReplacementPolicy.IGNORE)) {
                            String error = "Namespace ("
                                + ns
                                + ") already exists, and policy was to ingore, but this is not supported by the GME type selection component.  Change the setting to REPLACE to avoid this error.";
                            logger.error(error);
                            addError(error);
                            // TODO: should probably roll back all the files
                            // that where written, but that would leave included
                            // schemas behind if I just looped those found in
                            // the map

                            return null;
                        } else if (replacementPolicy.equals(NamespaceReplacementPolicy.ERROR)) {
                            String error = "Namespace ("
                                + ns
                                + ") already exists, and policy was to error.  Change the setting to REPLACE to avoid this error.";
                            logger.error(error);
                            addError(error);
                            // TODO: should probably roll back all the files
                            // that where written, but that would leave included
                            // schemas behind if I just looped those found in
                            // the map
                            return null;
                        }
                    }
                }

                // now walk again and actually create the types
                for (XMLSchemaNamespace ns : cachedSchemas.keySet()) {
                    File schemaFile = cachedSchemas.get(ns);
                    createNamespaceTypeForFile(schemaFile.getCanonicalPath(), schemaDestinationDir);

                }

                // Namespace ns = (Namespace) importedNsIter.next();
                // if (ns.getRaw().equals(selectedNS.getRaw())) {
                // ImportInfo importInfo = new ImportInfo(ns);
                // String filename = importInfo.getFileName();
                // types =
                // createNamespaceTypeFromFiles(tmpPath.getAbsolutePath
                // () + File.separator + filename,
                // ns, schemaDestinationDir, replacementPolicy);
                // }

                return types;
            } catch (Exception e) {
                logger.error(e);
                return null;
            } finally {
                progress.stopAll("");
            }
        } else {
            return new NamespaceType[0];
        }
    }


    private static Map<XMLSchemaNamespace, File> cacheSchemas(File dir, XMLSchemaNamespace namespace) throws Exception {
        GlobalModelExchangeClient client = new GlobalModelExchangeClient(ConfigurationUtil.getGlobalExtensionProperty(
            GMETypeSelectionComponent.GME_URL).getValue());
        return client.cacheSchemas(namespace, dir);
    }


    public static NamespaceType createNamespaceTypeForFile(String xsdFilename, File serviceSchemaDir) throws Exception {
        NamespaceType namespaceType = new NamespaceType();
        File xsdFile = new File(xsdFilename);
        String location;
        try {
            location = "./" + Utils.getRelativePath(serviceSchemaDir, xsdFile).replace('\\', '/');
        } catch (IOException e) {
            logger.error(e);
            throw new Exception("Problem getting relative path of XSD.", e);
        }
        namespaceType.setLocation(location);
        Document schemaDoc = XMLUtilities.fileNameToDocument(xsdFilename);

        String rawNamespace = schemaDoc.getRootElement().getAttributeValue("targetNamespace");
        String packageName = CommonTools.getPackageName(rawNamespace);
        namespaceType.setPackageName(packageName);
        namespaceType.setNamespace(rawNamespace);

        // get the types from the root document itself
        List<SchemaElementType> schemaTypesList = new ArrayList<SchemaElementType>();
        createSchemaElementsForDocument(schemaDoc, schemaTypesList);

        // extract any types from included documents
        List<Element> includeElements = schemaDoc.getRootElement().getChildren("include",
            Namespace.getNamespace(IntroduceConstants.W3CNAMESPACE));
        for (Element include : includeElements) {
            String includeLocation = include.getAttributeValue("schemaLocation");
            if (includeLocation == null || includeLocation.length() <= 0) {
                throw new Exception(
                    "Schema does not appear to be valid: an include does not contain a schemaLocation attribute.");
            }
            // load the included document and load those type too
            Document includedDoc = XMLUtilities.fileNameToDocument(xsdFilename);
            createSchemaElementsForDocument(includedDoc, schemaTypesList);
        }

        // merge all the types
        SchemaElementType[] schemaTypes = new SchemaElementType[schemaTypesList.size()];
        schemaTypes = schemaTypesList.toArray(schemaTypes);

        namespaceType.setSchemaElement(schemaTypes);
        return namespaceType;
    }


    private static void createSchemaElementsForDocument(Document schemaDoc, List<SchemaElementType> schemaTypesList)
        throws Exception {
        List<Element> elementTypes = schemaDoc.getRootElement().getChildren("element",
            Namespace.getNamespace(IntroduceConstants.W3CNAMESPACE));

        for (int i = 0; i < elementTypes.size(); i++) {
            Element element = elementTypes.get(i);
            SchemaElementType type = new SchemaElementType();
            String elementName = element.getAttributeValue("name");
            if (elementName == null || elementName.length() <= 0) {
                throw new Exception("Schema does not appear to be valid: an element does not contain a name attribute");
            }
            type.setType(elementName);
            schemaTypesList.add(type);
        }
    }

}
