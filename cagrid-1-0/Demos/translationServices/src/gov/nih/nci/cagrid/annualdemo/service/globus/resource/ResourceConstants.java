package gov.nih.nci.cagrid.annualdemo.service.globus.resource;

import javax.xml.namespace.QName;


public interface ResourceConstants {
	public static final String SERVICE_NS = "http://annualdemo.cagrid.nci.nih.gov/MageTranslationServices";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "MageTranslationServicesKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "MageTranslationServicesResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
