package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;

import java.util.Properties;

/** 
 *  FeatureCreator
 *  Base class for feature additions to the data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id$ 
 */
public abstract class FeatureCreator {
	
	private ServiceDescription serviceDescription;
	private ServiceType mainService;
	private Properties serviceProperties;

	public FeatureCreator(ServiceDescription desc, ServiceType mainService, Properties serviceProps) {
		this.serviceDescription = desc;
		this.mainService = mainService;
		this.serviceProperties = serviceProps;
	}
	
	
	protected ServiceDescription getServiceDescription() {
		return serviceDescription;
	}
	
	
	protected ServiceType getMainService() {
		return mainService;
	}
	
	
	protected Properties getServiceProperties() {
		return serviceProperties;
	}
	
	
	public abstract void addFeature() throws CreationExtensionException; 
}
