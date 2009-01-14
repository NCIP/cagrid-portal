package org.cagrid.gme.discoverytools;

import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.configuration.NamespaceReplacementPolicy;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gme.client.GlobalModelExchangeClient;
import org.cagrid.gme.domain.XMLSchemaNamespace;
import org.cagrid.gme.stubs.types.NoSuchNamespaceExistsFault;


public abstract class GMETypeSelectionComponentBase extends NamespaceTypeDiscoveryComponent {

    private static final Log logger = LogFactory.getLog(GMETypeSelectionComponentBase.class);


    private Map<XMLSchemaNamespace, File> cacheSchemas(File dir, XMLSchemaNamespace namespace)
        throws NoSuchNamespaceExistsFault, RemoteException, IOException, Exception {
        GlobalModelExchangeClient client;
        client = new GlobalModelExchangeClient(getGMEURL());

        return client.cacheSchemas(namespace, dir);
    }


    protected abstract String getGMEURL() throws Exception;


    protected abstract XMLSchemaNamespace getCurrentSchemaNamespace();


    @Override
    public NamespaceType[] createNamespaceType(File schemaDestinationDir, NamespaceReplacementPolicy replacementPolicy,
        MultiEventProgressBar progress) {
        XMLSchemaNamespace selectedNS = getCurrentSchemaNamespace();
        if (selectedNS == null) {
            String error = "No valid namespace was selected.";
            logger.error(error);
            addError(error);

            return null;
        }

        if (!selectedNS.toString().equals(IntroduceConstants.W3CNAMESPACE)) {
            try {

                int startEventID = progress.startEvent("Contacting GME for schemas...");
                // TODO change this to pass a Map<URI, File> of the existing
                // namespaces if the replacement policy is IGNORE (such that
                // those schemas can be reused)

                Map<XMLSchemaNamespace, File> cachedSchemas = null;

                try {
                    cachedSchemas = cacheSchemas(schemaDestinationDir, selectedNS);
                } catch (NoSuchNamespaceExistsFault e) {
                    String error = "Namespace (" + selectedNS + ") does not exist in the GME.";
                    logger.error(error, e);
                    addError(error);
                    return null;
                }

                progress.stopEvent(startEventID, "Successfully retrieved " + cachedSchemas.size() + " schemas.");

                NamespaceType[] types = null;

                // check that it is ok to apply the changes
                for (XMLSchemaNamespace ns : cachedSchemas.keySet()) {
                    if (namespaceAlreadyExists(ns.toString())) {
                        if (replacementPolicy.equals(NamespaceReplacementPolicy.IGNORE)) {

                            String error = "Namespace ("
                                + ns
                                + ") already exists, and policy was to ingore, but this is not supported by this type selection component.  Change the setting to REPLACE to avoid this error.";
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

                types = new NamespaceType[cachedSchemas.size()];
                int typesIndex = 0;
                // now walk again and actually create the types
                for (XMLSchemaNamespace ns : cachedSchemas.keySet()) {
                    File schemaFile = cachedSchemas.get(ns);
                    types[typesIndex++] = NamespaceTools.createNamespaceTypeForFile(schemaFile.getCanonicalPath(),
                        schemaDestinationDir);
                }

                return types;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e);
                addError(e.getMessage());
                return null;
            } finally {
                progress.stopAll("");
            }
        } else {
            return new NamespaceType[0];
        }
    }


    public GMETypeSelectionComponentBase(DiscoveryExtensionDescriptionType descriptor, NamespacesType currentNamespaces) {
        super(descriptor, currentNamespaces);
    }

}