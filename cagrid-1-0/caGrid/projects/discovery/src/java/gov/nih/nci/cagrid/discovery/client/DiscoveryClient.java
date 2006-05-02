package gov.nih.nci.cagrid.discovery.client;

import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.discovery.ResourcePropertyHelper;
import gov.nih.nci.cagrid.discovery.XPathUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.wsrf.WSRFConstants;
import org.globus.wsrf.encoding.ObjectDeserializer;


public class DiscoveryClient {

	private static final String DEFAULT_INDEX_SERVICE_URL = "http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService";
	private EndpointReferenceType indexEPR = null;

	private static Map nsMap = new HashMap();
	static {
		// nsMap.put("com",
		// "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
		nsMap.put(WSRFConstants.SERVICEGROUP_PREFIX, WSRFConstants.SERVICEGROUP_NS);
	}


	public DiscoveryClient() throws MalformedURIException {
		this(DEFAULT_INDEX_SERVICE_URL);
	}


	public DiscoveryClient(String indexURL) throws MalformedURIException {
		this.indexEPR = new EndpointReferenceType();
		this.indexEPR.setAddress(new Address(indexURL));

	}


	public DiscoveryClient(EndpointReferenceType indexEPR) {
		this.indexEPR = indexEPR;
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


	// ----common----
	// discoverServicesBySearchString
	// discoverServicesByCancerCenter
	// discoverServicesBySearchString
	// discoverServicesByConceptCode
	// discoverServicesByPointOfContact
	// ----service----
	// discoverServicesByOperationName
	// discoverServicesByOperationClass
	// discoverServicesByOperationInputClass
	// discoverServicesByOperationOutputClass
	//
	// ----data----
	// discoverServicesByModelName
	// discoverServicesByClass
	// discoverServicesByObjectsAssociatedWithClass???
	//

	public EndpointReferenceType getIndexEPR() {
		return indexEPR;
	}


	public void setIndexEPR(EndpointReferenceType indexEPR) {
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

		EndpointReferenceType[] allServices = null;
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
					ServiceMetadata commonMetadata = MetadataUtils.getServiceMetadata(service);
					if (commonMetadata != null && commonMetadata.getHostingResearchCenter() != null) {
						System.out.println("Service is from:"
							+ commonMetadata.getHostingResearchCenter().getDisplayName());
					}
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("ERROR:  Unable to access service's standard resource properties: " + e.getMessage());
				}
			}
		} else {
			System.out.println("No services found.");
		}
	}
}