package gov.nih.nci.cagrid.discovery.client;

import gov.nih.nci.cagrid.discovery.MetadataConstants;
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

	// Define the prefixes
	private static final String wssg = WSRFConstants.SERVICEGROUP_PREFIX;
	private static final String agg = "agg";
	private static final String cagrid = "cagrid";
	private static final String com = "com";
	private static final String serv = "serv";
	private static final String data = "data";
	// some common paths for reuse
	private static final String CONTENT_PATH = wssg + ":Content/" + agg + ":AggregatorData";

	// Map the prefixes to there namepsaces
	private static Map nsMap = new HashMap();
	static {
		nsMap.put(wssg, WSRFConstants.SERVICEGROUP_NS);
		nsMap.put(agg, MetadataConstants.AGGREGATOR_NAMESPACE);
		nsMap.put(cagrid, MetadataConstants.CAGRID_MD_NAMESPACE);
		nsMap.put(com, MetadataConstants.CAGRID_COMMON_MD_NAMESPACE);
		nsMap.put(serv, MetadataConstants.CAGRID_SERVICE_MD_NAMESPACE);
		nsMap.put(data, MetadataConstants.CAGRID_DATA_MD_NAMESPACE);
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


	/**
	 * Query the registry for all registered services
	 * 
	 * @return EndpointReferenceType[] contain all registered services
	 */
	public EndpointReferenceType[] getAllServices() throws Exception {
		return discoverByFilter("*");
	}


	/**
	 * Searches ALL metadata to find occurance of the given string. The search
	 * string is case-sensitive.
	 * 
	 * @param searchString
	 *            the search string.
	 * @return EndpointReferenceType[] matching the search string
	 */
	public EndpointReferenceType[] discoverServicesBySearchString(String searchString) throws Exception {
		return discoverByFilter(CONTENT_PATH + "//*[contains(text(),'" + searchString + "') or contains(@*,'"
			+ searchString + "')]");
	}


	protected EndpointReferenceType[] discoverByFilter(String xpathPredicate) throws Exception {
		EndpointReferenceType[] results = null;
		final String xpath = "/*/" + wssg + ":Entry[" + xpathPredicate + "]/" + wssg + ":MemberServiceEPR";
		System.out.println("Querying for: " + xpath);
		final String translatedxpath = XPathUtils.translateXPath(xpath, nsMap);
		System.out.println("Issuing actual query: " + translatedxpath);

		// query the service and deser the results
		MessageElement[] elements = ResourcePropertyHelper.queryResourceProperties(indexEPR, translatedxpath);
		Object[] objects = ObjectDeserializer.toObject(elements, EndpointReferenceType.class);

		// if we got results, cast them into what we are expected to return
		if (objects != null) {
			results = new EndpointReferenceType[objects.length];
			System.arraycopy(objects, 0, results, 0, objects.length);
		}

		return results;

	}


	// ----common----
	// discoverServicesByCancerCenter
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
					System.out.println("ERROR: Unable to access service's standard resource properties: "
						+ e.getMessage());
				}
			}
		} else {
			System.out.println("No services found.");
		}
	}
}