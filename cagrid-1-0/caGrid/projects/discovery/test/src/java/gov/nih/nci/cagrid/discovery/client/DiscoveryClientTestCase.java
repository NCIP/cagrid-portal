package gov.nih.nci.cagrid.discovery.client;

import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import junit.framework.TestCase;


public class DiscoveryClientTestCase extends TestCase {

	private PointOfContact nullPOC;
	private PointOfContact emptyPOC;
	private PointOfContact voidPOC;
	private PointOfContact emailPOC;
	private PointOfContact fullPOC;


	protected void setUp() throws Exception {
		super.setUp();
		nullPOC = null;
		emptyPOC = new PointOfContact();
		voidPOC = new PointOfContact("", "", "", "", "", "");
		emailPOC = new PointOfContact("", "emailV", "", "", "", "");
		fullPOC = new PointOfContact("affiliationV", "emailV", "firstNameV", "lastNameV", "phoneNumberV", "roleV");
	}


	public void testPredicateUtilNulls() {
		assertEquals("", DiscoveryClient.addNonNullPredicateFilter("foo", "", false));
		assertEquals("", DiscoveryClient.addNonNullPredicateFilter("foo", "  ", false));
		assertEquals("", DiscoveryClient.addNonNullPredicateFilter("foo", "", true));
		assertEquals("", DiscoveryClient.addNonNullPredicateFilter("foo", " ", true));
	}


	public void testPredicateUtil() {
		assertEquals(" and foo/text()='bar'", DiscoveryClient.addNonNullPredicateFilter("foo", "bar", false));
		assertEquals(" and @foo='bar'", DiscoveryClient.addNonNullPredicateFilter("foo", "bar", true));
	}


	public void testBuildPOCPredicateNulls() {
		assertEquals("*", DiscoveryClient.buildPOCPredicate(nullPOC));
		assertEquals("*", DiscoveryClient.buildPOCPredicate(emptyPOC));
		assertEquals("*", DiscoveryClient.buildPOCPredicate(voidPOC));
	}


	public void testBuildPOCPredicate() {
		assertTrue(DiscoveryClient.buildPOCPredicate(emailPOC).contains("email/text()='emailV'"));
		assertTrue(DiscoveryClient.buildPOCPredicate(fullPOC).contains("affiliation/text()='affiliationV'"));
		assertTrue(DiscoveryClient.buildPOCPredicate(fullPOC).contains("email/text()='emailV'"));
		assertTrue(DiscoveryClient.buildPOCPredicate(fullPOC).contains("firstName/text()='firstNameV'"));
		assertTrue(DiscoveryClient.buildPOCPredicate(fullPOC).contains("lastName/text()='lastNameV'"));
		assertTrue(DiscoveryClient.buildPOCPredicate(fullPOC).contains("phoneNumber/text()='phoneNumberV'"));
		assertTrue(DiscoveryClient.buildPOCPredicate(fullPOC).contains("role/text()='roleV'"));
	}


	public static void main(String[] args) {
		junit.textui.TestRunner.run(DiscoveryClientTestCase.class);
	}
}
