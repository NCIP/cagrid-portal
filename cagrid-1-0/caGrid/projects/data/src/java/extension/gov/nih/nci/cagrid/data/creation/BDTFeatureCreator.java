package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.util.Properties;

import javax.xml.namespace.QName;

/** 
 *  BDTFeatureCreator
 *  Provides implementation and methods for BDT query
 * 
 * @author David Ervin
 * 
 * @created Mar 9, 2007 3:45:48 PM
 * @version $Id: BDTFeatureCreator.java,v 1.2 2007-03-12 17:00:39 dervin Exp $ 
 */
public class BDTFeatureCreator extends FeatureCreator {
	
	public static final String BDT_RESOURCE_CREATION_LINE = "BDTResource thisResource = (BDTResource)bdtHome.find(bdtResourceKey);";
	

	public BDTFeatureCreator(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
		super(info, mainService, serviceProps);
	}


	public void addFeature() throws CreationExtensionException {
		System.out.println("ADDING BDT SERVICE");
		ensureBdtExtensionAdded();
		addBdtQueryMethod();
	}
	
	
	private void addBdtQueryMethod() throws CreationExtensionException {
		// create the BDT query method
		MethodType bdtQueryMethod = new MethodType();
		bdtQueryMethod.setName(DataServiceConstants.BDT_QUERY_METHOD_NAME);
		bdtQueryMethod.setDescription(DataServiceConstants.BDT_QUERY_METHOD_DESCRIPTION);
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
		// find the BDT handler service
		ServiceType[] services = getServiceInformation().getServices().getService();
		ServiceType bdtService = null;
		for (int i = 0; i < services.length; i++) {
			String serviceName = services[i].getName();
			String packageName = services[i].getPackageName();
			if (serviceName.endsWith("BulkDataHandler") && packageName.endsWith(".bdt")) {
				bdtService = services[i];
				break;
			}
		}
		if (bdtService == null) {
			throw new CreationExtensionException("BDT Sub-Service Not Found!");
		}
		QName bdtClientHandleQName = new QName(bdtService.getNamespace() + "/types",
			bdtService.getName() + "Reference");
		bdtHandleOutput.setQName(bdtClientHandleQName);
		bdtHandleOutput.setIsArray(false);
		bdtHandleOutput.setIsClientHandle(Boolean.TRUE);
		bdtHandleOutput.setClientHandleClass(bdtService.getPackageName() + ".client." 
			+ bdtService.getName() + "Client");
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
		} else {
			System.out.println("BDT Extension was already present in the service");
		}
	}
}
