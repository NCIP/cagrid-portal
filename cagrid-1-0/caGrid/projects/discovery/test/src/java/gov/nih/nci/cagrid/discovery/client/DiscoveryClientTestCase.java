package gov.nih.nci.cagrid.discovery.client;

import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.UMLClass;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Node;


public class DiscoveryClientTestCase extends TestCase {

	private static final String NO_SERVICES_FILENAME = "noServices.xml";


	protected void setUp() throws Exception {
		super.setUp();

	}


	public void testGetAllServices() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.getAllServices();
			assertNotNull(allServices);
			assertEquals(0, allServices.length);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesByName() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesByName("foo");
			assertNotNull(allServices);
			assertEquals(0, allServices.length);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesByOperationClass() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesByOperationClass(null);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			UMLClass clazz = new UMLClass();

			allServices = client.discoverServicesByOperationClass(clazz);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			clazz.setClassName("foo");
			allServices = client.discoverServicesByOperationClass(clazz);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesByOperationInput() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesByOperationInput(null);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			UMLClass clazz = new UMLClass();

			allServices = client.discoverServicesByOperationInput(clazz);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			clazz.setClassName("foo");
			allServices = client.discoverServicesByOperationInput(clazz);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesByOperationOuput() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesByOperationOutput(null);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			UMLClass clazz = new UMLClass();

			allServices = client.discoverServicesByOperationOutput(clazz);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			clazz.setClassName("foo");
			allServices = client.discoverServicesByOperationOutput(clazz);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesByOperationName() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesByOperationName(null);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			allServices = client.discoverServicesByOperationName("foo");
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesByPointOfContact() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesByPointOfContact(null);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			PointOfContact poc = new PointOfContact();

			allServices = client.discoverServicesByPointOfContact(poc);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			poc.setFirstName("foo");
			allServices = client.discoverServicesByPointOfContact(poc);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesByResearchCenter() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesByResearchCenter(null);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			allServices = client.discoverServicesByResearchCenter("foo");
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDiscoverServicesBySearchString() {
		DiscoveryClient client = createDiscoveryClient(NO_SERVICES_FILENAME);
		try {
			EndpointReferenceType[] allServices = client.discoverServicesBySearchString(null);
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

			allServices = client.discoverServicesBySearchString("foo");
			assertNotNull(allServices);
			assertEquals(0, allServices.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


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
