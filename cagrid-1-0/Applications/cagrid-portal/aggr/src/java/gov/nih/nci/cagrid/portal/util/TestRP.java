package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.MetadataConstants;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.StringWriter;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

public class TestRP {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		String url = "https://james.joshuadev.com:8467/wsrf/services/cagrid/CaaersDataService";
//		String url = "http://sbdev1000.semanticbits.com:18030/wsrf/services/cagrid/CaaersDataService";
//		String url = "https://128.252.227.112:8443/wsrf/services/cagrid/CaTissueCore";
		String url = "https://cagrid-cccwfu.wfubmc.edu:8443/wsrf/services/cagrid/CaaersDataService";
		EndpointReferenceType epr = new EndpointReferenceType();
		Address addr = new Address(url);
		epr.setAddress(addr);
		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(
				epr, MetadataConstants.CAGRID_MD_QNAME);
		ServiceMetadata sm = MetadataUtils.getServiceMetadata(epr);
		StringWriter w = new StringWriter();
		Utils.serializeObject(sm, MetadataConstants.CAGRID_MD_QNAME,w);
		System.out.println(w.getBuffer());
		
		DomainModel dm = MetadataUtils.getDomainModel(epr);
		w = new StringWriter();
		Utils.serializeObject(dm, MetadataConstants.CAGRID_DATA_MD_QNAME,w);
		System.out.println(w.getBuffer());
	}
}
