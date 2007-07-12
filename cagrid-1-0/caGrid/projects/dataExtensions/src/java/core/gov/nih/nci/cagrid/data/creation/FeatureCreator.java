package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;

import java.util.Properties;

/** 
 *  FeatureCreator
 *  Base class for feature additions to the data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id: FeatureCreator.java,v 1.1 2007-07-12 17:20:52 dervin Exp $ 
 */
public abstract class FeatureCreator {
	
	private ServiceInformation serviceInformation;
	private ServiceType mainService;
	private Properties serviceProperties;

	public FeatureCreator(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
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
	
	
	public abstract void addFeature() throws CreationExtensionException; 
}
