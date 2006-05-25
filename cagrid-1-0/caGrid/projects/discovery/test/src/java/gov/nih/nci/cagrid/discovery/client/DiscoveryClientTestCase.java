package gov.nih.nci.cagrid.discovery.client;

import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.UMLClass;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Node;


public class DiscoveryClientTestCase extends TestCase {

	private static final String NO_SERVICES_RESOURCE = "noServices.xml";
	private static final String TWO_SERVICES_ONE_VALID_RESOURCE = "1valid1Invalid.xml";

	private static final String SERVICE1_EPR_RESOURCE = "EPRs/service1_EPR.xml";
	private static final String SERVICE2_EPR_RESOURCE = "EPRs/service2_EPR.xml";

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

	private EndpointReferenceType service1EPR = null;
	private EndpointReferenceType service2EPR = null;


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
	}


	public void testGetAllServices() {
		final int operation = ALL_SERVICES;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(TWO_SERVICES_ONE_VALID_RESOURCE, operation, null);
		assertResultsEqual(new EndpointReferenceType[]{service1EPR, service2EPR}, services);

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


	public void testDiscoverServicesByOperationOuput() {
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
	}


	public void testDiscoverServicesBySearchString() {
		final int operation = BY_STRING;
		EndpointReferenceType[] services = null;

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, null);
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "");
		assertEquals(0, services.length);

		services = invokeDiscoveryMethod(NO_SERVICES_RESOURCE, operation, "foo");
		assertEquals(0, services.length);
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

			client = new MockDiscoveryClient(xmlNode);
		} catch (Exception e) {
			fail("Problem creating Mock DiscoveryClient:" + e.getMessage());
		}
		return client;
	}


	public static void main(String[] args) {
		junit.textui.TestRunner.run(DiscoveryClientTestCase.class);
	}
}
