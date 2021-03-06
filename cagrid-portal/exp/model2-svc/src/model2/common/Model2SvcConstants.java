/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package model2.common;

import javax.xml.namespace.QName;


public interface Model2SvcConstants {
	public static final String SERVICE_NS = "http://model2/Model2Svc";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "Model2SvcKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "Model2SvcResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName SERVICEMETADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	public static final QName DOMAINMODEL = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel");
	
}
