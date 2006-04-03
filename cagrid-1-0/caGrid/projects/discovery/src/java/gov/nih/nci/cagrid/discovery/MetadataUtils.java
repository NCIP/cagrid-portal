package gov.nih.nci.cagrid.discovery;

import gov.nih.nci.cagrid.metadata.common.CommonServiceMetadataType;

import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Element;


public class MetadataUtils {
	private static Map nsMap = new HashMap();
	static {
		nsMap.put("com", "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");

	}

	public static CommonServiceMetadataType getCommonMetadata(EndpointReferenceType serviceEPR) throws Exception {
		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(serviceEPR,
			MetadataConstants.COMMON_MD_QNAME);

		return (CommonServiceMetadataType) ObjectDeserializer.toObject(resourceProperty,
			CommonServiceMetadataType.class);
	}
	
	
}
