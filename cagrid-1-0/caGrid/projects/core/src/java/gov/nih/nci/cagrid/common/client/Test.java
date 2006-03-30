package gov.nih.nci.cagrid.common.client;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;


public class Test {

	public static void main(String[] args) {
		EndpointReferenceType indexEPR = new EndpointReferenceType();
		try {
			indexEPR.setAddress(new Address("http://dc01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService"));
		} catch (MalformedURIException e) {
			e.printStackTrace();
		}

		try {
			Element rps = ResourcePropertyHelper.getResourceProperties(indexEPR);
			System.out.println("Resource Property Set:");
			XMLUtils.PrettyElementToStream(rps, System.out);

			Element[] elements = ResourcePropertyHelper
				.queryResourceProperties(
					indexEPR,
					"/*/*[namespace-uri()='gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common' and local-name()='CommonServiceMetadata']");
			System.out.println("\nQuery Results:");
			if (elements != null) {
				for (int i = 0; i < elements.length; i++) {
					System.out.println(XmlUtils.toString(elements[i]));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
