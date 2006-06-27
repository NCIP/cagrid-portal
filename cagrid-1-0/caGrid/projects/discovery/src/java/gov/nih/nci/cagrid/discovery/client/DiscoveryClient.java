package gov.nih.nci.cagrid.discovery.client;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.discovery.MetadataConstants;
import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.discovery.ResourcePropertyHelper;
import gov.nih.nci.cagrid.discovery.XPathUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.UMLClass;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.WSRFConstants;
import org.globus.wsrf.encoding.ObjectDeserializer;


/**
 * @author oster
 * 
 */
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
	private static final String MD_PATH = CONTENT_PATH + "/" + cagrid + ":ServiceMetadata";
	private static final String SERV_PATH = MD_PATH + "/" + cagrid + ":serviceDescription/" + serv + ":Service";
	private static final String OPER_PATH = SERV_PATH + "/" + serv + ":serviceContextCollection/" + serv
		+ ":ServiceContext/" + serv + ":operationCollection/" + serv + ":Operation";

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

	protected static Log LOG = LogFactory.getLog(DiscoveryClient.class.getName());


	/**
	 * Uuses the Default Index Service
	 * 
	 * @throws MalformedURIException
	 *             if the Default Index Service is invalid
	 */
	public DiscoveryClient() throws MalformedURIException {
		this(DEFAULT_INDEX_SERVICE_URL);
	}


	/**
	 * Uses the specified Index Service
	 * 
	 * @param indexURL
	 *            the URL to the Index Service to use
	 * @throws MalformedURIException
	 *             if the specified Index Service URL is invalid
	 */
	public DiscoveryClient(String indexURL) throws MalformedURIException {
		this.indexEPR = new EndpointReferenceType();
		this.indexEPR.setAddress(new Address(indexURL));

	}


	/**
	 * Uses the specified Index Service
	 * 
	 * @param indexEPR
	 *            the EPR to the Index Service to use
	 */
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
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesBySearchString(String searchString) throws Exception {
		return discoverByFilter(CONTENT_PATH + "//*[contains(text(),'" + searchString + "') or @*[contains(string(),'"
			+ searchString + "')]]");
	}


	/**
	 * Searches research center info to find services provided by a given cancer
	 * center.
	 * 
	 * @param centerName
	 *            research center name
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByResearchCenter(String centerName) throws Exception {
		return discoverByFilter(MD_PATH + "/" + cagrid + ":hostingResearchCenter/" + com
			+ ":ResearchCenter[@displayName='" + centerName + "' or @shortName='" + centerName + "']");
	}


	/**
	 * Searches to find services that have the given point of contact associated
	 * with them. Any fields set on the point of contact are checked for a
	 * match. For example, you can set only the lastName, and only it will be
	 * checked, or you can specify several feilds and they all must be equal.
	 * 
	 * @param contact
	 *            point of contact
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByPointOfContact(PointOfContact contact) throws Exception {
		String pocPredicate = buildPOCPredicate(contact);

		return discoverByFilter(MD_PATH + "[" + cagrid + ":hostingResearchCenter/" + com + ":ResearchCenter/" + com
			+ ":pointOfContactCollection/" + com + ":PointOfContact[" + pocPredicate + "] or " + cagrid
			+ ":serviceDescription/" + serv + ":Service/" + serv + ":pointOfContactCollection/" + com
			+ ":PointOfContact[" + pocPredicate + "]]");
	}


	/**
	 * Searches to find services that have a given name.
	 * 
	 * @param serviceName
	 *            The service's name
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByName(String serviceName) throws Exception {
		return discoverByFilter(SERV_PATH + "[@name='" + serviceName + "']");
	}


	/**
	 * 
	 * Searches to find services that have a given operation.
	 * 
	 * @param operationName
	 *            The operation's name
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByOperationName(String operationName) throws Exception {
		return discoverByFilter(OPER_PATH + "[@name='" + operationName + "']");
	}


	/**
	 * Searches to find services that have an operation defined that takes the
	 * given UMLClass as input. Any fields set on the UMLClass are checked for a
	 * match. For example, you can set only the packageName, and only it will be
	 * checked, or you can specify several feilds and they all must be equal.
	 * 
	 * NOTE: Only attributes of the UMLClass are examined (associated objects
	 * (e.g. UMLAttributeCollection and SemanticMetadataCollection) are
	 * ignored).
	 * 
	 * @param clazzPrototype
	 *            The protype UMLClass
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByOperationInput(UMLClass clazzPrototype) throws Exception {
		String umlClassPredicate = buildUMLClassPredicate(clazzPrototype);

		return discoverByFilter(OPER_PATH + "/" + serv + ":inputParameterCollection/" + serv + ":InputParam/" + com
			+ ":UMLClass[" + umlClassPredicate + "]");
	}


	/**
	 * Searches to find services that have an operation defined that takes the
	 * given QName as input.
	 * 
	 * @param xml
	 *            The QName of the input type
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByOperationInput(QName xml) throws Exception {
		throw new Exception("Not yet implemented");
	}


	/**
	 * Searches to find services that have an operation defined that produces
	 * the given UMLClass. Any fields set on the UMLClass are checked for a
	 * match. For example, you can set only the packageName, and only it will be
	 * checked, or you can specify several feilds and they all must be equal.
	 * 
	 * NOTE: Only attributes of the UMLClass are examined (associated objects
	 * (e.g. UMLAttributeCollection and SemanticMetadataCollection) are
	 * ignored).
	 * 
	 * @param clazzPrototype
	 *            The protype UMLClass
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByOperationOutput(UMLClass clazzPrototype) throws Exception {
		String umlClassPredicate = buildUMLClassPredicate(clazzPrototype);

		return discoverByFilter(OPER_PATH + "/" + serv + ":output/" + serv + ":Output/" + com + ":UMLClass["
			+ umlClassPredicate + "]");
	}


	/**
	 * Searches to find services that have an operation defined that produces
	 * the given QName.
	 * 
	 * @param xml
	 *            The QName of the produced XML
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByOperationOutput(QName xml) throws Exception {
		throw new Exception("Not yet implemented");
	}


	/**
	 * Searches to find services that have an operation defined that produces
	 * the given UMLClass or takes it as input. Any fields set on the UMLClass
	 * are checked for a match. For example, you can set only the packageName,
	 * and only it will be checked, or you can specify several feilds and they
	 * all must be equal.
	 * 
	 * NOTE: Only attributes of the UMLClass are examined (associated objects
	 * (e.g. UMLAttributeCollection and SemanticMetadataCollection) are
	 * ignored).
	 * 
	 * @param clazzPrototype
	 *            The protype UMLClass
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByOperationClass(UMLClass clazzPrototype) throws Exception {
		String umlClassPredicate = buildUMLClassPredicate(clazzPrototype);

		return discoverByFilter(OPER_PATH + "[" + serv + ":output/" + serv + ":Output/" + com + ":UMLClass["
			+ umlClassPredicate + "] or " + serv + ":inputParameterCollection/" + serv + ":InputParam/" + com
			+ ":UMLClass[" + umlClassPredicate + "]" + "]");
	}


	/**
	 * Searches to find services that have an operation defined that produces
	 * the given QName or takes it as input.
	 * 
	 * @param xml
	 *            The QName to look for
	 * @return EndpointReferenceType[] matching the criteria
	 */
	public EndpointReferenceType[] discoverServicesByOperationClass(QName xml) throws Exception {
		throw new Exception("Not yet implemented");
	}


	public EndpointReferenceType[] discoverServicesByConceptCode(String conceptCode) throws Exception {
		throw new Exception("Not yet implemented");
	}


	
	/**
	 * Query the registry for all registered data services
	 * 
	 * @return EndpointReferenceType[] contain all registered services
	 */
	public EndpointReferenceType[] getAllDataServices() throws Exception {
		throw new Exception("Not yet implemented");
	}
	
	public EndpointReferenceType[] discoverDataServicesByDomainModel(String modelName) throws Exception {
		throw new Exception("Not yet implemented");
	}


	public EndpointReferenceType[] discoverDataServicesByModelConceptCode(String conceptCode) throws Exception {
		throw new Exception("Not yet implemented");
	}


	public EndpointReferenceType[] discoverDataServicesByExposedClass(UMLClass clazzPrototype) throws Exception {
		throw new Exception("Not yet implemented");
	}


	public EndpointReferenceType[] discoverDataServicesWithAssociationsWithClass(UMLClass clazzPrototype) throws Exception {
		throw new Exception("Not yet implemented");
	}
	

	/**
	 * Builds up a predicate for a PointOfContact, based on the prototype passed
	 * in.
	 * 
	 * @param contact
	 *            the prototype POC
	 * @return "*" if the prototype has no non-null or non-whitespace values, or
	 *         the predicate necessary to match all values.
	 */
	protected static String buildPOCPredicate(PointOfContact contact) {
		String pocPredicate = "true()";

		if (contact != null) {
			pocPredicate += addNonNullPredicate("affiliation", contact.getAffiliation(), true);
			pocPredicate += addNonNullPredicate("email", contact.getEmail(), true);
			pocPredicate += addNonNullPredicate("firstName", contact.getFirstName(), true);
			pocPredicate += addNonNullPredicate("lastName", contact.getLastName(), true);
			pocPredicate += addNonNullPredicate("phoneNumber", contact.getPhoneNumber(), true);
			pocPredicate += addNonNullPredicate("role", contact.getRole(), true);
		}

		return pocPredicate;
	}


	/**
	 * Builds up a predicate for a UMLClass, based on the prototype passed in.
	 * 
	 * NOTE: Only attributes of the UMLClass are examined (associated objects
	 * (e.g. UMLAttributeCollection and SemanticMetadataCollection) are
	 * ignored).
	 * 
	 * @param clazz
	 *            the prototype UMLClass
	 * @return "*" if the prototype has no non-null or non-whitespace values, or
	 *         the predicate necessary to match all values.
	 */
	protected static String buildUMLClassPredicate(UMLClass clazz) {
		String umlPredicate = "true()";

		if (clazz != null) {
			umlPredicate += addNonNullPredicate("projectName", clazz.getProjectName(), true);
			umlPredicate += addNonNullPredicate("projectVersion", clazz.getProjectVersion(), true);
			umlPredicate += addNonNullPredicate("className", clazz.getClassName(), true);
			umlPredicate += addNonNullPredicate("packageName", clazz.getPackageName(), true);
			umlPredicate += addNonNullPredicate("description", clazz.getDescription(), true);
		}

		return umlPredicate;
	}


	/**
	 * 
	 * @param name
	 *            the element or attribute name to check
	 * @param value
	 *            the value to add the predicate filter against if this is null
	 *            or whitespace only, no predicated is added.
	 * @param isAttribute
	 *            whether or not name represents an attribute or element
	 * @return "" or the specified predicate (prefixed with " and " )
	 */
	protected static String addNonNullPredicate(String name, String value, boolean isAttribute) {
		if (Utils.clean(value) == null) {
			return "";
		}
		if (isAttribute) {
			return " and @" + name + "='" + value + "'";
		} else {
			return " and " + name + "/text()='" + value + "'";
		}
	}


	/**
	 * Applies the specified predicate to the common path in the Index Service's
	 * Resource Properties to return registered services' EPRs that match the
	 * predicate.
	 * 
	 * @param xpathPredicate
	 *            predicate to apply to the "Entry" in Index Service
	 * @return EndpointReferenceType[] of matching services
	 * @throws Exception
	 */
	protected EndpointReferenceType[] discoverByFilter(String xpathPredicate) throws Exception {
		EndpointReferenceType[] results = null;

		// query the service and deser the results
		MessageElement[] elements = ResourcePropertyHelper.queryResourceProperties(indexEPR,
			translateXPath(xpathPredicate));
		Object[] objects = ObjectDeserializer.toObject(elements, EndpointReferenceType.class);

		// if we got results, cast them into what we are expected to return
		if (objects != null) {
			results = new EndpointReferenceType[objects.length];
			System.arraycopy(objects, 0, results, 0, objects.length);
		}

		return results;

	}


	protected String translateXPath(String xpathPredicate) {
		String xpath = "/*/" + wssg + ":Entry[" + xpathPredicate + "]/" + wssg + ":MemberServiceEPR";
		LOG.debug("Querying for: " + xpath);

		String translatedxpath = XPathUtils.translateXPath(xpath, nsMap);
		LOG.debug("Issuing actual query: " + translatedxpath);

		return translatedxpath;
	}


	/**
	 * Gets the EPR of the Index Service being used.
	 */
	public EndpointReferenceType getIndexEPR() {
		return indexEPR;
	}


	/**
	 * Sets the EPR of the Index Service to use.
	 * 
	 * @param indexEPR
	 *            the EPR of the Index Service to use.
	 */
	public void setIndexEPR(EndpointReferenceType indexEPR) {
		this.indexEPR = indexEPR;
	}


	/**
	 * testing stub
	 * 
	 * @param args
	 *            optional URL to Index Service to query.
	 */
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
							+ commonMetadata.getHostingResearchCenter().getResearchCenter().getDisplayName());
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