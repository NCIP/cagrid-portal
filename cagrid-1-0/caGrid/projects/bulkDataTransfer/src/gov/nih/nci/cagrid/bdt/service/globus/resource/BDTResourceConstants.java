package gov.nih.nci.cagrid.bdt.service.globus.resource;

import javax.xml.namespace.QName;


public interface BDTResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/BDTHandlerService";
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "BDTResourceProperties");
}