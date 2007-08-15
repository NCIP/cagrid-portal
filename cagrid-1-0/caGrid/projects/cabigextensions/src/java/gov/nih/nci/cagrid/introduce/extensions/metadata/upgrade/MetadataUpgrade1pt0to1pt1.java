package gov.nih.nci.cagrid.introduce.extensions.metadata.upgrade;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.upgrades.UpgradeException;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.extensions.metadata.common.MetadataExtensionHelper;
import gov.nih.nci.cagrid.introduce.extensions.metadata.constants.MetadataConstants;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.one.ExtensionUpgraderBase;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.service.ContextProperty;
import gov.nih.nci.cagrid.metadata.service.Fault;
import gov.nih.nci.cagrid.metadata.service.InputParameter;
import gov.nih.nci.cagrid.metadata.service.Operation;
import gov.nih.nci.cagrid.metadata.service.OperationFaultCollection;
import gov.nih.nci.cagrid.metadata.service.OperationInputParameterCollection;
import gov.nih.nci.cagrid.metadata.service.Output;
import gov.nih.nci.cagrid.metadata.service.Service;
import gov.nih.nci.cagrid.metadata.service.ServiceContext;
import gov.nih.nci.cagrid.metadata.service.ServiceContextContextPropertyCollection;
import gov.nih.nci.cagrid.metadata.service.ServiceContextOperationCollection;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * MetadataUpgrade1pt0to1pt1 copies metadata descriptions
 * 
 * @author oster
 * @created Apr 9, 2007 11:21:24 AM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class MetadataUpgrade1pt0to1pt1 extends ExtensionUpgraderBase {
    /**
     * Comment for <code>CAGRID_1_0_METADATA_JAR_PREFIX</code>
     */
    private static final String CAGRID_1_0_METADATA_JAR_PREFIX = "caGrid-1.0-metadata";
    protected MetadataExtensionHelper helper;
    protected static Log LOG = LogFactory.getLog(MetadataUpgrade1pt0to1pt1.class.getName());


    /**
     * @param extensionType
     * @param serviceInfo
     * @param servicePath
     * @param fromVersion
     * @param toVersion
     */
    public MetadataUpgrade1pt0to1pt1(ExtensionType extensionType, ServiceInformation serviceInfo, String servicePath,
        String fromVersion, String toVersion) {
        super("MetadataUpgrade1pt0to1pt1", extensionType, serviceInfo, servicePath, fromVersion, toVersion);
        this.helper = new MetadataExtensionHelper(serviceInfo);
    }


    @Override
    protected void upgrade() throws Exception {
        if (this.helper.getExistingServiceMetdata() == null) {
            LOG.info("Unable to locate service metdata; no metadata upgrade will be performed.");
            getStatus().addDescriptionLine("Unable to locate service metdata; no metadata upgrade will be performed.");
            getStatus().setStatus(StatusBase.UPGRADE_OK);
            return;
        }

        upgradeJars();

        // make a copy of the metadata backup
        writeBackupServiceMetadata();

        processService(this.helper.getExistingServiceMetdata().getServiceDescription().getService(),
            getServiceInformation().getServiceDescriptor());
        getStatus().addDescriptionLine("Successfully updated metadata instance.");

        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }


    /**
     * Upgrade the jars which are required for metadata
     */
    private void upgradeJars() throws UpgradeException {
        FileFilter metadataLibFilter = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(".jar") && name.startsWith(CAGRID_1_0_METADATA_JAR_PREFIX)
                    && !name.startsWith(CAGRID_1_0_METADATA_JAR_PREFIX + "-data")
                    && !name.startsWith(CAGRID_1_0_METADATA_JAR_PREFIX + "-security");
            }
        };
        FileFilter newMetadataLibFilter = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(".jar") && name.startsWith(MetadataConstants.METADATA_JAR_PREFIX)
                    && !name.startsWith(MetadataConstants.METADATA_JAR_PREFIX + "-data")
                    && !name.startsWith(MetadataConstants.METADATA_JAR_PREFIX + "-security");
            }
        };
        // locate the old data service libs in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] serviceMetadataLibs = serviceLibDir.listFiles(metadataLibFilter);
        // delete the old libraries
        for (File oldLib : serviceMetadataLibs) {
            oldLib.delete();
            getStatus().addDescriptionLine("caGrid 1.0 library " + oldLib.getName() + " removed");
        }
        // copy new libraries in
        File extLibDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "lib");
        File[] metadataLibs = extLibDir.listFiles(newMetadataLibFilter);
        List<File> outLibs = new ArrayList<File>(metadataLibs.length);
        for (File newLib : metadataLibs) {
            File out = new File(serviceLibDir.getAbsolutePath() + File.separator + newLib.getName());
            try {
                Utils.copyFile(newLib, out);
                getStatus().addDescriptionLine("caGrid 1.1 library " + newLib.getName() + " added");
            } catch (IOException ex) {
                throw new UpgradeException("Error copying new metadata library: " + ex.getMessage(), ex);
            }
            outLibs.add(out);
        }

        // update the Eclipse .classpath file
        File classpathFile = new File(getServicePath() + File.separator + ".classpath");
        File[] outLibArray = new File[metadataLibs.length];
        outLibs.toArray(outLibArray);
        try {
            ExtensionUtilities.syncEclipseClasspath(classpathFile, outLibArray);
            getStatus().addDescriptionLine("Eclipse .classpath file updated");
        } catch (Exception ex) {
            throw new UpgradeException("Error updating Eclipse .classpath file: " + ex.getMessage(), ex);
        }

    }


    public File writeBackupServiceMetadata() throws Exception {
        File backupFile = new File(this.helper.getMetadataAbsoluteFile().getAbsolutePath() + ".OLD");
        FileWriter writer = new FileWriter(backupFile);
        MetadataUtils.serializeServiceMetadata(this.helper.getExistingServiceMetdata(), writer);
        writer.close();
        getStatus().addDescriptionLine(
            "Wrote backup of old metadata file to location:" + backupFile.getAbsolutePath()
                + ".  This can be used to recover any issues, and then deleted.");
        return backupFile;
    }


    protected void processService(Service mdService, ServiceDescription introduceService) {
        introduceService.setDescription(mdService.getDescription());
        if (introduceService.getDescription() == null) {
            introduceService.setDescription("");
        }
        // build a map based on context names (only for lookup)
        Map<String, ServiceContext> contextMap = new HashMap<String, ServiceContext>();
        ServiceContext[] existingServiceContexts = mdService.getServiceContextCollection().getServiceContext();
        if (existingServiceContexts != null) {
            for (ServiceContext context : existingServiceContexts) {
                contextMap.put(context.getName(), context);
            }
        }

        // process services
        for (ServiceType duceService : introduceService.getServices().getService()) {
            // find the existing context
            ServiceContext currContext = contextMap.get(duceService.getName());
            if (currContext == null) {
                // if the context isn't found, can't copy over its metadata, so
                // just ignore
                continue;
            }
            // copy md service description over to Introduce service
            duceService.setDescription(currContext.getDescription());
            if (duceService.getDescription() == null) {
                duceService.setDescription("");
            }

            // process the resource properties
            processResourceProperties(duceService.getResourcePropertiesList(), currContext
                .getContextPropertyCollection());

            // process the methods
            processMethods(duceService.getMethods(), currContext.getOperationCollection());
        }

    }


    /**
     * @param resourcePropertiesList
     * @param contextPropertyCollection
     */
    private void processResourceProperties(ResourcePropertiesListType resourcePropertiesList,
        ServiceContextContextPropertyCollection contextPropertyCollection) {
        if (resourcePropertiesList == null || resourcePropertiesList.getResourceProperty() == null
            || contextPropertyCollection == null || contextPropertyCollection.getContextProperty() == null) {
            // nothing to save
            return;
        }
        // build a map based on rps names (only for lookup)
        Map<String, ContextProperty> propMap = new HashMap<String, ContextProperty>();

        for (ContextProperty prop : contextPropertyCollection.getContextProperty()) {
            propMap.put(prop.getName(), prop);
        }

        for (ResourcePropertyType rp : resourcePropertiesList.getResourceProperty()) {
            // find the existing context
            ContextProperty currProp = propMap.get(rp.getQName().toString());
            if (currProp == null) {
                // nothing to save
                continue;
            }
            rp.setDescription(currProp.getDescription());
            if (rp.getDescription() == null) {
                rp.setDescription("");
            }
        }

    }


    /**
     * @param methods
     * @param operationCollection
     */
    private void processMethods(MethodsType methods, ServiceContextOperationCollection operationCollection) {
        if (methods == null || methods.getMethod() == null || operationCollection == null
            || operationCollection.getOperation() == null) {
            // nothing to save
            return;
        }
        // build a map based on operation names (only for lookup)
        Map<String, Operation> opMap = new HashMap<String, Operation>();
        for (Operation op : operationCollection.getOperation()) {
            opMap.put(op.getName(), op);
        }

        for (MethodType method : methods.getMethod()) {
            // find the existing op
            Operation currOp = opMap.get(method.getName());
            if (currOp == null) {
                // nothing to save
                continue;
            }
            method.setDescription(currOp.getDescription());
            if (method.getDescription() == null) {
                method.setDescription("");
            }

            // process inputs
            processInputs(method.getInputs(), currOp.getInputParameterCollection());

            // process output
            processOutput(method.getOutput(), currOp.getOutput());

            // process faults
            processFaults(method.getExceptions(), currOp.getFaultCollection());
        }
    }


    /**
     * @param exceptions
     * @param faultCollection
     */
    private void processFaults(MethodTypeExceptions exceptions, OperationFaultCollection faultCollection) {
        if (exceptions == null || exceptions.getException() == null || faultCollection == null
            || faultCollection.getFault() == null) {
            // nothing to save
            return;
        }

        Map<String, Fault> faultMap = new HashMap<String, Fault>();
        for (Fault fault : faultCollection.getFault()) {
            faultMap.put(fault.getName(), fault);
        }

        for (MethodTypeExceptionsException exception : exceptions.getException()) {
            Fault currFault = faultMap.get(exception.getName());
            if (currFault == null) {
                // nothing to save
                continue;
            }
            exception.setDescription(currFault.getDescription());
            if (exception.getDescription() == null) {
                exception.setDescription("");
            }
        }
    }


    /**
     * @param output
     * @param output2
     */
    private void processOutput(MethodTypeOutput output, Output mdoutput) {
        if (output == null || mdoutput == null || mdoutput.getUMLClass() == null) {
            // nothing to save
            return;
        }
        output.setDescription(mdoutput.getUMLClass().getDescription());
        if (output.getDescription() == null) {
            output.setDescription("");
        }
    }


    /**
     * @param inputs
     * @param inputParameterCollection
     */
    private void processInputs(MethodTypeInputs inputs, OperationInputParameterCollection inputParameterCollection) {
        if (inputs == null || inputs.getInput() == null || inputParameterCollection == null
            || inputParameterCollection.getInputParameter() == null) {
            // nothing to save
            return;
        }
        // build a map based on fault names (only for lookup)
        Map<String, InputParameter> inputMap = new HashMap<String, InputParameter>();
        for (InputParameter input : inputParameterCollection.getInputParameter()) {
            inputMap.put(input.getName(), input);
        }

        for (MethodTypeInputsInput input : inputs.getInput()) {
            // find the existing
            InputParameter mdInput = inputMap.get(input.getName());
            // md currently doesn't have descriptions on the params, so best
            // guess is the uml description
            if (mdInput == null || mdInput.getUMLClass() == null) {
                // nothing to save
                continue;
            }

            input.setDescription(mdInput.getUMLClass().getDescription());
            if (input.getDescription() == null) {
                input.setDescription("");
            }
        }
    }
}
