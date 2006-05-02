package gov.nih.nci.cagrid.discovery;

import java.util.HashMap;
import java.util.Map;


public class MetadataUtils {
	private static Map nsMap = new HashMap();
	static {
		nsMap.put("com", "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");

	}

//	public static CommonServiceMetadataType getCommonMetadata(EndpointReferenceType serviceEPR) throws Exception {
//		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(serviceEPR,
//			MetadataConstants.COMMON_MD_QNAME);
//
//		return (CommonServiceMetadataType) ObjectDeserializer.toObject(resourceProperty,
//			CommonServiceMetadataType.class);
//	}
	
	
}
