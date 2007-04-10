package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;

import java.util.Properties;

/** 
 *  FeatureCodegen
 *  Abstract base class for data service features to run code generation
 * 
 * @author David Ervin
 * 
 * @created Mar 12, 2007 1:18:19 PM
 * @version $Id: FeatureCodegen.java,v 1.2 2007-04-10 14:30:29 hastings Exp $ 
 */
public abstract class FeatureCodegen {

	private ServiceInformation serviceInformation;
	private ServiceType mainService;
	private Properties serviceProperties;

	public FeatureCodegen(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
		this.serviceInformation = info;
		this.mainService = mainService;
		this.serviceProperties = serviceProps;
	}


	protected ServiceInformation getServiceInformation() {
		return serviceInformation;
	}


	protected ServiceType getMainService() {
		return mainService;
	}


	protected Properties getServiceProperties() {
		return serviceProperties;
	}


	public abstract void codegenFeature() throws CodegenExtensionException; 
}
