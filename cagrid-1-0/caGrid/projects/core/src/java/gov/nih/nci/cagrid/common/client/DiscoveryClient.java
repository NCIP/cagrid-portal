package gov.nih.nci.cagrid.common.client;

import gov.nih.nci.cagrid.common.XPathUtils;

import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.WSRFConstants;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Element;


public class DiscoveryClient {

	private EndpointReferenceType indexEPR = null;

	private static Map nsMap = new HashMap();
	static {
		nsMap.put("com", "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
		nsMap.put(WSRFConstants.SERVICEGROUP_PREFIX, WSRFConstants.SERVICEGROUP_NS);
	}


	public DiscoveryClient() throws MalformedURIException {
		// Default...can be changed with setter
		this("http://dc01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService");
	}


	public DiscoveryClient(String indexURL) throws MalformedURIException {
		this.indexEPR = new EndpointReferenceType();
		this.indexEPR.setAddress(new Address(indexURL));

	}


	public DiscoveryClient(EndpointReferenceType indexEPR) {
		this.indexEPR = indexEPR;
	}


	public static void main(String[] args) {
		DiscoveryClient client = null;
		try {
			if (args.length == 1) {
				client = new DiscoveryClient(args[1]);
			} else {
				client = new DiscoveryClient();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// try {
		// Element rps = ResourcePropertyHelper.getResourceProperties(indexEPR);
		// System.out.println("Index Resource Property Set:");
		// XMLUtils.PrettyElementToStream(rps, System.out);
		//
		// // ResourcePropertyHelper.getResourceProperties(indexEPR,new
		// // QName[]{new QName()});
		//
		EndpointReferenceType[] allServices=null;
		try {
			allServices = client.getAllServices();
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		if (allServices != null) {
			for (int i = 0; i < allServices.length; i++) {
				EndpointReferenceType service = allServices[i];
				System.out.println("\n\n" + service.getAddress());
				try {
					Element serviceRPs = ResourcePropertyHelper.getResourceProperties(service);
					System.out.println("Resource Property Set:");
					XMLUtils.PrettyElementToStream(serviceRPs, System.out);
				} catch (Exception e) {
					//e.printStackTrace();
					System.out.println("ERROR:  Unable to access service's resource properties: "+e.getMessage());
				}
			}
		} else {
			System.out.println("No services found.");
		}
		//
		// // String xpath =
		// // XPathUtils.translateXPath("//com:CommonServiceMetadata", nsMap);
		// //
		// // System.out.println("Querying for:" + xpath);
		// // Element[] elements =
		// // ResourcePropertyHelper.queryResourceProperties(indexEPR, xpath);
		// // System.out.println("\nQuery Results:");
		// // if (elements != null) {
		// // for (int i = 0; i < elements.length; i++) {
		// // System.out.println(XmlUtils.toString(elements[i]));
		// // }
		// // }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}


	public EndpointReferenceType[] getAllServices() throws Exception {
		String sg = WSRFConstants.SERVICEGROUP_PREFIX;
		EndpointReferenceType[] results = null;

		// build the xpath
		String xpath = XPathUtils.translateXPath("/*/" + sg + ":Entry/" + sg + ":MemberServiceEPR", nsMap);
		System.out.println("Querying for:" + xpath);

		// query the service and deser the results
		MessageElement[] elements = ResourcePropertyHelper.queryResourceProperties(indexEPR, xpath);
		Object[] objects = ObjectDeserializer.toObject(elements, EndpointReferenceType.class);

		// if we got results, cast them into what we are expected to return
		if (objects != null) {
			results = new EndpointReferenceType[objects.length];
			System.arraycopy(objects, 0, results, 0, objects.length);
		}

		return results;
	}
}
