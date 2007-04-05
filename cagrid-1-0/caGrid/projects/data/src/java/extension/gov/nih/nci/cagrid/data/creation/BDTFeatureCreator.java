package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Properties;

/** 
 *  BDTFeatureCreator
 *  Provides implementation, methods, classes, etc for BDT query
 * 
 * @author David Ervin
 * 
 * @created Apr 4, 2007 9:56:09 AM
 * @version $Id: BDTFeatureCreator.java,v 1.6 2007-04-05 13:43:36 dervin Exp $ 
 */
public class BDTFeatureCreator extends FeatureCreator {

   public BDTFeatureCreator(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
        super(info, mainService, serviceProps);
    }


    public void addFeature() throws CreationExtensionException {
        ensureBdtExtensionAdded();
        copySchemaAndWsdl();
        copyWsEnumerationLibs();
        addBdtQueryMethod();
    }
    
    
    private void copySchemaAndWsdl() throws CreationExtensionException {
        // the BDT Data Service wsdl
        copySchemaToService("BDTDataService.wsdl");
        // the Bulk Data Handler wsdl
        copySchemaToService("BulkDataHandler.wsdl");
        // the Bulk Data Handler reference schema
        File bdtRefSchema = copySchemaToService("BulkDataHandlerReference.xsd");
        // the Bulk Data Transfer Service metadata schema
        File bdtMetadataSchema = copySchemaToService("BulkDataTransferServiceMetadata.xsd");
        
        // create namespace type for BDT reference schema
        NamespaceType bdtRefNsType = null;
        try {
            bdtRefNsType = CommonTools.createNamespaceType(
                bdtRefSchema.getAbsolutePath(), bdtRefSchema.getAbsoluteFile().getParentFile());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CreationExtensionException(
                "Error creating BDT Reference namespace type: " + ex.getMessage(), ex);
        }
        // create namespace type for BDT metadata
        NamespaceType bdtMetadataNsType = null;
        try {
            bdtMetadataNsType = CommonTools.createNamespaceType(
                bdtMetadataSchema.getAbsolutePath(), bdtMetadataSchema.getAbsoluteFile().getParentFile());
        } catch (Exception ex) {
            throw new CreationExtensionException(
                "Error creating BDT metadata namespace type: " + ex.getMessage(), ex);
        }
        
        // add namespaces to the service information
        NamespaceType[] namespaces = getServiceInformation().getNamespaces().getNamespace();
        namespaces = (NamespaceType[]) Utils.appendToArray(namespaces, bdtRefNsType);
        namespaces = (NamespaceType[]) Utils.appendToArray(namespaces, bdtMetadataNsType);
        getServiceInformation().getNamespaces().setNamespace(namespaces);
    }
    
    
    private void addBdtQueryMethod() throws CreationExtensionException {
        // create the method
        MethodType bdtQueryMethod = new MethodType();
        bdtQueryMethod.setName(DataServiceConstants.BDT_QUERY_METHOD_NAME);
        bdtQueryMethod.setDescription(DataServiceConstants.BDT_QUERY_METHOD_DESCRIPTION);
        // set the input to be a CQL query
        MethodTypeInputs bdtQueryInputs = new MethodTypeInputs();
        MethodTypeInputsInput queryInputParameter = new MethodTypeInputsInput();
        queryInputParameter.setName(DataServiceConstants.QUERY_METHOD_PARAMETER_NAME);
        queryInputParameter.setDescription(DataServiceConstants.QUERY_METHOD_PARAMETER_DESCRIPTION);
        queryInputParameter.setIsArray(false);
        queryInputParameter.setQName(DataServiceConstants.CQL_QUERY_QNAME);
        bdtQueryInputs.setInput(new MethodTypeInputsInput[] {queryInputParameter});
        bdtQueryMethod.setInputs(bdtQueryInputs);
        // set the output to be a BulkDataHandler reference
        MethodTypeOutput bdtQueryOutput = new MethodTypeOutput();
        bdtQueryOutput.setIsArray(false);
        bdtQueryOutput.setIsClientHandle(Boolean.FALSE);
        bdtQueryOutput.setQName(DataServiceConstants.BDT_HANDLER_REFERENCE_QNAME);
        bdtQueryMethod.setOutput(bdtQueryOutput);
        // add malformed query and query processing exceptions
        MethodTypeExceptions bdtQueryExceptions = new MethodTypeExceptions();
        MethodTypeExceptionsException qpException = new MethodTypeExceptionsException(
            DataServiceConstants.QUERY_PROCESSING_EXCEPTION_DESCRIPTION,
            DataServiceConstants.QUERY_PROCESSING_EXCEPTION_NAME, 
            DataServiceConstants.QUERY_PROCESSING_EXCEPTION_QNAME);
        MethodTypeExceptionsException mqException = new MethodTypeExceptionsException(
            DataServiceConstants.MALFORMED_QUERY_EXCEPTION_DESCRIPTION,
            DataServiceConstants.MALFORMED_QUERY_EXCEPTION_NAME, 
            DataServiceConstants.MALFORMED_QUERY_EXCEPTION_QNAME);
        bdtQueryExceptions.setException(new MethodTypeExceptionsException[] {qpException, mqException});
        bdtQueryMethod.setExceptions(bdtQueryExceptions);
        // import information should indicate this method came from the BDT Data Service wsdl
        MethodTypeImportInformation bdtQueryImport = new MethodTypeImportInformation();
        bdtQueryImport.setFromIntroduce(Boolean.FALSE);
        bdtQueryImport.setWsdlFile("BDTDataService.wsdl");
        bdtQueryImport.setPackageName(DataServiceConstants.BDT_DATA_SERVICE_PACKAGE_NAME);
        bdtQueryImport.setPortTypeName("BDTDataServicePortType");
        bdtQueryImport.setNamespace(DataServiceConstants.BDT_DATA_SERVICE_NAMESPACE);
        bdtQueryImport.setInputMessage(DataServiceConstants.BDT_QUERY_METHOD_INPUT_MESSAGE);
        bdtQueryImport.setOutputMessage(DataServiceConstants.BDT_QUERY_METHOD_OUTPUT_MESSAGE);
        bdtQueryImport.setFromIntroduce(Boolean.TRUE);
        bdtQueryMethod.setIsImported(true);
        bdtQueryMethod.setImportInformation(bdtQueryImport);
        // add the method to the main service
        getMainService().getMethods().setMethod(
            (MethodType[]) Utils.appendToArray(
                getMainService().getMethods().getMethod(), bdtQueryMethod));
    }
    
    
    private void copyWsEnumerationLibs() throws CreationExtensionException {
        File extensionLibDir = new File(ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath() + File.separator + "lib");
        File[] sourceLibs = extensionLibDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                if (name.endsWith(".jar")) {
                    return name.endsWith("_enum.jar") || name.indexOf("wsEnum") != -1
                        || name.indexOf("cabigextensions-stubs") != -1;
                }
                return false;
            }
        });
        File serviceLibDir = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
            + File.separator + "lib");
        File[] outputLibs = new File[sourceLibs.length];
        try {
            for (int i = 0; i < sourceLibs.length; i++) {
                outputLibs[i] = new File(serviceLibDir.getAbsolutePath() 
                    + File.separator + sourceLibs[i].getName());
                Utils.copyFile(sourceLibs[i], outputLibs[i]);
            }
        } catch (IOException ex) {
            throw new CreationExtensionException("Error copying WS-Enumeration library: " + ex.getMessage(), ex);
        }
        
        // sync up the Eclipse classpath
        File classpathFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
            + File.separator + ".classpath");
        if (classpathFile.exists()) {
            try {
                ExtensionUtilities.syncEclipseClasspath(classpathFile, outputLibs);
            } catch (Exception ex) {
                throw new CreationExtensionException("Error syncing Eclipse .classpath file: " + ex.getMessage(), ex);
            }
        }
    }
    
    
    private File copySchemaToService(String schemaName) throws CreationExtensionException {
        String extensionSchemaDir = ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath() 
            + File.separator + "data" + File.separator + "schema" + File.separator + "Data";
        String serviceSchemaDir = getServiceInformation().getBaseDirectory().getAbsolutePath() 
            + File.separator + "schema" + File.separator + getMainService().getName();

        // the BDT Data Service wsdl
        File schemaIn = new File(extensionSchemaDir + File.separator + schemaName);
        File schemaOut = new File(serviceSchemaDir + File.separator + schemaIn.getName());
        try {
            Utils.copyFile(schemaIn, schemaOut);
        } catch (IOException ex) {
            throw new CreationExtensionException("Error copying required schema (" 
                + schemaIn.getName() + "): " + ex.getMessage(), ex);
        }
        return schemaOut;
    }
    
    
    /**
     * Verifies the BDT extension is installed and if it needs to be included
     * in this service, adds it.
     * 
     * @throws CreationExtensionException
     */
    private void ensureBdtExtensionAdded() throws CreationExtensionException {
        ExtensionDescription bdtDescription = ExtensionsLoader.getInstance().getExtension("bdt");
        if (bdtDescription == null) {
            throw new CreationExtensionException("BDT Extension is not installed!");
        }
        
        boolean bdtExtensionUsed = false;
        ExtensionType[] usedExtensions = getServiceInformation().getExtensions().getExtension();
        for (int i = 0; i < usedExtensions.length; i++) {
            if (usedExtensions[i].getName().equals("bdt")) {
                bdtExtensionUsed = true;
                break;
            }
        }
        
        if (!bdtExtensionUsed) {
            ExtensionType bdtExtension = new ExtensionType();
            bdtExtension.setName(bdtDescription.getServiceExtensionDescription().getName());
            bdtExtension.setExtensionType(bdtDescription.getExtensionType());
            bdtExtension.setVersion(bdtDescription.getVersion());
            ExtensionType[] moreExtensions = new ExtensionType[usedExtensions.length + 1];
            moreExtensions[0] = bdtExtension;
            System.arraycopy(usedExtensions, 0, moreExtensions, 1, usedExtensions.length);
            getServiceInformation().getExtensions().setExtension(moreExtensions);
        }
    }
}
