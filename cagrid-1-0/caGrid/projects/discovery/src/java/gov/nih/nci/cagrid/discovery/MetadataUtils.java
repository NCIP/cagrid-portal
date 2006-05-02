package gov.nih.nci.cagrid.discovery;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Element;


public class MetadataUtils {

	public static ServiceMetadata getServiceMetadata(EndpointReferenceType serviceEPR) throws Exception {
		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(serviceEPR,
			MetadataConstants.COMMON_MD_QNAME);

		return (ServiceMetadata) ObjectDeserializer.toObject(resourceProperty, ServiceMetadata.class);
	}
}
