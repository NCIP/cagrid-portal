package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.bdt.stubs.BDTDataServicePortType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
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

import org.projectmobius.common.MobiusException;

/** 
 *  BDTFeatureCreator
 *  Provides implementation and methods for BDT query
 * 
 * @author David Ervin
 * 
 * @created Mar 9, 2007 3:45:48 PM
 * @version $Id: BDTFeatureCreator.java,v 1.4 2007-03-20 17:32:30 dervin Exp $ 
 */
public class BDTFeatureCreator extends FeatureCreator {
	
	public static final String BDT_RESOURCE_CREATION_LINE = "BDTResource thisResource = (BDTResource)bdtHome.find(bdtResourceKey);";
	

	public BDTFeatureCreator(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
		super(info, mainService, serviceProps);
	}


	public void addFeature() throws CreationExtensionException {
		ensureBdtExtensionAdded();
		addBdtQueryMethod();
		copyWsEnumerationLibs();
        copyBdtDataServiceSchemas();
	}
    
    
    private void copyBdtDataServiceSchemas() throws CreationExtensionException {
        // locate the data extension schema directory
        String dataExtensionSchemaDir = ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath()
            + File.separator + "data" + File.separator + "schema" + File.separator + "Data";
        // the new service's schema directory
        String serviceSchemaDir = getServiceInformation().getBaseDirectory().getAbsolutePath() 
            + File.separator + "schema" + File.separator + getMainService().getName();
        // copy the BDT handler reference schema
        File bdtRefSchema = new File(dataExtensionSchemaDir + File.separator + "BulkDataHandlerReference.xsd");
        File outSchema = new File(serviceSchemaDir + File.separator + bdtRefSchema.getName());
        try { 
            Utils.copyFile(bdtRefSchema, outSchema);
        } catch (IOException ex) {
            throw new CreationExtensionException("Error copying BDT Handler Reference schema: " + ex.getMessage(), ex);
        }
        // add a namespace type for it
        NamespaceType nsType = null;
        try {
            nsType = CommonTools.createNamespaceType(outSchema.getAbsolutePath(), new File(serviceSchemaDir));
        } catch (MobiusException ex) {
            throw new CreationExtensionException("Error creating namespace type: " + ex.getMessage(), ex);
        }
        getServiceInformation().getNamespaces().setNamespace(
            (NamespaceType[]) Utils.appendToArray(
                getServiceInformation().getNamespaces().getNamespace(), nsType));
        // copy the BDT data service wsdl file
        File bdtDSWsdl = new File(dataExtensionSchemaDir + File.separator + "BDTDataService.wsdl");
        File outWsdl = new File(serviceSchemaDir + File.separator + bdtDSWsdl.getName());
        try {
            Utils.copyFile(bdtDSWsdl, outWsdl);
        } catch (IOException ex) {
            throw new CreationExtensionException("Error copying BDT data service wsdl: " + ex.getMessage(), ex);
        }
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
	
	
	private void addBdtQueryMethod() throws CreationExtensionException {
		// create the BDT query method
		MethodType bdtQueryMethod = new MethodType();
		bdtQueryMethod.setName(DataServiceConstants.BDT_QUERY_METHOD_NAME);
		bdtQueryMethod.setDescription(DataServiceConstants.BDT_QUERY_METHOD_DESCRIPTION);
		// import information
        bdtQueryMethod.setIsImported(true);
        MethodTypeImportInformation bdtImport = new MethodTypeImportInformation();
        bdtImport.setInputMessage(DataServiceConstants.BDT_QUERY_METHOD_INPUT_MESSAGE);
        bdtImport.setOutputMessage(DataServiceConstants.BDT_QUERY_METHOD_OUTPUT_MESSAGE);
        bdtImport.setPackageName(DataServiceConstants.BDT_DATA_SERVICE_PACKAGE_NAME);
        bdtImport.setWsdlFile("BDTDataService.wsdl");
        bdtImport.setNamespace(DataServiceConstants.BDT_DATA_SERVICE_NAMESPACE);
        bdtImport.setPortTypeName(BDTDataServicePortType.class.getSimpleName());
        bdtQueryMethod.setImportInformation(bdtImport);
        // provider information
        /*
        MethodTypeProviderInformation bdtProvider = new MethodTypeProviderInformation();
        bdtProvider.setProviderClass(BDTDataServiceProviderImpl.class.getName());
        bdtQueryMethod.setProviderInformation(bdtProvider);
        */
		// input of CQL query
		MethodTypeInputs inputs = new MethodTypeInputs();
		MethodTypeInputsInput cqlInput = new MethodTypeInputsInput();
		cqlInput.setQName(DataServiceConstants.CQL_QUERY_QNAME);
		cqlInput.setName(DataServiceConstants.QUERY_METHOD_PARAMETER_NAME);
		cqlInput.setDescription(DataServiceConstants.QUERY_METHOD_PARAMETER_DESCRIPTION);
		cqlInput.setIsArray(false);
		inputs.setInput(new MethodTypeInputsInput[] {cqlInput});
		bdtQueryMethod.setInputs(inputs);
		// output of BDT client handle
		MethodTypeOutput bdtHandleOutput = new MethodTypeOutput();
		bdtHandleOutput.setQName(DataServiceConstants.BDT_HANDLER_REFERENCE_QNAME);
		bdtHandleOutput.setIsArray(false);
		bdtHandleOutput.setIsClientHandle(Boolean.TRUE);
		bdtHandleOutput.setClientHandleClass(DataServiceConstants.BDT_HANDLER_CLIENT_CLASSNAME);
		bdtQueryMethod.setOutput(bdtHandleOutput);
		
		// add the method to the service
		getMainService().getMethods().setMethod(
			(MethodType[]) Utils.appendToArray(
				getMainService().getMethods().getMethod(), bdtQueryMethod));
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
