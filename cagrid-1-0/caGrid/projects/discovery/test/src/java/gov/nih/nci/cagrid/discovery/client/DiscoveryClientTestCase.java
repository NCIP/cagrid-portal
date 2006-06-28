package gov.nih.nci.cagrid.discovery.client;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.UMLClass;

import java.io.File;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DiscoveryClientTestCase extends TestCase {

	private static final String NO_SERVICES_RESOURCE = "noServices.xml";
	private static final String THREE_SERVICES_TWO_VALID_RESOURCES = "2valid1Invalid.xml";
	private static final String METADATA_XSD = "ext" + File.separator + "xsd" + File.separator + "cagrid"
		+ File.separator + "types" + File.separator + "caGridMetadata.xsd";

	private static final String SERVICE1_EPR_RESOURCE = "EPRs/service1_EPR.xml";
	private static final String SERVICE2_EPR_RESOURCE = "EPRs/service2_EPR.xml";
	private static final String SERVICE3_EPR_RESOURCE = "EPRs/service3_EPR.xml";

	// DEFINE THE DISCOVERY METHODS
	private static final int ALL_SERVICES = 0;
	private static final int BY_NAME = 1;
	private static final int BY_OP_NAME = 2;
	private static final int BY_OP_INPUT = 3;
	private static final int BY_OP_OUTPUT = 4;
	private static final int BY_OP_CLASS = 5;
	private static final int BY_POC = 6;
	private static final int BY_CENTER = 7;
	private static final int BY_STRING = 8;
	private static final int BY_CODE = 9;
	private static final int BY_DS_MODEL = 10;
	private static final int BY_DS_CODE = 11;
	private static final int BY_DS_CLASS = 12;
	private static final int BY_DS_ASSOC = 13;
	private static final int ALL_DS = 14;

	private EndpointReferenceType service1EPR = null;
	private EndpointReferenceType service2EPR = null;
	private EndpointReferenceType service3EPR = null;


	protected void setUp() throws Exception {
		super.setUp();
		org.w3c.dom.Document doc = null;
		InputStream is = null;

		is = getClass().getResourceAsStream(SERVICE1_EPR_RESOURCE);
		assertNotNull(is);
		doc = XmlUtils.newDocument(is);
		service1EPR = (EndpointReferenceType) ObjectDeserializer.toObject(doc.getDocumentElement(),
			EndpointReferenceType.class);
		assertNotNull(service1EPR);

		is = getClass().getResourceAsStream(SERVICE2_EPR_RESOURCE);
		assertNotNull(is);
		doc = XmlUtils.newDocument(is);
		service2EPR = (EndpointReferenceType) ObjectDeserializer.toObject(doc.getDocumentElement(),
			EndpointReferenceType.class);
		assertNotNull(service1EPR);

		is = getClass().getResourceAsStream(SERVICE3_EPR_RESOURCE);
		assertNotNull(is);
		doc = XmlUtils.newDocument(is);
		service3EPR = (EndpointReferenceType) ObjectDeserializer.toObject(doc.getDocumentElement(),
			EndpointReferenceType.class);
		assertNotNull(service1EPR);
	}


	public void testGetAllServices() {
		final int operation = ALL_SERVICES;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, null);
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service2EPR, service3EPR}, services);

	}


	public void testDiscoverServicesByName() {
		final int operation = BY_NAME;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "foo");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "CaDSRService");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR}, services);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "Dorian");
		assertResultsEqual(new EndpointReferenceType[]{service3EPR}, services);

	}


	public void testDiscoverServicesByOperationClass() {
		final int operation = BY_OP_CLASS;
		EndpointReferenceType[] services = null;
		UMLClass clazz = new UMLClass();

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, clazz);
		assertEquals(0, services.length);

		clazz.setClassName("non-present class");
		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, clazz);
		assertEquals(0, services.length);
	}


	public void testDiscoverServicesByOperationInput() {
		final int operation = BY_OP_INPUT;
		EndpointReferenceType[] services = null;
		UMLClass clazz = new UMLClass();

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, clazz);
		assertEquals(0, services.length);

		clazz.setClassName("non-present class");
		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, clazz);
		assertEquals(0, services.length);
	}


	public void testDiscoverServicesByOperationOutput() {
		final int operation = BY_OP_OUTPUT;
		EndpointReferenceType[] services = null;
		UMLClass clazz = new UMLClass();

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, clazz);
		assertEquals(0, services.length);

		clazz.setClassName("non-present class");
		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, clazz);
		assertEquals(0, services.length);
	}

	public void testDiscoverServicesByOperationName() {
		final int operation = BY_OP_NAME;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "foo");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "getServiceSecurityMetadata");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service3EPR}, services);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "findProjects");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR}, services);
	}


	public void testDiscoverServicesByPointOfContact() {
		final int operation = BY_POC;
		EndpointReferenceType[] services = null;
		PointOfContact criteria = new PointOfContact();

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, criteria);
		assertEquals(0, services.length);

		criteria.setFirstName("not valid");
		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, criteria);
		assertEquals(0, services.length);

		criteria.setFirstName("Scott");
		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, criteria);
		assertResultsEqual(new EndpointReferenceType[]{service1EPR}, services);

		criteria.setFirstName("Stephen");
		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, criteria);
		assertResultsEqual(new EndpointReferenceType[]{service3EPR}, services);

		criteria.setFirstName("");
		criteria.setAffiliation("OSU");
		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, criteria);
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service3EPR}, services);
	}


	public void testDiscoverServicesByResearchCenter() {
		final int operation = BY_CENTER;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "foo");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "foo");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "OSU");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service3EPR}, services);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "Ohio State University");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service3EPR}, services);
	}


	public void testDiscoverServicesBySearchString() {
		final int operation = BY_STRING;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "a");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "generate metadata extracts");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR}, services);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "IFS");
		assertResultsEqual(new EndpointReferenceType[]{service3EPR}, services);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "get");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service3EPR}, services);

		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "tainer");
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service3EPR}, services);
	}


	public void testDiscoverServicesByConceptCode() {
		final int operation = BY_CODE;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "not present");
		assertEquals(0, services.length);
		
		//TODO: add some positive tests when the test examples have UML info
		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, "not present");
		assertEquals(0, services.length);
	}


	public void testGetAllDataServices() {
		final int operation = ALL_DS;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);
		
		//TODO: add some positive tests when the test examples have dataservices
		services = invokeDiscoveryMethod(THREE_SERVICES_TWO_VALID_RESOURCES, operation, null);
		assertEquals(0, services.length);
	}


	public void testDiscoverDataServicesByDomainModel() {
		//fail("Not tested yet.");
	}


	public void testDiscoverDataServicesByModelConceptCode() {
		//fail("Not tested yet.");
	}


	public void testDiscoverDataServicesByExposedClass() {
		//fail("Not tested yet.");
	}


	public void testDiscoverDataServicesWithAssociationsWithClass() {
		//fail("Not tested yet.");
	}


	private void assertResultsEqual(EndpointReferenceType[] expected, EndpointReferenceType[] received) {
		// id like to be able to do this:
		// assertTrue(Arrays.equals(target, services));
		// but EndpointReferenceType doesn't support equals, so we'll just check
		// the Address

		assertEquals(expected.length, received.length);

		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i].getAddress(), received[i].getAddress());
		}
	}


	/**
	 * Handles basic failures and invokes the discovery type specified againast
	 * the specified file, using the specified criteria.
	 * 
	 * @param xmlFile
	 *            classpath reference to the XML document to query
	 * @param method
	 *            discovery type to perform
	 * @param criteria
	 *            the criteria of the discovery type
	 * @return the discovered services
	 */
	private EndpointReferenceType[] invokeDiscoveryMethod(String xmlFile, int method, Object criteria) {
		EndpointReferenceType[] eprs = null;
		DiscoveryClient client = createDiscoveryClient(xmlFile);

		try {
			switch (method) {
				case ALL_SERVICES :
					eprs = client.getAllServices();
					break;
				case BY_CENTER :
					eprs = client.discoverServicesByResearchCenter((String) criteria);
					break;
				case BY_NAME :
					eprs = client.discoverServicesByName((String) criteria);
					break;
				case BY_OP_CLASS :
					eprs = client.discoverServicesByOperationClass((UMLClass) criteria);
					break;
				case BY_OP_INPUT :
					eprs = client.discoverServicesByOperationInput((UMLClass) criteria);
					break;
				case BY_OP_NAME :
					eprs = client.discoverServicesByOperationName((String) criteria);
					break;
				case BY_OP_OUTPUT :
					eprs = client.discoverServicesByOperationOutput((UMLClass) criteria);
					break;
				case BY_POC :
					eprs = client.discoverServicesByPointOfContact((PointOfContact) criteria);
					break;
				case BY_STRING :
					eprs = client.discoverServicesBySearchString((String) criteria);
					break;
				case BY_CODE :
					eprs = client.discoverServicesByConceptCode((String) criteria);
					break;
				case BY_DS_MODEL :
					eprs = client.discoverDataServicesByDomainModel((String) criteria);
					break;
				case BY_DS_CLASS :
					eprs = client.discoverDataServicesByExposedClass((UMLClass) criteria);
					break;
				case BY_DS_CODE :
					eprs = client.discoverDataServicesByModelConceptCode((String) criteria);
					break;
				case BY_DS_ASSOC :
					eprs = client.discoverDataServicesWithAssociationsWithClass((UMLClass) criteria);
					break;
				case ALL_DS :
					eprs = client.getAllDataServices();
					break;
				default :
					fail("Invalid discovery method");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// we never return null, at least empty array
		assertNotNull(eprs);
		return eprs;

	}


	/**
	 * Constructs a "Mock" DiscoveryClient that extends the regular
	 * DiscoveryClient and overrides its method that invokes the Index Service,
	 * by issuing the XPath to a local XML document.
	 * 
	 * @param resourcePath
	 *            classpath reference to the XML document to query
	 * @return the Mock DiscoveryClient
	 */
	private DiscoveryClient createDiscoveryClient(String resourcePath) {
		DiscoveryClient client = null;
		InputStream resource = getClass().getResourceAsStream(resourcePath);
		assertNotNull(resource);
		try {
			Node xmlNode = XmlUtils.newDocument(resource);
			assertNotNull(xmlNode);

			XObject result = XPathAPI.eval(xmlNode, "//*[local-name()='ServiceMetadata']");
			assertNotNull(result);
			if (result instanceof XNodeSet) {
				XNodeSet set = (XNodeSet) result;
				NodeList list = set.nodelist();
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					String xmlContent = null;
					if (node instanceof Document) {
						xmlContent = XmlUtils.toString((Document) node);
					} else if (node instanceof Element) {
						xmlContent = XmlUtils.toString((Element) node);
					} else {
						throw new Exception("Unexpected query result!");
					}
					try {
						// validate the "registered" service's XML
						SchemaValidator validator = new SchemaValidator(getSchemaFilename());
						validator.validate(xmlContent);
					} catch (SchemaValidationException e) {
						fail("Schema validation error:" + e.getMessage() + "\n" + xmlContent);
					}
				}
			} else {
				throw new Exception("Unexpected query result!");
			}

			client = new MockDiscoveryClient(xmlNode);
		} catch (Exception e) {
			fail("Problem creating Mock DiscoveryClient:" + e.getMessage());
		}
		return client;
	}


	private String getSchemaFilename() {
		File file = new File(METADATA_XSD);
		assertNotNull(file);
		assertTrue(file.exists() && file.canRead());
		return file.getAbsolutePath();
	}


	public static void main(String[] args) {
		junit.textui.TestRunner.run(DiscoveryClientTestCase.class);
	}


}
