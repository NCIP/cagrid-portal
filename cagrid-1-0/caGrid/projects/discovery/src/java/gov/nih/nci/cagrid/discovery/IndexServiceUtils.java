package gov.nih.nci.cagrid.discovery;

import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.WSRFConstants;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Element;


public class IndexServiceUtils {

	private static Map nsMap = new HashMap();
	static {
		// nsMap.put("com",
		// "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
		nsMap.put(WSRFConstants.SERVICEGROUP_PREFIX, WSRFConstants.SERVICEGROUP_NS);
	}


	public static void test() {

		try {
			EndpointReferenceType indexEPR = new EndpointReferenceType();
			indexEPR.setAddress(new Address("http://dc01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService"));

			Element rps = ResourcePropertyHelper.getResourceProperties(indexEPR);
			System.out.println("Index Resource Property Set:");
			XMLUtils.PrettyElementToStream(rps, System.out);

			String sg = WSRFConstants.SERVICEGROUP_PREFIX;
			String xpath = XPathUtils.translateXPath("/*/" + sg + ":Entry/" + sg + ":ServiceGroupEntryEPR", nsMap);
			System.out.println("Querying for:" + xpath);

			// query the service and deser the results
			MessageElement[] elements = ResourcePropertyHelper.queryResourceProperties(indexEPR, xpath);
			Object[] objects = ObjectDeserializer.toObject(elements, EndpointReferenceType.class);
			for (int i = 0; i < objects.length; i++) {
				EndpointReferenceType sgEntryEPR = (EndpointReferenceType) objects[i];
				rps = ResourcePropertyHelper.getResourceProperties(sgEntryEPR);
				System.out.println("\n\nIndex Entry Resource Property Set:");
				XMLUtils.PrettyElementToStream(rps, System.out);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		IndexServiceUtils.test();
	}
}
